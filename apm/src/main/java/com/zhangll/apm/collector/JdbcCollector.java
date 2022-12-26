package com.zhangll.apm.collector;

import java.lang.instrument.Instrumentation;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/11/8
 */
public class JdbcCollector {
    private final String args;
    private final Instrumentation instrumentation;

    public JdbcCollector(String args, Instrumentation instrumentation) {
        this.args = args;
        this.instrumentation = instrumentation;
    }

}
