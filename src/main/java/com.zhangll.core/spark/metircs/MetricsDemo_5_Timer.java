package com.zhangll.core.spark.metircs;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

/**
 * 时间尺度，用户响应请求
 * 多久能够响应一次事件
 *
 * 计算事件
 * responses.getCount();
 * responses.getMeanRate();
 */
public class MetricsDemo_5_Timer {

    static final MetricRegistry metrics = new MetricRegistry();

    static Timer responses = metrics.timer(MetricRegistry.name(MetricsDemo_5_Timer.class, "responses"));
    public static void main(String[] args) {
        startReport();

        Timer.Context time = responses.time();
        // 等待多久能够执行
        waitMilSeconds(100);
        time.stop();
        waitSeconds(1);

        Timer.Context time2 = responses.time();
        time2.stop();
        waitSeconds(1);

        Timer.Context time3 = responses.time();
        time3.stop();
        waitSeconds(2);

        responses.getCount();
        responses.getMeanRate();
        /**
         * -- Timers ----------------------------------------------------------------------
         * com.zhangll.core.spark.metircs.MetricsDemo_5_Timer.responses
         *              count = 3
         *          mean rate = 0.72 calls/second
         *      1-minute rate = 0.00 calls/second
         *      5-minute rate = 0.00 calls/second
         *     15-minute rate = 0.00 calls/second
         *                min = 0.00 milliseconds
         *                max = 100.61 milliseconds
         *               mean = 33.04 milliseconds
         *             stddev = 47.25 milliseconds
         *             median = 0.00 milliseconds
         *               75% <= 100.61 milliseconds
         *               95% <= 100.61 milliseconds
         *               98% <= 100.61 milliseconds
         *               99% <= 100.61 milliseconds
         *             99.9% <= 100.61 milliseconds
         */
    }
    private static void waitSeconds(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
    private static void waitMilSeconds(int milsecond) {
        try {
            TimeUnit.MILLISECONDS.sleep(milsecond);
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
