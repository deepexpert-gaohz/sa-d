package com.ideatech.ams.customer.dto;

import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;


@Data
public class CustomerPublicLogInfo extends BaseMaintainableDto {

    /**
     * id
     */
    private Long id;

    /**
     * 扩展字段6 --记录基本户编号生成时间=防止被覆盖
     */


    private String string006;
    /**
     * 扩展字段7
     */


    private String string007;
    /**
     * 扩展字段8
     */


    private String string008;
    /**
     * 扩展字段9
     */


    
    private String string009;
    /**
     * 扩展字段10
     */


    
    private String string010;
    /**
     * 更新状态(New新增记录/Error更新异常/Hold暂挂)
     */


    
    private String status;
    /**
     * 更新描述
     */


    
    private String description;
    /**
     * 客户ID
     */


    
    private Long customerId;
    /**
     * 客户号
     */


    
    private String customerNo;
    /**
     * 存款人名称
     */


    
    private String depositorName;
    /**
     * 客户类型(1个人2对公3金融)
     */


    
    
    private CustomerType customerClass;
    /**
     * 证件类型
     */


    
    private String credentialType;
    /**
     * 证件号码
     */


    
    private String credentialNo;
    /**
     * 证件到期日
     */


    
    private String credentialDue;
    /**
     * 机构fullId
     */


    
    private String organFullId;
    /**
     * 客户公章名称
     */


    
    private String sealName;
    /**
     * 机构英文名称
     */


    
    private String orgEnName;
    /**
     * 注册国家代码
     */


    
    private String regCountry;
    /**
     * 注册省份
     */


    
    private String regProvince;
    /**
     * 注册地区省份中文名
     */


    
    private String regProvinceChname;
    /**
     * 注册城市
     */


    
    private String regCity;
    /**
     * 注册城市中文名
     */


    
    private String regCityChname;
    /**
     * 注册地区
     */


    
    private String regArea;
    /**
     * 注册地区中文名
     */


    
    private String regAreaChname;
    /**
     * 注册地地区代码
     */


    
    private String regAreaCode;
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


    
    private BigDecimal registeredCapital;
    /**
     * 经营（业务）范围
     */


    
    private String businessScope;
    /**
     * 经营（业务）范围(信用代码证)
     */


    
    private String businessScopeEccs;
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
     * 同业金融机构编码
     */


    
    private String interbankNo;
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */


    
    private String noTaxProve;
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
     * 办公国家代码
     */


    
    private String workCountry;
    /**
     * 办公省份
     */


    
    private String workProvince;
    /**
     * 办公地区省份中文名
     */


    
    private String workProvinceChname;
    /**
     * 办公城市
     */


    
    private String workCity;
    /**
     * 办公城市中文名
     */


    
    private String workCityChname;
    /**
     * 办公地区
     */


    
    private String workArea;
    /**
     * 办公地区中文名
     */


    
    private String workAreaChname;
    /**
     * 办公详细地址
     */


    
    private String workAddress;

    /**
     * 是否与与注册地址一致
     */


    
    private String isSameRegistArea;

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
     * 经济行业分类Code
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
     * 基本户开户银行金融机构编码
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
     * 企业规模
     */


    
    private String corpScale;
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
     * 上级法人联系电话
     */


    private String parLegalTelephone;
    /**
     * 上级法人类型
     */


    
    private String parLegalType;
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
     * 关联单据ID
     */


    
    private Long refBillId;

    private Long refCustomerBillId;

    /**
     * 扩展字段1
     */


    
    private String string001;
    /**
     * 扩展字段2
     */


    
    private String string002;
    /**
     * 扩展字段3
     */


    
    private String string003;
    /**
     * 扩展字段4
     */


    
    private String string004;
    /**
     * 扩展字段5
     */


    
    private String string005;

    /**
     * 前一笔日志记录ID
     */


    
    private Long preLogId;
    /**
     * 序号
     */


    
    private Long sequence;

    /**
     * 关联企业历史
     */


    private Set<RelateCompanyLogInfo> relateCompanysLog;

    /**
     * 企业股东/高管信息历史
     */


    private Set<CompanyPartnerLogInfo> companyPartnersLog;

    private String operatorIdcardDue;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [更新状态(New新增记录/Error更新异常/Hold暂挂)]的必填验证
        /*if (this.getStatus() == null || this.getStatus().equals("")) {
            throw new RuntimeException("[更新状态(New新增记录/Error更新异常/Hold暂挂)]不能为空");
        }*/
        // [客户ID]的必填验证
        // if (this.getCustomerId() == null) {
        // throw new RuntimeException("[客户ID]不能为空");
        // }
        // [存款人名称]的必填验证
        if (this.getDepositorName() == null || this.getDepositorName().equals("")) {
            throw new RuntimeException("[存款人名称]不能为空");
        }
        // [客户类型(1个人2对公3金融)]的必填验证
        if (this.getCustomerClass() == null) {
            throw new RuntimeException("[客户类型(1个人2对公3金融)]不能为空");
        }
        /*// [证件类型]的必填验证
        if (this.getCredentialType() == null || this.getCredentialType().equals("")) {
            throw new RuntimeException("[证件类型]不能为空");
        }
        // [证件号码]的必填验证
        if (this.getCredentialNo() == null || this.getCredentialNo().equals("")) {
            throw new RuntimeException("[证件号码]不能为空");
        }*/
        // [关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
