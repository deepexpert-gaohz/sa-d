package com.ideatech.ams.risk.highRisk.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;


@Data
public class HighRiskListDto extends PagingDto<HighRiskListDto> {


    private Long id;
    /**
     * 编号
     */
    private String ordNum;

    /**
     * 客户号
     */
    private String customerNo;

    /**
     * 账号
     */
    private String accountNo;
    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 企业证件号
     */
    private String depositorNo;

    /**
     * 账号类型
     */
    private String accountType;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 开户日期
     */
    private String acctCreateDate;
    /**
     * 风险原因
     */
    private String riskDesc;
    private String startEndTime;

    private String status;//处理状态0:未处理,1:不处理,2:止付,3:暂停非柜面
}
