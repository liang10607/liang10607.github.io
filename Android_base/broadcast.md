[回目录页](..)

# BroadcastReceiver广播

**模型中有3个角色：**

* 消息订阅者（广播接收者）
* 消息发布者（广播发布者）
* 消息中心（AMS，即Activity Manager Service）


原理描述：

* 广播接收者 通过 Binder机制在 AMS 注册

* 广播发送者 通过 Binder 机制向 AMS 发送广播

* AMS 根据 广播发送者 要求，在已注册列表中，寻找合适的广播接收者
> 寻找依据：IntentFilter / Permission

* AMS将广播发送到合适的广播接收者相应的消息循环队列中；

广播接收者通过 消息循环 拿到此广播，并回调 onReceive()

特别注意：广播发送者 和 广播接收者的执行 是 异步的，发出去的广播不会关心有无接收者接收，也不确定接收者到底是何时才能接收到；

# 1. 广播的具体使用

###  1.1 自定义广播接收者BroadcastReceiver

* 继承自BroadcastReceivre基类

* 必须复写抽象方法onReceive()方法
> 默认情况下，广播接收器运行在UI线程，因此，onReceive方法不能执行耗时操作，否则将导致ANR。

### 广播注册

**静态广播注册**
  在AndroidManifest.xml里通过 <receive> 标签声明

**动态广播注册**

在代码中通过调用Context的*registerReceiver（）*方法进行动态注册BroadcastReceiver，具体代码如下：

动态广播最好在Activity的onResume()注册、onPause()注销。原因：

* 对于动态广播，有注册就必然得有注销，否则会导致内存泄露

* 重复注册、重复注销也不允许

### 广播发送者向AMS发送广播

* ①. 普通广播（Normal Broadcast）

即开发者自身定义intent的广播（最常用）。发送广播使用如下：

```
Intent intent = new Intent();
//对应BroadcastReceiver中intentFilter的action
intent.setAction(BROADCAST_ACTION);
//发送广播
sendBroadcast(intent);
```

* 系统广播（System Broadcast）

Android中内置了多个系统广播：只要涉及到手机的基本操作（如开机、网络状态变化、拍照等等），都会发出相应的广播

* 有序广播（Ordered Broadcast）
定义 发送出去的广播被广播接收者按照先后顺序接收

有序是针对广播接收者而言的

广播接受者接收广播的顺序规则（同时面向静态和动态注册的广播接受者）

按照Priority属性值从大-小排序；
Priority属性相同者，动态注册的广播优先；
特点

接收广播按顺序接收
1. 先接收的广播接收者可以对广播进行截断，即后接收的广播接收者不再接收到此广播；
2. 先接收的广播接收者可以对广播进行修改，那么后接收的广播接收者将接收到被修改后的广播
3. 具体使用 有序广播的使用过程与普通广播非常类似，差异仅在于广播的发送方式：

```
sendOrderedBroadcast(intent);
```

* App应用内广播（Local Broadcast）

背景 Android中的广播可以跨App直接通信（exported对于有intent-filter情况下默认值为true）

```
//注册应用内广播接收器
//步骤1：实例化BroadcastReceiver子类 & IntentFilter mBroadcastReceiver 
mBroadcastReceiver = new mBroadcastReceiver(); 
IntentFilter intentFilter = new IntentFilter(); 

//步骤2：实例化LocalBroadcastManager的实例
localBroadcastManager = LocalBroadcastManager.getInstance(this);

//步骤3：设置接收广播的类型 
intentFilter.addAction(android.net.conn.CONNECTIVITY_CHANGE);

//步骤4：调用LocalBroadcastManager单一实例的registerReceiver（）方法进行动态注册 
localBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);

//取消注册应用内广播接收器
localBroadcastManager.unregisterReceiver(mBroadcastReceiver);

//发送应用内广播
Intent intent = new Intent();
intent.setAction(BROADCAST_ACTION);
localBroadcastManager.sendBroadcast(intent);
```
