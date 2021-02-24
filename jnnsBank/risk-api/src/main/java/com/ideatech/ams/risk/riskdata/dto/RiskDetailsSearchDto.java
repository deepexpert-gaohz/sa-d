package com.ideatech.ams.risk.riskdata.dto;

import com.ideatech.ams.risk.model.dto.RiskDataDto;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class RiskDetailsSearchDto extends PagingDto<Object[]> {
    private String  modelName;
    private String jgName;
    private String startEndTimeTwo;
    private String orgId;
    private String khId;
    private String tableName;
    private String modelId;
    private String minDate;
    private String maxDate;
    private String ofield;   //其他字段
    private String riskPoint;
    private String riskDate;
    private String fullId;
    private String riskPointDesc;//风险点描述
    private String accountNo;
}
