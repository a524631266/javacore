package com.zhangll.core.concurrent.reference;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        // 如果 permits = 1 ，则死锁
        semaphore.acquire();
        semaphore.release();

        System.out.println("ok");
        Thread thread = new Thread();
        thread.suspend();
        thread.resume();
    }
    // 协程 有兴趣再看
    // docs.paralleluniverse.co/quasar/

}
