package com.zhangll.apm.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PerformantModel {
    private Long startTime;
    private Long costTime;
    private String methodName;
    private String error;
}
