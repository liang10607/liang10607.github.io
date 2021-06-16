[回目录页](..)

# Java虚拟机

## 重点学习资料
[Intro.pdf](/Files/Java_JVM_II.pdf)

## Java内存区域和内存溢出
### java运行时的数据区

![avatar](/image/java_jvm_data_area.png)
> 方法区（公有）： 用户存储已被虚拟机加载的类信息，常量，静态常量，即时编译器编译后的代码等数据。异常状态 OutOfMemoryError

> 其中包含常量池：用户存放编译器生成的各种字面量和符号引用。

> 堆（公有）： 是JVM所管理的内存中最大的一块。唯一目的就是存放实例对象，几乎所有的对象实例都在这里分配。Java堆是垃圾收集器管理的主要区域，因此很多时候也被称为“GC堆”。异常状态 OutOfMemoryError

> 虚拟机栈（线程私有）： 描述的是java方法执行的内存模型：每个方法在执行时都会创建一个栈帧，用户存储局部变量表，操作数栈，动态连接，方法出口等信息。每一个方法从调用直至完成的过程，就对应着一个栈帧在虚拟机栈中入栈到出栈的过程。 对这个区域定义了两种异常状态 OutOfMemoryError StackOverflowError

> 本地方法栈（线程私有）: 与虚拟机栈所发挥的作用相似。它们之间的区别不过是虚拟机栈为虚拟机执行java方法，而本地方法栈为虚拟机使用到的Native方法服务。

> 程序计数器（线程私有）： 一块较小的内存，当前线程所执行的字节码的行号指示器。字节码解释器工作时，就是通过改变这个计数器的值来选取下一条需要执行的字节码指令。

关于主内存与工作内存之间具体的交互协议，即一个变量如何从主内存拷贝到工作内存、如何从工作内存同步回主内存之类的实现细节，Java内存模型中定义了8种操作来完成，并且每种操作都是原子的、不可再分的。

八种操作：

| 类型 |	说明|
| --- | --- |
| lock | 	作用于主内存的变量，把一个变量标识为一条线程独占的状态|
| unlock | 	作用于主内存的变量，把一个处于锁定状态的变量释放出来。|
| read | 	把一个变量的值从主内存传输到工作内存中，以便随后的load使用。|
| load | 	把read操作从主内存中得到的变量值放入到工作内存的变量副本中。|
| use | 	把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作。|
| assign | 	把一个从执行引擎中接收到的值赋值给工作内存中的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。|
| store	|  把工作内存中的一个变量的值传递到主内存，以便随后的write使用。|
| write	|  把store操作从工作内存中得到的变量值放入到主内存的变量中。|

1. 程序计数器
   程序计数器（Program Counter Register）是一块较小的内存空间，它可以看作是当前**线程**所执行的字节码的行号指示器。字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。
2. 虚拟机栈，本地方法栈:方法运行的内存模型，是线程私有的
3. 数据堆：存储对象和常量数据
4. 分析数据是否为垃圾的方法：引用计数算法，可达性算法
5. 清除垃圾的算法包括：标记清除法，复制法，标记整理法，分代收集算法

### 分代收集算法
大部分的对象都很短命，都在很短的时间内都被回收了（IBM 专业研究表明，一般来说，98% 的对象都是朝生夕死的，经过一次 Minor GC 后就会被回收），所以分代收集算法根据对象存活周期的不同将堆分成新生代和老生代
1. 新生代：新生代又分为 Eden 区， from Survivor 区（简称S0），to Survivor 区(简称 S1),三者的比例为 8: 1 : 1。
   * 第一次GC时先标记Eden区的存活对象，然后使用复制算法把存活对象复制到S0区并清空Eden。第二次GC时会把Eden和S0存活的对象复制到S1,并清空eden和S0.
   * 每次GC时，新生代中的存活对象都会对其年龄加1
   * 当新生代对象的年龄达到阈值，则会呗移动到老年代中
   * 大对象会直接分配到老年代中
   * 在 S0（或S1）区相同年龄的对象大小之和大于 S0（或S1）空间一半以上时，则年龄大于等于该年龄的对象也会晋升到老年代。
