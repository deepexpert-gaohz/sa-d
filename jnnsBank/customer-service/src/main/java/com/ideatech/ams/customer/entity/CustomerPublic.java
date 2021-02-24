package com.ideatech.ams.customer.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对公客户信息表
 *
 * @author RJQ
 *
 */
@Entity
@Table(name = "CUSTOMER_PUBLIC",
        indexes = {@Index(name = "customer_public_ci_idx",columnList = "customerId")})
@Data
public class CustomerPublic extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_CUSTOMER_PUBLIC";

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 注册地区
     */
    @Column(length = 30)
    private String regArea;

    /**
     * 注册地区中文名
     */
    @Column(length = 100)
    private String regAreaChname;
    /**
     * 注册地地区代码
     */
    @Column(length = 50)
    private String regAreaCode;

    /**
     * 注册详细地址
     */
    @Column(length = 255)
    private String regAddress;
    /**
     * 完整注册地址(与营业执照一致)
     */
    @Column(length = 500)
    private String regFullAddress;
    /**
     * 行业归属
     */
    @Column(length = 150)
    private String industryCode;
    /**
     * 登记部门
     */
    @Column(length = 30)
    private String regOffice;
    /**
     * 工商注册类型
     */
    @Column(length = 30)
    private String regType;
    /**
     * 工商注册编号
     */
    @Column(length = 100)
    private String regNo;
    /**
     * 证明文件1编号(工商注册号)
     */
    @Column(length = 100)
    private String fileNo;
    /**
     * 证明文件1种类(工商注册类型)
     */
    @Column(length = 50)
    private String fileType;
    /**
     * 证明文件1设立日期
     */
    @Column(length = 10)
    private String fileSetupDate;
    /**
     * 证明文件1到期日
     */
    @Column(length = 10)
    private String fileDue;
    /**
     * 证明文件2编号
     */
    @Column(length = 100)
    private String fileNo2;
    /**
     * 证明文件2种类
     */
    @Column(length = 50)
    private String fileType2;
    /**
     * 证明文件2设立日期
     */
    @Column(length = 10)
    private String fileSetupDate2;
    /**
     * 证明文件2到期日
     */
    @Column(length = 10)
    private String fileDue2;
    /**
     * 成立日期
     */
    @Column(length = 10)
    private String setupDate;
    /**
     * 营业执照号码
     */
    @Column(length = 50)
    private String businessLicenseNo;
    /**
     * 营业执照到期日
     */
    @Column(length = 10)
    private String businessLicenseDue;
    /**
     * 未标明注册资金
     */
    @Column(length = 100)
    private String isIdentification;
    /**
     * 注册资本币种
     */
    @Column(length = 10)
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
    @Column(length = 22)
    private BigDecimal registeredCapital;
    /**
     * 经营（业务）范围
     */
    @Column(length = 2000)
    private String businessScope;
    /**
     * 经营（业务）范围(信用代码证)
     */
    @Column(length = 2000)
    private String businessScopeEccs;
    /**
     * 企业规模
     */
    @Column(length = 30)
    private String corpScale;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
    @Column(length = 50)
    private String legalType;
    /**
     * 法人姓名
     */
    @Column(length = 50)
    private String legalName;
    /**
     * 法人证件类型
     */
    @Column(length = 20)
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    @Column(length = 50)
    private String legalIdcardNo;
    /**
     * 法人证件到期日
     */
    @Column(length = 20)
    private String legalIdcardDue;
    /**
     * 法人联系电话
     */
    @Column(length = 50)
    private String legalTelephone;
    /**
     * 组织机构代码
     */
    @Column(length = 100)
    private String orgCode;
    /**
     * 组织机构代码证件到期日
     */
    @Column(length = 10)
    private String orgCodeDue;
    /**
     * 机构信用代码
     */
    @Column(length = 100)
    private String orgEccsNo;
    /**
     * 机构状态
     */
    @Column(length = 30)
    private String orgStatus;
    /**
     * 组织机构类别
     */
    @Column(length = 30)
    private String orgType;
    /**
     * 组织机构类别细分
     */
    @Column(length = 30)
    private String orgTypeDetail;
    /**
     * 同业金融机构编码
     */
    @Column(length = 50)
    private String interbankNo;
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    @Column(length = 100)
    private String noTaxProve;
    /**
     * 纳税人识别号（国税）
     */
    @Column(length = 100)
    private String stateTaxRegNo;
    /**
     * 国税证件到期日
     */
    @Column(length = 10)
    private String stateTaxDue;
    /**
     * 纳税人识别号（地税）
     */
    @Column(length = 100)
    private String taxRegNo;
    /**
     * 地税证件到期日
     */
    @Column(length = 10)
    private String taxDue;
    /**
     * 办公国家代码
     */
    @Column(length = 30)
    private String workCountry;
    /**
     * 办公省份
     */
    @Column(length = 30)
    private String workProvince;
    /**
     * 办公地区省份中文名
     */
    @Column(length = 100)
    private String workProvinceChname;
    /**
     * 办公城市
     */
    @Column(length = 30)
    private String workCity;
    /**
     * 办公城市中文名
     */
    @Column(length = 100)
    private String workCityChname;
    /**
     * 办公地区
     */
    @Column(length = 30)
    private String workArea;
    /**
     * 办公地区中文名
     */
    @Column(length = 100)
    private String workAreaChname;
    /**
     * 办公详细地址
     */
    @Column(length = 255)
    private String workAddress;
    /**
     * 完整办公地址(与营业执照一致)
     */
    @Column(length = 500)
    private String workFullAddress;

    /**
     * 是否与与注册地址一致
     */
    @Column(length = 30)
    private String isSameRegistArea;

    /**
     * 联系电话
     */
    @Column(length = 50)
    private String telephone;
    /**
     * 邮政编码
     */
    @Column(length = 6)
    private String zipcode;
    /**
     * 财务主管姓名
     */
    @Column(length = 50)
    private String financeName;
    /**
     * 财务部联系电话
     */
    @Column(length = 50)
    private String financeTelephone;
    /**
     * 财务主管身份证号
     */
    @Column(length = 50)
    private String financeIdcardNo;
    /**
     * 经济类型
     */
    @Column(length = 30)
    private String economyType;
    /**
     * 经济行业分类Code
     */
    @Column(length = 30)
    private String economyIndustryCode;
    /**
     * 经济行业分类
     */
    @Column(length = 100)
    private String economyIndustryName;
    /**
     * 基本开户许可核准号
     */
    @Column(length = 50)
    private String accountKey;

    /**
     * 基本户状态
     */
    @Column(length = 30)
    private String basicAccountStatus;
    /**
     * 基本户注册地地区代码
     */
    @Column(length = 100)
    private String basicAcctRegArea;
    /**
     * 基本户开户银行金融机构编码
     */
    @Column(length = 14)
    private String basicBankCode;
    /**
     * 基本户开户银行名称
     */
    @Column(length = 100)
    private String basicBankName;
    /**
     * 贷款卡编码
     */
    @Column(length = 100)
    private String bankCardNo;
    /**
     * 上级基本户开户许可核准号
     */
    @Column(length = 50)
    private String parAccountKey;
    /**
     * 上级机构名称
     */
    @Column(length = 100)
    private String parCorpName;
    /**
     * 上级法人证件号码
     */
    @Column(length = 50)
    private String parLegalIdcardNo;
    /**
     * 上级法人证件类型
     */
    @Column(length = 30)
    private String parLegalIdcardType;
    /**
     * 上级法人证件到期日
     */
    @Column(length = 10)
    private String parLegalIdcardDue;
    /**
     * 上级法人姓名
     */
    @Column(length = 50)
    private String parLegalName;
    /**
     * 上级法人类型
     */
    @Column(length = 30)
    private String parLegalType;
    /**
     * 上级法人联系电话
     */
    @Column(length = 50)
    private String parLegalTelephone;
    /**
     * 上级组织机构代码
     */
    @Column(length = 100)
    private String parOrgCode;
    /**
     * 上级组织机构代码证到期日
     */
    @Column(length = 10)
    private String parOrgCodeDue;
    /**
     * 上级机构信用代码
     */
    @Column(length = 100)
    private String parOrgEccsNo;
    /**
     * 上级机构信用代码证到期日
     */
    @Column(length = 10)
    private String parOrgEccsDue;
    /**
     * 上级登记注册号类型
     */
    @Column(length = 30)
    private String parRegType;
    /**
     * 上级登记注册号码
     */
    @Column(length = 100)
    private String parRegNo;
    /**
     * 扩展字段1
     */
    @Column(length = 255)
    private String string001;
    /**
     * 扩展字段2
     */
    @Column(length = 255)
    private String string002;
    /**
     * 扩展字段3
     */
    @Column(length = 255)
    private String string003;
    /**
     * 扩展字段4
     */
    @Column(length = 255)
    private String string004;
    /**
     * 扩展字段5
     */
    @Column(length = 255)
    private String string005;
    /**
     * 扩展字段6
     */
    @Column(length = 255)
    private String string006;
    /**
     * 扩展字段7
     */
    @Column(length = 255)
    private String string007;
    /**
     * 扩展字段8
     */
    @Column(length = 255)
    private String string008;
    /**
     * 扩展字段9
     */
    @Column(length = 255)
    private String string009;
    /**
     * 扩展字段10
     */
    @Column(length = 255)
    private String string010;

    /**
     * 客户公章名称
     */
    @Column(length = 200)
    private String sealName;

    /**
     * 机构英文名称
     */
    @Column(length = 100)
    private String orgEnName;
    /**
     * 注册国家代码
     */
    @Column(length = 30)
    private String regCountry;
    /**
     * 注册省份
     */
    @Column(length = 30)
    private String regProvince;
    /**
     * 注册地区省份中文名
     */
    @Column(length = 100)
    private String regProvinceChname;
    /**
     * 注册城市
     */
    @Column(length = 30)
    private String regCity;
    /**
     * 注册城市中文名
     */
    @Column(length = 100)
    private String regCityChname;

    /**
     * 机构fullId
     */
    private String organFullId;

    /**
     * 存款人名称
     */
    @Column(length = 200)
    private String depositorName;

    /**
     * 证明文件到期日是否超期
     */
    @Column(length = 10)
    private Boolean isFileDueOver;

    /**
     * 法人证件到期日是否超期
     */
    @Column(length = 10)
    private Boolean isLegalIdcardDueOver;

    /**
     * 核实状态
     */
    @Column(length = 500)
    private String checkStatus;

    /**
     * 账户号
     */
    private String acctNo;

    /**
     * 开户行
     */
    private String openBank;

