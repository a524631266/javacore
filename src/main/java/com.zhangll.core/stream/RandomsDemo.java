package com.zhangll.core.stream;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomsDemo {
    public static void main(String[] args) {
        IntStream ints = new Random().ints(5, 20);
        ints.distinct().limit(7).sorted().forEach(System.out::println);
    }
}
