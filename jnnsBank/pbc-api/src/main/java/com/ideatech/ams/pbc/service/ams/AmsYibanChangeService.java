package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 一般户变更就可，用以步骤实现一般户变更
 * 
 * @author zoulang
 *
 */
public interface AmsYibanChangeService {

	/**
	 * 一般户变更第一步---校验是否满足开户条件
	 * 
	 * @param auth
	 * @param cndition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	void changeAccountFirstStep(LoginAuth auth, AmsYibanSyncCondition cndition) throws SyncException, Exception;

	/**
	 * 一般户变更第二步---是否能获取对应基本户信息
	 * 
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	AmsAccountInfo changeAccountSecondStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception;

	/**
	 * 一般户变更最后一步---是否提示开户成功
	 * 
	 * @param auth
	 * @throws Exception
	 * @throws SyncException
	 */
	void changeAccountLastStep(LoginAuth auth) throws SyncException, Exception;
}
