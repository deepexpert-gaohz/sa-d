package com.ideatech.ams.account.dto.poi;


import lombok.Data;

@Data
public class CompanyAccountPoi {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 账户性质
     */
    private String acctType;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 网点号
     */
    private String bankCode;

    /**
     * 本异地标识
     */
    private String accountSiteType;

    /**
     * 开户日期
     */
    private String acctCreateDate;

    /**
     * 销户日期
     */
    private String cancelDate;
}
