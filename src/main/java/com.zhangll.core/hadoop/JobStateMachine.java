package com.zhangll.core.hadoop;

//import org.apache.hadoop.yarn.state.StateMachine;


import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.InvalidStateTransitonException;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

public class JobStateMachine implements EventHandler<JobEvent> {
    String jobID;

    EventHandler eventHandler;

    StateMachine<JobStateInternal, JobEventType, JobEvent> stateMachine;

    StateMachineFactory<JobStateMachine,JobStateInternal, JobEventType, JobEvent> stateMachineFactory
             = new StateMachineFactory<JobStateMachine,JobStateInternal, JobEventType, JobEvent>(JobStateInternal.NEW)
            .addTransition(JobStateInternal.NEW,JobStateInternal.INITED,JobEventType.JOB_INIT,new InitTranstion())
            .addTransition(JobStateInternal.INITED,JobStateInternal.SETUP,JobEventType.JOB_START,new StartTranstion())
            .installTopology();


    @Override
    public void handle(JobEvent event) {
        try {
            this.stateMachine.doTransition(event.getType(), event);
        } catch ( InvalidStateTransitonException e ) {
            e.printStackTrace();
        }
    }

    private class InitTranstion implements org.apache.hadoop.yarn.state.SingleArcTransition<JobStateMachine, JobEvent> {
        @Override
        public void transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("recieving event:" + jobEvent );
            jobStateMachine.eventHandler.handle(new JobEvent(JobEventType.JOB_INIT, jobEvent.getJobID()));
        }
    }

    private class StartTranstion implements org.apache.hadoop.yarn.state.SingleArcTransition<JobStateMachine, JobEvent> {
        @Override
        public void transition(JobStateMachine jobStateMachine, JobEvent jobEvent) {
            System.out.println("staring job");
            jobStateMachine.eventHandler.handle(new JobEvent(JobEventType.JOB_START, jobEvent.getJobID()));
        }
    }

    public JobStateMachine(String jobID, EventHandler eventHandler) {
        this.jobID = jobID;
        this.eventHandler = eventHandler;
        this.stateMachine = stateMachineFactory.make(this);
    }
}
