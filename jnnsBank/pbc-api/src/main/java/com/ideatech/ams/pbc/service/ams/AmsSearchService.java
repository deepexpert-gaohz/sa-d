package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.exception.SyncException;

import org.apache.http.Header;

import java.util.List;

/**
 * 查询人行账管系统信息接口
 * 
 * @author zoulang
 *
 */
public interface AmsSearchService {

	/**
	 * 根据账号查询在账户审核列表中是否存在
	 * 
	 * @param auth
	 * @param acctNo
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	boolean existAcctInCheckListByAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 根据账号查询人行账管账户信息
	 * 
	 * @param auth
	 * @param acctNo
	 *            账号
	 * @return
	 * @throws SyncException
	 */
	AmsAccountInfo getAmsAccountInfoByAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 取消核准变更用变更接口查询数据（获取法定代表人类型）
	 *
	 * @param auth
	 * @param allAcct
	 *            账号
	 * @return
	 * @throws SyncException
	 */
	AmsAccountInfo getAmsAccountInfoByAcctNoFromBeiAnChange(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception;

	/**
	 * 销户时，根据账号查询人行账管系统账户信息
	 * 
	 * @param auth
	 * @param acctNo
	 *            账号
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 销户时，根据账号查询人行账管系统账户信息(增加账户性质)
	 *
	 * @param auth
	 * @param acctNo
	 *            账号
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(LoginAuth auth, String acctNo, SyncAcctType acctType) throws SyncException, Exception;

	/**
	 * 账户久悬时，根据账号查询人行账管系统账户信息
	 * 
	 * @param auth
	 * @param acctNo
	 *            账号
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 根据基本户开户许可证查询人行账管信息，基于开一般戶、非預算單位專用存款賬戶
	 * 
	 * @param auth
	 * @param accountKey
	 *            基本戶開戶許可和智能
	 * @param regAreaCode
	 *            基本戶註冊地地區代碼
	 * @return
	 * @throws SyncException
	 */
	AmsAccountInfo getAmsAccountInfoByAccountKey(LoginAuth auth, String accountKey, String regAreaCode) throws SyncException, Exception;

	/**
	 * 一般户开户校验
	 *
	 * 根据基本户开户许可证查询人行账管信息，基于开一般户（以开户代替查询）
	 * 查询账户是否满足开户条件
	 * @param auth        账管用户登录对象
	 * @param accountKey     基本户开户许可证
	 * @param regAreaCode    基本户注册地地区代码
	 * @return
	 * @throws SyncException
	 */
	AmsAccountInfo getAmsAccountInfoByAccountKeyForYiBanOpen(LoginAuth auth, String accountKey, String regAreaCode) throws SyncException, Exception;

	/**
	 * 根据年检账户查找账户信息
	 * 
	 * @param auth
	 * @param acctNo
	 *            待年检的账号
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoByAnnaulAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 根据基本户开户许可证获取许可证地区代码
	 * 
	 * @param auth
	 * @param accountKey
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	String getAreaCodeByAccountKey(LoginAuth auth, String accountKey) throws SyncException, Exception;

	/**
	 * 根据基本户许可证地区代码获取当时开户的地区代码（非预算开立异地基本户时需要这个步骤）
	 *
	 * @param auth
	 * @param jibenRegAreaCode
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	String getRegAreaCodeForRemoteOpen(LoginAuth auth, String jibenRegAreaCode) throws SyncException, Exception;

	/**
	 * 根据人行机构号码查询银行所在地地区编码
	 * 
	 * @param auth
	 * @param bankCode
	 *            人行机构码
	 * @return 数组:[0]=地区中文名 ，[1]=地区编码
	 * @throws SyncException
	 * @throws Exception
	 */
	String[] getBankAreCode(LoginAuth auth, String bankCode) throws SyncException, Exception;

	/**
	 * 从人行账管系统中获取所在机构的机构码
	 * 
	 * @param auth
	 * @return 数组:[0]=机构中文名称 ，[1]=机构代码
	 * @throws SyncException
	 * @throws Exception
	 */
	String[] getBankCodeByLoginName(LoginAuth auth) throws SyncException, Exception;

	/**
	 * 2级用户根据一般户开立第一个页面来获取机构代码,机构中文名,地区代码
	 *
	 *
	 * @param auth
	 * @return 数组:[0]=机构中文名称 ，[1]=机构代码 ,[2]=地区代码
	 * @throws SyncException
	 * @throws Exception
	 */
	String[] getBankNameAndRegAreaCodeForUserType2(LoginAuth auth) throws SyncException, Exception;

	/**
	 * 4级用户根据取消核准的基本户开立第一个页面来获取机构代码,机构中文名,地区代码
	 *
	 * @param auth
	 * @return 数组:[0]=机构中文名称 ，[1]=机构代码, [2]=地区代码
	 * @throws SyncException
	 * @throws Exception
	 */
	String[] getBankNameAndRegAreaCodeForUserType4(LoginAuth auth) throws SyncException, Exception;

	/**
	 * 一般户开户、变更业务，需要发起征询查询进行查询
	 * 
	 * @param auth
	 * @param condition
	 * @return 若征询成功则返回success
	 * @throws Exception
	 * @throws SyncException
	 */
	ResultVO consultRemoteServer(LoginAuth auth, AmsYibanSyncCondition condition) throws Exception, SyncException;

	/**
	 * 非预算开户、变更，需要发起征询查询进行查询
	 *
	 * @param auth
	 * @param condition
	 * @return 若征询成功则返回success
	 * @throws Exception
	 * @throws SyncException
	 */
	ResultVO consultRemoteServer(LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws Exception, SyncException;

	/**
	 * 取消核准基本户开户业务，需要发起征询查询进行查询
	 * @param auth
	 * @param condition
	 * @return 若征询成功则返回success
	 * @throws Exception
	 * @throws SyncException
	 */
	ResultVO consultRemoteServer(LoginAuth auth, AllAcct condition) throws Exception, SyncException;
	/**
	 * 取消核准非临时开户业务，需要发起征询查询进行查询
	 * @param auth
	 * @param condition
	 * @return 若征询成功则返回success
	 * @throws Exception
	 * @throws SyncException
	 */
	ResultVO consultRemoteServer(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception, SyncException;


	/**
	 * 9月15号人行更新后新加回调函数
	 * 20200915人行更新后新加回调函数
	 *
	 * @param auth
	 * @param checkPageSubmitValue
	2 隐藏行
	 */
	void checkPageSubmit(LoginAuth auth, String checkPageSubmitValue) throws SyncException, Exception;


	/**
	 * 20200915人行更新后新加回调函数
	 *
	 * @param auth
	 * @param html1
	 * @throws SyncException
	 * @throws Exception
	 */
	void callbackMethod(LoginAuth auth, String html1) throws SyncException, Exception;

	/**
	 * 20200915 请求增加header头
	 *
	 * @param auth
	 * @param url 上一步请求url
	 * @throws SyncException
	 * @throws Exception
	 */
	List<Header> getHeaderForShowResult(LoginAuth auth, String url);
}
