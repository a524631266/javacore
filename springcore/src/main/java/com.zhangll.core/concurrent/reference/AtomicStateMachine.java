package com.zhangll.core.concurrent.reference;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @see org.apache.flink.runtime.taskmanager.Task
 * 在flink task的状态机制中，有如下几个
 * 1. 改变当前状态，这个状态流是怎么的吖？
 * 2. 使用这个状态机来表示任务的各种状态的迁移
 */
public class AtomicStateMachine {
    enum State{
        start,running,interval,stop;
    }
    volatile State state = State.start;
    static AtomicReferenceFieldUpdater<AtomicStateMachine, State> updater = AtomicReferenceFieldUpdater
            .newUpdater(AtomicStateMachine.class, State.class, "state");

    public static void main(String[] args) {
        AtomicStateMachine machine = new AtomicStateMachine();

        boolean b = updater.compareAndSet(machine, State.start, State.running);
        System.out.println(b);
        System.out.println("new state: " + machine.state);
    }
}