2. 老年代的空间担保：虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象的总空间，如果大于，那么Minor GC 可以确保是安全的,如果不大于，那么虚拟机会查看 HandlePromotionFailure 设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代对象的平均大小，如果大于则进行 Minor GC，否则可能进行一次 Full GC。
3. Stop The World：如果老年代满了，会触发 Full GC, Full GC 会同时回收新生代和老年代（即对整个堆进行GC），它会导致 Stop The World（简称 STW）,造成挺大的性能开销。什么是 STW ？所谓的 STW, 即在 GC（minor GC 或 Full GC）期间，只有垃圾回收器线程在工作，其他工作线程则被挂起。
4. 年轻代的垃圾回收算法使用的是复制算法，复制算法的基本思想就是将内存分为两块，每次只用其中一块，当这一块内存用完，就将还活着的对象复制到另外一块上面。复制算法不会产生内存碎片

### **垃圾收集器**
![avatar](/image/gc_condition.png)
上图展示了7种作用于不同分代的收集器，如果两个收集器之间存在连线，说明它们可以搭配使用。
  *  其中Serial,ParNew,Parallel Scavenge属于新生代的垃圾收集器；使用复制算法
  *  CMS,Serial Old,Paralel Old属于老年代的回收器，G1则两者都可以使用；使用标记整理算法

#### 1.Serial收集器
   最早的垃圾收集器，是一个单线程收集器。它在进行垃圾收集器时，必须暂停其他所有的工作线程，直到它收收集结束。
 ![avatar](/image/Serial_gc_conditon.png)  
   **优势：** 简单而高效，单CPU情况下，无线程切换开销，可以获得最高的单线程效率

#### 2. ParNew收集器
    ParNew收集器就是Seial收集器的多线程版本，其默认开启的收集线程数与CPU的数量相同
 ![avatar](/image/ParNew_gc_condition.png)

#### 3. Parallel Scavenge收集器
![avatar](/image/Parallel_Scavenge_gc_condition.png)

    是一个并行的多线程收集器，目标是达到一个可控制的**吞吐量**。吞吐量= 运行用户代码的时间/(运行用户代码时间+垃圾收集时间)
     
     主要适合在后台运行而不需要太多交互的任务。

#### 4. Serial Old收集器
如果在Server模式下，它主要还有两大用途：

1.与Parallel Scavenge收集器搭配使用

2.作为CMS收集器的后备预案，在并发收集发生Conurrent Mode Failure使用。

#### 5. Parallel  Old收集器
   在注重吞吐量以及CPU资源敏感的场合，都可以优先考虑Parallel Scavenge+Parallel Old收集器

#### 6. CMS收集器
![avatar](/image/cms_gc_condition.png)

   第一款真正意义上的并发收集器，它第一次实现了让垃圾收集器与用户线程同时工作，尽可能缩短垃圾收集时用户线程的停顿时间

   CMS也是基于标记-整理算法实现

   CMS收集器是基于“标记-清除”算法实现的，整个过程分为4个步骤：

①初始标记

②并发标记

③重新标记

④并发清除

其中，初始标记，重新标记这两个步骤仍然需要“Stop The World”。初始标记仅仅只标记一下GC Roots能直接关联到的对象，速度很快。并发标记阶段就是 进行GC Roots Tracing的过程。

重新标记阶段则是为了修正并发标记期间因用户程序继续运作而导致标记产生变动的那一部分对象的标记几率，这个阶段的停顿时间一般会比初始标记阶段稍长，但远比并发标记时间短。

整个过程耗时最长的阶段是并发标记，并发清除过程，但这两个过程可以和用户线程一起工作。

**缺点**
1. 对CPU资源非常敏感，不会导致用户线程停顿，但会占用一部分线程
2. CMS无法处理浮动垃圾。因为CMS是并发执行回收，在并发执行过程中新产生的垃圾叫浮动垃圾。当老年代使用68%就会激活CMS收集器，而不是像其他垃圾收集器可以等待使用空间满。当预留空间超过68，则启动Seril Old后备预案清理垃圾
3. CMS是基于标记-清除算法实现的垃圾收集器，会有大量的空间碎片产生

#### 7. G1收集器
  是一款面向服务端应用的垃圾收集器
   特点：

①并行与并发

能充分利用多CPU，多核环境下的硬件优势，**缩短Stop-The-World停顿的时间**，同时可以通过并发的方式让Java程序继续执行

②分代收集

可以**不需要其他收集器**的配合管理整个堆，但是仍采用不同的方式去处理分代的对象。

③空间整合

G1从整体上来看，采用基于“标记-整理”算法实现收集器

