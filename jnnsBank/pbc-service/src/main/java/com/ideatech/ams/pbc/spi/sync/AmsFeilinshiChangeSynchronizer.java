package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsFeilinshiChangeBeiAnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非临时户变更接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiChangeSynchronizer extends AbstractSynchronizer {
	@Autowired
	AmsFeilinshiChangeBeiAnService amsFeilinshiChangeBeiAnService;
	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
			//取消核准走取消核准接口
			doSynchronBeian(auth, allAcct);
		}else{
			super.doAmsCheckTypeAcctSynchron(auth, allAcct);
		}
	}

	private void doSynchronBeian(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception{
		//TODO 非临时变更走变更接口
		logger.info("非临时存款账户走取消核准变更接口报备人行......");
		AmsFeilinshiSyncCondition condition = new AmsFeilinshiSyncCondition();
		BeanUtils.copyProperties(allAcct, condition);
		amsFeilinshiChangeBeiAnService.openAccountFirstStep(auth,condition);
		amsFeilinshiChangeBeiAnService.openAccountSecondStep(auth,condition);
		amsFeilinshiChangeBeiAnService.openAccountLastStep(auth,condition);
	}
}
