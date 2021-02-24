package com.ideatech.ams.customer.dto.neecompany;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class FreshCompanyDto extends BaseMaintainableDto {

    private Long id;

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
     * 注册号
     */
    private String registNo;

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
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 市区代码
     */
    private String cityCode;

    /**
     * 地区代码
     */
    private String areaCode;

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
