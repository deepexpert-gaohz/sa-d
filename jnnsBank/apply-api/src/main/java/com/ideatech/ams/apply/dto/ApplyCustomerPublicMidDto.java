package com.ideatech.ams.apply.dto;


import com.ideatech.ams.apply.enums.*;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.validator.EnumValidator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Data
public class ApplyCustomerPublicMidDto extends BaseMaintainableDto {

  private Long id;

  /**
   * 更新时需要用到
   * 关联到的开户流水的主键
   */
  private Long openAccountLogId;

  /**
   * 更新时需要用到
   * 账户信息的主键
   *
   */
  private Long accountInfoId;

  /**
   * 更新时需要用到
   * 客户信息详情的主键
   */
  private Long customerPublicMidId;

  /**
   *  **************************************** 账户信息 ****************************************
   */

  /**
   * 账号 （开户完成后才能填入此项）
   */
  private String acctNo;

  /**
   * 账户类型 一般户 基本户
   */
  @EnumValidator(enumClazz = AcctType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的账户类型和选项中的账户类型不匹配")
  private String acctType;

  /**
   * 开户银行代码
   */
  private String bankCode;

  /**
   * 开户银行名称
   */
  private String bankName;

  /**
   * 开户银行日期
   */
  private String acctCreateDate;

  /**
   * 开户证明文件种类
   */
  @EnumValidator(enumClazz = AcctFileType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的开户证明文件种类和选项的预填项不匹配")
  private String acctFileType;

  /**
   * 开户证明文件种类编号
   */
  private String acctFileNo;

  /**
   * 账户备注信息
   */
  private String remark;

  /**
   *  **************************************** 创建人相关信息 ****************************************
   */

  /**
   * 扩展字段6
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
   *  **************************************** 客户信息 ****************************************
   */

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
  @EnumValidator(enumClazz = DepositorType.class, attribute = "value", acceptEmptyString = true,
          message = "存款人类别选项不包含当前的输入项")
  private String depositorType;
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
   * 客户类别(01企业法人、02非法人企业、03机关、04预算事业单位、05非预算事业单位、06团（含）以上军队、07团（含）以上武警部队、08社
   * 会团体、09宗教组织、10民办非企业组织、11异地常设机构、12外国驻华机构、13有字号个体工商户、14无字号个体工商户、15居委会/村委会/社
   * 区委、16单位独立核算附属机构、17其他组织、18QFII、19临时机构、20境外机构、21港澳台机构、99其他、30金融同业)
   */

  private String customerCategory;
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
  @EnumValidator(enumClazz = IndustryCode.class, attribute = "value", acceptEmptyString = true,
          message = "输入的行业归属信息和输入项不匹配")
  private String industryCode;
  /**
   * 登记部门
   */

  @EnumValidator(enumClazz = RegOffice.class, attribute = "value", acceptEmptyString = true,
          message = "输入登记部门与下拉菜单中的选项不匹配")
  private String regOffice;
  /**
   * 工商注册类型
   */
  @EnumValidator(enumClazz = RegType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的工商注册类型和下拉菜单中的选项不匹配")
  private String regType;
  /**
   * 工商注册编号
   */
  @Getter
  @Setter
  private String regNo;
  /**
   * 证明文件1编号(工商注册号)
   */
  @Getter
  @Setter
  private String fileNo;
  /**
   * 证明文件1种类(工商注册类型)
   */
  @EnumValidator(enumClazz = FileType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的证明文件1的种类和下拉菜单中的选项不匹配")
  private String fileType;
  /**
   * 证明文件1设立日期
   */
  @Getter
  @Setter
  private String fileSetupDate;
  /**
   * 证明文件1到期日
   */
  @Getter
  @Setter
  private String fileDue;
  /**
   * 证明文件2编号
   */
  @Getter
  @Setter
  private String fileNo2;
  /**
   * 证明文件2种类
   */
  @EnumValidator(enumClazz = FileType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的证明文件2的种类和下拉菜单中的选项不匹配")
  private String fileType2;
  /**
   * 证明文件2设立日期
   */
  @Getter
  @Setter
  private String fileSetupDate2;
  /**
   * 证明文件2到期日
   */
  @Getter
  @Setter
  private String fileDue2;
  /**
   * 成立日期
   */
  @Getter
  @Setter
  private String setupDate;
  /**
   * 营业执照号码
   */
  @Getter
  @Setter
  private String businessLicenseNo;
  /**
   * 营业执照到期日
   */
  @Getter
  @Setter
  private String businessLicenseDue;
  /**
   * 未标明注册资金
   */
  @EnumValidator(enumClazz = IsIdentification.class, attribute = "value", acceptEmptyString = true,
          message = "未标明注册资金选项的输入内容与预选项不匹配")
  private String isIdentification;
  /**
   * 注册资本币种
   */
  @EnumValidator(enumClazz = RegCurrencyType.class, acceptEmptyString = true, attribute = "value",
          message = "输入的注册资本币种和下拉菜单中的选项不匹配")
  private String regCurrencyType;
  /**
   * 注册资金（元）
   */
  @Getter
  @Setter
  private BigDecimal registeredCapital;
  /**
   * 经营（业务）范围
   */
  @Getter
  @Setter
  private String businessScope;
  /**
   * 经营（业务）范围(信用代码证)
   */
  @Getter
  @Setter
  private String businessScopeEccs;
  /**
   * 法人类型（法定代表人、单位负责人）
   */
  @EnumValidator(enumClazz = LegalType.class, acceptEmptyString = true, attribute = "value",
          message = "输入的法人类型和选项中的法人类型不匹配")
  private String legalType;
  /**
   * 法人姓名
   */
  @Getter
  @Setter
  private String legalName;
  /**
   * 法人证件类型
   */
  @EnumValidator(enumClazz = LegalIdCardType.class, acceptEmptyString = true, attribute = "value",
          message = "输入的法人证件类型与下拉菜单中的预选项不匹配")
  private String legalIdcardType;
  /**
   * 法人证件编号
   */
  @Getter
  @Setter
  private String legalIdcardNo;
  /**
   * 法人证件到期日
   */
  @Getter
  @Setter
  private String legalIdcardDue;
  /**
   * 法人联系电话
   */
  @Getter
  @Setter
  private String legalTelephone;
  /**
   * 组织机构代码
   */
  @Getter
  @Setter
  private String orgCode;
  /**
   * 组织机构代码证件到期日
   */
  @Getter
  @Setter
  private String orgCodeDue;
  /**
   * 机构信用代码
   */
  @Getter
  @Setter
  private String orgEccsNo;
  /**
   * 机构状态
   */
  @EnumValidator(enumClazz = OrgStatus.class, attribute = "value", acceptEmptyString = true,
          message = "输入的机构状态与下拉菜单中的选项不匹配")
  private String orgStatus;
  /**
   * 组织机构类别
   */
  @EnumValidator(enumClazz = OrgType.class, acceptEmptyString = true, attribute = "value",
          message = "输入的组织结构类别与下拉菜单中的选项不匹配")
  private String orgType;
  /**
   * 组织机构类别细分
   */
  @EnumValidator(enumClazz = OrgTypeDetails.class, acceptEmptyString = true, attribute = "value",
          message = "输入的组织结构类别细分与下拉菜单中的选项不匹配")
  private String orgTypeDetail;
  /**
   * 同业金融机构编码
   */
  @Getter
  @Setter
  private String interbankNo;
  /**
   * 无需办理税务登记证的文件或税务机关出具的证明
   */
  @Getter
  @Setter
  private String noTaxProve;
  /**
   * 纳税人识别号（国税）
   */
  @Getter
  @Setter
  private String stateTaxRegNo;
  /**
   * 国税证件到期日
   */
  @Getter
  @Setter
  private String stateTaxDue;
  /**
   * 纳税人识别号（地税）
   */
  @Getter
  @Setter
  private String taxRegNo;
  /**
   * 地税证件到期日
   */
  @Getter
  @Setter
  private String taxDue;
  /**
   * 办公国家代码
   */
  @Getter
  @Setter
  private String workCountry;
  /**
   * 办公省份
   */
  @Getter
  @Setter
  private String workProvince;
  /**
   * 办公地区省份中文名
   */
  @Getter
  @Setter
  private String workProvinceChname;
  /**
   * 办公城市
   */
  @Getter
  @Setter
  private String workCity;
  /**
   * 办公城市中文名
   */
  @Getter
  @Setter
  private String workCityChname;
  /**
   * 办公地区
   */
  @Getter
  @Setter
  private String workArea;
  /**
   * 办公地区中文名
   */
  @Getter
  @Setter
  private String workAreaChname;
  /**
   * 办公详细地址
   */
  @Getter
  @Setter
  private String workAddress;

  /**
   * 是否与与注册地址一致
   */
  @EnumValidator(enumClazz = IsSameRegistArea.class, attribute = "value", acceptEmptyString = true,
          message = "与注册地是否一致的输入信息与预选项不匹配")
  private String isSameRegistArea;

  /**
   * 联系电话
   */
  @Getter
  @Setter
  private String telephone;
  /**
   * 邮政编码
   */
  @Getter
  @Setter
  private String zipcode;
  /**
   * 财务主管姓名
   */
  @Getter
  @Setter
  private String financeName;
  /**
   * 财务部联系电话
   */
  @Getter
  @Setter
  private String financeTelephone;
  /**
   * 财务主管身份证号
   */
  @Getter
  @Setter
  private String financeIdcardNo;
  /**
   * 经济类型
   */
  @EnumValidator(enumClazz = EconomyType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的经济类型和选项中选项不匹配")
  private String economyType;
  /**
   * 经济行业分类Code
   */
  @Getter
  @Setter
  private String economyIndustryCode;
  /**
   * 经济行业分类
   */
  @Getter
  @Setter
  private String economyIndustryName;
  /**
   * 基本开户许可核准号
   */
  @Getter
  @Setter
  private String accountKey;
  /**
   * 基本户状态
   */
  @EnumValidator(enumClazz = BasicAccountStatus.class, acceptEmptyString = true, attribute = "value",
          message = "输入基本户状态和选项中的基本户状态不匹配")
  private String basicAccountStatus;
  /**
   * 基本户注册地地区代码
   */
  @Getter
  @Setter
  private String basicAcctRegArea;
  /**
   * 基本户开户银行金融机构编码
   */
  @Getter
  @Setter
  private String basicBankCode;
  /**
   * 基本户开户银行名称
   */
  @Getter
  @Setter
  private String basicBankName;
  /**
   * 贷款卡编码
   */
  @Getter
  @Setter
  private String bankCardNo;
  /**
   * 企业规模
   */
  @EnumValidator(enumClazz = CorpScale.class, attribute = "value", acceptEmptyString = true,
          message = "输入的企业规模和选项中可选的企业规模不匹配")
  private String corpScale;
  /**
   * 上级基本户开户许可核准号
   */
  @Getter
  @Setter
  private String parAccountKey;
  /**
   * 上级机构名称
   */
  @Getter
  @Setter
  private String parCorpName;
  /**
   * 上级法人证件号码
   */

  private String parLegalIdcardNo;
  /**
   * 上级法人证件类型
   */
  @EnumValidator(enumClazz = ParLegalIdCardType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的上级法人证件类型与下拉菜单中的选项不匹配")
  private String parLegalIdcardType;
  /**
   * 上级法人证件到期日
   */
  @Getter
  @Setter
  private String parLegalIdcardDue;
  /**
   * 上级法人姓名
   */
  @Getter
  @Setter
  private String parLegalName;
  /**
   * 上级法人类型
   */
  @EnumValidator(enumClazz = ParLegalType.class, acceptEmptyString = true, attribute = "value",
          message = "输入的上级法人类型与下拉菜单中的选项不匹配")
  private String parLegalType;
  /**
   * 上级法人联系电话
   */
  @Getter
  @Setter
  private String parLegalTelephone;
  /**
   * 上级组织机构代码
   */
  @Getter
  @Setter
  private String parOrgCode;
  /**
   * 上级组织机构代码证到期日
   */
  @Getter
  @Setter
  private String parOrgCodeDue;
  /**
   * 上级机构信用代码
   */
  @Getter
  @Setter
  private String parOrgEccsNo;
  /**
   * 上级机构信用代码证到期日
   */
  @Getter
  @Setter
  private String parOrgEccsDue;
  /**
   * 上级登记注册号类型
   */
  @EnumValidator(enumClazz = ParRegType.class, attribute = "value", acceptEmptyString = true,
          message = "输入的上级登记注册号类型和下拉菜单中的选项不匹配")
  private String parRegType;
  /**
   * 上级登记注册号码
   */
  @Getter
  @Setter
  private String parRegNo;
  /**
   * 关联单据ID
   */
  @Getter
  @Setter
  private Long refBillId;
  /**
   * 扩展字段1
   */
  @Getter
  @Setter
  private String string001;
  /**
   * 扩展字段2
   */
  @Getter
  @Setter
  private String string002;
  /**
   * 扩展字段3
   */
  @Getter
  @Setter
  private String string003;
  /**
   * 扩展字段4
   */
  @Getter
  @Setter
  private String string004;
  /**
   * 扩展字段5
   */
  @Getter
  @Setter
  private String string005;

  /**
   *  **************************************** 南京银行附加字段 ****************************************
   */

  /**
   * 从业人数
   */
  private String empNumber;

  /**
   * 营业收入
   */
  private String sales;

  /**
   * 资产总额
   */
  private String totalAssets;


  /**
   * 法人手机
   */
  private String legalMobile;

    /**
     * 临时户类型
     */
  private String acctType2;

    /**
     * 临时户有效期
     */
  private String acctType2Due;

    /**
     * 资金性质（专户）
     */
  private String capitalProperty;

  /**
   *  **************************************** 南京银行附加字段结束 ****************************************
   */

  /**
   * 注册信息中的邮政编码
   */
  private String regZipcode;

}
