package com.ideatech.ams.risk.highRisk.dto;


import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.Map;

@Data
public class HighRiskDto extends PagingDto<Map<String,Object>> {

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
