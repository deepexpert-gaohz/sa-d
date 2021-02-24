package com.ideatech.ams.risk.rule.dto;

import com.ideatech.ams.risk.model.dto.ModeAndKindlDto;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class RuleSearchDto extends PagingDto<RuleConfigureDto> {
    private String modelId;//模型编号
    private String id;
}
