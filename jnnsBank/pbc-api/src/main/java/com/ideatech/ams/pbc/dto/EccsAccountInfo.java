package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 机构信用代码证系统账户信息
 * 
 * @author zoulang
 *
 */
@Data
public class EccsAccountInfo {

	private Long id;
	private String creditCode = ""; // 机构信用代码

	private String depositorName = ""; // 机构中文名称
	/** 机构中文名称信息来源代码 */
	private String depositorNameInfoSourceCode = "";
	/** 机构中文名称信息获取时间 */
	private String depositorNameUpdateTime = "";

	private String orgEnName = ""; // 机构英文名称
	/** 机构英文名称信息来源代码 */
	private String orgEnNameInfoSourceCode = "";
	/** 机构英文名称信息获取时间 */
	private String orgEnNameUpdateTime = "";

	private String orgCode = ""; // 组织机构代码
	private String bankCardNo = ""; // 货代卡编码
	private String accountKey = ""; // 开户许可证核准号
	private String regType = ""; // 登记注册号类型
	private String regTypeStr = ""; // 登记注册号类型
	private String regNo = ""; // 登记注册号码
	private String stateTaxRegNo = ""; // 纳税人识别号（国税）
	private String taxRegNo = ""; // 纳税人识别号（地税）

	private String country = ""; // 国家
	private String countryStr = "";
	private String provCode = ""; // 省
	private String provCodeStr = "";
	private String cityCode = ""; // 城市
	private String cityCodeStr = "";
	private String countyCode = ""; // 区/县
	private String countyCodeStr = "";
	private String regAddress = ""; // 地址
	/** 注册地址信息来源代码 */
	private String regAddressInfoSourceCode = "";
	/** 注册地址信息获取时间 */
	private String regAddressUpdateTime = "";

	private String regOffice = ""; // 登记部门
	private String regOfficeStr = ""; // 登记部门
	/** 登记部门信息来源代码 */
	private String regOfficeInfoSourceCode = "";
	/** 登记部门信息获取时间 */
	private String regOfficeUpdateTime = "";

	private String setupDate = ""; // 成立日期
	/** 成立日期信息来源代码 */
	private String setupDateInfoSourceCode = "";
	/** 成立日期信息获取时间 */
	private String setupDateUpdateTime = "";

	private String effectiveDate = ""; // 证书到期日
	/** 证书到期日信息来源代码 */
	private String effectiveDateInfoSourceCode = "";
	/** 证书到期日信息获取时间 */
	private String effectiveDateUpdateTime = "";

	private String controlid = "";
	private String legalName = ""; // 法定代表人（负责人）：名称
	private String legalIdcardType = ""; // 证件类型
	private String legalIdcardTypeStr = "";
	private String legalIdcardNo = ""; // 证件号码;
	/** 法定代表人（负责人）信息来源代码 */
	private String legalInfoSourceCode = "";
	/** 法定代表人（负责人）信息获取时间 */
	private String legalUpdateTime = "";

	private String regCurrencyType = ""; // 注册资本币种
	private String regCurrencyTypeStr = "";
	private String registeredCapital = ""; // 注册资本(万元)
	/** 注册资本信息来源代码 */
	private String registeredCapitalInfoSourceCode = "";
	/** 注册资本信息获取时间 */
	private String registeredCapitalUpdateTime = "";

	private String businessScope = ""; // 经营（业务）范围
	/** 经营（业务）范围信息来源代码 */
	private String businessScopeInfoSourceCode = "";
	/** 经营（业务）范围信息获取时间 */
	private String businessScopeUpdateTime = "";

	/** 组织机构类别大类 */
	private String orgTypeDetails;
	private String orgTypeDetailsStr = "";
	/** 组织机构类别细分 */
	private String orgType = ""; // 组织机构类别
	private String orgTypeStr = ""; // 组织机构类别
	/** 组织机构类别信息来源代码 */
	private String orgTypeInfoSourceCode = "";
	/** 组织机构类别信息获取时间 */
	private String orgTypeUpdateTime = "";

	private String economyIndustry = ""; // 经济行业分类
	private String economyIndustryStr = ""; //
	/** 经济行业分类信息来源代码 */
	private String economyIndustryInfoSourceCode = "";
	/** 经济行业分类信息获取时间 */
	private String economyIndustryUpdateTime = "";

	private String economyType = ""; // 经济类型
	private String economyTypeStr = "";
	/** 经济类型信息来源代码 */
	private String economyTypeInfoSourceCode = "";
	/** 经济类型信息获取时间 */
	private String economyTypeUpdateTime = "";

	private String corpScale = ""; // 企业规模
	private String corpScaleStr = "";
	/** 企业规模信息来源代码 */
	private String corpScaleInfoSourceCode = "";
	/** 企业规模信息获取时间 */
	private String corpScaleUpdateTime = "";

	private String orgStatus = ""; // 机构状态
	private String orgStatusStr = "";
	/** 机构状态信息来源代码 */
	private String orgStatusInfoSourceCode = "";
	/** 机构状态信息获取时间 */
	private String orgStatusUpdateTime = "";

