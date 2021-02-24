package com.ideatech.ams.dto;

import lombok.Data;

@Data
public class JnnsPbcResponse {

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;

    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;

    /**
     * 法人类型（法定代表人、单位负责人）
     */
    private String legalType;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人证件类型
     */
    private String legalIdcardType;

    /**
     * 法人证件编号
     */
    private String legalIdcardNo;


















}
