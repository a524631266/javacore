package com.zhangll.apm.agent;

import com.zhangll.apm.collector.HttpServerCollector;

import java.lang.instrument.Instrumentation;

public class GeneralAgent {
    public static void premain(String args, Instrumentation instrumentation) {

        new HttpServerCollector(instrumentation);
    }
}
