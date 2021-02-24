package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import org.springframework.stereotype.Component;



@Component
public class DefaultSynchronizer extends AbstractSynchronizer {

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		// TODO 默认抛出不支持报备规则
		StringBuffer buffer = new StringBuffer();
		buffer.append(allAcct.getAcctType().getFullName());
		buffer.append(allAcct.getAcctNo());
		buffer.append(allAcct.getOperateType().getFullName());
		buffer.append("不支持报备");
		throw new SyncException(buffer.toString());
	}
}
