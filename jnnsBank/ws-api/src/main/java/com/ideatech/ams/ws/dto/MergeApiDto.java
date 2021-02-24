package com.ideatech.ams.ws.dto;

import com.ideatech.ams.kyc.dto.*;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 合并接口返回
 */
@Data
public class MergeApiDto extends BaseMaintainableDto {

    /**
     * ID
     */
    private Long id;

    ///// 人行数据-start /////
    /**
     * 采集日期 yyyyMMdd hh:mm:ss
     */
    private String parDate;

    /**
     * 存款人名称
     */
//    private String depositorName;

    /**
     * 银行机构号（人行14位）
     */
    private String bankCode;

    /**
     * organFullId
     */
    private String organfullId;

    /**
     * 基本户注册地地区代码
     */
    private String basicAcctRegArea;
    /**
     * 银行机构名称
     */
    private String bankName;
    /**
     * 账户性质
     */
    private AccountType acctType;
    /**
     * 账户许可核准号
     */
    private String accountLicenseNo;
    /**
     * 帐号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户开户日期
     */
    private String acctCreateDate;
    /**
     * 账户销户日期
     */
    private String cancelDate;
    /**
     * 存款人类别
     */
    private String depositorType;
    /**
     * 注册地地区代码
     */
    private String regAreaCode;
    /**
     * 工商注册地址
     */
    private String indusRegArea;
    /**
     * 组织机构代码
     */
    private String orgCode;
    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;
    /**
     * 证明文件1编号(工商注册号)
     */
//    private String fileNo;
    /**
     * 证明文件2种类
     */
    private String fileType2;
    /**
     * 证明文件2编号
     */
    private String fileNo2;
    /**
     * 法人类型（法定代表人、单位负责人）
     */
    private String legalType;
    /**
     * 法人姓名
     */
//    private String legalName;
    /**
     * 法人证件类型
     */
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    private String legalIdcardNo;
    /**
     * 行业归属
     */
    private String industryCode;
    /**
     * 国税
     */
    private String stateTaxRegNo;
    /**
     * 地税
     */
    private String taxRegNo;
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    private String noTaxProve;
    /**
     * 注册币种
     */
    private String regCurrencyType;
    /**
     * 注册资金
     */
//    private String registeredCapital;
    /**
     * 经营（业务）范围（上报人行）
     */
    private String businessScope;
    /**
     * 经营（业务）范围（上报信用机构）
     */
    private String businessScopeEccs;
    /**
     * 注册（登记）地址
     */
//    private String regAddress;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 上级单位存款人名称
     */
    private String parCorpName;
    /**
     * 上级单位基本户开户许可证
     */
    private String parAccountKey;
    /**
     * 上级组织机构代码
     */
    private String parOrgCode;
    /**
     * 上级法人类型（法人代表、单位负责人）
     */
    private String parLegalType;
    /**
     * 上级法人姓名
     */
    private String parLegalName;
    /**
     * 上级法人证件类型
     */
    private String parLegalIdcardType;
    /**
     * 上级法人证件编号
     */
    private String parLegalIdcardNo;
    /**
     * 基本户开户许可证核准号
     */
    private String accountKey;
    /**
     * 账户状态
     */
    private AccountStatus accountStatus;
    /**
     * 资金性质
     */
    private String capitalProperty;
    /**
     * 账户证明文件类型(一般户、预算专户、非预算专户、非临时)
     */
    private String accountFileType;
    /**
     * 账户证明文件编号(一般户、预算专户、非预算专户、非临时)
     */
    private String accountFileNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 非临时_账户有效时间
     */
    private String effectiveDate;
    /**
     * 非临时户_账户申请开户原因
     */
    private String createAccountReason;
    /**
     * 非临时户_项目部名称
     */
    private String projectName;
    /**
     * 非临时户_负责人姓名
     */
    private String flsFzrLegalName;
    /**
     * 非临时户_负责人证件类型
     */
    private String flsFzrLegalIdcardType;
    /**
     * 非临时户_负责人证件号码
     */
    private String flsFzrLegalIdcardNo;
    /**
     * 非临时户_负责人联系电话
     */
    private String flsFzrTelephone;
    /**
     * 非临时户_负责人邮政编码
     */
    private String flsFzrZipCode;
    /**
     * 非临时户_负责人地址
     */
    private String flsFzrAddress;
    /**
     * 专用户_账户名称构成方式
     */
    private String accountNameFrom;
    /**
     * 专用户_账户后缀
     */
    private String saccpostfix;
    /**
     * 专用户_账户前缀
     */
    private String saccprefix;
    /**
     * 专用户_资金人姓名
     */
    private String moneyManager;
    /**
     * 专用户_资金身份种类
     */
    private String moneyManagerCtype;
    /**
     * 专用户_资金身份编号
     */
    private String moneyManagerCno;
    /**
     * 专用户_内设部门名称
     */
    private String insideDepartmentName;
    /**
     * 专用户_内设部门负责人名称
     */
    private String insideSaccdepmanName;
    /**
     * 专用户_内设部门负责人身份种类
     */
    private String insideSaccdepmanKind;
    /**
     * 专用户_内设部门负责人身份编号
     */
    private String insideSaccdepmanNo;
    /**
     * 专用户_内设部门联系电话
     */
    private String insideTelphone;
    /**
     * 专用户_内设部门邮编
     */
    private String insideZipCode;
    /**
     * 专用户_内设部门地址
     */
    private String insideAddress;
    /**
     * 专用户_开户证明文件种类2
     */
    private String accountFileType2;
    /**
     * 专用户_开户证明文件编号2
     */
    private String accountFileNo2;

