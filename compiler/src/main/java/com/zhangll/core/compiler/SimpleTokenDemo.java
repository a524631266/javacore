package com.zhangll.core.compiler;

public class SimpleTokenDemo {
    public static void main(String[] args) {
        String expression = "age>=45";

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            System.out.println(c);
            initState(c);

        }

    }

    private static void initState(char c) {

    }

    enum State {
        Initial,
        Idetifier,
        BIG,
        EBIG,
        NumberLiteral
    }
}
