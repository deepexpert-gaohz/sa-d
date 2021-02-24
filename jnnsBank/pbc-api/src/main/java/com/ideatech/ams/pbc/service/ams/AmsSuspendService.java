package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsSuspendSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统账户久悬接口
 * 
 * @author zoulang
 *
 */
public interface AmsSuspendService {

	/**
	 * 账户久悬第一步,校验是否允许久悬并且反显客户信息
	 * 
	 * @param condition
	 * @param auth
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo suspendAccountFirstStep(LoginAuth auth, AmsSuspendSyncCondition condition) throws SyncException, Exception;

	/**
	 * 久悬业务提交，并最终在人行中进行销户
	 * 
	 * @param auth
	 * @throws SyncException
	 * @throws Exception
	 */
	void suspendAccountLastStep(LoginAuth auth) throws SyncException, Exception;
}
