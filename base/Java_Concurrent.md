[回目录页](..)

# Java并发
## Java创建线程的三种方式
1. 直接使用Thread类创建
2. 实现Runable接口，并通过Thread类来启动
3. 通过实现Callable接口，并FutureTask来包装运行Callable实现，最终使用Thread来启动Callable线程; 其中可通过FutureTask来获取Callablexi线程执行结果
 * （1）创建Callable接口的实现类，并实现call()方法，该call()方法将作为线程执行体，并且有返回值。

 * （2）创建Callable实现类的实例，使用FutureTask类来包装Callable对象，该FutureTask对象封装了该Callable对象的call()方法的返回值。

 * （3）使用FutureTask对象作为Thread对象的target创建并启动新线程。

 * （4）调用FutureTask对象的get()方法来获得子线程执行结束后的返回值，调用get()方法会阻塞线程。

 Callable创建的线程核心代码如下：


```

public class CallableThreadTest implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int i = 0;
        for (; i < 2; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
        return i;
    }
}
CallableThreadTest ctt = new CallableThreadTest();
FutureTask<Integer> ft = new FutureTask<>(ctt);
new Thread(ft, "有返回值的线程").start();
 System.out.println("子线程的返回值：" + ft.get());
```

## Java线程池
### 线程池的优势
* 通过复用线程，也可以减少线程创建销毁时的资源消耗，管控线程创建数量，减少资源消耗，避免线程创建的等待时间
* 管控线程的过度创建，提高资源利用的效率
* 线程池还提供一些额外的功能

### 线程池的使用
* 现场池中，线程队列有四种，分别是

| 阻塞队列 |	说明 |
| --- | --— |
| ArrayBlockingQueue | 基于数组实现的有界的阻塞队列,该队列按照FIFO（先进先出）原则对队列中的元素进行排序。|
|LinkedBlockingQueue	 | 基于链表实现的阻塞队列，该队列按照FIFO（先进先出）原则对队列中的元素进行排序。|
|SynchronousQueue	 | 内部没有任何容量的阻塞队列。在它内部没有任何的缓存空间。对于SynchronousQueue中的数据元素只有当我们试着取走的时候才可能存在。|
|PriorityBlockingQueue	 | 具有优先级的无限阻塞队列。|

* 可以通过execute和submit两种方式来向线程池提交一个任务。

   > execute 当我们使用execute来提交任务时，由于execute方法没有返回值，所以说我们也就无法判定任务是否被线程池执行成功。当我们使用submit来提交任务时,它会返回一个future,我们就可以通过这个future来判断任务是否执行成功，还可以通过future的get方法来获取返回值。如果子线程任务没有完成，get方法会阻塞住直到任务完成，而使用get(long timeout, TimeUnit unit)方法则会阻塞一段时间后立即返回，这时候有可能任务并没有执行完
* 线程池执行流程描述为：
> 1. 线程池中的线程数量没有达到核心的线程数量，这时候就回启动一个核心线程来执行任务。

> 2. 如果线程池中的线程数量已经超过核心线程数，这时候任务就会被插入到任务队列中排队等待执行。

> 3. 由于任务队列已满，无法将任务插入到任务队列中。这个时候如果线程池中的线程数量没有达到线程池所设定的最大值，那么这时候就会立即启动一个非核心线程来执行任务。

> 4. 如果线程池中的数量达到了所规定的最大值，那么就会拒绝执行此任务，这时候就会调用RejectedExecutionHandler中的rejectedExecution方法来通知调用者。

![avatar](/image/Thread_pool_flow.png)

### 四种线程池类

Java中四种具有不同功能常见的线程池。他们都是直接或者间接配置ThreadPoolExecutor来实现他们各自的功能。这四个线程池可以通过Executors类获取。
这四种线程池分别是
* newFixedThreadPool：所容纳最大的线程数就是我们设置的核心线程数，只有核心线程，并且这些线程都不会被回收，也就是它能够更快速的响应外界请求

```
public static ExecutorService newFixedThreadPool(int nThreads) {
	return new ThreadPoolExecutor(nThreads, nThreads,
		0L, TimeUnit.MILLISECONDS,
		new LinkedBlockingQueue<Runnable>());
}
```

* newCachedThreadPool：核心线程数为0， 线程池的最大线程数Integer.MAX_VALUE；


