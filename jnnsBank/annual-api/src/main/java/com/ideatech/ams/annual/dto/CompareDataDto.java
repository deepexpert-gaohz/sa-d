package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.DataSourceEnum;
import lombok.Data;

/**
 * 比对数据
 *
 * @author van
 * @date 19:40 2018/8/9
 */
@Data
public class CompareDataDto {

	/**
	 * 账号
	 */
	private String acctNo;

	/**
	 * 存款人名称
	 */
	private String depositorName;

	/**
	 * 组织机构代码
	 */
	private String orgCode;

	/**
	 * 法人姓名
	 */
	private String legalName;

	/**
	 * 注册号
	 */
	private String regNo;

	/**
	 * 经营范围
	 */
	private String businessScope;

	/**
	 * 注册地址
	 */
	private String regAddress;

	/**
	 * 注册资金
	 */
	private String registeredCapital;

	/**
	 * 账户状态
	 */
	private String accountStatus;

	/**
	 * 法人证件种类
	 */
	private String legalIdcardType;

	/**
	 * 法人证件号码
	 */
	private String legalIdcardNo;

	/**
	 * 注册币种
	 */
	private String regCurrencyType;

	/**
	 * 国税登记证
	 */
	private String stateTaxRegNo;

	/**
	 * 地税登记证
	 */
	private String taxRegNo;

	/**
	 * 数据源
	 */
	private DataSourceEnum dataSource;

	/**
	 * 机构代码
	 */
	private String organCode;

}
