package com.ideatech.ams.pbc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 账户年检校验结果
 * 
 * @version 1.0.0
 */
public enum AmsAnnualResultStatus {
	Success("0", "年检成功"), // 可年检
	Suspend("1", "账户久悬"), // 账户已久悬
	Revoke("2", "账户销户"), // 账户已销户
	NotFind("3", "账户不存在"), // 账户不存在
	AlreadyAnnual("4", "已经年检"), // 已经年检
	NewAccount("5", "当年新开"), // 当年新开账户
	Exception("6", "年检异常"), // 年检异常
	NotPassValid("8", "未通过数据校验"), // 数据校验
	AcctExpire("9", "账户逾期"), // 账户逾期
	SumbitFail("12", "年检提交失败"), // 年检提交失败
	InputAcctError("7", "进行年检账户录入异常"), // 进行年检账户录入异常
	NotAnnualTime("11", "年检功能未开放"), // 人行年检功能未开放
	PassWordEmpty("13", "用户名密码不完整"), // 用户名、密码不完整
	PasswordError("14", "用户名密码错误"), // 用户名、密码错误
	PasswordExpire("15", "密码过期"), // 密码过期
	UserNameError("16", "用户名不存在"), // 用户名不存在
	UserNameLock("17", "用户被锁"), // 用户被锁
	NotPower("18","没有账户年检功能权限"),//没有执行<银行结算账户年检>功能的权限
	SystemSleep("10", "人行账管系统后台服务关闭");// 当前系统正在运行后台批处理

	private String value;

	private String fullName;

	private AmsAnnualResultStatus(String value, String fullName) {
		this.value = value;
		this.fullName = fullName;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return this.value;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	public static AmsAnnualResultStatus value2Enum(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else if ("0".equals(value)) {
			return Success;
		} else if ("1".equals(value)) {
			return Suspend;
		} else if ("2".equals(value)) {
			return Revoke;
		} else if ("3".equals(value)) {
			return NotFind;
		} else if ("4".equals(value)) {
			return AlreadyAnnual;
		} else if ("5".equals(value)) {
			return NewAccount;
		} else if ("6".equals(value)) {
			return Exception;
		} else if ("8".equals(value)) {
			return NotPassValid;
		} else if ("9".equals(value)) {
			return AcctExpire;
		} else if ("10".equals(value)) {
			return SystemSleep;
		} else {
			return null;
		}
	}
}
