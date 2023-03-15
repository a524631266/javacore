package com.zhangll.core.config.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PerformantModel {
    private Integer id;
    private Integer parentId;
    private Long startTime;
    private Long costTime;
    private String methodName;
    private String error;
    private Object[] parameters;
}
