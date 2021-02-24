package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;

/**
 * 人行账管系统登录接口
 * 
 * @author zoulang
 *
 */
public interface AmsLoginService {

	/**
	 * 人行账管系统登录
	 * 
	 * @param ip
	 *            登录Ip
	 * @param amsUserName
	 *            登录用户名
	 * @param amsUserPwd
	 *            登录密码
	 * @return
	 */
	LoginAuth amsLogin(String ip, String amsUserName, String amsUserPwd);

	/**
	 * 人行账管系统密码修改
	 * 
	 * @param loginAuth
	 * @param oldPassWord
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	PwdModifyStatus modifyPassWord(LoginAuth loginAuth, String oldPassWord, String newPassword);

	/**
	 * 校验人行登录规则
	 * 
	 * @param amsUserAccount
	 * @return 若满足条件则返回 Success
	 */
	LoginStatus validateAmsLogin(PbcUserAccount amsUserAccount);

}
