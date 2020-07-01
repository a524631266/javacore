package com.zhangll.core.spark.metircs;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;




/**
 * 安全检测模型，此模型用于常规检查
 * 比如调用连接的时候是否正常，可以通过一定的模型
 * 来设计获取系统细粒度的安全体系
 */
public class MetricsDemo_6_HealthChecks {

//    new HealthCheckRegistry


    public static void main(String[] args) {
        startReport();

    }
    private static void waitSeconds(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
    /**
     * 使用console方式报告内容
     */
    static void startReport(){
//        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
//                .convertRatesTo(TimeUnit.SECONDS)
//                .convertDurationsTo(TimeUnit.MILLISECONDS)
//                .build();
//        consoleReporter.start(1, TimeUnit.SECONDS);
    }
}
