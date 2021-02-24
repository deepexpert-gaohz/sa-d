package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统一般户开户(分为3步),三步同时满足则开户成功
 * 
 * @author zoulang
 *
 */
public interface AmsYibanOpenService {
	/**
	 * 一般户开户第一步---校验是否满足开户条件
	 * 
	 * @param auth
	 * @param cndition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	void openAccountFirstStep(LoginAuth auth, AmsYibanSyncCondition cndition) throws SyncException, Exception;

	/**
	 * 一般户开户第二步---是否能获取对应基本户信息
	 * 
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo openAccountSecondStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception;

	/**
	 * 一般户开户最后一步---是否提示开户成功
	 * 
	 * @param auth
	 * @throws Exception
	 * @throws SyncException
	 */
	void openAccountLastStep(LoginAuth auth) throws SyncException, Exception;
}
