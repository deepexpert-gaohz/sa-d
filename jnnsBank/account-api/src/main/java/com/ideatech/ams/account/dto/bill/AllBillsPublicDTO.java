package com.ideatech.ams.account.dto.bill;

import com.ideatech.ams.account.enums.*;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.customer.dto.CompanyPartnerInfo;
import com.ideatech.ams.customer.dto.RelateCompanyInfo;
import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author jogy.he
 * @Description 流水信息数据传递对象
 * 主要处理的类(是一张视图,包含所有账户以及客户表的字段)
 */

@Data
public class AllBillsPublicDTO {

    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_PUBLIC_BILLS_ALL_V";

    /**
     * 流水id
     */
    private Long id;

    private String createdDate;

    private String createdBy;

    private String lastUpdateBy;
    private String lastUpdateDate;

    /**
     * 单据编号
     */
    private String billNo;
    /**
     * 单据日期
     */
    private String billDate;
    /**
     * 单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）
     */
//    @Enumerated(EnumType.STRING)
    private BillType billType;
    /**
     * 单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)
     */
//    @Enumerated(EnumType.STRING)
    private BillStatus status;
    /**
     * 审核人id
     */
    private Long approver;
    /**
     * 审核日期
     */
    private String approveDate;
    /**
     * 审核/驳回说明
     */
    private String approveDesc;
    /**
     * 退回原因
     */
    private String denyReason;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * T+1是否来自核心(0否1是)
     */
//    @Column(length = 10)
    private CompanyIfType acctIsFromCore;
    /**
     * T+1数据是否完整(0否1是)
     */
//    @Enumerated(EnumType.STRING)
    private CompanyIfType coreDataCompleted;
    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 单据描述
     */
    private String description;
    /**
     * 上报信用代码证状态（01成功；02失败；03无需上报）
     */
//    @Enumerated(EnumType.STRING)
    private CompanySyncStatus eccsSyncStatus;
    /**
     * 上报信用代码证错误信息
     */
    private String eccsSyncError;
    /**
     * 上报操作人
     */
    private Long eccsOperator;
    /**
     * 上报信用代码证时间
     */
    private String eccsSyncTime;
    /**
     * 账户报备成功后，再次校验是否报备成功
     */
//    @Enumerated(EnumType.STRING)
    private SyncCheckStatus eccsSyncCheck;
    /**
     * T+1账户是否需要手工处理(0否1是)
     */
//    @Enumerated(EnumType.STRING)
    private CompanyIfType handingMark;
    /**
     * 流水的完整机构ID
     */
    private String organFullId;
    /**
     * 上报人行账管状态（01成功；02失败；03无需上报）
     */
//    @Enumerated(EnumType.STRING)
    private CompanySyncStatus pbcSyncStatus;
    /**
     * 上报人行账管错误信息
     */
    private String pbcSyncError;
    /**
     * 上报操作人
     */
    private Long pbcOperator;
    /**
     * 上报人行账管时间
     */
    private String pbcSyncTime;
    /**
     * 校验账户报备是否成功
     */
//    @Enumerated(EnumType.STRING)
    private SyncCheckStatus pbcSyncCheck;
    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
//    @Enumerated(EnumType.STRING)
    private CompanySyncOperateType pbcSyncMethod;

    /**
     * 人行核准状态(01待审核、02审核通过、03无需审核)
     */
//    @Enumerated(EnumType.STRING)
    private CompanyAmsCheckStatus pbcCheckStatus;
    /**
     * 人行核准日期
     */
    private String pbcCheckDate;

    /**
     * 核心同步状态
     */
    private CompanySyncStatus coreSyncStatus;

