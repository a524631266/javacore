package com.zhangll.core.spark.metircs;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * 用来控制检测系统的相关指标
 *  mark 为触发事件
 *    官方网站 https://metrics.dropwizard.io/3.1.0/getting-started/#meters
 */
public class MetricsDemo_1_Meter {
    // 1.这个是指标仓库，MetricRegistry，用于检测指标的，是指标体系的核心
    // 可以查看各种指标体系，例如网络请求次数等等，可以检测1 5 15分钟的
    static final MetricRegistry metrics = new MetricRegistry();
    public static void main(String[] args) {
        startReport();
        // 2. 测量一个名为requestsss的尺子，提供给外部，并内部获取一个
        // 返回一个Meter，Meter是用来给外部使用的对讲机，该对讲机是与注册
        // 中心的中央部分进行交互的
        Meter requests = metrics.meter("requestsss");
        // 3.交互
        requests.mark();
        requests.mark(6);

        // 获取普通计数方式
        long count = requests.getCount();
        double fifteenMinuteRate = requests.getFifteenMinuteRate();
        final double meanRate = requests.getMeanRate();
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
