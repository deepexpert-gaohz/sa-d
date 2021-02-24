
package com.ideatech.ams.pbc.dto;

import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;


@Data
public class AllAcct implements java.io.Serializable {

	// 变更时，机构信用代码证
	private EccsSearchCondition eccsSerarchCondition;

	private static final long serialVersionUID = 5454155825314635342L;

	private Long id;// Id

	private String acctNo; // 账户

	private SyncAcctType acctType;
	/** 业务操作类型 */
	private SyncOperateType operateType;

	private String depositorName;// 存款人名称

	private String depositorType;// 存款人类型

	private String acctCreateDate;// 开户时间

	private String legalType;// 1：法定代表人， 2：单位负责人

	private String legalName;// 法人名称

	private String legalIdcardTypeAms;// 账管系统法人证据类型

	private String legalIdcardTypeEccs;// 信用代码法人证件类型

	private String legalIdcardNo;// 法人证件编号

	/**
	 * 注册详细地址 报备信用机构
	 */
	private String regAddress;
	/**
	 * 开户许可证号
	 */
	private String accountLicenseNo;
	/**
	 * 工商注册地址 报备人行
	 */
	private String indusRegArea;

	private String telephone;// 电话

	private String zipCode;// 邮政编码

	private String orgCode;// 组织机构代码

	private String stateTaxRegNo;// 国税登记证号

	private String taxRegNo;// 地税登记证号

	private String fileType;// 证明文件1种类

	private String fileNo;// 证明文件1编号

	private String fileType2;// 证明文件2种类

	private String fileNo2;// 证明文件2编号

	private String regType;// 登记注册号类型

	private String regNo;// 登记注册号码
	/**
	 * 取消核准转户开户  operateType 隐藏域值  用于后续开户传递参数用
	 */


	/**
	 * 经营（业务）范围（上报人行）
	 */
	private String businessScope;

	/**
	 * 经营（业务）范围（上报信用机构）
	 * 
	 * <pre>
	 * 由于经营范围在信用代码证长度不能超过200个汉字
	 * </pre>
	 *
	 */
	private String businessScopeEccs;

	private String regAreaCode;// 注册地区代码

	private String regCurrencyTypeAms;// 账管系统注册资金币种

	private String regCurrencyTypeEccs;// 信用代码证系统注册资金币种

	private String registeredCapital;// 注册资金

	private String orgEccsNo;// 机构信用代码

	private String industryName;// 行业归属

	private String regOffice;// 登记部门

	private String setupDate;// 成立日期

	private String tovoidDate;// 证书到期日期

	private String orgType;// 组织机构类别

	private String orgTypeDetail;// 组织机构类别细分

	private String corpScale;// 企业规模

	private String economyIndustryCode;// 经济行业分类

	private String economyIndustryName;// 经济行业分类

	private String workAddress;// 办公（生产、经营）地址

	private String orgEnName;// 机构英文名称

	private String bankCardNo;// 贷款卡编码

	private String bankCode;// 银行机构代码

	private String bankName;// 银行机构名称

	private String isIdentification;// 未标明注册资金 1:未标明注册资金

	private String noTaxProve;// 无需办理税务登记证的文件或税务机关出具的证明

	private String accountKey;// 开户许可证号

	private String industryCode;// 行业代码

	private String economyType;// 经济类型

	private String orgStatus;// 机构状态

	private String basicAccountStatus;// 基本户状态

	private String parCorpName;// 上级单位名称

	private String financeTelephone;// 财务联系电话

	private String parAccountKey;// 上级基本户开户许可核准号

	private String parLegalType;// 上级法人或负责人

	private String parLegalName;// 上级法人姓名

	private String parLegalIdcardType;// 上级法人证件类型

	private String parLegalIdcardNo;// 上级法人证件号码

	private String parOrgCode;// 上级组织机构代码

	private String parRegType;// 上级登记注册类型

	private String parRegNo;// 上级登记注册号码

	private String parOrgEccsNo;// 上级信用机构信息代码

	private String accountNameFrom;// 账户名称构成方式

	private String accountFileNo2; // 报备类证明文件2编号

	private String accountFileType2;// 报备类证明文件2类型

	private String capitalProperty;// 预算-资金性质

	private String enchashmentType;// 预算_取现标识 0：否 1：是

	private String moneyManager;// 预算_资金人姓名

	private String moneyManagerCtype;// 预算_资金身份种类

	private String moneyManagerCno;// 预算_资金身份编号

	private String insideDepartmentName;// 预算_内设部门名称

	private String insideSaccdepmanName;// 预算_内设负责人名称

	private String insideSaccdepmanKind;// 预算_内设负责人身份种类