G1从局部上来看，采用基于“复制”算法实现。

④可预测停顿

使用G1收集器时，Java堆内存布局与其他收集器有很大差别，它将整个Java堆划分成为多个大小相等的独立区域。 G1跟踪各个Region里面的垃圾堆积的价值大小（回收所获得的空间大小以及回收所需时间的经验值），在后台维护一个优先列表，每次根据允许的收集时间，优先回收价值最大的Region。

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

## Javad对象创建,内存布局，访问定位
### 对象创建
new指令->常量池找类的符号引用->检查该类是否被加载，解析和初始化，没有的话，则执行加载动作->为对象分配内存空间

### 对象的内存布局
对象内存分为三个部分：对象头，实例数据，对齐补充

#### 对象头
  * 第一部分：对象自身运行数据，如Hash码，GC分代年龄，锁状态标记，线程持有的锁，偏向线程ID,偏向锁等；分别占用32bit或者64bit,官方称其为Mark Word
  * 第二部分：类型指针，虚拟机通过该指针判断对象是属于哪个类的实例。若是java数组，则对象头还有个字段用于标示数组长度

#### 实例数据
是对象真正存储的有效信息，也是在程序代码中所定义的各种类型的字段内容

#### 对齐填充
对齐填充不是必然存在的。HotSpot VM的自动内存管理系统要求对象起始地址必须是8字节的整数倍，对象头是符合要求的， 但实例数据根据实际存储情况进行补齐

### 对象的访问定位
ava程序需要通过栈上了reference数据来操作堆上的具体对象。

目前主流的访问方式有使用句柄和直接指针两种。

* 句柄访问
![avatar](/image/refenrence_visit_1.png)

* 直接访问
![avatar](/image/refenrence_2.png)

## Java类加载机制
### 定义
    把描述类的数据从class文件加载到内存中，并对数据进行校验，转化解析和初始化，最终形成可以被虚拟机直接使用的Java类型
    
    类的加载过程都在程序运行期间完成的，这会带来性能的一些消耗， 但也使得java语言具备更多的动态和灵活性特征

### 类的生命周期

Java类的生命周期顺序为：加载，验证，准备，解析，初始化，使用，卸载。 其中解析可能在初始化之后，具体的步骤图如下：

**其中加载，验证，准备，解析及初始化是属于类加载机制中的步骤。注意此处的加载不等同于类加载。**

![avatar](/image/java_impl_life.png)

### 触发类加载的条件
1. new一个对象，putstatic和getstatic(读取和设置静态变量)，invokestatic（调用静态方法）
2. java.lang.reflect包的方法对类进行**反射**调用的时候
3. 程序入口main对应的类，会先被加载和初始化
4. 如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic,REF_putStatic,REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行初始化，则需要先出发初始化

### 类加载的具体过程

1. 加载：
  * 通过类的全限定名获取定义此类的二进制字节楼
  * 蒋这个字节流所代表的静态存储结构转化为方法区内的运行时数据结构
  * 在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口

2. 验证: 符合当前虚拟机的要求，并且不会危害虚拟机自身的安全
 * 文件格式验证: 是否符合Class文件格式的规范
 * 元数据验证：对类的元数据(描述数据的数据)信息进行 **语义校验**，看是否符合Java语言规范
 * 字节码验证：通过数据流和控制流分析，确定程序语义时合法的，符合逻辑的。同时也会对方法体进校验分析，避免威胁系统运行安全
 * 符号引用验证：蒋符号引用转化为直接引用，符号验证的目的时保障解析动作能正常运行

3. 准备：正式为类变量分配内存并设置类变量初始值，这些变量使用的内存将在方法区中分配，这里说的类变量指的时static静态变量。实例变量仍然时放在java堆中

4. 解析：虚拟机将常量池内的符号引用替换为直接引用的过程

5. 初始化：类加载机制的最后一步，初始化阶段时执行类构造器方法的过程，初始化类变量动作和静态语句块。简单地说，**初始化就是对类变量进行赋值及执行静态代码块**

### 类加载器
  类加载部分是将类的class二进制文件读入内存，并为之创建一个Java.lang.Class对象。这部分是由类加载器来实现的

