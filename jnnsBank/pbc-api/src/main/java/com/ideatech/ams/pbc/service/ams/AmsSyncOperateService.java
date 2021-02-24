package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.AmsFeiyusuanSyncCondition;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统报备处理，用来处理各种账户性质的账户进行上报需要的公共处理方法
 * 
 * @author zoulang
 *
 */
public interface AmsSyncOperateService {

	/**
	 * 在核准类管理列表中根据账号删除对应账户，可能有多条记录
	 * 
	 * @param auth
	 * @param delAccountNo
	 *            待删除账号
	 * @throws SyncException
	 * @throws Exception
	 */
	boolean deleteAccountInCheckList(LoginAuth auth, String delAccountNo) throws SyncException, Exception;

	/**
	 * 校验错误信息
	 * 
	 * @param html
	 *            请求响应的页面
	 * @param rightMatchStr
	 *            响应要求的包含的正确字符串,若html包含此字段，则认为处理正常
	 * @return 若html未包含rightMatchStr字符串，返回错误信息；若包含则返回“”
	 * @throws SyncException
	 */
	String validateOp(String html, String rightMatchStr) throws SyncException;

	/**
	 * 校验错误信息(包括一般户征询数据)
	 * 
	 * @param html
	 *            请求响应的页面
	 * @param auth
	 * @param rightMatchStr
	 *            响应要求的包含的正确字符串,若html包含此字段，则认为处理正常
	 * @param condition
	 * @return 若html未包含rightMatchStr字符串，返回错误信息；若包含则返回“”
	 * @throws SyncException
	 * @throws Exception
	 */
	String validateOp(String html, String rightMatchStr, LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception;

	/**
	 * 校验错误信息(包括非预算征询数据)
	 * 
	 * @param html
	 *            请求响应的页面
	 * @param auth
	 * @param rightMatchStr
	 *            响应要求的包含的正确字符串,若html包含此字段，则认为处理正常
	 * @param condition
	 * @return 若html未包含rightMatchStr字符串，返回错误信息；若包含则返回“”
	 * @throws SyncException
	 * @throws Exception
	 */
	String validateOp(String html, String rightMatchStr, LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws SyncException, Exception;

	/**
	 *
	 * @param html 响应页面
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */

	String validate(String html) throws SyncException, Exception;

	/**
	 *基本户征询
	 * @param html
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	String validateOp(String html,  LoginAuth auth, AllAcct condition) throws SyncException, Exception;

	/**
	 * 非临时征询
	 * @param html
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	String validateOp(String html,  LoginAuth auth, AmsFeilinshiSyncCondition condition) throws SyncException, Exception;

}