```
public static ExecutorService newCachedThreadPool() {
	return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
		60L, TimeUnit.SECONDS,
		new SynchronousQueue<Runnable>());
}
```

* newScheduledThreadPool:它的核心线程数是固定的，对于非核心线程几乎可以说是没有限制的，并且当非核心线程处于限制状态的时候就会立即被回收.ScheduledExecutorService功能强大，对于定时执行的任务，建议多采用该方法


```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}
public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
          new DelayedWorkQueue());
}
```

* newSingleThreadExecutor：在这个线程池中只有一个核心线程，对于任务队列没有大小限制，也就意味着这一个任务处于活动状态时，其他任务都会在任务队列中排队等候依次执行。


###  线程池的使用技巧

|任务类别|说明|
| --- | --- |
| CPU密集型任务 | 	线程池中线程个数应尽量少，如配置N+1个线程的线程池。|
| IO密集型任务	 | 由于IO操作速度远低于CPU速度，那么在运行这类任务时，CPU绝大多数时间处于空闲状态，那么线程池可以配置尽量多些的线程，以提高CPU利用率，如2*N。|
| 混合型任务	 | 可以拆分为CPU密集型任务和IO密集型任务，当这两类任务执行时间相差无几时，通过拆分再执行的吞吐率高于串行执行的吞吐率，但若这两类任务执行时间有数据级的差距，那么没有拆分的意义。|

### 线程池常见面试题
  待完善

## 线程锁的一些概念

1 重入锁的概念

   当线程获取到对象的锁后，同一个线程再次请求该对象锁，直接获得，不会被阻塞； 这就是锁的重入，Sychronized和ReentrantLock都是可重入锁。重入锁实例如下：

```
public class SynchronizedTest {
    public void method1() {
        synchronized (SynchronizedTest.class) {
            System.out.println("方法1获得ReentrantTest的锁运行了");
            method2();
        }
    }
    public void method2() {
        synchronized (SynchronizedTest.class) {
            System.out.println("方法1里面调用的方法2重入锁,也正常运行了");
        }
    }
    public static void main(String[] args) {
        new SynchronizedTest().method1();
    }
}
```

上述代码中，调用method1()方法时，已经获得了锁，此时内部调用method2()方法时，由于本身已经具有该锁，所以可以再次获取。ReenTrantLock也是类似

  2 公平锁


  CPU在调度线程的时候是从等待队列中随机挑选一个线程执行，这种随机性无法保障线程的先到先得的，Sychronized就是非公平锁。这就会产生**饥饿**现象，即部分线程可能永远无法得到它的资源，永远无法获得cpu的执行权，而有些线程会不断强化它的资源。解决饥饿
  现象就需要公平锁了， 公平锁会保障线程的先后顺序，避免饥饿现象。但公平锁效率比较低，因为它需要维护一个有序队列

ReentrantLock便是一种公平锁，通过在构造方法中传入true就是公平锁，传入false，就是非公平锁。

3. 死锁

 一般来说，要出现死锁问题需要满足以下条件：
* 互斥条件：一个资源每次只能被一个线程使用。
* 请求与保持条件：一个线程因请求资源而阻塞时，对已获得的资源保持不放。
* 不剥夺条件：线程已获得的资源，在未使用完之前，不能强行剥夺。
* 循环等待条件：若干线程之间形成一种头尾相接的循环等待资源关系。
在JAVA编程中，有3种典型的死锁类型：
静态的锁顺序死锁，动态的锁顺序死锁，协作对象之间发生的死锁。

## synchronized和ReentrantLock
### Synchroized关键字
JVM基于进入和退出Monitor对象来实现 代码块同步 和 方法同步 ，两者实现细节不同。

* 代码块同步： 在编译后通过将monitorenter指令插入到同步代码块的开始处，将monitorexit指令插入到方法结束处和异常处，通过反编译字节码可以观察到。任何一个对象都有一个monitor与之关联，线程执行monitorenter指令时，会尝试获取对象对应的monitor的所有权，即尝试获得对象的锁。

* 方法同步： synchronized方法在method_info结构有ACC_synchronized标记，线程执行时会识别该标记，获取对应的锁，实现方法同步。

