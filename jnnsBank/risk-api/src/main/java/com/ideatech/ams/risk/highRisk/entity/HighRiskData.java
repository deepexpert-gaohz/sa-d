package com.ideatech.ams.risk.highRisk.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="risk_high_info")
@Data
public class HighRiskData extends BaseMaintainablePo {
    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 账户
     */
    private String accountNo;
    /**
     * 企业名称
     */
    private String depositorName;
    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 企业证件种类
     */
    private String depositorcardType ;
    /**
     * 企业证件号
     */
    private String  depositorcardNo;
    /**
     * 法人证件种类
     */
    private String legalIdcardType;
    /**
     * 法人证件号码
     */
    private String legalIdcardNo;

    /**
     * 风险原因
     */
    private String riskDesc;

    /**
     * 模型id
     */
    private String riskId;
    /**
     *数据日期
     */
    private String riskDate;

    /**
     *处理原因
     */
    private String reason;

    private String status;//处理状态0:未处理,1:不处理,2:止付,3:暂停非柜面
}
