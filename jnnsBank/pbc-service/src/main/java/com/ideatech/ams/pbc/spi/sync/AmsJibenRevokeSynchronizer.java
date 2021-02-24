package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeBeiAnSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsRevokeBeiAnService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统基本户销户接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsJibenRevokeSynchronizer extends AbstractSynchronizer {
	@Autowired
	AmsRevokeBeiAnService amsRevokeBeiAnService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		// 校验基本户开户许可证是否在他行有久悬户,禁止销户
		try {
			checkIsExistSuspend(auth, allAcct);
		} catch (Exception e) {
			if(StringUtils.isNotBlank(e.getMessage())){
				if (e.getMessage().contains("久悬")) {
					throw e;
				}
			}

		}
		//TODO:需要判断是否走取消核准接口
		if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
			doSynchronBeian(auth,allAcct);
		}

	}

	/**
	 * 校验账户有没有其他久悬账户
	 * 
	 * @param auth
	 * @param allAcct
	 * @throws Exception
	 */
	private void checkIsExistSuspend(LoginAuth auth, AllAcct allAcct) throws Exception {
		if (StringUtils.isNotBlank(allAcct.getAccountKey()) && StringUtils.isNotBlank(allAcct.getRegAreaCode())) {
			amsSearchService.getAmsAccountInfoByAccountKey(auth, allAcct.getAccountKey(), allAcct.getRegArea());
		} else {
			AmsAccountInfo info = amsSearchService.getAmsAccountInfoByAcctNo(auth, allAcct.getAcctNo());
			if (info != null && StringUtils.isNotBlank(info.getAccountKey()) && StringUtils.isNotBlank(info.getRegAreaCode())) {
				amsSearchService.getAmsAccountInfoByAccountKey(auth, allAcct.getAccountKey(), allAcct.getRegArea());
			}
		}

	}
	private void doSynchronBeian(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception{
		logger.info("基本存款账户走取消核准销户接口报备人行......");
		AmsRevokeBeiAnSyncCondition condition = new AmsRevokeBeiAnSyncCondition();
		BeanUtils.copyProperties(allAcct, condition);
		amsRevokeBeiAnService.revokeAccountFirstStep(auth, condition);
		amsRevokeBeiAnService.revokeAccountSecondStep(auth, condition);
		if(CollectionUtils.isNotEmpty(condition.getALLAccountData()) && condition.getALLAccountData().size()>0){
			allAcct.setALLAccountData(condition.getALLAccountData());
		}

	}
}
