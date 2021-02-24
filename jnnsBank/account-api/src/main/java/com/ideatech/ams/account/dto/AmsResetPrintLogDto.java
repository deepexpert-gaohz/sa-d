package com.ideatech.ams.account.dto;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class AmsResetPrintLogDto {

    /**
     * id
     */
    private Long id;
    /**
     * 账号
     */
    private String acctNo;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 基本户开户许可证（老）
     */
    private String accountKeyOld;
    /**
     * 基本户编号（老）
     */
    private String openKeyOld;
    /**
     * 查询密码（老）
     */
    private String selectPwdOld;

    /**
     * 基本户开户许可证（新）
     */
    private String accountKeyNew;

    /**
     * 基本户编号（新）
     */
    private String openKeyNew;

    /**
     * 查询密码（新）
     */
    private String selectPwdNew;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户显示名称
     */
    private String cname;

    /**
     * 机构 短
     */
    private String organCode;

    /**
     * 法定代表人
     */
    private String legalName;

}
