package com.ideatech.ams.risk.riskdata.dto;


import lombok.Data;

@Data
public class RiskRecordInfoDto {
    private Long id;
    private String riskId;   //风险编号
    private String riskDesc;   //风险描述
    private String riskCnt;   //风险数量
    private String riskType;   //风险类型
    private String customerId;   //客户号
    private String customerName;   //客户号
    private String riskDate;   //风险日期
    private String accountNo;   //账号
    private String accountName;   //账户名称
    private String status;//状态
    private String accCreateDate;//开户时间
    private String riskPoint;//开户时间
    private String corporateFullId;//机构层级
    private String riskPointDesc;

    private String corporateBank;//法人机构行标识
    private Boolean deleted = Boolean.FALSE;//删除标记

    private String organCode;//机构号

}
