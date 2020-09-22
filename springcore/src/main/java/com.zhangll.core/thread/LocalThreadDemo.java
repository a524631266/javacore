package com.zhangll.core.thread;
import java.lang.ThreadLocal;
import java.util.Properties;

public class LocalThreadDemo {
    public static void main(String[] args) {
        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        ThreadLocal<Properties> propertiesThreadLocal = new ThreadLocal<>();
        stringThreadLocal.set("main");
        System.out.println(Thread.currentThread());
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread());

            Thread thread2 = new Thread(() -> {
                System.out.println(Thread.currentThread());
            });
            thread2.start();
        });
        thread.start();

        Thread thread3 = new Thread(() -> {
            System.out.println(Thread.currentThread());

            Thread thread2 = new Thread(() -> {
                System.out.println(Thread.currentThread());
            });
            thread2.start();
        });
        thread3.start();

    }
}