	private String insideSaccdepmanNo;// 预算_内设身份编号

	private String insideTelphone;// 预算_内设电话

	private String insideZipCode;// 预算_内设编码

	private String insideAddress;// 预算_内设地址

	private String saccprefix; // 前缀

	private String saccpostfix; // 后缀

	private String effectiveDate;// 非临时_有效时间

	private String createAccountReason;// 非临时_申请开户原因

	private String flsProjectName; // 非临时_项目部名称

	private String flsFzrAddress;// 非临时负责人地址

	private String flsFzrZipCode;// 非临时负责人邮政编码

	private String flsFzrTelephone;// 非临时负责人电话

	private String flsFzrLegalIdcardType;// 非临时负责人证件类型

	private String flsFzrLegalIdcardNo;// 非临时负责人证件号码

	private String flsFzrLegalName;// 非临时负责人姓名

	private String accountFileType;// 报备类证明文件1类型

	private String accountFileNo;// 报备类证明文件1编号

	private String remark;// 备注

	private String economyKind;// 产业分类

	private String cancenReason;// 撤销原因

	private String accountinfoimode;// 资金管理人\内设部门种类

	private String acctName;// 报备类_账户名

	private String regProvince;// 省份代码

	private String regCity;// 城市代码

	private String regArea;// 地区代码

	private String regProvinceCHName;// 注册地区省份名

	private String regCityCHName;// 注册地区城市名

	private String regAreaCHName;// 注册地区名称

	private String workProvince;// 办公省份代码

	private String workCity;// 办公城市代码

	private String workArea;// 办公地区代码

	private String userRegCode;// 操作人所在注册地区代码

	/* 高管及主要关系人信息 */
	/** 董事长名称 */
	private String managerName1;

	/** 董事长证件类型 */
	private String certifiType1;

	/** 董事长证件号码 */
	private String certificode1;

	/** 总经理/主要负责人 姓名 */
	private String managerName2;

	/** 总经理/主要负责人 证件类型 */
	private String certifiType2;

	/** 总经理/主要负责人 证件号码 */
	private String certificode2;

	/** 财务负责人 姓名 */
	private String managerName3;

	/** 财务负责人 证件类型 */
	private String certifiType3;

	/** 财务负责人 证件号码 */
	private String certificode3;

	/** 监事长 姓名 */
	private String managerName4;

	/** 监事长 证件类型 */
	private String certifiType4;

	/** 监事长 证件号码 */
	private String certificode4;

	/* 实际控制人 */
	/** 控制人1类型 */
	private String controlType1 = "";

	private String controlTypeStr1 = "";

	/** 控制人1姓名 */
	private String controlName1 = "";

	/** 控制人1证件类型 */
	private String controlIdType1 = "";

	/** 控制人1证件类型中文 */
	private String controlIdTypeStr1 = "";

	/** 控制人1 证件编号 */
	private String controlCode1 = "";

	/** 控制人1 组织机构代码 */
	private String controlOrgCode1 = "";

	/** 控制人1 机构信用代码 */
	private String controlEccsCode1 = "";

	/** 控制人2 类型 */
	private String controlType2 = "";

	private String controlTypeStr2 = "";

	/** 控制人2 姓名 */
	private String controlName2 = "";

	/** 控制人2证件类型 */
	private String controlIdType2 = "";

	/** 控制人2证件类型中文 */
	private String controlIdTypeStr2 = "";

	/** 控制人2 证件号码 */
	private String controlCode2 = "";

	/** 控制人2 组织机构代码 */
	private String controlOrgCode2 = "";

	/** 控制人2 机构信用代码 */
	private String controlEccsCode2 = "";

	/** 控制人3 类型 */
	private String controlType3 = "";

	private String controlTypeStr3 = "";

	/** 控制人3 姓名 */
	private String controlName3 = "";

	/** 控制人3证件类型 */
	private String controlIdType3 = "";

	/** 控制人3证件类型中文 */
	private String controlIdTypeStr3 = "";

	/** 控制人3 证件号码 */
	private String controlCode3 = "";

	/** 控制人3 组织机构代码 */
	private String controlOrgCode3 = "";

	/** 控制人3 机构信用代码 */
	private String controlEccsCode3 = "";

	/* 重要股东信息 */
	/** 股东1类型 */
	private String shareHolderType1 = "";

	private String shareHolderTypeStr1 = "";

	/** 股东1姓名 */
	private String shareHolderName1 = "";

	/** 股东1证件类型 */
	private String shareHolderIdType1 = "";

	private String shareHolderIdTypeStr1 = "";

	/** 股东1证件号码 */
	private String shareHolderCode1 = "";

	/** 股东1组织机构代码 */
	private String shareHolderOrgCode1 = "";