#### 类加载器分类
1. 启动类加载器（Bootstrap ClassLoader）:由C++语言实现，负责将存放在<JAVA_HOMe>\lib目录下或者-Xbootclasspath参数指定的路劲中的类库加载到内存中，即负责加载Java核心类
2. 其他类加载器：由Java语言实现，继承自抽象类ClassLoader. 如
   * 扩展类加载器（Extension ClassLoader）:负责加载<JAVA_HOME>\lib\ext目录或者java.ext.dirs系统变量指定的路劲中的所有类库，即负责加载Java扩展的核心类之外的类
   * 应用程序类加载器（Application ClassLoader）:负责加载**用户**类路径（classpath）上指定类库，可以在java代码中使用ClassLoader.getSystemClassLoader()方法直接获取
**注意**：我们可以直接使用这个类加载器。一般情况，如果我们没有自定义类加载器默认就是用这个加载器。
![avatar](/image/double_link_class_load.png)


#### 双亲委派模型
[双亲委派机制实例](https://www.jianshu.com/p/acc7595f1b9d)
* 双亲委派模型的工作流程是：
   如果一个类加载器收到类的加载请求，它首先不会自己去尝试加载这个类，二十把请求委托给**父加载器**去完成，依次向上。因此所有的类加载器最终都应该被传递到顶层的启动类加载器中，

* 双亲委派好处：
> 黑客自定义一个java.lang.String类，该String类具有系统的String类一样的功能，只是在某个函数稍作修改。比如equals函数，这个函数经常使用，如果在这这个函数中，黑客加入一些“病毒代码”。并且通过自定义类加载器加入到JVM中。此时，如果没有双亲委派模型，那么JVM就可能误以为黑客自定义的java.lang.String类是系统的String类，导致“病毒代码”被执行。

> 或许你会想，我在自定义的类加载器里面强制加载自定义的java.lang.String类，不去通过调用父加载器不就好了吗?确实，这样是可行。但是，在JVM中，判断一个对象是否是某个类型时，如果该对象的实际类型与待比较的类型的类加载器不同，那么会返回false。判断一个实例是否属于某个类时，不仅判断类名等元数据而且会判断是否为同一个类加载器

### 自定义类加载器
#### LoadClass方法
```
protected Class<?> loadClass(String name, boolean resolve)
    throws ClassNotFoundException
{
    synchronized (getClassLoadingLock(name)) {
        // First, check if the class has already been loaded
        Class c = findLoadedClass(name);
        if (c == null) {
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                    c = parent.loadClass(name, false);
                } else {
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException thrown if class not found
                // from the non-null parent class loader
            }

            if (c == null) {
                // If still not found, then invoke findClass in order
                // to find the class.
                long t1 = System.nanoTime();
                c = findClass(name);

                // this is the defining class loader; record the stats
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
```
> 1. 首先，检查一下指定名称的类是否已经加载过，如果加载过了，就不需要再加载，直接返回。
  2. 如果此类没有加载过，那么，再判断一下是否有父加载器；如果有父加载器，则由父加载器加载（即调用parent.loadClass(name, false);）.或者是调用bootstrap类加载器来加载。
  3. 如果父加载器及bootstrap类加载器都没有找到指定的类，那么调用当前类加载器的findClass方法来完成类加载。
**所以，要实现自定义类加载器，则需重写findClass方法**  

#### findClass()方法
   父加载器和boot加载器都没成功加载到class, 走到自定义类加载器则会默认抛出ClassNotFoundException.具体代码如下：
```
protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
}
```   
   如果是是读取一个指定的名称的类为byte数组的话，这很好办。但是如何将字节数组转为Class对象呢？很简单，Java提供了**defineClass**方法，通过这个方法，就可以把一个字节数组转为Class对象啦~. 即要实现自定义的findClass，需要依赖defineClass方法
   
### defineClass()方法
> 将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。如，假设class文件是加密过的，则需要解密后作为形参传入defineClass函数。
```
protected final Class<?> defineClass(String name, byte[] b, int off, int len)
        throws ClassFormatError  {
        return defineClass(name, b, off, len, null);
}
```  
### 自定义类加载器调用过程
 ![avatar](/image/custom_class_load.png)
```
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class Main {
    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;

        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

    };

    public static void main(String args[]) throws Exception {
        MyClassLoader classLoader = new MyClassLoader("D:/test");
        Class clazz = classLoader.loadClass("com.huachao.cl.Test");
        Object obj = clazz.newInstance();
        Method helloMethod = clazz.getDeclaredMethod("hello", null);
        helloMethod.invoke(obj, null);
    }
}
```


