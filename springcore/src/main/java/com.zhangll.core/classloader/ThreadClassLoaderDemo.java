package com.zhangll.core.classloader;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * 线程上下文处理器有没有处理过
 * 线程上下文加载器是为了解决父加载器无法访问子加载器而做的一个中间代理商
 *
 * 此时这个ServiceLoader。load方法就是保证不管该语句放在何处（root/ext/app），都可以访问当前
 * runtime对应的上下文加载器内的所有类，所以，可以在java核心包中只放置驱动（抽象或接口），
 * 那么应用层就可以通过加载不同的实现类包，保证了接口分离原则！
 * @see java.util.ServiceLoader#load(Class)
 */
public class ThreadClassLoaderDemo
{
    public static void main(String[] args) throws IOException {

        new Thread(new MyTest25()).start();
        System.out.println("current thread Class:" + Thread.currentThread().getContextClassLoader().getClass());
        System.out.println("Thread.class:"+Thread.class.getClassLoader());
        ServiceLoader.load(Thread.class);
    }

    static class MyTest25 implements Runnable{
        Thread thread;

        public MyTest25() {
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            ClassLoader classLoader = this.thread.getContextClassLoader();
            this.thread.setContextClassLoader(classLoader);
            System.out.println("Class; " + classLoader.getClass());
            System.out.println("Parent; " + classLoader.getParent().getClass());
        }
    }
}
