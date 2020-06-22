package com.zhangll.core.hadoop;

import org.apache.hadoop.yarn.event.AbstractEvent;

/**
 * 定义一个jobEvent，在状态机器中用来标志一个
 */
public class JobEvent extends AbstractEvent<JobEventType> {
    private final String jobID;
    public JobEvent(JobEventType jobEventType, String jobID) {
        super(jobEventType);
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }
}
