package com.ideatech.ams.risk.rule.dto;

import lombok.Data;

@Data
public class RuleConfigureDto {
    private  Long id;
    private String ruleId;//配置规则id
    private  String condition;//条件
    private  String value;//值
    private  String conAndVal;//拼装值
    private String modelId;//模型编号
    private  String modelName;
    private  String field;
    private  String fieldName;
    //区间值
    private String inValueF;
    private String inValueS;

}