    // 20170927 --新增字段
    /** 授权经办人姓名 */
    private String agentName;

    /** 授权经办人证件类型 */
    private String agentType;

    /** 授权经办人证件号码 */
    private String agentTypeNo;

    /** 授权经办人证件到期日 */
    private String agentTypeNoVoidDate;

    /** 法人证件到期日 */
    private String legalIdcardToVoidDate;

    /** 组织机构代码证件到期日 */
    private String orgCodeToVoidDate;

    /**
     * 国税证件到期日
     *
     * @return
     */
    private String stateTaxToVoidDate;

    /**
     * 地税证件到期日
     *
     * @return
     */
    private String taxRegNoToVoidDate;

    /**
     * 机构信用代码
     *
     * @return
     */
    private String orgEccsToVoidDate;
    /**
     * 上级法人证件到期日
     *
     * @return
     */
    private String parLegalIdcardDate;

    /**
     * 工商执照证件到期日
     *
     * @return
     */
//    private String tovoidDate;

    /** 高管股东信息姓名 */
    private String stockHoderName;

    /** 股东证件类型 */
    private String stockHoderType;

    /** 股东证件号码 */
    private String stockHoderTypeNo;

    /** 关联企业 */
    private String relateCompanyName;

    /** 关联企业类型 */
    private String relateCompanyType;

    /** 关联企业证件 */
    private String relateCompanyFileType;

    /** 关联企业社会信用代码 */
    private String relateCompanyFileNo;

    /** 备用字段 */
    private String string020;

    /** 备用字段 */
    private String string021;

    /** 备用字段 */
    private String string022;

    /** 备用字段 */
    private String string023;

    /** 备用字段 */
    private String string024;

    // 20170927 新增字段结束

    //可配置页面新增相关运营商校验字段 --开始
    /** 财务主管姓名*/
    private String financeName;

    /** 上级法人联系方式*/
    private String parLegalTelephone;

    /** 法人联系电话*/
    private String legalTelephone;

    /** 经办人联系电话*/
    private String agentTypeNoelephone;

    /** 股东联系电话*/
    private String stockHoderTelephone;

    /** 资金管理人联系电话*/
    private String moneyManagerTelephone;
    /**
     * 取现标识
     */
    private String enchashmentType;

    ///// 人行数据-end /////

