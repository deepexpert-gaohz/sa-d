package com.ideatech.ams.risk.model.dto;


import lombok.Data;

@Data
public class ModeAndKindlDto {

    private Long id;
    private String modelId;//模型编号
    private String name;//模型名称
    private String mdesc;//模型描述
    private String levelName;//风险等级
    private String ruleName;//风险规则
    private String typeName;//风险类型
    private String status;//模型状态
    private String typeId;//模型状态
    public ModeAndKindlDto() {
    }

    public ModeAndKindlDto(Long id,String modelId, String name, String mdesc, String levelName, String typeName, String ruleName, String status,String typeId) {
        this.id = id;
        this.modelId = modelId;
        this.name = name;
        this.mdesc = mdesc;
        this.levelName = levelName;
        this.ruleName = ruleName;
        this.typeName = typeName;
        this.status = status;
        this.typeId = typeId;
    }

//    private Model model;
//    private RiskLevel riskLevel;
//    private RiskType riskType;
//    private RiskRule riskRule;
//
//    public ModelDo(Model model, RiskLevel riskLevel, RiskType riskType, RiskRule riskRule) {
//        this.model = model;
//        this.riskLevel = riskLevel;
//        this.riskType = riskType;
//        this.riskRule = riskRule;
//    }
}
