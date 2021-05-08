package com.liang.review;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Author:bernie-mac
 * Data:2021/5/7 10:06
 * Description: com.liang.review
 */
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