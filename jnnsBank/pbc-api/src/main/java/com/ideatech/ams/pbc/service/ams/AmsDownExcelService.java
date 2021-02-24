package com.ideatech.ams.pbc.service.ams;


import com.ideatech.ams.pbc.dto.AmsDownAnnualTask;
import com.ideatech.ams.pbc.dto.AmsDownTask;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统Excel下载接口，用以下载人行账户列表excel等
 * 
 * @author zoulang
 *
 */
public interface AmsDownExcelService {
	/**
	 * 按银行机构代码导出excel,下载的文件名以银行机构号为名
	 * 
	 * @param auth
	 * @param task
	 * @throws Exception
	 */
	public void downRHAccount(LoginAuth auth, AmsDownTask task) throws SyncException, Exception;

	/**
	 * 按银行机构代码导出机构年检情况excel,下载的文件名以银行机构号为名
	 * 
	 * @param auth
	 * @param task
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnualAccount(LoginAuth auth, AmsDownAnnualTask task) throws SyncException, Exception;
}