	private String accountStatus = ""; // 基本户状态
	private String accountStatusStr = "";
	/** 基本户状态信息来源代码 */
	private String accountStatusInfoSourceCode = "";
	/** 基本户状态信息获取时间 */
	private String accountStatusUpdateTime = "";

	private String crcCode = ""; // 信用代码证 内部id
	// 上级机构（主管单位）信息
	private String parCorpName = ""; // 机构名称
	private String parOrgEccsNo = ""; // 机构信用代码
	private String parOrgCode = ""; // 组织机构代码
	private String parRegType = ""; // 登记注册号类型
	private String parRegTypeStr = "";
	private String parRegNo = ""; // 登记注册号码
	/** 上级机构（主管单位）信息来源代码 */
	private String parCorpInfoSourceCode = "";
	/** 上级机构（主管单位）信息获取时间 */
	private String parCorpUpdateTime = "";

	// 联络信息
	private String provCodeW = ""; // 省
	private String provCodeWStr = "";
	private String cityCodeW = ""; // 市
	private String cityCodeWStr = "";
	private String countyCodeW = ""; // 区/县
	private String countyCodeWStr = "";
	private String workAddress = ""; // 办公（生产、经营）地址
	/** 办公（生产、经营）地址信息来源代码 */
	private String workAddressInfoSourceCode = "";
	/** 办公（生产、经营）地址信息获取时间 */
	private String workAddressUpdateTime = "";

	private String workTelephone = ""; // 联系电话
	/** 联系电话信息来源代码 */
	private String workTelephoneInfoSourceCode = "";
	/** 联系电话信息获取时间 */
	private String workTelephoneUpdateTime = "";

	private String financeTelephone = ""; // 财务部联系电话
	/** 财务部联系电话信息来源代码 */
	private String financeTelephoneInfoSourceCode = "";
	/** 财务部联系电话信息获取时间 */
	private String financeTelephoneUpdateTime = "";
	/* 高管及主要关系人信息 */
	/** 董事长名称 */
	private String managerName1 = "";
	/** 董事长证件类型 */
	private String certifiType1 = "";
	/** 董事长证件类型中文名称 */
	private String certifiTypeStr1 = "";
	/** 董事长证件号码 */
	private String certificode1 = "";
	/** 董事长信息来源代码 */
	private String managerInfoSourceCode1 = "";
	/** 董事长信息获取时间 */
	private String managerUpdateTime1 = "";

	/** 总经理/主要负责人 姓名 */
	private String managerName2 = "";
	/** 总经理/主要负责人 证件类型 */
	private String certifiType2 = "";
	/** 总经理证件类型中文名称 */
	private String certifiTypeStr2 = "";
	/** 总经理/主要负责人 证件号码 */
	private String certificode2 = "";
	/** 总经理/主要负责人信息来源代码 */
	private String managerInfoSourceCode2 = "";
	/** 总经理/主要负责人信息获取时间 */
	private String managerUpdateTime2 = "";

	/** 财务负责人 姓名 */
	private String managerName3 = "";
	/** 财务负责人 证件类型 */
	private String certifiType3 = "";
	/** 财务负责人 证件中文名 */
	private String certifiTypeStr3 = "";
	/** 财务负责人 证件号码 */
	private String certificode3 = "";
	/** 财务负责人信息来源代码 */
	private String managerInfoSourceCode3 = "";
	/** 财务负责人信息获取时间 */
	private String managerUpdateTime3 = "";

	/** 监事长 姓名 */
	private String managerName4 = "";
	/** 监事长 证件类型 */
	private String certifiType4 = "";
	/** 监事长 证件类型中文名称 */
	private String certifiTypeStr4 = "";
	/** 监事长 证件号码 */
	private String certificode4 = "";
	/** 监事长信息来源代码 */
	private String managerInfoSourceCode4 = "";
	/** 监事长信息获取时间 */
	private String managerUpdateTime4 = "";

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
	/** 控制人1信息来源代码 */
	private String controlInfoSourceCode1 = "";
	/** 控制人1信息获取时间 */
	private String controlUpdateTime1 = "";

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
	/** 控制人2信息来源代码 */
	private String controlInfoSourceCode2 = "";
	/** 控制人2信息获取时间 */
	private String controlUpdateTime2 = "";

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
	/** 控制人3信息来源代码 */
	private String controlInfoSourceCode3 = "";
	/** 控制人3信息获取时间 */
	private String controlUpdateTime3 = "";

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
	/** 股东1信息来源代码 */
	private String shareHolderInfoSourceCode1 = "";
	/** 股东1信息获取时间 */
	private String shareHolderUpdateTime1 = "";

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
	/** 股东2信息来源代码 */
	private String shareHolderInfoSourceCode2 = "";
	/** 股东2信息获取时间 */
	private String shareHolderUpdateTime2 = "";

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
	/** 股东3信息来源代码 */
	private String shareHolderInfoSourceCode3 = "";
	/** 股东3信息获取时间 */
	private String shareHolderUpdateTime3 = "";

	/** 变更时 标记要修改的信息是否已经发放 true：已发放 ;false:未发放 */
	private boolean infoisOk;

	/** 用户对应的机构信用代码证银行代码 */
	public String eccsBankCode;

	private String hiddenParm = "";

	/** 信用代码证系统的id */
	private String assetProjId;


}