# 常见问题分析1-yunfeng
## 一. 问题汇总
1. JVM。垃圾回收机制，标记算法，回收算法的使用
2. 字节码
3. Java内存模型，堆栈相关内容
4. Voltile和synchronize两个关键字, AQS ，CAS
5. Glide原理，当Activity销毁后，图片内存的处理机制，三级缓存，LRUCache
6. OKhttp任务链
7. Http2
8. https证书安全
9. 手机硬件安全，防篡改
10. IPC机制，为啥要自创Bundle模式，其优缺点.通信过程中的复制次数，socket复制次数，binder只复制一次的原理
11. 算法：单向链表的翻转
12. HashMap的构造函数传size，代码内部会发生什么; 链表转红黑树的条件是为啥
13. 性能优化，怎么分析内存泄漏，LeakCanary监测原理

# pingan
问题汇总：
1. TCP/IP理解
TCP/IP有哪几层
TCP的特点
三次握手
四次挥手，四次挥手中，服务端向客户端发送断开请求，客户端没收到怎么办，重试时间是多久
通信过程中客户端断电服务端会出现什么情况
DNS解析原理
DNS防劫持
2. HashMap
底层实现是怎么样的
扩容机制
无参和有参构造函数的区别，初始容量可以随便传吗？怎么计算初始容量
红黑树的时间复杂度
红黑树与其他树的对比，优势
3. Handler机制
消息机制原理
为什么调用Loop.prepare，而不是直接new
消息队列是什么数据结构
4. 图片加载，LruCache算法
图片加载怎么做的
LruCache算法实现原理
LinkedHashMap访问元素是怎么处理的，元素可不可以指定添加到头部
5. ipc通信方式有哪些，Binder理解
Binder数据层原理
Binder在安卓端的实现，客户端是怎么拿到服务端的代理类
6. 适配器模式的特点，有哪几种，构建者模式怎么做
7. 做过哪些性能优化

tcl
1. 怎么保证项目按时高质量交付
2. 发现的问题怎么从流程上闭环，问题回溯流程
3. 团队管理相关的介绍，氛围提升，技术提升，绩效考核
4. 技术提升和技术分享具体怎么做的，频率怎么样，效果怎么样
5. 怎么评定组员绩效，有哪些指标？ 组员技术水平的提升，怎么看出来
6. 怎么理解组件化和插件化，ARouter原理
7. app的架构选择思路，为什么选择mvvm，其对比其他框架的优缺点是什么
8. 做过那些性能优化，总结下
9. 售卖机机器离线是怎么保障的，怎么提高机器运维效率
10. 电商app的支付了订单但拿不到货的问题怎么闭环
11. 项目上的技术安全是怎么做的：网络安全，资金安全，隐私安全等等
12. Mqtt长连接的原理，qos怎么设置的，选择mqtt的原因


货拉拉
一面：
StringBuffer StringBuilder 区别 线程安全
des aes
app cpu监控  内存监控
PreShareference  是否可以跨进程通信，怎么进行缓存的跨进程操作
自旋锁 互斥锁
设计模式
app安全
app保活
app数据安全 加固  混淆  JNI技术
HashMap和HastTable的区别
Hashmap的数据结构情况
Handler
wait和sheep的区别

二面：
哪些类可以作为GcRoot对象
Activity启动流程
插件化怎么实现的，未在manifast中中注册的Activity是怎么处理的：hook
Apk安装过程
dagger2  inject  privider使用场景
AMS
PMS
View的粘性事件处理
ViewModel源码 其内部实现机制
生产消费者模型
kotlin中的let wait方法使用区别
组件化  ARouter原理
性能监控
Sychronzie高版本的优化有哪些
AQS是否了解
设计模式
性能优化监控,做过哪些性能优化,省电优化怎么做
Kotlin的协程怎么做同步
HashMap数据结构原理


## 二. 分问题解答
### 1. JVM垃圾回收机制
