package com.zhangll.core.annotaions;

public class TestInnerClass {
    public static void main(String[] args) {
        MethodGetDelaringClass.Inner inner = new MethodGetDelaringClass.Inner();
        inner.sayhell();
    }
}
