package com.ideatech.ams.domain;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 行内核心表
 */
@Data
@Entity
public class CoreAccount extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 4214870702711170454L;

    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户状态
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    /**
     * 账户开户日期
     */
    private String acctCreateDate;
    /**
     * 账户有效期(临时账户)
     */
    private String effectiveDate;
    /**
     * 基本开户许可核准号
     */
    private String accountKey;
    /**
     * 核心机构号
     */
    private String organFullId;
    /**
     * 核心机构名称
     */
    private String bankName;
    /**
     * 基本户、一般户、专用户、临时户
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;
    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 存款人名称
     */
    private String depositorName;
    /**
     * 注册省份
     */
    private String regProvince;
    /**
     * 注册城市
     */
    private String regCity;
    /**
     * 注册地区
     */
    private String regArea;
    /**
     * 注册详细地址
     */
    private String regAddress;
    /**
     * 行业归属
     */
    private String industryCode;
    /**
     * 办公地址
     */
    private String regOffice;
    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;
    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;
    /**
     * 证明文件1证件成立日期
     */
    private String fileSetupDate;
    /**
     * 证明文件1到期日
     */
    private String fileDue;
    /**
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
    private String registeredCapital;
    /**
     * 经营（业务）范围
     */
    private String businessScope;
    /**
     * 企业规模
     */
    private String corpScale;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 证件种类
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
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 组织机构代码到期日
     */
    private String orgCodeDue;
    /**
     * 纳税人识别号（国税）
     */
    private String stateTaxRegNo;
    /**
     * 国税到期日
     */
    private String stateTaxDue;
    /**
     * 纳税人识别号（地税）
     */
    private String taxRegNo;
    /**
     * 地税到期日
     */
    private String taxDue;
    /**
     * 办公详细地址
     */
    private String workAddress;
    /**
     * 企业联系电话
     */
    private String telephone;
    /**
     * 邮政编码
     */
    private String zipcode;
    /**
     * 经济类型
     */
    private String economyType;
    /**
     * 交换号账号
     */
    private String jhhAcctNo;

}
