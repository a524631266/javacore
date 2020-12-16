package com.zhangll.core.future;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class AsynDemo {
    private Queue<String>  queue ;
    private final int capacity;

    public AsynDemo(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    /**
     * 约束条件
     * 1. 如果元素数量大于capacity就直接剔除掉
     * @param element
     * @return
     */
    private Optional<String> tryPut(String element){
        System.out.println("quere size: " +queue.size());
        if(queue.size() >= capacity){
            return Optional.empty();
        }else{
            queue.add(element);
            return Optional.ofNullable(element);
        }
    }

    public static void main(String[] args) {
        int capacity = 10;
        AsynDemo asynDemo = new AsynDemo(capacity);

        for (int i = 0; i < capacity + 1; i++) {
            Optional<String> s = asynDemo.tryPut(String.valueOf(i));
            System.out.println(s.isPresent());
        }

        System.out.println(asynDemo.queue.size());
    }
}
