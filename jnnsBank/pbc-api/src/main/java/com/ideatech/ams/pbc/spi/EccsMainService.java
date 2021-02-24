package com.ideatech.ams.pbc.spi;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 机构信用代码证对外接口
 * 
 * @author zoulang
 *
 */
public interface EccsMainService {

	/**
	 * 机构信用代码证系统登录
	 * 
	 * @param eccsUserAccount
	 * @return
	 */
	LoginAuth eccsLogin(PbcUserAccount eccsUserAccount);

	/**
	 * 校验机构信用代码证系统用户名、密码是否正确
	 * 
	 * @param eccsUserAccount
	 *            登录用户信息
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	void checkEccsUserName(PbcUserAccount eccsUserAccount) throws SyncException, Exception;

	/**
	 * 修改机构信用代码证系统用户密码
	 * 
	 * @param eccsIp
	 *            机构信用代码证Ip
	 * @param eccsUserName
	 *            机构信用代码证用户名
	 * @param eccsOldPwd
	 *            机构信用代码证原密码
	 * @param eccsNewPwd
	 *            机构信用代码证新密码
	 */
	void modifyEccsPwd(String eccsIp, String eccsUserName, String eccsOldPwd, String eccsNewPwd) throws SyncException, Exception;

	/**
	 * 机构信用代码证系统账户开户报备
	 * 
	 * @param pbcUserAccount
	 * @param allAcct
	 * @throws Exception
	 * @throws SyncException
	 */
	void eccsAccountOpenSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception, SyncException;

	/**
	 * 机构信用代码证系统账户开户报备
	 * 
	 * @param auth
	 * @param allAcct
	 * @throws Exception
	 * @throws SyncException
	 */
	void eccsAccountOpenSync(LoginAuth auth, AllAcct allAcct) throws Exception, SyncException;

	/**
	 * 机构信用代码证系统变更报备
	 * 
	 * @param pbcUserAccount
	 *            代码证系统登录对象
	 * @param allAcct
	 *            报备对象
	 * @param condition
	 *            查询要变更信息的查询条件,未变更前此账户信息的信息
	 * @throws Exception
	 * @throws SyncException
	 */
	void eccsAccountChangeSync(PbcUserAccount pbcUserAccount, AllAcct allAcct, EccsSearchCondition condition) throws Exception, SyncException;

	/**
	 * 机构信用代码证系统变更报备
	 * 
	 * @param auth
	 *            代码证系统登录对象
	 * @param allAcct
	 *            报备对象
	 * @param condition
	 *            查询要变更信息的查询条件,未变更前此账户信息的信息
	 * @throws Exception
	 * @throws SyncException
	 */
	void eccsAccountChangeSync(LoginAuth auth, AllAcct allAcct, EccsSearchCondition condition) throws Exception, SyncException;

	/**
	 * 根据查询条件查询机构信用代码证信息
	 * 
	 * @param condition
	 * @return
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByCondition(PbcUserAccount eccsUserAccount, EccsSearchCondition condition) throws Exception, SyncException;

	/**
	 * 根据查询条件查询机构信用代码证信息
	 * 
	 * @param condition
	 * @return
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByCondition(LoginAuth auth, EccsSearchCondition condition) throws Exception, SyncException;

	/**
	 * 根据组织机构代码查询机构代码证信息
	 * 
	 * @param eccsUserAccount
	 * @param orgCode
	 *            组织机构代码
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByOrgCode(PbcUserAccount eccsUserAccount, String orgCode) throws Exception, SyncException;

	/**
	 * 根据组织机构代码查询机构代码证信息
	 * 
	 * @param auth
	 * @param orgCode
	 *            组织机构代码
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByOrgCode(LoginAuth auth, String orgCode) throws Exception, SyncException;

	/**
	 * 根据工商注册类型和工商注册编号查询机构信用代码证信息
	 * 
	 * @param eccsUserAccount
	 * @param regType
	 *            工商注册类型
	 * @param regNo
	 *            工商注册编号
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByRegTypeAndRegNo(PbcUserAccount eccsUserAccount, String regType, String regNo) throws Exception, SyncException;

	/**
	 * 根据工商注册类型和工商注册编号查询机构信用代码证信息
	 * 
	 * @param auth
	 * @param regType
	 *            工商注册类型
	 * @param regNo
	 *            工商注册编号
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByRegTypeAndRegNo(LoginAuth auth, String regType, String regNo) throws Exception, SyncException;

	/**
	 * 根据机构信用代征编号查询机构信用代码证信息
	 * 
	 * @param eccsUserAccount
	 * @param orgEccsNo
	 *            机构信用代码证信息
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByOrgEccsNo(PbcUserAccount eccsUserAccount, String orgEccsNo) throws Exception, SyncException;

	/**
	 * 根据机构信用代征编号查询机构信用代码证信息
	 * 
	 * @param auth
	 * @param orgEccsNo
	 *            机构信用代码证信息
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByOrgEccsNo(LoginAuth auth, String orgEccsNo) throws Exception, SyncException;

	/**
	 * 根据基本户开户许可证号查询机构信用代码证信息
	 * 
	 * @param account
	 * @param accountkey
	 *            基本户卡户许可证信息
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByAccountKey(PbcUserAccount account, String accountkey) throws Exception, SyncException;

	/**
	 * 根据基本户开户许可证号查询机构信用代码证信息
	 * 
	 * @param auth
	 * @param accountkey
	 *            基本户卡户许可证信息
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfoByAccountKey(LoginAuth auth, String accountkey) throws Exception, SyncException;
}