	/** 股东1机构信用代码 */
	private String shareHolderEccsCode1 = "";

	/** 股东1持股比例（%） */
	private String holdingRatio1 = "";

	/** 股东2类型 */
	private String shareHolderType2 = "";

	private String shareHolderTypeStr2 = "";

	/** 股东2姓名 */
	private String shareHolderName2 = "";

	/** 股东2证件类型 */
	private String shareHolderIdType2 = "";

	private String shareHolderIdTypeStr2 = "";

	/** 股东2证件号码 */
	private String shareHolderCode2 = "";

	/** 股东2组织机构代码 */
	private String shareHolderOrgCode2 = "";

	/** 股东2机构信用代码 */
	private String shareHolderEccsCode2 = "";

	/** 股东2持股比例（%） */
	private String holdingRatio2 = "";

	/** 股东3类型 */
	private String shareHolderType3 = "";

	private String shareHolderTypeStr3 = "";

	/** 股东3姓名 */
	private String shareHolderName3 = "";

	/** 股东3证件类型 */
	private String shareHolderIdType3 = "";

	private String shareHolderIdTypeStr3 = "";

	/** 股东3证件号码 */
	private String shareHolderCode3 = "";

	/** 股东3组织机构代码 */
	private String shareHolderOrgCode3 = "";

	/** 股东3机构信用代码 */
	private String shareHolderEccsCode3 = "";

	/** 股东3持股比例（%） */
	private String holdingRatio3 = "";

	/** 代码证发放申领经办人姓名 */
	private String eccsFaFangApplicantName;
	/** 代码证发放申领经办人证件种类 */
	private String eccsFaFangApporKind;
	/** 代码证发放申领经办人证件编号 */
	private String eccsFaFangAppcreCode;
	/** 代码证发放时间 */
	private String eccsFaFangDispatchTime;
	/** 代码证发放申领经办人姓名 */
	private String eccsFaFangOrgName;
	/** 代码证发放申领经办人姓名 */
	private String eccsFaFangDispaterName;


	//开户许可证号
	private String openKey;
	//查询密码
	private String selectPwd;
	//日期
	private String printDate;
	//账户数据
	private List<AmsPrintInfo> aLLAccountData;
	/**
	 * 原基本开户许可证号
	 */
	private String oldAccountKey;

	/**
	 * 本地异地标识(1本地、2异地)
	 */
	private String openAccountSiteType;


	/**
	 * 信用代码证是否发放
	 */
	private boolean xymdFaFang = false;
	/**
	 * 2018-10-10
	 * 机构所在地地区代码
	 */
	private String bankAreaCode;

	/**
	 * 判断是否走取消核准接口
	 */
	private Boolean cancelHeZhun;
	/**
	 * 开户许可证处理方式
	 */
	private String iaccState;
	/**
	 * 取消核准第二步的字段（证明文件种类）
	 */
	private String acctFileType;

	/**
	/**
	 * 取消核准转户开户  operateType 隐藏域值  用于后续开户传递参数用
	 */
	private String operateTypeValue;
    /**
     * 本外币一体化后
     */
    private String currencyType;
    /**
     * checkbox 数据选项保存
     */
    private String currency0;
    /**
     * redio 数据选项保存
     */
    private String currency1;
     /**
     * 国籍
     */
    private String nationality;
    /**
     * 工商营业执照 开户传值
     */
    private String saccfiletype1;
	/**
	 * 报备器
	 * 
	 * @param preFix
	 *            同步系统简称
	 * @return
	 */
	public String getAccountSynchronizerName(String preFix) {
		return getComponentName(preFix, "Synchronizer");
	}

	/**
	 * 报备账户校验器
	 * 
	 * @param preFix
	 *            同步系统简称
	 * @return
	 */
	public String getAccountSyncValidater(String preFix) {
		return getComponentName(preFix, "SyncValidater");
	}

	/**
	 * 报备账户校验器
	 * 
	 * @param preFix
	 *            同步系统简称
	 * @return
	 */
	public String getAccountSyncParamsValidater(String preFix) {
		return getComponentName(preFix, "SyncParamsValidater");

	}

	/**
	 * 账户同步接口参数
	 * 
	 * @param prefix
	 * @return
	 */
	public String getAccountSyncParmsName(String prefix) {
		return getComponentName(prefix, "SyncParameter");
	}


	private String getComponentName(String preFix, String postFix) {
		StringBuffer name = new StringBuffer();
		name.append(preFix);
		name.append(StringUtils.capitalize(acctType.toString()));
		name.append(StringUtils.capitalize(StringUtils.substringAfter(operateType.toString().toLowerCase(), "_")));
		return name.append(postFix).toString();
	}
}