    ///// 工商数据-start /////
    /**
     * 地址
     */
    private String address;

    /**
     * 最后年检年度 YYYY
     */
    private String ancheyear;

    /**
     * 营业期限终止日期
     */
    private String enddate;

    /**
     * 法人
     */
    private String legalperson;

    /**
     * 法人类型可能的值为“法定代表人”，“经营者”，“负责人”
     */
    private String legalpersontype;

    /**
     * 核准日期
     */
    private String licensedate;

    /**
     * 名称
     */
    private String name;

    /**
     * 开业日期
     */
    private String opendate;

    /**
     * 省
     */
    private String province;

    /**
     * 注册资金
     */
    private String registfund;

    /**
     * 注册资金币种
     */
    private String registfundcurrency;

    /**
     * 注册号
     */
    private String registno;

    /**
     * 登记机关
     */
    private String registorgan;

    /**
     * 全国企业信用信息公示系统代码
     */
    private String saiccode;

    /**
     * 经营范围
     */
//    private String scope;

    /**
     * 营业期限起始日期
     */
    private String startdate;

    /**
     * 经营状态
     */
    private String state;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据更新时间
     */
    private Date updatetime;

    /**
     * 失败JSON
     */
    private String failjsonstr;

    /**
     * 内部工商号
     */
    private String idpno;

    /**
     * 消息公告
     */
    private String notice;

    /**
     * 注册地地区
     */
    private String regarea;

    /**
     * 注册地地区代码
     */
    private String regareacode;

    /**
     * 注册地城市
     */
    private String regcity;

    /**
     * 注册地省份
     */
    private String regprovince;

    /**
     * 注销或吊销日期
     */
    private String revokedate;

    /**
     * 工商注册号
     */
    private String unitycreditcode;

    private String idpId;

    /**
     * 股东
     */
    private List<StockHolderDto> stockholders;

    /**
     * 主要成员
     */
    private List<EmployeeDto> employees;

    /**
     * 分支机构
     */
    private List<BranchDto> branchs;

    /**
     * 变更记录
     */
    private List<ChangeRecordDto> changes;

    /**
     * 经营异常信息
     */
    private List<ChangeMessDto> changemess;

    /**
     * 年报信息
     */
    private List<ReportDto> reports;

    /**
     * 企业对外投资情况
     */
    private List<EntInvDto> entInvList;

    /**
     * 法定代表人对外投资信息
     */
    private List<FrInvDto> frInvList;

    /**
     * 法定代表人其他公司任职信息
     */
    private List<FrPositionDto> frPositionList;

    /**
     * 失信被执行人信息
     */
    private List<PunishBreakDto> punishBreakList;

    /**
     * 行政处罚历史信息
     */
    private List<CaseInfoDto> caseInfoList;

    /**
     * 股权冻结历史信息
     */
    private List<SharesFrostDto> sharesFrostList;

    /**
     * 股权出质历史信息
     */
    private List<SharesImpawnDto> sharesImpawnList;

    /**
     * 动产抵押信息
     */
    private List<MorDetailDto> morDetailList;

    /**
     * 动产抵押物信息
     */
    private List<MorGuaInfoDto> morGuaInfoList;

    /**
     * 清算信息
     */
    private List<LiquidationDto> liquidationList;


    /**
     * 严重违法失信企业名单
     */
    private List<IllegalDto> illegals;

    /**
     * 欠贷信息
     */
    private List<AliDebtDto> aliDebtList;

    /**
     * 董事
     */
    private List<DirectorDto> directorList;

    /**
     * 监事
     */
    private List<SuperviseDto> superviseList;

    /**
     * 高管
     */
    private List<ManagementDto> managementList;

    /**
     * 受益人
     */
    private List<BeneficiaryDto> beneficiaryList;

    /**
     * 股权结构
     */
    private List<EquityShareDto> equityShareList;

    /**
     * 基本户履历
     */
    private List<BaseAccountDto> baseAccountList;

    ///// 工商数据-end /////
}
