package com.ideatech.ams.risk.modelKind.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
public class RiskRuleSearchDto extends PagingDto<RiskRuleDto> {
    private String ruleName;
}
