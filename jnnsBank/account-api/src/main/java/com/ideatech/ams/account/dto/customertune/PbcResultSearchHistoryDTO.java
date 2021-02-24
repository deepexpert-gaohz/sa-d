package com.ideatech.ams.account.dto.customertune;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 人行查询历史
 */
@Data
public class PbcResultSearchHistoryDTO extends BaseMaintainableDto {

    private Long id;
    /**
     * 账号
     */
    private String acctNo;

    /**
     * 基本户注册地地区代码
     */
    private String basicAcctRegArea;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 证明文件1种类
     */
    private String fileType;

    /**
     * 证明文件2种类
     */
    private String fileType2;

    /**
     * 证明文件1编号
     */
    private String fileNo;
    /**
     * 证明文件2编号
     */
    private String fileNo2;

    /**
     * 法人证件类型
     */
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    private String legalIdcardNo;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
    private String legalType;
    /**
     * 邮政编码
     */
    private String zipcode;
    /**
     * 企业联系电话
     */
    private String telephone;
    /**
     * 组织机构代码
     */
    private String orgCode;
    /**
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
    private BigDecimal registeredCapital;
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    private String noTaxProve;
    /**
     * 纳税人识别号（国税）
     */
    private String stateTaxRegNo;
    /**
     * 纳税人识别号（地税）
     */
    private String taxRegNo;
    /**
     * 注册详细地址
     */
    private String regAddress;
    /**
     * 经营（业务）范围
     */
    private String businessScope;
    /**
     * 上级基本户开户许可核准号
     */
    private String parAccountKey;
    /**
     * 上级机构名称
     */
    private String parCorpName;
    /**
     * 上级法人姓名
     */
    private String parLegalName;
    /**
     * 上级法人类型
     */
    private String parLegalType;
    /**
     * 上级法人证件号码
     */
    private String parLegalIdcardNo;
    /**
     * 上级法人证件类型
     */
    private String parLegalIdcardType;
    /**
     * 上级组织机构代码
     */
    private String parOrgCode;
    /**
     * 账户备注
     */
    private String remark;

}
