package com.zhangll.core.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorsOne {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 定时收器执行器，有两个时间参数
         * 1. 初始化的延时时机
         * 2. 间隔时机
         */
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // 开启调度器
        ScheduledFuture<?> hello = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 是同一个上下文吗？
                System.out.println(Thread.currentThread());
                System.out.println("hello");
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);

        // 关闭 调度器执行的任务，取消掉
        TimeUnit.SECONDS.sleep(5);
        hello.cancel(false);
        // 直接执行
        for (int i = 0; i < 1000; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ee:" + Thread.currentThread());
                    System.out.println("ee:");
                }
            });
        }
        hello = null;
        executorService.shutdownNow();
    }
}
