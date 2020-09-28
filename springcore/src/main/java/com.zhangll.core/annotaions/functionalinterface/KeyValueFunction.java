package com.zhangll.core.annotaions.functionalinterface;
@FunctionalInterface
public interface KeyValueFunction<K, V> {
    K getKey(V value) throws Exception;
}
