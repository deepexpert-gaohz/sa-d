package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 年检提交后从人行账管系统查询的账户信息
 * 
 * @author zoulang
 *
 */
@Data
public class AmsAnnualInfo {

	private String acctNo; // 账号
	private String acctType;// 账户性质
	private String bankCode;// 银行 机构代码
	private String bankName;// 银行机构名称
	private String depositorName; // 存款人名称
	private String orgCode; // 组织机构代码
	private String fileType; // 证明文件1种类
	private String fileNo; // 证明文件1编号
	private String legalName; // 姓名
	private String legalIdcardType; // 证件类型
	private String legalIdcardNo; // 证件号码
	private String stateTaxRegNo; // 纳税人识别号（国税）
	private String taxRegNo; // 纳税人识别号（地税）
	private String regCurrencyType; // 注册资本币种
	private String registeredCapital; // 注册资金
	private String businessScope; // 经营（业务）范围
	private String regAddress; // 注册（登记）地址
	private String zipCode; // 邮政编码
	private String telephone; // 电话
	private String accountKey; // 开户许可证核准号

}
