package com.zhangll.apm.agent;

import com.zhangll.apm.collector.JdbcCollector;

import java.lang.instrument.Instrumentation;

/**
 * @see java.sql.Driver
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/11/8
 */
public class JdbcAgent {
    public static void premain(String args, Instrumentation instrumentation){
        new JdbcCollector(args, instrumentation);
    }
}
