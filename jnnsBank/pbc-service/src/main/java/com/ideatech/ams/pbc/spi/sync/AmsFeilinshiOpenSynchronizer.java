package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsFeilinshiOpenBeiAnService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统非临时户开户接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiOpenSynchronizer extends AbstractSynchronizer {
	@Autowired
	AmsFeilinshiOpenBeiAnService amsFeilinshiOpenBeiAnService;
	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
//		super.doAmsCheckTypeAcctSynchron(auth, allAcct);
		//需要判断是否走取消核准接口
		if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
			doSynchronBeian(auth,allAcct);
		}else{
			super.doAmsCheckTypeAcctSynchron(auth, allAcct);
		}
	}
	private void doSynchronBeian(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception{
		//非临时户走备案机制接口
		logger.info("非临时账户取消核准接口报备人行.....");
		AmsFeilinshiSyncCondition condition = new AmsFeilinshiSyncCondition();
		BeanUtils.copyProperties(allAcct, condition);
		amsFeilinshiOpenBeiAnService.openAccountFirstStep(auth,condition);
		amsFeilinshiOpenBeiAnService.openAccountSecondStep(auth,condition);
		amsFeilinshiOpenBeiAnService.openAccountThirdStep(auth,condition);
		amsFeilinshiOpenBeiAnService.openAccountLastStep(auth,condition);
		//存储非临时机构临时存款账户编号（L）
		if(StringUtils.isNotBlank(condition.getAccountLicenseNo())){
			allAcct.setAccountLicenseNo(condition.getAccountLicenseNo());
		}
		//存储开户许可证号
		if(StringUtils.isNotBlank(condition.getOpenKey())){
			allAcct.setOpenKey(condition.getOpenKey());
		}
	}
}
