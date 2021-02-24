package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 补打基本户返回对象
 */
@Data
public class AmsResetPrintInfo {

    /**
     * 账户名称
     */
    private String depositorName;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 机构号
     */
    private String pbcCode;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 基本户核准号
     */
    private String accountKey;
    /**
     * 查询密码
     */
    private String selectPwd;
    /**
     * 基本户编号（新）
     */
    private String openKey;

}
