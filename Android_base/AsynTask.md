[回目录页](..)

# 1. AsyncTask使用

### 1.1 AsyncTask的简单使用

```
class DownloadTask extends AsyncTask<Void, Integer, Boolean> {  

    @Override  
    protected void onPreExecute() {  
        progressDialog.show();  
    }  

    @Override  
    protected Boolean doInBackground(Void... params) {  
        try {  
            while (true) {  
                int downloadPercent = doDownload();  
                publishProgress(downloadPercent);  
                if (downloadPercent >= 100) {  
                    break;  
                }  
            }  
        } catch (Exception e) {  
            return false;  
        }  
        return true;  
    }  

    @Override  
    protected void onProgressUpdate(Integer... values) {  
        progressDialog.setMessage("当前下载进度：" + values[0] + "%");  
    }  

    @Override  
    protected void onPostExecute(Boolean result) {  
        progressDialog.dismiss();  
        if (result) {  
            Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();  
        } else {  
            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();  
        }  
    }  
}
```

这里我们模拟了一个下载任务，在doInBackground()方法中去执行具体的下载逻辑，在onProgressUpdate()方法中显示当前的下载进度，在onPostExecute()方法中来提示任务的执行结果。如果想要启动这个任务，只需要简单地调用以下代码即可：

```
new DownloadTask().execute();
```

### 1.1 AsyncTask注意事项

①异步任务的实例必须在UI线程中创建，即AsyncTask对象必须在UI线程中创建。

②execute(Params... params)方法必须在UI线程中调用。

③不要手动调用onPreExecute()，doInBackground(Params... params)，onProgressUpdate(Progress... values)，onPostExecute(Result result)这几个方法。

④不能在doInBackground(Params... params)中更改UI组件的信息。

⑤一个任务实例只能执行一次，如果执行第二次将会抛出异常。

# 2. AsyncTask源码分析

### 2.1 始化一个AsyncTask

只是初始化了两个变量，mWorker和mFuture:

```
public AsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                mTaskInvoked.set(true);
                Result result = null;
                try {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    //noinspection unchecked
                    result = doInBackground(mParams);
                    Binder.flushPendingCommands();
                } catch (Throwable tr) {
                    mCancelled.set(true);
                    throw tr;
                } finally {
                    postResult(result);
                }
                return result;
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                try {
                    postResultIfNotInvoked(get());
                } catch (InterruptedException e) {
                    android.util.Log.w(LOG_TAG, e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occurred while executing doInBackground()",
                            e.getCause());
                } catch (CancellationException e) {
                    postResultIfNotInvoked(null);
                }
            }
        };
    }
```

### 2.2 加载Future线程到线程池

```
 public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }


  public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
            Params... params) {
        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        onPreExecute();

        mWorker.mParams = params;
        exec.execute(mFuture);

        return this;
    }
```

### 2.3 线程池源码

```
private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }


 ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
```

### 2.4 postResult消息传递

```
 private Result postResult(Result result) {
        @SuppressWarnings("unchecked")
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT,
                new AsyncTaskResult<Result>(this, result));
        message.sendToTarget();
        return result;
    }
```

InternalHandler是一个静态类，为了能够将执行环境切换到主线程，因此这个类必须在主线程中进行加载。所以变相要求AsyncTask的类必须在主线程中进行加载。

### 2.5 AsyncTask的串行和并行
   
   从上述源码分析中分析得到，默认情况下AsyncTask的执行效果是**串行**的，因为有了SerialExecutor类来维持保证队列的串行。
   
   如果想使用并行执行任务，那么可以直接跳过SerialExecutor类，使用AsyncTask.executeOnExecutor()来执行任务。

# 3. AsyncTask使用不当的后果
  1.)生命周期
  
  AsyncTask不与任何组件绑定生命周期，所以在Activity/或者Fragment中创建执行AsyncTask时，最好在Activity/Fragment的onDestory()调用 cancel(boolean)；
  
  2.)内存泄漏
  
  如果AsyncTask被声明为Activity的非静态的内部类，那么AsyncTask会保留一个对创建了AsyncTask的Activity的引用。
  
  如果Activity已经被销毁，AsyncTask的后台线程还在执行，它将继续在内存里保留这个引用，导致Activity无法被回收，引起内存泄露。
  
  正确的姿势应该是将你的异步任务变成静态内部类,并且持有MyActivity的一个弱引用,当更新UI(即执行onPostExecute)的时候我们需要拿到activity的一个强引用,这个时候去判断activity的状态,是销毁了还是健在再去做处理,就避免了内存泄漏
  
  class MyActivity extends Activity {
    static class MyTask extends AsyncTask<Void, Void, Void> {
      // 弱引用是允许被gc回收的;
      private final WeakReference<MyActivity> weakActivity;
  
      MyTask(MyActivity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
      }
  
      @Override
      public Void doInBackground(Void... params) {
        // do async stuff here
      }
  
      @Override
      public void onPostExecute(Void result) {
  
        MyActivity activity = weakActivity.get();
        if (activity == null
            || activity.isFinishing()
            || activity.isDestroyed()) {
          // activity没了,就结束可以了
          return;
        }
  
        // 继续更新ui
      }
    }
  }

  
  3.) 结果丢失
  
  屏幕旋转或Activity在后台被系统杀掉等情况会导致Activity的重新创建，之前运行的AsyncTask（非静态的内部类）会持有一个之前Activity的引用，这个引用已经无效，这时调用onPostExecute()再去更新界面将不再生效。
  
  解决方案是: 发生旋转后，重新设置activi引用给到AsynTask
  
  