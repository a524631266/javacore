package com.zhangll.core.transformClass;

public class Person {
    public void sayHello(String name){
        System.out.println("hello: " + name);
    }

    public void m() throws Exception
    {
        Thread.sleep(100);
    }
    public static long timer;

    public void m2() throws Exception {
        timer -= System.currentTimeMillis();
        Thread.sleep(100);
        timer += System.currentTimeMillis();
    }
}
