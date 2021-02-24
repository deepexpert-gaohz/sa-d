package com.ideatech.ams.pbc.service.eccs;


import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;

/**
 * 机构信用代码证系统登录接口
 * 
 * @author zoulang
 *
 */
public interface EccsLoginService {

	/**
	 * 机构信用代码证系统登录
	 * 
	 * @param ip
	 *            机构信用代码证系统 Ip
	 * @param eccsUserName
	 *            用户名
	 * @param eccsPassword
	 *            密码
	 * @return
	 */
	LoginAuth eccsLogin(String ip, String eccsUserName, String eccsPassword);

	/**
	 * 获取登录用户在机构信用代码证系统的Id
	 * 
	 * @param eccsIp
	 *            ip
	 * @param eccsUserName
	 *            登录用户名
	 * @param eccsPassword
	 *            登录密码
	 * @return
	 */
	String getEccsUserId(String eccsIp, String eccsUserName, String eccsPassword);

	/**
	 * 当机构信用代码证系统提示过期时修改密码
	 * 
	 * @param loginAuth
	 * @param oldPassWord
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @param userId
	 *            用户在机构信用代码证系统的Id
	 * @return
	 * @throws Exception
	 */
	PwdModifyStatus modifyEccsPassWordBxpire(LoginAuth loginAuth, String oldPassWord, String newPassword, String userId);

	/**
	 * 机构信用代码证系统密码修改（正常修改）
	 * 
	 * @param loginAuth
	 * @param oldPassWord
	 * @param newPassword
	 * @return
	 */
	PwdModifyStatus modifyEccsPassWord(LoginAuth loginAuth, String oldPassWord, String newPassword);

	/**
	 * 账户系统退出
	 * 
	 * @param loginAuth
	 */
	void logout(LoginAuth loginAuth);

	/**
	 * 校验代码证系统登录条件
	 * 
	 * @param eccsUserAccount
	 * @return 若满足条件则返回 Success
	 */
	LoginStatus validateEccsLogin(PbcUserAccount eccsUserAccount);
}
