package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统临时户变更接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsLinshiChangeSynchronizer extends AbstractSynchronizer {

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		super.doAmsCheckTypeAcctSynchron(auth, allAcct);
	}
}