    /**
     * 上报人行账管错误信息
     */
    private String coreSyncError;
    /**
     * 上报操作人
     */
    private Long coreOperator;
    /**
     * 上报人行账管时间
     */
    private String coreSyncTime;
    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
    private CompanySyncOperateType coreSyncMethod;

    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 客户Log表id
     */
    private Long customerLogId;
    /**
     * 流水最终状态标识(0否1是)
     */
//    @Enumerated(EnumType.STRING)
    private CompanyIfType finalStatus;
    /**
     * 流水初始化待补录(完整性)状态(0-无需补录、1-待补录、2-已补录)
     */
//    @Column(length = 15)
    private String initFullStatus;
    /**
     * 流水初始化备注信息
     */
//    @Column(length = 1000)
    private String initRemark;
    /**
     * 流水来源(预填单、核心T+0、核心T+1、AMS)
     */
//    @Enumerated(EnumType.STRING)
//    @Column(length = 15)
    private BillFromSource fromSource;
    /**
     * 客户中间表Id
     */
    private Long midId;
    /**
     * 客户日志表Id
     */
    private Long custLogId;
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
     * 客户类型(1个人2对公3金融)
     */
//    @Enumerated(EnumType.STRING)
    private CustomerType customerClass;
    /**
     * 存款人名称
     */
    private String depositorName;
    /**
     * 客户机构fullId
     */
    private String custOrganFullId;
    /**
     * 基本开户许可核准号
     */
    private String accountKey;
    /**
     * 账户许可核准号
     */
    private String accountLicenseNo;
    /**
     * 贷款卡编码
     */
    private String bankCardNo;
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
     * 经营（业务）范围
     */
    private String businessScope;
    /**
     * 经营（业务）范围(信用代码证)
     */
    private String businessScopeEccs;
    /**
     * 企业规模
     */
    private String corpScale;
    /**
     * 客户类别(01企业法人、02非法人企业、03机关、04预算事业单位、05非预算事业单位、06团（含）以上军队、07团（含）以上武警部队、08社
     * 会团体、09宗教组织、10民办非企业组织、11异地常设机构、12外国驻华机构、13有字号个体工商户、14无字号个体工商户、15居委会/村委会/社
     * 区委、16单位独立核算附属机构、17其他组织、18QFII、19临时机构、20境外机构、21港澳台机构、99其他、30金融同业)
     */
    private String customerCategory;
    /**
     * 经济行业分类Code
     */
    private String economyIndustryCode;
    /**
     * 经济行业分类
     */
    private String economyIndustryName;
    /**
     * 证明文件1到期日
     */
    private String fileDue;
    /**
     * 证明文件2到期日
     */
    private String fileDue2;
    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;
    /**
     * 证明文件2编号
     */
    private String fileNo2;
    /**
     * 证明文件1设立日期
     */
    private String fileSetupDate;
    /**
     * 证明文件2设立日期
     */
    private String fileSetupDate2;
    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;
    /**
     * 证明文件2种类(工商注册类型)
     */
    private String fileType2;
    /**
     * 行业归属
     */
    private String industryCode;
    /**
     * 同业金融机构编码
     */
    private String interbankNo;
    /**
     * 未标明注册资金
     */
    private String isIdentification;
    /**
     * 是否与与注册地址一致
     */
    private String isSameRegistArea;
    /**
     * 法人证件编号
     */
    private String legalIdcardNo;
    /**
     * 法人证件到期日
     */
    private String legalIdcardDue;
    /**
     * 法人证件类型
     */
    private String legalIdcardType;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 法人联系电话
     */
    private String legalTelephone;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
    private String legalType;
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    private String noTaxProve;
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
     * 上级登记注册号码
     */
    private String parRegNo;
    /**
     * 上级登记注册号类型
     */
    private String parRegType;
    /**
     * 注册详细地址
     */
    private String regAddress;
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
     * 注册城市
     */
    private String regCity;
    /**
     * 注册城市中文名
     */
    private String regCityChname;
    /**
     * 注册国家代码
     */
    private String regCountry;
    /**
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 完整注册地址(与营业执照一致)
     */
    private String regFullAddress;
    /**
     * 工商注册编号
     */
    private String regNo;
    /**
     * 登记部门
     */
    private String regOffice;
    /**
     * 注册省份
     */
    private String regProvince;
    /**
     * 注册地区省份中文名
     */
    private String regProvinceChname;
    /**
     * 工商注册类型
     */
    private String regType;
    /**
     * 注册资金（元）
     */
    private BigDecimal registeredCapital;
    /**
     * 客户公章名称
     */
    private String sealName;
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
     * 企业联系电话
     */
    private String telephone;
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
     * 完整办公地址(与营业执照一致)
     */
    private String workFullAddress;
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
     * 账户流水日志id
     */
    private Long acctLogId;
    /**
     * 账户分类(1个人2对公3金融)
     */
//    @Enumerated(EnumType.STRING)
    private AccountClass accountClass;
    /**
     * 账户状态
     */
//    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    /**
     * 销户原因
     */
    private String acctCancelReason;
    /**
     * 账户开户日期
     */
    private String acctCreateDate;
    /**
     * 账户激活日期
     */
    private String acctActiveDate;
    /**
     * 开户原因
     */
    private String acctCreateReason;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户简称
     */
    private String acctShortName;
    /**
     * 久悬日期
     */
    private String acctSuspenDate;
    /**
     * 开户银行金融机构编码
     */
    private String bankCode;
    /**
     * 开户银行名称
     */
    private String bankName;
    /**
     * 账户销户日期
     */
    private String cancelDate;
    /**
     * 账户有效期(临时账户)
     */
    private String effectiveDate;
    /**
     * 账户建立机构Fullid
     */
    private String acctOrgFullid;
    /**
     * 币种
     */
//    private String currencyType;
    /**
     * 账户备注
     */
    private String remark;
    /**
     * 账户名称构成方式
     */
    private String accountNameFrom;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */
    private AcctBigType acctBigType;

    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7
     * 非预算单位专用存款账户)
     */
