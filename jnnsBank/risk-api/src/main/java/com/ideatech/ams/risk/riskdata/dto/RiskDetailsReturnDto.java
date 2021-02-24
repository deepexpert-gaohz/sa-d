package com.ideatech.ams.risk.riskdata.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.Map;
@Data
public class RiskDetailsReturnDto extends PagingDto<Map<String,Object>> {

    private String modelName;
    private String modelId;

}
