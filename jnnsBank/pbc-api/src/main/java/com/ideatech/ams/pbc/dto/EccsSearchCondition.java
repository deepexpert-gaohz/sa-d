package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 机构信用代码证查询条件
 */
@Data
public class EccsSearchCondition {

	private String orgEccsNo = "";// 机构信用代码
	private String orgCode = ""; // 组织机构代码
	private String accountKey = ""; // 开户许可证核准号
	private String regType = ""; // 登记注册号类型
	private String regNo = ""; // 登记注册号码
	private String stateTaxRegNo = ""; // 纳税人识别号（国税）
	private String taxRegNo = ""; // 纳税人识别号（地税）



}
