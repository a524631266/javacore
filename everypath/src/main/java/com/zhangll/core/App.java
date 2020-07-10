package com.zhangll.core;

public class App extends ClassLoader implements AppSay{
    @Override
    public void say(){
        System.out.println(this.getResource(".").getPath());
        System.out.println(System.getProperty("MAVEN_PROJECTBASEDIR"));
    }
    public static void main(String[] args) {
        new App().say();
    }
}
