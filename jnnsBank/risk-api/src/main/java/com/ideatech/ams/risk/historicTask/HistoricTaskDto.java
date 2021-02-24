package com.ideatech.ams.risk.historicTask;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricTaskDto {
    private String id;
    private String name;
    private String countId;
    private String description;
    private Date startTime;
    private Date endTime;
    private String processInstanceId;//流程流转ID
    private String processDefinitionId;//流程定义ID
    private String flowName;//流程名称
    private String status;
}
