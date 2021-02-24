package com.ideatech.ams.pbc.spi;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 账户同步器
 * 
 * @author zoulang
 *
 */
public interface Synchronizer {

	/**
	 * 账户同步
	 * 
	 * @param auth
	 *            同步系统登录信息
	 * @param allAcct
	 *            同步对象
	 * @throws Exception
	 * @throws SyncException
	 */
	void synchronizer(LoginAuth auth, SyncSystem syncSystem, AllAcct allAcct) throws SyncException, Exception;

}
