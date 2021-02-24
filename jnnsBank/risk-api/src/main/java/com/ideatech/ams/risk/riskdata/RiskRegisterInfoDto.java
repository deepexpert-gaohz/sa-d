package com.ideatech.ams.risk.riskdata;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import java.util.Date;

@Data
public class RiskRegisterInfoDto extends BaseMaintainablePo {
    private Long id;
    private String riskId;   //风险编号
    private String customerNo;   //客户号
    private Date handleDate;   //风险日期
    private String accountNo;   //账号
    private String handleMode;//处理状态 0: 不处理，1：止付 2： 暂停非柜面
    private String riskDesc;//风险描述
    private Boolean deleted = Boolean.FALSE;//删除标记
    private String handler;//处理人
    private String status;//处理状态
    private String riskPoint;//风险点
    private Date riskDate;//风险日期
    private String dubiousReason;//暂停非柜面可疑原因
}
