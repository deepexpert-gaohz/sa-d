package com.ideatech.ams.pbc.enums;

/**
 * 修改密码状态
 * 
 * @author zoulang
 *
 */
public enum PwdModifyStatus {

	/**
	 * 
	 */
	Success("修改成功"),
	/**
	 * 
	 */
	OldPwdError("原密码错误"),
	/**
	 * 
	 */
	PwdSameOld("密码与原先密码相同"),
	/**
	 * 
	 */
	PwdSameBeforeFour("新密码不能与前四次密码重复"),
	/**
	 * 
	 */
	PwdMidfyFail("密码修改失败"),
	/**
	 * 
	 */
	NetException("网络异常");

	private String fullName = "";

	PwdModifyStatus(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

}
