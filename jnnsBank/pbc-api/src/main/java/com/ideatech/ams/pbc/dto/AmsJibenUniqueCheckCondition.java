package com.ideatech.ams.pbc.dto;

import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 基本户唯一性校验查询参数（即取消核准基本户开户第一步所需参数)）
 *
 * @author fantao
 * @date 2019-04-24 17:04
 */
@Data
public class AmsJibenUniqueCheckCondition {

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 注册地（住所地）地区代码
     */
    private String regAreaCode;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 证件种类
     */
    private String legalIdcardTypeAms;

    /**
     * 证件号码
     */
    private String lgalIdcardNo;

    /**
     * 工商营业执照注册号
     */
    private String fileNo;

    /**
     * 工商营业执照有效期
     */
    private String tovoidDate;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 银行机构代码
     */
    private String bankCode;

    /**
     * 银行机构代码
     */
    private String organCode;


    /**
     * 银行机构名称
     */
    private String bankName;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 开户日期
     */
    private String acctCreateDate;

    private String oldAccountKey;

    public void validate() {
        validateQuery();

        if (StringUtils.isBlank(this.depositorName)) {
            throw new RuntimeException("存款人名称不能为空");
        }
        if (StringUtils.isBlank(this.fileNo)) {
            throw new RuntimeException("工商营业执照注册号不能为空");
        }
        if (StringUtils.isBlank(this.acctNo)) {
            throw new RuntimeException("账号不能为空");
        }
        if (StringUtils.isBlank(this.acctCreateDate)) {
            throw new RuntimeException("开户日期不能为空");
        }
        if (StringUtils.isBlank(this.depositorType)) {
            throw new RuntimeException("存款人类别不能为空");
        }

        String[] arrays = {"01", "02", "13", "14"};
        if (!ArrayUtils.contains(arrays, this.depositorType)) {
            throw new RuntimeException("存款人类别必须为\"01\", \"02\", \"13\", \"14\"中的一种");
        }

    }

    public void validateQuery() {
        if (StringUtils.isBlank(this.depositorName) && StringUtils.isBlank(this.fileNo)) {
            throw new RuntimeException("存款人名称与工商营业执照注册号不能同时为空");
        }
        if (StringUtils.isBlank(this.regAreaCode)) {
            throw new RuntimeException("注册地（住所地）地区代码不能为空");
        }
        if (StringUtils.isBlank(this.bankCode)) {
            throw new RuntimeException("银行机构代码不能为空");
        }
        if (StringUtils.equals(this.depositorType, "14")) {
            if (StringUtils.isBlank(this.legalIdcardTypeAms)) {
                throw new RuntimeException("当存款人类别为【14-无字号的个体工商户】时，证件种类不能为空");
            }
            if (StringUtils.isBlank(this.lgalIdcardNo)) {
                throw new RuntimeException("当存款人类别为【14-无字号的个体工商户】时，证件号码不能为空");
            }
            if ("个体户".equals(this.depositorName)) {
                throw new RuntimeException("当存款人类别为【14-无字号的个体工商户】时,存款人名称不能仅为【个体户】!");
            }
        }
    }

}
