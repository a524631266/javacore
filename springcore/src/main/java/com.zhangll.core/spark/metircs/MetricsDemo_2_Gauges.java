package com.zhangll.core.spark.metircs;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.spark_project.jetty.util.BlockingArrayQueue;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用来控制检测系统的相关指标
 *
 * 返回Gauges getValuse
 *
 *
 * metrics.getGauges();
 */
public class MetricsDemo_2_Gauges {
    // 1.这个是指标仓库，MetricRegistry，用于检测指标的，是指标体系的核心
    // 可以查看各种指标体系，例如网络请求次数等等，可以检测1 5 15分钟的
    static final MetricRegistry metrics = new MetricRegistry();

    static final Queue queue = new BlockingArrayQueue();

    public static void main(String[] args) {

        startReport();
        // 注册名字比较多的方式
        // MetricRegistry.name(MetricsDemoTwo.class,"size")，使用stringbuilder构建名称
        //
        metrics.register(MetricRegistry.name(MetricsDemo_2_Gauges.class,"size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                // 比较灵活
                // 可以在事件监听的时候，修改事件，比一般不这么做
                queue.add(1);
                // 但是一般情况下，size的时间负责度时O（n），不建议这么做，应该引入第三个counter
                return queue.size();
            }
        });
        waitSeconds(2);


        // 获取指标数据的方法
        SortedMap<String, Gauge> gauges = metrics.getGauges();
        Iterator<Map.Entry<String, Gauge>> iterator = gauges.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Gauge> next = iterator.next();
            Gauge value = next.getValue();
            Integer value1 = (Integer) value.getValue();
            System.out.println(value1);
        }

        wait5Seconds();
    }

    private static void wait5Seconds() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private static void waitSeconds(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    static void startReport(){
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }
}
