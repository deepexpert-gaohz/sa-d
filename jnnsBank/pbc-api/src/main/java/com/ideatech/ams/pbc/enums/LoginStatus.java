package com.ideatech.ams.pbc.enums;

/**
 * 人行系统登录状态
 * 
 * @author zoulang
 *
 */
public enum LoginStatus {
	/**
	 * 登录成功
	 */
	Success("登录成功"),
	/**
	 * 用户名密码错误
	 */
	PasswordError("用户名密码错误"),
	/**
	 * 网络不通
	 */
	NetNotConn("网络不通"),
	/**
	 * 密码过期
	 */
	PasswordExpire("密码过期"),
	/**
	 * 用户不存在
	 */
	UserNameError("用户不存在"),
	/**
	 * 用户名或密码为空
	 */
	PassWordEmpty("用户名或密码为空"),
	/**
	 * 人行账管Ip为空
	 */
	PbcIpEmpty("人行系统Ip地址不能为空"),
	/**
	 * 用户被锁
	 */
	UserNameLock("用户被锁"),
	/**
	 * 首次登录需修改密码
	 */
	FirstLogin("首次登录需修改密码"),
	/**
	 * 网络超时
	 */
	NetTimeOut("人行网络超时"),
	/**
	 * 未知异常
	 */
	PageUnknow("未知异常"),
	/**
	 * 用户被停用
	 */
	UserStop("此用户以被停用"),
	/**
	 * 用户必须使用2级操作员
	 */
	MustTwoLevelRoleUser("用户必须使用2级或者4级操作员"),
	/**
	 * 网络异常
	 */
	NetUnknow("网络异常"),
	/**
	 * 人行账管系统后台异常
	 */
	AmsException("人行账管系统后台异常"),
	/**
	 * 人行账管系统无法访问
	 */
	AmsClose("人行账管系统无法访问"),
	/**
	 * 账管系统正在运行后台批处理
	 */
	AmsSleep("账管系统正在运行后台批处理"),
	/**
	 * 客户机登录日期跟服务器日期不为同一天
	 */
	LoginTimeError("客户机登录日期跟服务器日期不为同一天"),
	/**
	 * 用户登录信息失效
	 */
		NoSession("用户登录信息失效");

	private String fullName = "";

	public String getFullName() {
		return fullName;
	}

	private LoginStatus(String fullName) {
		this.fullName = fullName;
	}
}
