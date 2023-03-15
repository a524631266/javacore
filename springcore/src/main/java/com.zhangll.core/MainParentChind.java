package com.zhangll.core;

import java.math.BigDecimal;

public class MainParentChind {
    public static void main(String[] args) {
        Parent children = new Children();
        // 这是一个很迷惑人的地方，因为你不知道Parent类型的对象是什么类型的
        // 所以行为可能跟表明的不一样，也是多态的体现
        children.say();

        BigDecimal a = new BigDecimal(100);
        int x;
//        x =10;
//        System.out.println(x);
        BigDecimal bigDecimal = new BigDecimal("");
        System.out.println(bigDecimal);
    }
}
