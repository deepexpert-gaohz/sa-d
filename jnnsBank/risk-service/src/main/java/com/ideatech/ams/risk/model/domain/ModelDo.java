package com.ideatech.ams.risk.model.domain;



import lombok.Data;


@Data
public class ModelDo {

    private Long id;
    private String modelId;//模型编号
    private String name;//模型名称
    private String mdesc;//模型描述
    private String levelName;//风险等级
    private String ruleName;//风险规则
    private String typeName;//风险类型
    private String status;//模型状态

    public ModelDo(Long id,String modelId, String name, String mdesc, String levelName, String ruleName, String typeName, String status) {

        this.id = id;
        this.modelId = modelId;
        this.name = name;
        this.mdesc = mdesc;
        this.levelName = levelName;
        this.ruleName = ruleName;
        this.typeName = typeName;
        this.status = status;
    }


}
