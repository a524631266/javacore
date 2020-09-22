package com.zhangll.core.spark.metircs;

import com.codahale.metrics.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * counter使用来统计次数的所以
 * 使用的时候
 * Histogram .getSnapshot();
 */
public class MetricsDemo_4_Histograms {

    static final MetricRegistry metrics = new MetricRegistry();
    static final Histogram responseSizes = metrics.histogram(MetricRegistry.name(MetricsDemo_4_Histograms.class,"response-sizes"));
    public static void main(String[] args) {
        startReport();

        responseSizes.update(123);
        waitSeconds(1);

        responseSizes.update(300);
        waitSeconds(1);

        responseSizes.update(200);
        waitSeconds(5);

        // 获取备份，备忘录模式
        Snapshot snapshot = responseSizes.getSnapshot();
        // 可用于保存数据

        String file = "data/file";
        OutputStream outdata = null;
        try {
            outdata = new FileOutputStream(file);
            snapshot.dump(outdata);
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } finally {
            if (outdata != null){
                try {
                    outdata.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 这个用于统计请求的长度信息，方便后期统计，比如
         * 比如可以统计请求的长度，
         * -- Histograms ------------------------------------------------------------------
         * com.zhangll.core.spark.metircs.MetricsDemo_4_Histograms.response-sizes
         *              count = 3
         *                min = 123
         *                max = 300
         *               mean = 208.05
         *             stddev = 72.22
         *             median = 200.00
         *               75% <= 300.00
         *               95% <= 300.00
         *               98% <= 300.00
         *               99% <= 300.00
         *             99.9% <= 300.00
         */

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
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }
}
