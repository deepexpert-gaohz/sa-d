package com.ideatech.ams.customer.dto.poi;

import lombok.Data;

@Data
public class FreshCompanyPoi {
    /**
     * 公司名称
     */
    private String name;

    /**
     * 法人名称
     */
    private String legalPerson;

    /**
     * 成立日期
     */
    private String openDate;

    /**
     * 企业状态
     */
    private String state;

    /**
     * 社会统一信用代码
     */
    private String unityCreditCode;

    /**
     * 注册资本
     */
    private String registFund;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 省份name
     */
    private String provinceName;

    /**
     * 市区name
     */
    private String cityName;

    /**
     * 地区name
     */
    private String areaName;

}
