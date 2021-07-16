[回目录页](..)

# 1. 简介

* 1. IntentService是Android里面的一个封装类，继承自四大组件之一的Service。
* 2. 可以顺序执行收到的intent工作
* 3. 任务是执行在子线程
* 4. 内部是依赖HangdlerThread和Handler结合的方式实现顺序执行和子线程执行

其工作执行是在onHandleIntent()中：

```    
/*复写onStartCommand()方法*/
       //默认实现将请求的Intent添加到工作队列里
       @Override
       public int onStartCommand(Intent intent, int flags, int startId) {
           Log.i("myIntentService", "onStartCommand");
           return super.onStartCommand(intent, flags, startId);
       }
```

# 2. 源码分析

# 2.1 Intent开启一个独立线程

  在IntentService的Oncreate（）方法中，启动一个HandlerThread, 并且绑定到IntentService的Handler对象中，最终在handler对象的handlemMessage()中执行任务，即调用OnhandlIntent()
  

```
// IntentService源码中的 onCreate() 方法
@Override
public void onCreate() {
    super.onCreate();
    // HandlerThread继承自Thread，内部封装了 Looper
    //通过实例化HandlerThread新建线程并启动
    //所以使用IntentService时不需要额外新建线程
    HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
    thread.start();

    //获得工作线程的 Looper，并维护自己的工作队列
    mServiceLooper = thread.getLooper();
    //将上述获得Looper与新建的mServiceHandler进行绑定
    //新建的Handler是属于工作线程的。
    mServiceHandler = new ServiceHandler(mServiceLooper);
}

private final class ServiceHandler extends Handler {

    public ServiceHandler(Looper looper) {
        super(looper);
    }

    // IntentService的handleMessage方法把接收的消息交给onHandleIntent()处理
    // onHandleIntent()是一个抽象方法，使用时需要重写的方法
    @Override
    public void handleMessage(Message msg) {
        // onHandleIntent 方法在工作线程中执行，执行完调用 stopSelf() 结束服务。
        onHandleIntent((Intent) msg.obj);
        //onHandleIntent 处理完成后 IntentService会调用 stopSelf() 自动停止。
        stopSelf(msg.arg1);
    }
}

// onHandleIntent()是一个抽象方法，使用时需要重写的方法
@WorkerThread
protected abstract void onHandleIntent(Intent intent);

```

### 2.2 onStartCommand()传递给服务intent并依次插入到工作队列中

   IntentService的startCommand方法会把intent通过handler的sendmessage发送到HanlderThread去执行。具体代码如下：
   
```
public int onStartCommand(Intent intent, int flags, int startId) {
       onStart(intent, startId);
       return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
   }
   
   public void onStart(Intent intent, int startId) {
       Message msg = mServiceHandler.obtainMessage();
       msg.arg1 = startId;
       //把 intent 参数包装到 message 的 obj 中，然后发送消息，即添加到消息队列里
       //这里的Intent 就是启动服务时startService(Intent) 里的 Intent。
       msg.obj = intent;
       mServiceHandler.sendMessage(msg);
   }
   
   //清除消息队列中的消息
   @Override
   public void onDestroy() {
       mServiceLooper.quit();
   }
```   

### 2.3 总结

总结

从上面源码可以看出，IntentService本质是采用Handler & HandlerThread方式：

* 通过HandlerThread单独开启一个名为IntentService的线程
* 创建一个名叫ServiceHandler的内部Handler
* 把内部Handler与HandlerThread所对应的子线程进行绑定
* 通过onStartCommand()传递给服务intent，依次插入到工作队列中，并逐个发送给onHandleIntent()
* 通过onHandleIntent()来依次处理所有Intent请求对象所对应的任务
* 因此我们通过复写方法onHandleIntent()，再在里面根据Intent的不同进行不同的线程操作就可以了

**注意事项：工作任务队列是顺序执行的。**

    如果一个任务正在IntentService中执行，此时你再发送一个新的任务请求，这个新的任务会一直等待直到前面一个任务执行完毕才开始执行。
    
由于onCreate() 方法只会调用一次，所以只会创建一个工作线程；
当多次调用 startService(Intent) 时（onStartCommand也会调用多次）其实并不会创建新的工作线程，只是把消息加入消息队列中等待执行，所以，多次启动 IntentService 会按顺序执行事件；
如果服务停止，会清除消息队列中的消息，后续的事件得不到执行    

# 3. IntentService对比Service和其他普通线程

### 3.1 IntentService与Service的区别

* 从属性 & 作用上来说 Service：依赖于应用程序的主线程（不是独立的进程 or 线程）

    不建议在Service中编写耗时的逻辑和操作，否则会引起ANR；

    IntentService：创建一个工作线程来处理多线程任务 　　

* Service需要主动调用stopSelft()来结束服务，而IntentService不需要（在所有intent被处理完后，系统会自动关闭服务）

### 3.1 IntentService与普通线程

* IntentService内部采用了HandlerThread实现，作用类似于后台线程；

* 与后台线程相比，IntentService是一种后台服务，优势是：**优先级高**（不容易被系统杀死），从而保证任务的执行。

    对于后台线程，若进程中没有活动的四大组件，则该线程的优先级非常低，容易被系统杀死，无法保证任务的执行

