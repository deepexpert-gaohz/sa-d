package com.ideatech.ams.risk.modelKind.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ModelKindSearchDto extends PagingDto<ModelKindDto> {
    private String ruleId;//模型规则
    private String typeId;//模型类型
    private String levelId;//模型等级
}
