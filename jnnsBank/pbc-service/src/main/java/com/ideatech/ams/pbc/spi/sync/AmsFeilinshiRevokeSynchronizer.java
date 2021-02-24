package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsRevokeBeiAnSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsRevokeBeiAnService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非临时户销户接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiRevokeSynchronizer extends AbstractSynchronizer {
	@Autowired
	AmsRevokeBeiAnService amsRevokeBeiAnService;
	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
			//取消核准走销户接口
			doSynchronBeian(auth, allAcct);
		}
	}

	private void doSynchronBeian(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception{
		//TODO 非临时取消核准销户接口
		logger.info("非临时存款账户走取消核准销户接口报备人行......");
		AmsRevokeBeiAnSyncCondition condition = new AmsRevokeBeiAnSyncCondition();
		BeanUtils.copyProperties(allAcct, condition);
		amsRevokeBeiAnService.revokeAccountFirstStep(auth, condition);
		amsRevokeBeiAnService.revokeAccountSecondStep(auth, condition);
		if(CollectionUtils.isNotEmpty(condition.getALLAccountData()) && condition.getALLAccountData().size()>0){
			allAcct.setALLAccountData(condition.getALLAccountData());
		}
	}
}
