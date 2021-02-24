package com.ideatech.ams.pbc.service.eccs;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 机构信用代码证基本户变更参数接口
 * 
 * @author zoulang
 *
 */
public interface EccsChangeAccountParamService {

	/**
	 * 已发放的机构信用信用代码证变更接口参数
	 * 
	 * @param model
	 * @param allAcct
	 * @return
	 * @throws SyncException
	 */
	StringBuffer getChangeXydmzUrl(EccsAccountInfo model, AllAcct allAcct) throws SyncException;

	/**
	 * 未发放的机构信用代码证变更接口参数
	 * 
	 * @param model
	 * @param allAcct
	 * @return
	 * @throws SyncException
	 */
	StringBuffer getNotFaFangChangeXydmzUrl(EccsAccountInfo model, AllAcct allAcct) throws SyncException;
}
