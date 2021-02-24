package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAnnualInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行系统年检接口，用以提交校验、查询、提交年检任务
 * 
 * @author zoulang
 *
 */
public interface AmsAnnualService {

	/**
	 * 校验年检功能是否开放
	 * 
	 * @param auth
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAnnualResultStatus gotoAnnuanlPage(LoginAuth auth) throws SyncException, Exception;

	/**
	 * 校验账号是否可以进行进行年检,若账户可以进行年检并同反显账户的客户信息
	 * 
	 * @param auth
	 * @param acctNo
	 * @return
	 * @throws Exception
	 */
	AmsAnnualResultStatus validAcctNoAnnual(LoginAuth auth, String acctNo, AmsAnnualInfo amsAnnualInfo) throws SyncException, Exception;

	/**
	 * 满足年检条件的账户进行年检提交
	 * 
	 * @param auth
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAnnualResultStatus sumitAnnualAccount(LoginAuth auth, String acctNo) throws SyncException, Exception;

}