//  /**
//   * 关联企业
//   */
//
//
//  @OneToMany(mappedBy = "customerPublic")
//  private Set<RelateCompany> relateCompanys;
//
//  /**
//   * 企业股东/高管信息
//   */
//
//
//  @OneToMany(mappedBy = "customerPublic")
//  private Set<CompanyPartner> companyPartners;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
        /*if (StringUtils.isBlank(this.getCustomerCategory())) {
            throw new RuntimeException("[客户类别 ]不能为空");
        }*/
        if (StringUtils.isBlank(this.getRegProvince())) {
            throw new RuntimeException("[注册省份 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegProvince())) {
            throw new RuntimeException("[注册省份 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegCity())) {
            throw new RuntimeException("[注册城市 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegArea())) {
            throw new RuntimeException("[注册地区 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegAddress())) {
            throw new RuntimeException("[注册详细地址 ]不能为空");
        }
        if (StringUtils.isBlank(this.getBasicAcctRegArea())) {
            throw new RuntimeException("[基本户注册地地区代码 ]不能为空");
        }
        if (StringUtils.isBlank(this.getIndustryCode())) {
            throw new RuntimeException("[行业归属 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegOffice())) {
            throw new RuntimeException("[登记部门 ]不能为空");
        }
        if (StringUtils.isBlank(this.getIsIdentification())) {
            throw new RuntimeException("[未标明注册资金 ]不能为空");
        }
        if (StringUtils.isBlank(this.getRegCurrencyType())) {
            throw new RuntimeException("[注册资本币种 ]不能为空");
        }
        if (this.getRegisteredCapital() != null) {
            throw new RuntimeException("[注册资金 ]不能为空");
        }

        if (StringUtils.isBlank(this.getEconomyIndustryCode())) {
            throw new RuntimeException("[经济行业分类 ]不能为空");
        }
        if (StringUtils.isBlank(this.getCorpScale())) {
            throw new RuntimeException("[企业规模 ]不能为空");
        }
        if (StringUtils.isBlank(this.getFinanceTelephone())) {
            throw new RuntimeException("[财务部联系电话 ]不能为空");
        }
//    if (StringUtils.isBlank(this.getFinanceName())) {
//      throw new RuntimeException("[财务主管姓名 ]不能为空");
//    }
        if (StringUtils.isBlank(this.getTelephone())) {
            throw new RuntimeException("[联系电话 ]不能为空");
        }
        if (StringUtils.isBlank(this.getZipcode())) {
            throw new RuntimeException("[邮政编码 ]不能为空");
        }
        if (StringUtils.isBlank(this.getOrgType())) {
            throw new RuntimeException("[组织机构类别 ]不能为空");
        }
        if (StringUtils.isBlank(this.getOrgCode())) {
            throw new RuntimeException("[组织机构代码 ]不能为空");
        }
        if (StringUtils.isBlank(this.getOrgCodeDue())) {
            throw new RuntimeException("[组织机构代码证件到期日 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalType())) {
            throw new RuntimeException("[法人类型 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalName())) {
            throw new RuntimeException("[法人姓名 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalIdcardType())) {
            throw new RuntimeException("[法人证件类型 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalIdcardNo())) {
            throw new RuntimeException("[法人证件编号 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalIdcardDue())) {
            throw new RuntimeException("[法人证件到期日 ]不能为空");
        }
        if (StringUtils.isBlank(this.getLegalTelephone())) {
            throw new RuntimeException("[法人联系电话 ]不能为空");
        }
        if (StringUtils.isBlank(this.getBusinessScope())) {
            throw new RuntimeException("[经营（业务）范围 ]不能为空");
        }

        // [客户ID]的必填验证
        // if (this.getCustomerId() == null) {
        // throw new RuntimeException("[客户ID]不能为空");
        // }
    }
}
