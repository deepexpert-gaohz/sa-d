package com.ideatech.ams.account.util;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import org.apache.commons.lang.StringUtils;

public class AcctCheckNullUtil {
	public AllBillsPublicDTO jibenCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人名称是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人类别是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断工商注册地址是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegFullAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断行业归属是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getIndustryCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件1种类是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件2种类是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileType2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件1编号是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件2编号是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileNo2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断成立日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getSetupDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 未标明注册资金
		else if (StringUtils.isBlank(allBillsPublicDTO.getIsIdentification())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金币种
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegCurrencyType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金（元）
		else if (allBillsPublicDTO.getRegisteredCapital() == null || StringUtils.isBlank(allBillsPublicDTO.getRegisteredCapital().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 经营（业务）范围
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessScope())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人代表（负责）人
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 无需办理税务登记证的文件或税务机关出具的证明
		else if (StringUtils.isBlank(allBillsPublicDTO.getNoTaxProve())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（国税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 联系电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 基本户开户许可证
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// parCorpName上级单位名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getParCorpName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级基本户开户许可核准号
		else if (StringUtils.isBlank(allBillsPublicDTO.getParAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法定代表人类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;
	}

	public AllBillsPublicDTO yibanCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 基本户开户许可证
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctFileType开户证明文件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户证明文件种类编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;
	}

	public AllBillsPublicDTO yusuanCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 基本户开户许可证
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctFileType开户证明文件类型1
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户证明文件种类编号1
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctFileType开户证明文件类型2
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileType2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户证明文件种类编号2
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileNo2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户构成方式
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountNameFrom())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户名称前缀
		else if (StringUtils.isBlank(allBillsPublicDTO.getSaccprefix())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户名称后缀saccpostfix
		else if (StringUtils.isBlank(allBillsPublicDTO.getSaccpostfix())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金性质
		else if (StringUtils.isBlank(allBillsPublicDTO.getCapitalProperty())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 取现标识
		else if (StringUtils.isBlank(allBillsPublicDTO.getEnchashmentType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManager())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人身份证种类
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManagerIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人身份证编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManagerIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideDeptName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门负责人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 负责人身份证件种类
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 负责人身份证件编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门地址
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;
	}

	public AllBillsPublicDTO feiyusuanCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 基本户开户许可证
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户构成方式
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountNameFrom())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户名称前缀
		else if (StringUtils.isBlank(allBillsPublicDTO.getSaccprefix())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 账户名称后缀saccpostfix
		else if (StringUtils.isBlank(allBillsPublicDTO.getSaccpostfix())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金性质
		else if (StringUtils.isBlank(allBillsPublicDTO.getCapitalProperty())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 取现标识
		else if (StringUtils.isBlank(allBillsPublicDTO.getEnchashmentType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManager())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人身份证种类
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManagerIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 资金管理人身份证编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getFundManagerIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideDeptName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门负责人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 负责人身份证件种类
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 负责人身份证件编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideLeadIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 内设部门地址
		else if (StringUtils.isBlank(allBillsPublicDTO.getInsideAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;
	}

	public AllBillsPublicDTO linshiCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// effectiveDate账户有效日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getEffectiveDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人名称是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人类别是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断工商注册地址是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegFullAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断行业归属是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getIndustryCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctFileType开户证明文件类型1
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户证明文件种类编号1
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctFileType开户证明文件类型2
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileType2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户证明文件种类编号2
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctFileNo2())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断成立日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getSetupDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 未标明注册资金
		else if (StringUtils.isBlank(allBillsPublicDTO.getIsIdentification())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金币种
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegCurrencyType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金（元）
		else if (allBillsPublicDTO.getRegisteredCapital() == null || StringUtils.isBlank(allBillsPublicDTO.getRegisteredCapital().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 经营（业务）范围
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessScope())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人代表（负责）人
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 无需办理税务登记证的文件或税务机关出具的证明
		else if (StringUtils.isBlank(allBillsPublicDTO.getNoTaxProve())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（国税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 联系电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// parCorpName上级单位名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getParCorpName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级基本户开户许可核准号
		else if (StringUtils.isBlank(allBillsPublicDTO.getParAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法定代表人类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;
	}

	public AllBillsPublicDTO feilinshiCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 基本户开户许可证
		else if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// effectiveDate账户有效日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getEffectiveDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// acctCreateReason申请开户原因
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateReason())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 项目部名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpProjectName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// nontmpLegalName非临时负责人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 非临时身份证件种类
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 非临时身份证件编号
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 非临时联系电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}

		// 非临时邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 非临时地址
		else if (StringUtils.isBlank(allBillsPublicDTO.getNontmpAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		return allBillsPublicDTO;

	}

	public AllBillsPublicDTO teshuCheck(AllBillsPublicDTO allBillsPublicDTO) {
		// 判断账号是否为空
		if (StringUtils.isBlank(allBillsPublicDTO.getAcctNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断账户性质是否为空
		else if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 开户银行代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicBankCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断开户日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getAcctCreateDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人名称是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断存款人类别是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getDepositorType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断工商注册地址是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegFullAddress())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断注册地地区代码是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断行业归属是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getIndustryCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件1种类是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断证明文件1编号是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getFileNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 判断成立日期是否为空
		else if (StringUtils.isBlank(allBillsPublicDTO.getSetupDate())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 到期日期
		else if (StringUtils.isBlank(allBillsPublicDTO.getCredentialDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 营业执照到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessLicenseDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 未标明注册资金
		else if (StringUtils.isBlank(allBillsPublicDTO.getIsIdentification())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 取现标识
		else if (StringUtils.isBlank(allBillsPublicDTO.getEnchashmentType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金币种
		else if (StringUtils.isBlank(allBillsPublicDTO.getRegCurrencyType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 注册资金（元）
		else if (allBillsPublicDTO.getRegisteredCapital() == null || StringUtils.isBlank(allBillsPublicDTO.getRegisteredCapital().toString())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 经营（业务）范围
		else if (StringUtils.isBlank(allBillsPublicDTO.getBusinessScope())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人代表（负责）人
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 组织机构证到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getOrgCodeDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 无需办理税务登记证的文件或税务机关出具的证明
		else if (StringUtils.isBlank(allBillsPublicDTO.getNoTaxProve())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（国税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxRegNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 纳税人识别号（地税）到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getStateTaxDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 联系电话
		else if (StringUtils.isBlank(allBillsPublicDTO.getTelephone())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 邮政编码
		else if (StringUtils.isBlank(allBillsPublicDTO.getZipcode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// parCorpName上级单位名称
		else if (StringUtils.isBlank(allBillsPublicDTO.getParCorpName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级基本户开户许可核准号
		else if (StringUtils.isBlank(allBillsPublicDTO.getParAccountKey())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级机构信用证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgEccsDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级组织机构代码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParOrgCode())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法定代表人类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人姓名
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalName())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件类型
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardType())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件号码
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardNo())) {
			allBillsPublicDTO.setInitFullStatus("1");
		}
		// 上级法人证件到期日
		else if (StringUtils.isBlank(allBillsPublicDTO.getParLegalIdcardDue())) {
			allBillsPublicDTO.setInitFullStatus("1");
		} else {
			allBillsPublicDTO.setInitFullStatus("0");
		}
		/*
		 * try { billsPublicService.save(allBillsPublicDTO, userInfo, true); } catch
		 * (Exception e) { throw new IdeaException(e.getMessage()); }
		 */
		return allBillsPublicDTO;
	}
}