两者虽然实现细节不同，但本质上都是对一个对象的监视器（monitor）的获取。任意一个对象都拥有自己的监视器，当同步代码块或同步方法时，执行方法的线程必须先获得该对象的监视器才能进入同步块或同步方法，没有获取到监视器的线程将会被阻塞，并进入同步队列，状态变为BLOCKED。当成功获取监视器的线程释放了锁后，会唤醒阻塞在同步队列的线程，使其重新尝试对监视器的获取。

对象、监视器、同步队列和执行线程间的关系如下图：
![avatar](/image/synchronized_struct.png)

### ReentrantLock可重入锁
1. Lock接口的主要方法

* void lock(): 执行此方法时，如果锁处于空闲状态，当前线程将获取到锁。相反，如果锁已经被其他线程持有，将禁用当前线程，直到当前线程获取到锁。
* boolean tryLock()： 如果锁可用，则获取锁，并立即返回true，否则返回false. 该方法和lock()的区别在于，tryLock()只是"试图"获取锁，如果锁不可用，不会导致当前线程被禁用，当前线程仍然继续往下执行代码。而lock()方法则是一定要获取到锁，如果锁不可用，就一直等待，在未获得锁之前,当前线程并不继续向下执行. 通常采用如下的代码形式调用tryLock()方法：
* void unlock()： 执行此方法时，当前线程将释放持有的锁. 锁只能由持有者释放，如果线程并不持有锁，却执行该方法，可能导致异常的发生.
* Condition newCondition()： 条件对象，获取等待通知组件。该组件和当前的锁绑定，当前线程只有获取了锁，才能调用该组件的await()方法，而调用后，当前线程将缩放锁。

2. 使用方法。
* lock()方法获取锁，然后使用unlock()方法释放锁。
* lock()方法及之后的代码块要用try包裹，且必须要在finally中调用unlock()。 不然如果代码块发生异常，锁将永远不会释放，而发生死锁。

3. Condition实现等待/通知
关键字synchronized与wait()和notify()/notifyAll()方法相结合可以实现等待/通知模式，类似ReentrantLock也可以实现同样的功能，但需要借助于Condition对象。

关于Condition实现等待/通知就不详细介绍了，可以完全类比wait()/notify()，基本使用和注意事项完全一致。
就只简单介绍下类比情况：

condition.await()————>lock.wait()

condition.await(long time, TimeUnit unit)————>lock.wait(long timeout)

condition.signal()————>lock.notify()

condition.signaAll()————>lock.notifyAll()

特殊之处：synchronized相当于整个ReentrantLock对象只有一个单一的Condition对象情况。而一个ReentrantLock却可以拥有多个Condition对象，来实现通知部分线程。

具体实现方式：
假设有两个Condition对象：ConditionA和ConditionB。那么由ConditionA.await()方法进入等待状态的线程，由ConditionA.signalAll()通知唤醒；由ConditionB.await()方法进入等待状态的线程，由ConditionB.signalAll()通知唤醒。篇幅有限，代码示例就不写了。




## 生产者/消费者（Object.wait）
###  waith()和notify()
1. 方法简述
* wait()方法： 让当前线程进入等待，并释放锁。

* wait(long)方法： 让当前线程进入等待，并释放锁，不过等待时间为long，超过这个时间没有对当前线程进行唤醒，将自动唤醒。

* notify()方法： 让当前线程通知那些处于等待状态的线程，当前线程执行完毕后释放锁，并从其他线程中唤醒**随机**其中一个继续执行。

* notifyAll()方法： 让当前线程通知那些处于等待状态的线程，当前线程执行完毕后释放锁，将唤醒所有等待状态的线程。

2. 注意事项，wait()和notify()方法使用注意事项
* 当前的线程必须拥有当前对象的monitor，也即lock，就是锁，才能调用wait()方法，否则将抛出异常java.lang.IllegalMonitorStateException。

* 线程调用wait()方法，释放它对锁的拥有权，然后等待另外的线程来通知它（通知的方式是notify()或者notifyAll()方法），这样它才能重新获得锁的拥有权和恢复执行。

* 要确保调用wait()方法的时候拥有锁，即，wait()方法的调用必须放在synchronized方法或synchronized块中。

* wait()方法会释放锁资源，Thread.sleep()不会释放锁资源

* 被notify()唤醒的线程是不能被执行的，需要等到当前线程放弃这个对象的锁，当前线程会在方法执行完毕后释放锁。


