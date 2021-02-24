package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsYibanChangeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class AmsYibanChangeSynchronizer extends AbstractSynchronizer {

	@Autowired
	AmsYibanChangeService amsYibanChangeService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		AmsYibanSyncCondition amsYibanSyncCondition = new AmsYibanSyncCondition();
		BeanUtils.copyProperties(allAcct, amsYibanSyncCondition);
		amsYibanChangeService.changeAccountFirstStep(auth, amsYibanSyncCondition);
		amsYibanChangeService.changeAccountSecondStep(auth, amsYibanSyncCondition);
		if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
			return;
		}
		amsYibanChangeService.changeAccountLastStep(auth);
	}
}
