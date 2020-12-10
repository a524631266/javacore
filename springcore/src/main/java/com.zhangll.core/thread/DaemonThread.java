package com.zhangll.core.thread;

import java.util.concurrent.TimeUnit;

/**
 * 守护线程的生命周期不受jvm 主流层线程的管理。
 *
 * 镇魂街，大家都看到过吧，每个街道都有一个御灵使都有一个守护灵
 * 如果一个御灵使消失，那么其守护灵也会马上消失。
 * 这就是守护的意思。
 *
 * 守护意味着奉献，意味着
 *
 * 对于一个Thread对象，如果设置为非守护，并不是意味着于main线程是一致的。
 * 只是它们都被当作非守护线程看待
 * 而jvm虚拟机一直存活的依据是，非守护线程一直存活
 *
 *
 */
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println("11223");
            }
        });

        System.out.println("out thread: " + Thread.currentThread().getName());
        // false =》 非守护，那么于main同生共死
        thread.setDaemon(false);
        // true 意味着守护，守护
        thread.setDaemon(true);
        thread.start();
//        TimeUnit.SECONDS.sleep(1);
    }
}
