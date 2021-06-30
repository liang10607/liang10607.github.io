[回目录页](..)

# Android Service组件

# 1. Service简介

* Service是android是实现程序后台运行的解决方案，它非常适用于去执行那些不需要和用户交互但需要长时间运行的任务，
* Service并不会运行在子线程众，是运行在UI线程众，因此不要执行耗时操作，耗时操作需要创建子线程来操作

在什么情况下使用 startService 或 bindService 或 同时使用startService 和 bindService？
①如果你只是想要启动一个后台服务长期进行某项任务那么使用 startService 便可以了。

②如果你想要与正在运行的 Service 取得联系，那么有两种方法，一种是使用 broadcast ，另外是使用 bindService ，前者的缺点是如果交流较为频繁，容易造成性能上的问题，并且 BroadcastReceiver 本身执行代码的时间是很短的（也许执行到一半，后面的代码便不会执行），而后者则没有这些问题，因此我们肯定选择使用 bindService（这个时候你便同时在使用 startService 和 bindService 了，这在 Activity 中更新 Service 的某些运行状态是相当有用的）。

③如果你的服务只是公开一个远程接口，供连接上的客服端（android 的 Service 是C/S架构）远程调用执行方法。这个时候你可以不让服务一开始就运行，而只用 bindService ，这样在第一次 bindService 的时候才会创建服务的实例运行它，这会节约很多系统资源，特别是如果你的服务是Remote Service，那么该效果会越明显（当然在 Service 创建的时候会花去一定时间，你应当注意到这点）。

# 2.Service的几种典型使用实例
### 1.不可交互的后台服务
  ①不运行在一个独立的进程中，它同样执行在UI线程中，因此，在Service中创建了子线程来完成耗时操作。

  ②当Service关闭后，如果在onDestory()方法中不关闭线程，你会发现我们的子线程进行的耗时操作是一直存在的，此时关闭该子线程的方法需要直接关闭该应用程序。因此，在onDestory()方法中要进行必要的清理工作。

### 2.可交互的后台服务

* 可交互的后台服务是指前台页面可以调用后台服务的方法，通过bindService()方式开启。Service的生命周期很简单，分别为onCreate、onBind、onUnBind、onDestroy这四个。
* 可交互的后台服务实现步骤是和不可交互的后台服务实现步骤是一样的，区别在于启动的方式和获得Service的代理对象。
创建服务类
* 
和普通Service不同在于这里返回一个代理对象，返回给前台进行获取，即前台可以获取该代理对象执行后台服务的方法

```
public class BackService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回MyBinder对象
        return new MyBinder();
    }
    //需要返回给前台的Binder类
    class MyBinder extends Binder {
        public void showTip(){
            System.out.println("我是来此服务的提示");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
```

前台调用
通过以下方式绑定服务：

```
bindService(mIntent,con,BIND_AUTO_CREATE);
//其中第二个参数：

private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackService.MyBinder myBinder = (BackService.MyBinder) service;
            myBinder.showTip();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
```
### 3.混合型后台服务
  将上面两种启动方式结合起来就是混合性交互的后台服务了，即可以单独运行后台服务，也可以运行后台服务中提供的方法，其完整的生命周期是：onCreate->onStartCommand->onBind->onUnBind->onDestroy

### 4.前台服务

   由于后台服务优先级相对比较低，当系统出现内存不足的情况下，它就有可能会被回收掉，所以前台服务就是来弥补这个缺点的，它可以一直保持运行状态而不被系统回收。