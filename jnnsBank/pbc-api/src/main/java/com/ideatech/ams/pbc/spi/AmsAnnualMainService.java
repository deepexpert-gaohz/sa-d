package com.ideatech.ams.pbc.spi;

import com.ideatech.ams.pbc.dto.AmsDownAnnualTask;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 人行账管系统年检接口，用以所有与年检有关的接口
 * 
 * @author zoulang
 *
 */
public interface AmsAnnualMainService {

	/**
	 * 账号在人行做年检标记
	 * 
	 * @param pbcUserAccount
	 * @param acctNo
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	AmsAnnualResultStatus sumbitAnnual(PbcUserAccount pbcUserAccount, String acctNo) throws SyncException, Exception;

	/**
	 * 账号在人行做年检标记
	 * 
	 * @param auth
	 * @param acctNo
	 * @return
	 * @throws Exception
	 * @throws SyncException
	 */
	AmsAnnualResultStatus sumbitAnnual(LoginAuth auth, String acctNo) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表,包括已年检、未年检状况
	 * 
	 * @param pbcUserAccount
	 * @param annualYear
	 *            年检年份
	 * @param folderPath
	 *            下载的文件夹路径
	 * @param bankId
	 *            要下载的人行机构号
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, String folderPath, String bankId, String annualYear) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表,包括已年检、未年检状况
	 * 
	 * @param auth
	 * @param annualYear
	 *            年检年份
	 * @param folderPath
	 *            下载的文件夹路径
	 * @param bankId
	 *            要下载的人行机构号
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(LoginAuth auth, String folderPath, String bankId, String annualYear) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表，包括已年检、未年檢狀況
	 * 
	 * @param pbcUserAccount
	 * @param task
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, AmsDownAnnualTask task) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表，包括已年检、未年檢狀況
	 * 
	 * @param auth
	 * @param task
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(LoginAuth auth, AmsDownAnnualTask task) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表,默认下载当年账户年检情况列表,包括已年检、未年检状况
	 * 
	 * @param pbcUserAccount
	 * @param folderPath
	 *            下载的文件夹路径
	 * @param bankId
	 *            要下载的人行机构号
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, String folderPath, String bankId) throws SyncException, Exception;

	/**
	 * 下载人行账户年检列表,默认下载当年账户年检情况列表,包括已年检、未年检状况
	 * 
	 * @param auth
	 * @param folderPath
	 *            下载的文件夹路径
	 * @param bankId
	 *            要下载的人行机构号
	 * @throws SyncException
	 * @throws Exception
	 */
	public void downAnnuanAccountExcel(LoginAuth auth, String folderPath, String bankId) throws SyncException, Exception;

}
