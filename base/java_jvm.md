[回目录页](..)

# Java虚拟机

## 重点学习资料
[Intro.pdf](/Files/Java_JVM_II.pdf)

## Java内存区域和内存溢出
### java运行时的数据区

![avatar](/image/java_jvm_data_area.png)

1. 程序计数器
   程序计数器（Program Counter Register）是一块较小的内存空间，它可以看作是当前**线程**所执行的字节码的行号指示器。字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。
2. 虚拟机栈，本地方法栈:方法运行的内存模型，是线程私有的
3. 数据堆：存储对象和常量数据
4. 分析数据是否为垃圾的方法：引用计数算法，可达性算法
5. 清除垃圾的算法包括：标记清除法，复制法，标记整理法，分代收集算法

### 分带收集算法
大部分的对象都很短命，都在很短的时间内都被回收了（IBM 专业研究表明，一般来说，98% 的对象都是朝生夕死的，经过一次 Minor GC 后就会被回收），所以分代收集算法根据对象存活周期的不同将堆分成新生代和老生代
1. 新生代：新生代又分为 Eden 区， from Survivor 区（简称S0），to Survivor 区(简称 S1),三者的比例为 8: 1 : 1。
   * 第一次GC时先标记Eden区的存活对象，然后使用复制算法把存活对象复制到S0区并清空Eden。第二次GC时会把Eden和S0存活的对象复制到S1,并清空eden和S0.
   * 每次GC时，新生代中的存活对象都会对其年龄加1
   * 当新生代对象的年龄达到阈值，则会呗移动到老年代中
   * 大对象会直接分配到老年代中
   * 在 S0（或S1） 区相同年龄的对象大小之和大于 S0（或S1）空间一半以上时，则年龄大于等于该年龄的对象也会晋升到老年代。
2. 老年代的空间担保：虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象的总空间，如果大于，那么Minor GC 可以确保是安全的,如果不大于，那么虚拟机会查看 HandlePromotionFailure 设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代对象的平均大小，如果大于则进行 Minor GC，否则可能进行一次 Full GC。
3. Stop The World：如果老年代满了，会触发 Full GC, Full GC 会同时回收新生代和老年代（即对整个堆进行GC），它会导致 Stop The World（简称 STW）,造成挺大的性能开销。什么是 STW ？所谓的 STW, 即在 GC（minor GC 或 Full GC）期间，只有垃圾回收器线程在工作，其他工作线程则被挂起。

### Java堆和栈的区别
1. 栈存储的时线程的局部变量和方法调用，堆是存储全局的Java对象
2. 栈是线程独享，堆是全局共享
3. 栈满了后报StackOverFlowErro,堆满了是会报OutOfMemeryErro
4. 栈内存是远远低于堆内存的，例如栈内递归没及时跳出，会报StackOverFlowErro

### 强引用，软引用，弱引用，虚引用
1. 强引用，系统宁愿发生OutOfMemery也不会回收
2. 软引用，如果内存空间充足，则不会呗回收，若是不充足时则不会被回收
> 垃圾收集线程会在虚拟机抛出OutOfMemoryError之前回收软引用对象，而且虚拟机会尽可能优先回收长时间闲置不用的软引用对象。SoftReference  ReferenceQueue
3. 弱引用：一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
