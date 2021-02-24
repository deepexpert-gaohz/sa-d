package com.ideatech.ams.account.dto.poi;

import lombok.Data;

/**
 * 经办人证件到期信息导出类
 * @author jzh
 * @date 2019/4/24.
 */
@Data
public class OperatorIdcardDuePoi {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 人行机构号
     */
    private String bankCode;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 证件类型
     */
    private String operatorIdcardType;


    /**
     * 证件编号
     */
    private String operatorIdcardNo;

    /**
     * 开户日期
     */
    private String acctCreateDate;

    /**
     * 到期日期
     */
    private String operatorIdcardDue;

    /**
     * 是否超期
     */
    private String isOperatorIdcardDue;
}
