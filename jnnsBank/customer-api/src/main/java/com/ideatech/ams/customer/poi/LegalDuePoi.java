package com.ideatech.ams.customer.poi;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/4/24.
 */
@Data
public class LegalDuePoi {

    /**
     * 企业名称
     */
    private String depositorName;

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

    /**
     * 法人证件到期日
     */
    private String legalIdcardDue;

    /**
     * 法人证件到期日是否超期
     */
    private String isLegalIdcardDueOver;
}
