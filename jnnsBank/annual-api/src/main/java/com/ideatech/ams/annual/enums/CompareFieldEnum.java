package com.ideatech.ams.annual.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public enum CompareFieldEnum {

	ACCT_NO("账号", "acctNo"),

	DEPOSITOR_NAME("企业名称", "depositorName"),

	ORG_CODE("组织机构代码", "orgCode"),

	ORGAN_CODE("机构代码", "organCode"),

	LEGAL_NAME("法人姓名", "legalName"),

	REG_NO("工商注册号", "regNo"),

	BUSINESS_SCOPE("经营范围", "businessScope"),

	REG_ADDRESS("注册地址", "regAddress"),

	REG_CAPITAL("注册资金", "registeredCapital"),

	ACCOUNT_STATUS("账户状态", "accountStatus"),

	LEGAL_ID_CARD_TYPE("法人证件种类", "legalIdcardType"),

	LEGAL_ID_CARD_NO("法人证件号码", "legalIdcardNo"),

	REG_CURRENCY_TYPE("注册币种", "regCurrencyType"),

	STATE_TAX_REG_NO("国税登记证", "stateTaxRegNo"),

	TAX_REG_NO("地税登记证", "taxRegNo");

	private String value;

	private String field;

	CompareFieldEnum(String value, String field) {
		this.value = value;
		this.field = field;
	}

	public static CompareFieldEnum str2enum(String field){
		if(StringUtils.isBlank(field)){
			return null;
		}
		if("acctNo".equals(field) || "ACCT_NO".equals(field)){
			return CompareFieldEnum.ACCT_NO;
		}else if("depositorName".equals(field) || "DEPOSITOR_NAME".equals(field)){
			return CompareFieldEnum.DEPOSITOR_NAME;
		}else if("orgCode".equals(field) || "ORG_CODE".equals(field)){
			return CompareFieldEnum.ORG_CODE;
		}else if("organCode".equals(field) || "ORGAN_CODE".equals(field)){
			return CompareFieldEnum.ORGAN_CODE;
		}else if("legalName".equals(field) || "LEGAL_NAME".equals(field)){
			return CompareFieldEnum.LEGAL_NAME;
		}else if("regNo".equals(field) || "REG_NO".equals(field)){
			return CompareFieldEnum.REG_NO;
		}else if("businessScope".equals(field) || "BUSINESS_SCOPE".equals(field)){
			return CompareFieldEnum.BUSINESS_SCOPE;
		}else if("regAddress".equals(field) || "REG_ADDRESS".equals(field)){
			return CompareFieldEnum.REG_ADDRESS;
		}else if("registeredCapital".equals(field) || "REG_CAPITAL".equals(field)){
			return CompareFieldEnum.REG_CAPITAL;
		}else if("accountStatus".equals(field) || "ACCOUNT_STATUS".equals(field)){
			return CompareFieldEnum.ACCOUNT_STATUS;
		}else if("legalIdcardType".equals(field) || "LEGAL_ID_CARD_TYPE".equals(field)){
			return CompareFieldEnum.LEGAL_ID_CARD_TYPE;
		}else if("legalIdcardNo".equals(field) || "LEGAL_ID_CARD_NO".equals(field)){
			return CompareFieldEnum.LEGAL_ID_CARD_NO;
		}else if("regCurrencyType".equals(field) || "REG_CURRENCY_TYPE".equals(field)){
			return CompareFieldEnum.REG_CURRENCY_TYPE;
		}else if("stateTaxRegNo".equals(field) || "STATE_TAX_REG_NO".equals(field)){
			return CompareFieldEnum.STATE_TAX_REG_NO;
		}else if("taxRegNo".equals(field) || "TAX_REG_NO".equals(field)){
			return CompareFieldEnum.TAX_REG_NO;
		}
		return null;
	}
}
