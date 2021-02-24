package com.ideatech.ams.risk.modelFlow.dto;

import lombok.Data;

@Data
public class ModelFlowConfigerDto {
    private Long id;
    private String modelId;//模型编号
    private String isAuto;// is '是否自动分配（1:是,0:否）'
    private String flowKey;//流程编码
    private String  flowUserId;//流程发起人id
    private String auditUserType;//用户类型(001：机构，002：用户，003：角色)
    private String flowName;//流程名称
    private String modelName;//模型名称
}
