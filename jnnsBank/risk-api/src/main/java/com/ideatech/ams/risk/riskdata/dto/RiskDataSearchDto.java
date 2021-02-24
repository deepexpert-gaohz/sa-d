package com.ideatech.ams.risk.riskdata.dto;

import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class RiskDataSearchDto extends PagingDto<RiskDataInfoDto> {
    private String bankName;//机构名称
    private String customerNo;//客户号
    private String accountNo;//账户号
    private String accountName;//账户名称
    private String bankCode;//机构完整ID
    private String customerName;//客户名称
    private String startEndTime;//开始结束时间
    private String modelName;//模型名称
    private String modelId;//模型编号
    private String typeId;//模型名称
    private String handleMode;//处理方式
    private String status;//处理状态

}
