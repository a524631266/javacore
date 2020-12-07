package com.zhangll.core.classloader;

import java.sql.DriverManager;

/**
 * 有三个等级 一个时 bootClassLoader 这个根为null，底层是由C写的
 * 第二为ext classloader
 * 第三为 appClassLoader
 * 父亲类加载器加载的类，需要通过上下文来获取应用ClassLoader
 * @see sun.misc.Launcher
 * Launcher.ExtClassLoader.getExtClassLoader();
 * this.loader = Launcher.AppClassLoader.getAppClassLoader(var1);
 * @see ClassLoader#getClassLoader(Class)
 */
public class ClassLoaderDemo {
    public static void main(String[] args) {
         // null
        ClassLoader classLoader = int.class.getClassLoader();
        // null
        System.out.println(DriverManager.class.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(ClassLoaderDemo.class.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(Thread.currentThread().getContextClassLoader());

        // 系统类加载器 null
        System.out.println(System.getProperty("java.system.class.loader"));
        System.out.println(ClassLoader.getSystemClassLoader());
    }
}
