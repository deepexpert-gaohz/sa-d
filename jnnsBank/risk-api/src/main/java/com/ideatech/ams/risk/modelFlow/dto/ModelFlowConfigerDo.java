package com.ideatech.ams.risk.modelFlow.dto;


import lombok.Data;

import java.util.Date;

@Data
public class ModelFlowConfigerDo  {
    private String id;
    private String modelId;//模型编号
    private String name;//流程编码
    private String flowkey;
    private String flowName;
    private String isAuto;
    private String lastUpdateDate;//更新时间

    public ModelFlowConfigerDo() {
    }

    public ModelFlowConfigerDo(String id, String modelId, String name, String flowkey, String flowName, String isAuto, String lastUpdateDate) {
        this.id = id;
        this.modelId = modelId;
        this.name = name;
        this.flowkey = flowkey;
        this.flowName = flowName;
        this.isAuto = isAuto;
        this.lastUpdateDate = lastUpdateDate;
    }
}
