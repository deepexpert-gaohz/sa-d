package com.ideatech.ams.account.dto.core;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 核心数据文件实体
 * @author van
 */

@Data
public class CoreAccountDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 数据是否已处理
     */
    private Boolean process;

    /**
     * 处理错误信息
     */
    private String message;

    /**
     * 文件处理批次ID
     */
    private Long fileBatchId;

    /**
     * 操作类型
     */
    private String billType;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 核心机构的代码
     */
    private String organCode;

    /**
     * 账户性质大类
     */
    private String acctBigType;

    /**
     * 账户性质
     */
    private String acctType;

    /**
     * 客户号
     */
    private String customerNo;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 存款人类别
     */
    private String depositorType;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 账户简名
     */
    private String acctShortName;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 币种
     */
    private String currencyType;

    /**
     * 账户开户日期
     */
    private String acctCreateDate;

    /**
     * 账户激活日期
     */
    private String acctActiveDate;

    /**
     * 账户有效期(临时账户)
     */
    private String effectiveDate;

    /**
     * 开户原因
     */
    private String acctCreateReason;

    /**
     * 账户销户日期
     */
    private String cancelDate;

    /**
     * 销户原因
     */
    private String acctCancelReason;

    /**
     * 久悬日期
     */
    private String acctSuspenDate;

    /**
     * 影像批次号
     */
    private String imageBatchNo;

    /**
     * 注册国家代码
     */
    private String regCountry;

    /**
     * 注册省份（代码）
     */
    private String regProvince;

    /**
     * 注册城市（代码）
     */
    private String regCity;

    /**
     * 注册地区（代码）
     */
    private String regArea;

    /**
     * 注册详细地址
     */
    private String regAddress;

    /**
     * 完整注册地址(与营业执照一致)
     */
    private String regFullAddress;

    /**
     * 行业归属
     */
    private String industryCode;

    /**
     * 登记部门
     */
    private String regOffice;

    /**
     * 工商注册类型
     */
    private String regType;

    /**
     * 工商注册编号
     */
    private String regNo;

    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;

    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;

    /**
     * 证明文件1设立日期
     */
    private String fileSetupDate;

    /**
     * 证明文件1到期日
     */
    private String fileDue;

    /**
     * 证明文件2编号
     */
    private String fileNo2;

    /**
     * 证明文件2种类
     */
    private String fileType2;

    /**
     * 证明文件2设立日期
     */
    private String fileSetupDate2;

    /**
     * 证明文件2到期日
     */
    private String fileDue2;

    /**
     * 成立日期
     */
    private String setupDate;

    /**
     * 营业执照号码
     */
    private String businessLicenseNo;

    /**
     * 营业执照到期日
     */
    private String businessLicenseDue;

    /**
     * 未标明注册资金
     */
    private String isIdentification;

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
     * 法人类型
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
     * 法人联系电话
     */
    private String legalTelephone;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 组织机构代码证件到期日
     */
    private String orgCodeDue;

    /**
     * 机构信用代码
     */
    private String orgEccsNo;

    /**
     * 机构状态
     */
    private String orgStatus;

    /**
     * 组织机构类别
     */
    private String orgType;

    /**
     * 组织机构类别细分
     */
    private String orgTypeDetail;

    /**
     * 纳税人识别号（国税）
     */
    private String stateTaxRegNo;

    /**
     * 国税证件到期日
     */
    private String stateTaxDue;

    /**
     * 纳税人识别号（地税）
     */
    private String taxRegNo;

    /**
     * 地税证件到期日
     */
    private String taxDue;

    /**
     * 办公地址国家（代码）
     */
    private String workCountry;

    /**
     * 办公地址省份（代码）
     */
    private String workProvince;

    /**
     * 办公城市（代码）
     */
    private String workCity;

    /**
     * 办公地区（代码）
     */
    private String workArea;

    /**
     * 办公详细地址
     */
    private String workAddress;

    /**
     * 完整办公地址
     */
    private String workFullAddress;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 邮政编码
     */
    private String zipcode;

    /**
     * 财务主管姓名
     */
    private String financeName;

    /**
     * 财务部联系电话
     */
    private String financeTelephone;

    /**
     * 财务主管身份证号
     */
    private String financeIdcardNo;

    /**
     * 经济类型
     */
    private String economyType;

    /**
     * 经济行业分类（代码）
     */
    private String economyIndustryCode;

    /**
     * 经济行业分类
     */
    private String economyIndustryName;

    /**
     * 基本开户许可核准号
     */
    private String accountKey;

    /**
     * 基本户状态
     */
    private String basicAccountStatus;

    /**
     * 基本户注册地地区代码
     */
    private String basicAcctRegArea;

    /**
     * 基本户开户银行代码
     */
    private String basicBankCode;

    /**
     * 基本户开户银行名称
     */
    private String basicBankName;

    /**
     * 贷款卡编码
     */
    private String bankCardNo;

    /**
     * 上级基本户开户许可核准号
     */
    private String parAccountKey;

    /**
     * 上级机构名称
     */
    private String parCorpName;

    /**
     * 上级法人证件号码
     */
    private String parLegalIdcardNo;

    /**
     * 上级法人证件类型
     */
    private String parLegalIdcardType;

    /**
     * 上级法人证件到期日
     */
    private String parLegalIdcardDue;

    /**
     * 上级法人姓名
     */
    private String parLegalName;

    /**
     * 上级法人类型
     */
    private String parLegalType;

    /**
     * 上级法人联系电话
     */
    private String parLegalTelephone;

    /**
     * 上级组织机构代码
     */
    private String parOrgCode;

    /**
     * 上级组织机构代码证到期日
     */
    private String parOrgCodeDue;

    /**
     * 上级机构信用代码
     */
    private String parOrgEccsNo;

    /**
     * 上级机构信用代码证到期日
     */
    private String parOrgEccsDue;

    /**
     * 上级登记注册号类型
     */
    private String parRegType;

    /**
     * 上级登记注册号码
     */
    private String parRegNo;

    /**
     * 机构英文名称
     */
    private String orgEnName;

    /**
     * 开户证明文件种类1
     */
    private String acctFileType;

    /**
     * 开户证明文件编号1
     */
    private String acctFileNo;

    /**
     * 开户证明文件种类2
     */
    private String acctFileType2;

    /**
     * 开户证明文件编号2
     */
    private String acctFileNo2;

    /**
     * 账户名称构成方式
     */
    private String accountNameFrom;

    /**
     * 账户前缀
     */
    private String saccprefix;

    /**
     * 账户后缀
     */
    private String saccpostfix;

    /**
     * 资金性质
     */
    private String capitalProperty;

    /**
     * 取现标识
     */
    private String enchashmentType;

    /**
     * 资金管理人姓名
     */
    private String fundManager;

    /**
     * 资金管理人身份证种类
     */
    private String fundManagerIdcardType;

    /**
     * 资金管理人身份证编号
     */
    private String fundManagerIdcardNo;

    /**
     * 资金管理人证件到期日
     */
    private String fundManagerIdcardDue;

    /**
     * 资金管理人联系电话
     */
    private String fundManagerTelephone;

    /**
     * 内设部门名称
     */
    private String insideDeptName;

    /**
     * 内设部门负责人名称
     */
    private String insideLeadName;

    /**
     * 内设部门负责人身份种类
     */
    private String insideLeadIdcardType;

    /**
     * 内设部门负责人身份编号
     */
    private String insideLeadIdcardNo;

    /**
     * 负责人证件到期日
     */
    private String insideLeadIdcardDue;

    /**
     * 内设部门联系电话
     */
    private String insideTelephone;

    /**
     * 内设部门地址
     */
    private String insideAddress;

    /**
     * 内设部门邮编
     */
    private String insideZipcode;

    /**
     * 非临时项目部名称
     */
    private String nontmpProjectName;

    /**
     * 非临时负责人姓名
     */
    private String nontmpLegalName;

    /**
     * 非临时联系电话
     */
    private String nontmpTelephone;

    /**
     * 非临时邮政编码
     */
    private String nontmpZipcode;

    /**
     * 非临时地址
     */
    private String nontmpAddress;

    /**
     * 非临时身份证件编号
     */
    private String nontmpLegalIdcardNo;

    /**
     * 非临时身份证件种类
     */
    private String nontmpLegalIdcardType;

    /**
     * 非临时身份证件到期日
     */
    private String nontmpLegalIdcardDue;

    /**
     * 备注
     */
    private String remark;

}
