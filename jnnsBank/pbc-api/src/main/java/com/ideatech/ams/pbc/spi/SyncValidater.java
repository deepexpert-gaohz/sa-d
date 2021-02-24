package com.ideatech.ams.pbc.spi;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 系统同步校验接口， 用以校验同步系统的参数是否满足条件
 * 
 * @author zoulang
 *
 */
public interface SyncValidater {

	/**
	 * 校验报备人行系统的参数条件是否满足规则
	 * 
	 * @param allAcct
	 * @param syncSystem
	 *            要上报的系统
	 * @throws SyncException
	 * @throws Exception
	 */
	void validater(AllAcct allAcct, SyncSystem syncSystem) throws SyncException, Exception;

}
