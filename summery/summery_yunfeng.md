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

## 二. 分问题解答
### 1. JVM垃圾回收机制
