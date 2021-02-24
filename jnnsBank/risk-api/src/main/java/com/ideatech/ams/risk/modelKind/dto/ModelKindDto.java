package com.ideatech.ams.risk.modelKind.dto;

import lombok.Data;

@Data
public class ModelKindDto {
    private Long id;
    private String ruleId;//模型规则
    private String typeId;//模型类型
    private String levelId;//模型等级
}
