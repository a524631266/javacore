package com.zhangll.core.spark.metircs.developer;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 对于内存实时的情况，我们可以针对这个
 *  原理很简单，使用java内置的ManagementFactory.xxxMxBean
 *  时候获取信息
 */
public class MemoryUsageGaugeSetDemo {
    static final MetricRegistry metrics = new MetricRegistry();

    static final MemoryUsageGaugeSet mug = new MemoryUsageGaugeSet();

    public static void main(String[] args) {
        metrics.register(MetricRegistry.name(MemoryUsageGaugeSetDemo.class, "memory"),
                new Gauge<Map<String, Object>>(){
                    @Override
                    public Map<String, Object> getValue() {
                        return transform(mug.getMetrics());
                    }
                });
        startReport();
        sleep(5);

    }

    private static Map<String,Object> transform(Map<String,Metric> metrics) {
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<String, Metric> stringMetricEntry : metrics.entrySet()) {
            Object value = ((Gauge) stringMetricEntry.getValue()).getValue();
            map.put(stringMetricEntry.getKey(), value);
        }
        return map;
    }

    private static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
            System.out.println("========================");
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private static void showMemoryUsage(Map<String, Metric> metrics) {
        Iterator<Map.Entry<String, Metric>> iterator = metrics.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Metric> next = iterator.next();
            System.out.println(next.getKey());
            Gauge value =(Gauge) next.getValue();
            System.out.println(value.getValue());
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
