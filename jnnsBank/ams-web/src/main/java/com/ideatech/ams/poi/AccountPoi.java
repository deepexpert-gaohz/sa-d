package com.ideatech.ams.poi;

import lombok.Data;

@Data
public class AccountPoi {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 机构号
     */
    private String bankCode;

    /**
     * 机构名称
     */
    private String bankName;
}
