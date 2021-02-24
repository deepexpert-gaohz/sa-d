package com.ideatech.ams.risk.account.dto;


import com.ideatech.ams.risk.model.dto.ModelAndCountDto;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @Author: yinjie
 * @Date: 2019/5/9 15:59
 */

@Data
public class AccountTransactionRiskSearchDto extends PagingDto<ModelAndCountDto> {

    private String officeName; //机构名称
    private String khName;//客户名称
    private String modelName;//模型名称
    private String dataDate;//数据日期
    private String endDate;
}
