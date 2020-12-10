package com.zhangll.core.thread;

import com.zhangll.core.util.StaticHashTable;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomInitDelay {
    public static void main(String[] args) {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        StaticHashTable<Long, Integer> hashtable = new StaticHashTable();
        for (int i = 0; i < 100; i++) {
            long l = current.nextLong(1, 5);
            hashtable.addHashTable(l, 1);
        }

        Set<Map.Entry<Long, Integer>> entries = hashtable.entrySet();
        System.out.println(entries);
    }
}
