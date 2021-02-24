package com.ideatech.ams.risk.model.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;


@Data
public class ModelSearchExtendDto extends PagingDto<ModelsExtendDto> {
    private String orgName; //机构名称
    private String riskType;//风险类型
    private String riskPoint;//风险点
    private String modelName;//模型名称
    private String dataDate;//数据日期
    private String endDate;
    private String startEndTime;//开始和结束日期
    private String status;//处理状态
    private String accountNo;//账号
    private String acctName;//账户名称
    private String riskPointDesc;//风险点描述

    private String organCode;//机构号


}
