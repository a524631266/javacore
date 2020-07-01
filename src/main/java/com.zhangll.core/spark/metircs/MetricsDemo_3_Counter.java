package com.zhangll.core.spark.metircs;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * counter使用来统计次数的所以
 * 使用的时候
 * inc() inc(n)
 * dec() dec(n)
 *
 * Counter. getCount
 */
public class MetricsDemo_3_Counter {

    static final MetricRegistry metrics = new MetricRegistry();
    static final Counter pendingJobs = metrics.counter(MetricRegistry.name(MetricsDemo_3_Counter.class,"pending-jobs"));
    public static void main(String[] args) {
        startReport();
        pendingJobs.inc();
        pendingJobs.inc(8);
        pendingJobs.dec();
        pendingJobs.dec(2);
        // 获取指标数据的方法
        long count = pendingJobs.getCount();
        System.out.println("count:" + count);
        /**
         * -- Counters --------------------------------------------------------------------
         * com.zhangll.core.spark.metircs.MetricsDemo_Three_Counter.pending-jobs
         *              count = 9
         */
        wait5Seconds();
    }
    private static void wait5Seconds() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
    /**
     * 使用console方式报告内容
     */
    static void startReport(){
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }
}
