/**
 * @ProjectName idea-ams-core
 * @FileName FormStatus.java
 * @PackageName:com.idea.ams.domain.enums
 * @author ku
 * @date 2015年7月30日下午3:47:00
 * @since 1.0.0
 * @Copyright (c) 2015,kuyonggang@ideatech.info All Rights Reserved.
 */
package com.ideatech.ams.system.proof.enums;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @ClassName: CompanyAcctType
 * @Description: 对公报备账户性质枚举
 * @author zoulang
 * @date 2015年11月13日 上午9:55:09
 *
 */
public enum CompanyAcctType {

	jiben("基本存款账户"),

	yiban("一般存款账户"),

	yusuan("预算单位专用存款账户"),

	feiyusuan("非预算单位专用存款账户"),

	linshi("临时机构临时存款账户"),

	feilinshi("非临时机构临时存款账户"),

	teshu("特殊单位专用存款账户"),

	yanzi("验资户临时存款账户"),

	zengzi("增资户临时存款账户"),
	/**
	 * 专用存款账户，未分预、非预算、特殊
	 */
	specialAcct("专用存款账户 "),
	/**
	 * 临时存款账户 未分临时机构、非临时机构
	 */
	tempAcct("临时存款账户"),
	/**
	 * 从核心未识别的账户性质
	 */
	unknow("未知账户性质 ");

	private String value;

	CompanyAcctType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static CompanyAcctType str2enum(String acctType) {
		if (StringUtils.isBlank(acctType)) {
			return null;
		}
		if (acctType.equals("jiben") || acctType.equals("基本存款账户")) {
			return CompanyAcctType.jiben;
		} else if (acctType.equals("yiban") || acctType.equals("一般存款账户")) {
			return CompanyAcctType.yiban;
		} else if (acctType.equals("linshi") || acctType.equals("临时机构临时存款账户")) {
			return CompanyAcctType.linshi;
		} else if (acctType.equals("feilinshi") || acctType.equals("非临时机构临时存款账户")) {
			return CompanyAcctType.feilinshi;
		} else if (acctType.equals("teshu") || acctType.equals("特殊单位专用存款账户")) {
			return CompanyAcctType.teshu;
		} else if (acctType.equals("feiyusuan") || acctType.equals("非预算单位专用存款账户")) {
			return CompanyAcctType.feiyusuan;
		} else if (acctType.equals("yusuan") || acctType.equals("预算单位专用存款账户")) {
			return CompanyAcctType.yusuan;
		} else if (acctType.equals("specialAcct") || acctType.equals("专用账户")) {
			return CompanyAcctType.specialAcct;
		} else if (acctType.equals("tempAcct") || acctType.equals("临时账户")) {
			return CompanyAcctType.tempAcct;
		} else if (acctType.equals("unknow") || acctType.equals("未知账户")) {
			return CompanyAcctType.unknow;
		} else if (acctType.equals("yanzi") || acctType.equals("验资户临时存款账户")) {
			return CompanyAcctType.yanzi;
		} else if (acctType.equals("zengzi") || acctType.equals("增资户临时存款账户")) {
			return CompanyAcctType.zengzi;
		}
		return null;
	}


	public boolean isPbcAcctType() {
		if (this == null) {
			return false;
		}
		return (this == CompanyAcctType.jiben || this == CompanyAcctType.yiban || this == CompanyAcctType.linshi || this == CompanyAcctType.feilinshi
				|| this == CompanyAcctType.teshu || this == CompanyAcctType.feiyusuan || this == CompanyAcctType.yusuan);

	}

	public boolean isHeZhun() {
		if (this == null) {
			return false;
		}
		return (this == CompanyAcctType.jiben || this == CompanyAcctType.linshi || this == CompanyAcctType.feilinshi || this == CompanyAcctType.teshu || this == CompanyAcctType.yusuan);
	}

	public static CompanyAcctType getValue(String name) {
		CompanyAcctType[] values = CompanyAcctType.values();
		for (CompanyAcctType value : values) {
			if (StringUtils.equals(value.getValue(), name)) {
				return value;
			}
		}
		return null;
	}

}
