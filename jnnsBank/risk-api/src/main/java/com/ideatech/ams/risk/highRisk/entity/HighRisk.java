package com.ideatech.ams.risk.highRisk.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description 高风险数据
 * @Author ywz
 * @Date 2019/9/23
 **/
@Entity
@Table(name="risk_high_Customer")
@Data
public class HighRisk extends BaseMaintainablePo {
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

}
