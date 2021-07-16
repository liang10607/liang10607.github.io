[回目录页](..)

# 1. HandlerThread简介

想象一下如果我们在项目中经常要执行耗时操作，如果经常要开启线程，接着又销毁线程，这无疑是很消耗性能的？那有什么解决方法呢？

* 使用线程池
* 使用HandlerThread

HandlerThread是Google帮我们封装好的，可以用来执行多个耗时操作，而不需要多次开启线程，里面是采用Handler和Looper实现的。


# 2. HandlerThread的使用

### 2.1 创建HandlerThread的实例对象

```
  HandlerThread handlerThread = new HandlerThread("myHandlerThread");//  该参数表示线程的名字，可以随便选择。

```  

### 2.2 启动我们创建的HandlerThread线程

```
handlerThread.start();
```

### 2.3 将我们的handlerThread与Handler绑定在一起。
 
 还记得是怎样将Handler与线程对象绑定在一起的吗？其实很简单，就是将线程的looper与Handler绑定在一起，代码如下：
 
```
mThreadHandler = new Handler(mHandlerThread.getLooper()) {
    @Override
    public void handleMessage(Message msg) {
        checkForUpdate();
        if(isUpdate){
            mThreadHandler.sendEmptyMessage(MSG_UPDATE_INFO);
        }
    }
};

```

  是构建一个可以用于异步操作的handler，并将前面创建的HandlerThread的Looper对象以及Callback接口类作为参数传递给当前的handler，这样当前的异步handler就拥有了HandlerThread的Looper对象，由于HandlerThread本身是异步线程，因此Looper也与异步线程绑定，从而handlerMessage方法也就可以异步处理耗时任务了，这样我们的Looper+Handler+MessageQueue+Thread异步循环机制构建完成


