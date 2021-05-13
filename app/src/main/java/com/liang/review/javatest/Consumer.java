package com.liang.review.javatest;

import java.util.concurrent.locks.ReentrantLock;

public class Consumer {
    private String lock;
    ReentrantLock s = new ReentrantLock()

    public Consumer(String lock) {
        super();
        this.lock = lock;
    }
    public void getValue(){
        try {
            synchronized (lock) {
                if(StringObject.value.equals("")){
                    //没值，不进行消费
                    lock.wait();
                }
                System.out.println("get的值是："+StringObject.value);
                StringObject.value = "";
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
}
