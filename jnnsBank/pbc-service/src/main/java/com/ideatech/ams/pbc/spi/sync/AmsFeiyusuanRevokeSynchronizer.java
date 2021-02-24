package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非预算专用账户销户接口,用来报备人行账管系统
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeiyusuanRevokeSynchronizer extends AbstractSynchronizer {
	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		super.doRevokeReportTypeAcct(auth, allAcct);
	}

}
