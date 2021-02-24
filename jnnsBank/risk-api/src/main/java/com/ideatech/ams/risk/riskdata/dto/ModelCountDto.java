package com.ideatech.ams.risk.riskdata.dto;



import com.ideatech.ams.risk.modelKind.dto.RiskLevelDto;
import com.ideatech.ams.risk.modelKind.dto.RiskRuleDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import lombok.Data;

@Data
public class ModelCountDto {
    private  Long id;
    private String cjrq;   //采集时间
    private String khId;   //客户编号
    private String khName; //客户名称
    private String minDate;
    private String maxDate;
    private String modelName; //模型名称
    private String modelsId;//models表中的id
    private String riskId;		// 数据编号
    private String riskTable;		// 风险表名
    private String fullId;
    private String name;
    private String orgId;		// 机构编号
    private String orgName;    //机构名称
    private String status;		// 处理状态
    private String riskAmt;//金额
    private String orgNameName;    //机构名称
    private String orgIdId;		// 机构编号备用
    private String modelId;
    private String ruleId;
    private String levelId;
    private String typeId;
    private String riskType;

    public ModelCountDto(String cjrq, String khId, String khName, String minDate, String maxDate, String modelName, String modelsId, String riskId, String riskTable, String fullId, String name, String orgId, String orgName, String status, String riskAmt, String orgNameName, String orgIdId) {
        this.cjrq = cjrq;
        this.khId = khId;
        this.khName = khName;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.modelName = modelName;
        this.modelsId = modelsId;
        this.riskId = riskId;
        this.riskTable = riskTable;
        this.fullId = fullId;
        this.name = name;
        this.orgId = orgId;
        this.orgName = orgName;
        this.status = status;
        this.riskAmt = riskAmt;
        this.orgNameName = orgNameName;
        this.orgIdId = orgIdId;
    }

    public ModelCountDto() {
    }
}
