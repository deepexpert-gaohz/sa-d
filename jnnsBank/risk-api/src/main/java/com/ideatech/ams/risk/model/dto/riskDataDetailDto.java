package com.ideatech.ams.risk.model.dto;
import lombok.Data;

@Data
public class riskDataDetailDto {
    private String customerNo;//客户号
    private String depositorName;//客户名称
    private String accountNo;//账号
    private String accountName;//账户名称
    private String legalIdcardNo;//法人证件号
    private String legalIdcardName;//法人姓名
    private String operatorIdcardNo;//经办人证件号
    private String operatorIdcardName;//经办人姓名
    private String workAddress;//经营地地址
    private String bankName;//开户行地址
    private String financeIdcardNo;//财务负责人证件号
    private String financeIdcardName;//财务负责人姓名
    private String openDate;//开户日期
    private String regAddress;//注册地址
}
