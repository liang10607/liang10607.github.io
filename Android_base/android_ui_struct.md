[回目录页](..)

# Android的UI的层次结构

![Android UI结构](/image/activity_view_layer.png)


其实ViewRootImpl的作用不止如此，还有许多功能，如事件分发。

要知道，当用户点击屏幕产生一个触摸行为，这个触摸行为则是通过底层硬件来传递捕获，然后交给ViewRootImpl，接着将事件传递给DecorView，而DecorView再交给PhoneWindow，PhoneWindow再交给Activity，然后接下来就是我们常见的View事件分发了。

硬件 -> ViewRootImpl -> DecorView -> PhoneWindow -> Activity

不详细介绍了，如果感兴趣，可以看这篇文章。

由此可见ViewRootImpl的重要性，是个连接器，负责WindowManagerService与DecorView之间的通信。

通过以上了解可以知道，Activity就像个控制器，不负责视图部分。Window像个承载器，装着内部视图。DecorView就是个顶层视图，是所有View的最外层布局。ViewRoot像个连接器，负责沟通，通过硬件的感知来通知视图，进行用户之间的交互。