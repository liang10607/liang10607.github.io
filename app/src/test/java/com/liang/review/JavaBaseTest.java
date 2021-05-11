package com.liang.review;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class JavaBaseTest implements Runnable {

    @Test
    public void testHashMap(){
        ThreadPoolExecutor service = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        service.execute(new Runnable() {
            public void run() {
                System.out.println("execute方式");
            }
        });

        Future<Integer> future = service.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                System.out.println("submit方式");
                return 2;
            }
        });
        try {
            Integer number = future.get();
        } catch (ExecutionException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //创建公平锁
    private static ReentrantLock lock=new ReentrantLock(false);
    public void run() {
        while(true){
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName()+"获得锁");
            }finally{
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {

    }

    @Test
    public  void mainTest() {
        JavaBaseTest lft=new JavaBaseTest();
        Thread th1=new Thread(lft,"线程1");
        Thread th2=new Thread(lft,"线程2");
        th1.start();
        th2.start();
    }
}
