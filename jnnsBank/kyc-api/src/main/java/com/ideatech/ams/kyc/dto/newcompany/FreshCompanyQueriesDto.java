package com.ideatech.ams.kyc.dto.newcompany;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class FreshCompanyQueriesDto extends BaseMaintainableDto {
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

}
