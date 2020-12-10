package com.zhangll.core.util;

import java.util.Hashtable;

public class StaticHashTable<K, V> extends Hashtable<K, V> {
    public void addHashTable(K key, V value){
        V old = putIfAbsent(key, value);
        if(old != null) {
            if(value instanceof Integer){
                Integer newValue = (Integer) value;
                Integer oldValue = (Integer) old;
                put(key, (V) Integer.valueOf(newValue + oldValue));
            }
        }
    }
}
