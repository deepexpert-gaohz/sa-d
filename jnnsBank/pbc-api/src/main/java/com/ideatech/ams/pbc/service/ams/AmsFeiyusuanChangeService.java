package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsFeiyusuanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统非预算单位专用户变更(分为3步),三步同时满足则变更成功
 * 
 * @author zoulang
 *
 */
public interface AmsFeiyusuanChangeService {

	/**
	 * 非预算单位专用存款账户变更第一步---校验是否满足变更条件
	 * 
	 * @param auth
	 * @param cndition
	 * @return 获取变更前非预算的账户信息
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsFeiyusuanSyncCondition changeAccountFirstStep(LoginAuth auth, AmsFeiyusuanSyncCondition cndition) throws SyncException, Exception;

	/**
	 * 非预算单位专用存款账户变更第二步---是否能获取对应基本户信息
	 * 
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo changeAccountSecondStep(LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws SyncException, Exception;

	/**
	 * 非预算单位专用存款账户变更最后一步---是否提示变更成功
	 * 
	 * @param auth
	 * @throws Exception
	 * @throws SyncException
	 */
	void changeAccountLastStep(LoginAuth auth) throws SyncException, Exception;
}
