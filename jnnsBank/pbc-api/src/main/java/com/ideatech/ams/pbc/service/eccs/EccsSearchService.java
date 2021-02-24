package com.ideatech.ams.pbc.service.eccs;


import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 机构信用代码证系统查询接口
 * 
 * @author zoulang
 *
 */
public interface EccsSearchService {

	/**
	 * 根据查询条件查询机构信用代码证信息(已发放状态)
	 * @param auth
	 * @param eccsSearchBuffer
	 * @return
	 * @throws SyncException
	 */
	EccsAccountInfo getEccsAccountInfo(LoginAuth auth, StringBuffer eccsSearchBuffer) throws Exception, SyncException;

	/**
	 * 根据查询条件获取变更查询是否存在参数
	 * @param model
	 * @return
	 */
	StringBuffer getEccsSearchUrlParams(EccsSearchCondition model);
}
