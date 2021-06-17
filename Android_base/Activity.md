[回目录页](..)

# Acvity全解析

**生命周期经典图**
![avatar](/image/activiy_life.png)

## 生命周期

### 几种普通情况

1. 针对一个特定的Activity，第一次启动，回调如下：onCreate()->onStart()->onResume()

3. 用户打开新的Activiy的时候，上述Activity的回调如下：onPause()->onStop()

4. 再次回到原Activity时，回调如下：onRestart()->onStart()->onResume()

5. 按back键回退时，回调如下：onPause()->onStop()->onDestory()

6. 按Home键切换到桌面后又回到该Actitivy，回调如下：onPause()->onStop()->onRestart()->onStart()->onResume()

7. 调用finish()方法后，回调如下：onDestory()(以在onCreate()方法中调用为例，不同方法中回调不同，通常都是在onCreate()方法中调用)

### 横竖屏切换时生命周期

   在横竖屏切换过程中，会发生Activity被销毁并重建的过程

   横竖屏切换过程中，有两个重要回调要注意：onSaveInstanceState和onRestoreInstanceState
> 在Activity由于异常情况下终止时，系统会调用onSaveInstanceState来保存当前Activity的状态。这个方法的调用是在onStop之前，它和onPause没有既定的时序关系，该方法只在Activity被异常终止的情况下调用。当异常终止的Activity被重建以后，系统会调用onRestoreInstanceState，并且把Activity销毁时onSaveInstanceState方法所保存的Bundle对象参数同时传递给onRestoreInstanceState和onCreate方法。因此，可以通过onRestoreInstanceState方法来恢复Activity的状态，该方法的调用时机是在onStart之后。其中onCreate和onRestoreInstanceState方法来恢复Activity的状态的区别： onRestoreInstanceState回调则表明其中Bundle对象非空，不用加非空判断。onCreate需要非空判断。建议使用onRestoreInstanceState。

  横竖屏切换的生命周期：onPause()->onSaveInstanceState()-> onStop()->onDestroy()->onCreate()->onStart()->onRestoreInstanceState->onResume()

  可以通过在AndroidManifest文件的Activity中指定如下属性：

```
android:configChanges = "orientation| screenSize"
```

来避免横竖屏切换时，Activity的销毁和重建，而是回调了下面的方法：

```
@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
```

### 资源内存不足时导致优先级不高的Activiy被杀死

**Activity优先级排序：**

(1) 前台Activity——正在和用户交互的Activity，优先级最高。

(2) 可见但非前台Activity——比如Activity中弹出了一个对话框，导致Activity可见但是位于后台无法和用户交互。

(3) 后台Activity——已经被暂停的Activity，比如执行了onStop，优先级最低

### Activity的三种运行状态

①Resumed（活动状态）

又叫Running状态，这个Activity正在屏幕上显示，并且有用户焦点。这个很好理解，就是用户正在操作的那个界面。

②Paused（暂停状态）

这是一个比较不常见的状态。这个Activity在屏幕上是可见的，但是并不是在屏幕最前端的那个Activity。比如有另一个非全屏或者透明的Activity是Resumed状态，没有完全遮盖这个Activity。

③Stopped（停止状态）

当Activity完全不可见时，此时Activity还在后台运行，仍然在内存中保留Activity的状态，并不是完全销毁。这个也很好理解，当跳转的另外一个界面，之前的界面还在后台，按回退按钮还会恢复原来的状态，大部分软件在打开的时候，直接按Home键，并不会关闭它，此时的Activity就是Stopped状态。

## Activity的启动模式

  Activity管理是使用栈的形式，后进先出

**主要包括以下四种模式:**

1. Standard


   每启动一次Activity，就会创建一个新的Activity实例并置于栈顶。谁启动了这个Activity，那么这个Activity就运行在启动它的那个Activity所在的栈中。

  **特殊情况，如果在Service或Application中启动一个Activity，其并没有所谓的任务栈，可以使用标记位Flag来解决。解决办法：为待启动的Activity指定FLAG_ACTIVITY_NEW_TASK标记位，创建一个新栈。**

2. SigleTask

   该模式是一种单例模式，即一个栈内只有一个该Activity实例

   可以通过在AndroidManifest文件的Activity中指定该Activity需要加载到那个栈中，即singleTask的Activity可以指定想要加载的目标栈。singleTask和taskAffinity配合使用，指定开启的Activity加入到哪个栈中。

```
<activity android:name=".Activity1"
	android:launchMode="singleTask"
	android:taskAffinity="com.lvr.task"
	android:label="@string/app_name">
</activity>
```

  关于**taskAffinity**的值： 每个Activity都有taskAffinity属性，这个属性指出了它希望进入的Task。如果一个Activity没有显式的指明该Activity的taskAffinity，那么它的这个属性就等于Application指明的taskAffinity，如果Application也没有指明，那么该taskAffinity的值就等于包名。

  一个SingleTask模式的Activity被创建时，如果所在栈有该Activity实例，则把该Activity实例之上的Activity杀死清除出栈，重用并让该Activity实例处在栈顶，然后调用onNewIntent()方法。

  singleTask会具有clearTop特性，把之上的栈内Activity清除。

  **应用场景**： 大多数App的主页。对于大部分应用，当我们在主界面点击回退按钮的时候都是退出应用，那么当我们第一次进入主界面之后，主界面位于栈底，以后不管我们打开了多少个Activity，只要我们再次回到主界面，都应该使用将主界面Activity上所有的Activity移除的方式来让主界面Activity处于栈顶，而不是往栈顶新加一个主界面Activity的实例，通过这种方式能够保证退出应用时所有的Activity都能报销毁。在跨应用Intent传递时，如果系统中不存在singleTask Activity的实例，那么将创建一个新的Task，然后创建SingleTask Activity的实例，将其放入新的Task中。

3. SingleInstance

   作为栈内复用模式（singleTask）的加强版,打开该Activity时，直接创建一个新的任务栈，并创建该Activity实例放入新栈中。

   一旦该模式的Activity实例已经存在于某个栈中，任何应用再激活该Activity时都会重用该栈中的实例

4. SingleTop

   如果需要新建的Activity位于任务栈栈顶，那么此Activity的实例就不会重建，而是重用栈顶的实例。并回调如下方法：

   SingleTop模式，在栈中还是会存在多个同Activity实例，只是栈顶只能存在一个同Activity实例





