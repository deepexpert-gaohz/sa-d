package com.ideatech.ams.risk.modelKind.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
public class RiskLevelSearchDto extends PagingDto<RiskLevelDto> {
    private String levelName;//风险等级
}
