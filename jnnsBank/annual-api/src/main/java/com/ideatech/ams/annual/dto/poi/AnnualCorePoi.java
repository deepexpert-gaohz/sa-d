package com.ideatech.ams.annual.dto.poi;

import lombok.Data;

/**
 * @Description 年检核心模板列表poi
 * @Author wanghongjie
 * @Date 2018/8/31
 **/
@Data
public class AnnualCorePoi {
    /**账号*/
    private String acctNo;
    /**企业名称*/
    private String depositorName;
    /**法人姓名*/
    private String legalName;
    /**工商注册号*/
    private String regNo;
    /**组织机构代码证*/
    private String orgCode;
    /**机构代码(行内机构号)*/
    private String organCode;
    /**注册资金*/
    private String registeredCapitalStr;
    /**注册地址*/
    private String regFullAddress;
    /**法人证件种类*/
    private String legalIdcardType;
    /**法人证件号码*/
    private String legalIdcardNo;
    /**注册币种*/
    private String regCurrencyType;
    /**国税登记证*/
    private String stateTaxRegNo;
    /**地税登记证*/
    private String taxRegNo;
    /**经营范围*/
    private String businessScope;
    /**账户性质*/
    private String acctType;
}