//    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;

    /**
     * 存款人类别
     */
    private String depositorType;
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
     * 非临时身份证件种类
     */
    private String nontmpLegalIdcardType;
    /**
     * 非临时身份证件编号
     */
    private String nontmpLegalIdcardNo;
    /**
     * 非临时身份证件到期日
     */
    private String nontmpLegalIdcardDue;
    /**
     * 业务经办人证件类型
     */
    private String operatorIdcardType;
    /**
     * 业务经办人证件号码
     */
    private String operatorIdcardNo;
    /**
     * 业务经办人证件有效日期
     */
    private String operatorIdcardDue;
    /**
     * 业务经办人联系电话
     */
    private String operatorTelephone;
    /**
     * 业务经办人姓名
     */
    private String operatorName;
    /**
     * 账户前缀
     */
    private String saccprefix;
    /**
     * 账户后缀
     */
    private String saccpostfix;

    /**
     * 关联企业
     */

    private Set<RelateCompanyInfo> relateCompanys;

    /**
     * 企业股东/高管信息
     */

    private Set<CompanyPartnerInfo> companyPartners;

    /**
     * 机构英文名称
     */
    private String orgEnName;

    /**
     * 经济类型
     */
    private String economyType;

    /**
     * 内设部门地址
     */
    private String insideAddress;

    /**
     * 关联单据ID
     */
    private Long refBillId;
    /**
     * 原始流水id（变更销户久悬时，存前一笔流水id）
     */
    private Long originalBillId;
    /**
     * 变更字段的流水是否需要上报人行(0:无上报字段 1：包含上报字段)
     */
//    @Column(length = 14)
    private String changeFieldIsPbcSync;
    /**
     * 变更字段的流水是否需要上报信用机构(0:无上报字段 1：包含上报字段)
     */
