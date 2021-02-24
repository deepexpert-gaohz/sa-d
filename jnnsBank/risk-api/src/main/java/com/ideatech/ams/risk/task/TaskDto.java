package com.ideatech.ams.risk.task;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {
    private String id;
    private String name; //案例名称
    private String assignee;//当前节点处理人
    private String createTime;//处理时间
    private String category;
    private String processInstanceId;//流程流转ID
    private String processDefinitionId;//流程定义ID
    private String flowName;//流程名称
    private String status;//状态
    private String countId;
}
