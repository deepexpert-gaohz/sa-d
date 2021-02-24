package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeiyusuanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsFeiyusuanChangeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统非预算专用账户变更报备
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeiyusuanChangeSynchronizer extends AbstractSynchronizer {

	@Autowired
	AmsFeiyusuanChangeService amsFeiyusuanChangeService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		AmsFeiyusuanSyncCondition amsFeiyusuanOpenCondition = new AmsFeiyusuanSyncCondition();
		BeanUtils.copyProperties(allAcct, amsFeiyusuanOpenCondition);
		amsFeiyusuanChangeService.changeAccountFirstStep(auth, amsFeiyusuanOpenCondition);
		amsFeiyusuanChangeService.changeAccountSecondStep(auth, amsFeiyusuanOpenCondition);
		if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
			return;
		}
		amsFeiyusuanChangeService.changeAccountLastStep(auth);
	}

}
