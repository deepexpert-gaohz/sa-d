package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 报备类销户账户接口，用以实现报备类账户（一般户、非预算）销户
 * 
 * @author zoulang
 *
 */
public interface AmsRevokeService {

	/**
	 * 报备类账户销户第一步,校验是否允许销户并且反显客户信息
	 * 
	 * @param condition
	 * @param auth
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo revokeAccountFirstStep(LoginAuth auth, AmsRevokeSyncCondition condition) throws SyncException, Exception;

	/**
	 * 销户业务提交，并最终在人行中进行销户
	 * 
	 * @param auth
	 * @param condition
	 * @throws SyncException
	 * @throws Exception
	 */
	void revokeAccountLastStep(LoginAuth auth, AmsRevokeSyncCondition condition) throws SyncException, Exception;
}