//    @Column(length = 14)
    private String changeFieldIsEccsSync;

    private String submitApproveFlag;

    /**
     * 本次提交是否需要上报人行
     */
    private Boolean submitPbcFlag;

    /**
     * 本次提交是否需要上报信用代码
     */
    private Boolean submitEccsFlag;

    private Long recId;

    //预约相关新增字段开始
    /**
     * 预约id
     */
    private String preOpenAcctId;
    /**
     * 预约编号
     */
    private String applyId;
    /**
     * 当前预约机构的银行联行号
     */
    private String applyorganid;
    /**
     * 预约状态
     */
    private ApplyEnum applyStatus;
    /**
     * 影像临时编号
     */
    private String imageTempNo;
    /**
     * 按钮操作类型
     */
    private String action;
    //预约相关新增字段结束

    /**
     * 行内机构号
     */
    private String organCode;
    /**
     * 存量字段的标志位（1：存量数据，null：非存量数据）
     * 扩展字段3
     *
     */
    private String string003;
    /**
     * 存量数据的影像补录状态的标志位（1：已补录，0：未补录）
     * 扩展字段4
     */
    private String string004;

    /**
     * 存量数据的是否人行信息覆盖核心的标志位（1：已更新，0：未更新）
     * 扩展字段5
     */
    private String string005;

    /**
     * 存放基本户编号生成时间
     */
    private String string006;

    private Long acctId;

    /**
     * 白名单
     */
    private String whiteList;

    /**
     * 是否取消核准
     */
    private Boolean cancelHeZhun;

    /**
     * 查询密码
     */
    private String selectPwd;

    /**
     * 开户许可证（取消核准   不带字母）
     */
    private String openKey;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private OpenAccountSiteType openAccountSiteType;

    /**
     * 影像批次号（甘肃）
     */
    private String imageBatchNo;

    private List<AmsPrintInfo> allAccountData;

    /**
     * 原基本开户许可证号
     */
    private String oldAccountKey;

    /**
     * 影像上报状态
     */
    private CompanySyncStatus imgaeSyncStatus;

    /**
     * 变更字段集合
     */
    private String changeFields;

    //预约新增接口调用的字段

    /**
     * 客户预约办理时间
     */
    private String applyDate;

    /**
     * 预约客户姓名
     */
    private String applyOperator;

    /**
     * 预约客户手机号
     */
    private String applyPhone;

    private String currencyType;
    private String currency0;
    private String currency1;
    private String nationality;
    //预约新增接口调用的字段结束

    /**
     * @author jogy.he
     * @Description 保存前必要栏位验证
     */
    public void validate() {
        // [单据编号]的必填验证
        if (this.getBillNo() == null || this.getBillNo().equals("")) {
            throw new RuntimeException("[单据编号]不能为空");
        }

        if (this.getAcctType() == null || this.getAcctType().toString().equals("")) {
            throw new RuntimeException("[账户性质]不能为空");
        }

        if (this.getBillType() == null || this.getBillType().toString().equals("")) {
            throw new RuntimeException("[单据类型]不能为空");
        }

        if (this.acctType == CompanyAcctType.jiben || this.acctType ==
                CompanyAcctType.linshi || this.acctType == CompanyAcctType.teshu) {

            if (this.getDepositorName() == null || this.getDepositorName().equals("")) {
                throw new RuntimeException("[存款人名称]不能为空");
            }
        }

        if (this.getOrganFullId() == null || this.getOrganFullId().equals("")) {
            throw new RuntimeException("[流水机构fullId]不能为空");
        }

        if (this.getCustOrganFullId() == null || this.getCustOrganFullId().equals("")) {
            throw new RuntimeException("[客户机构fullId]不能为空");
        }

        if (this.getAcctOrgFullid() == null || this.getAcctOrgFullid().equals("")) {
            throw new RuntimeException("[账户机构fullId]不能为空");
        }

        // if (this.getAcctNo() == null || this.getAcctNo().equals("")) {
        // throw new RuntimeException("[账户账号]不能为空");
        // }else{
        if (StringUtils.isNotBlank(this.getAcctNo()) && this.getAcctNo().length() > 32) {
            throw new RuntimeException("账号不能超过32个字符");
            // }
        }
    }

    public String takeValidateName() {
        return takeComponentName("Validate");
    }

    private String takeComponentName(String postFix) {
        StringBuffer name = new StringBuffer();
        if (acctType != null) {
            name.append(StringUtils.uncapitalize(acctType.toString()));
        } else {
            throw new RuntimeException("账户性质不能为空");
        }
        name.append("Ams");
        name.append(StringUtils.capitalize(StringUtils.substringAfter(billType.toString().toLowerCase(), "_")));
        name.append("AllAccount");
        return name.append(postFix).toString();
    }

    /**
     * 校验存款人和账号
     */
    public void validateAcctNoAndDepositorName() {
        if(StringUtils.isBlank(this.getDepositorName())){
            throw new RuntimeException("[存款人名称]不能为空");
        }
        if(StringUtils.isBlank(this.getAcctNo())){
            throw new RuntimeException("[账户账号]不能为空");
        }
    }

    /**
     * 校验存款人和账号
     */
    public void validateAcctNo() {
        if(StringUtils.isBlank(this.getAcctNo())){
            throw new RuntimeException("[账户账号]不能为空");
        }
    }
}
