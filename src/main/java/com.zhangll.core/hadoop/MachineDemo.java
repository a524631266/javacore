package com.zhangll.core.hadoop;

import org.apache.hadoop.yarn.event.Event;
import org.apache.hadoop.yarn.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MachineDemo {
    public static void main(String[] args) throws InterruptedException {
        String jobID = UUID.randomUUID().toString();


        JobEvent initJobEvent = new JobEvent(JobEventType.JOB_INIT, jobID);
        JobEvent startJobEvent = new JobEvent(JobEventType.JOB_START, jobID);
        JobEvent killJobEvent = new JobEvent(JobEventType.JOB_KILL, jobID);
        JobEvent completedJobEvent = new JobEvent(JobEventType.JOB_COMPLETED, jobID);

        EventHandler handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                Enum type = event.getType();
                System.out.println("外部处理" + type);
            }
        };
        JobStateMachine jobStateMachine = new JobStateMachine(initJobEvent.getJobID(), handler);
        JobStateInternal currentState = jobStateMachine.stateMachine.getCurrentState();
        System.out.println("pre state:" + currentState);
        //
        jobStateMachine.handle(initJobEvent);
        JobStateInternal currentState2 = jobStateMachine.stateMachine.getCurrentState();
        System.out.println("post state:" + currentState2);


        //
        jobStateMachine.handle(initJobEvent);
        JobStateInternal currentState3 = jobStateMachine.stateMachine.getCurrentState();
        System.out.println("post state2:" + currentState3);

        while(true) {
            System.out.println("233");
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
