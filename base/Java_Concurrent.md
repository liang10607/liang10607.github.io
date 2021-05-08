[回目录页](/README.md)

# Java并发
## Java创建线程的三种方式
1. 直接使用Thread类创建
2. 实现Runable接口，并通过Thread类来启动
3. 通过实现Callable接口，并FutureTask来包装运行Callable实现，最终使用Thread来启动Callable线程; 其中可通过FutureTask来获取Callablexi线程执行结果
 * （1）创建Callable接口的实现类，并实现call()方法，该call()方法将作为线程执行体，并且有返回值。

 * （2）创建Callable实现类的实例，使用FutureTask类来包装Callable对象，该FutureTask对象封装了该Callable对象的call()方法的返回值。

 * （3）使用FutureTask对象作为Thread对象的target创建并启动新线程。

 * （4）调用FutureTask对象的get()方法来获得子线程执行结束后的返回值，调用get()方法会阻塞线程。
 * Callable创建的线程核心代码如下：
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

