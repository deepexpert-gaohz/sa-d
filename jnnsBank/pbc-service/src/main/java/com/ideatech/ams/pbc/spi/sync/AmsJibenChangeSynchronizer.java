package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenChangeBeiAnService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统基本户变更接口
 * 
 * @author zoulang
 *
 */
@Component
public class AmsJibenChangeSynchronizer extends AbstractSynchronizer {

	@Autowired
	AmsSearchService amsSearchService;
	@Autowired
	AmsJibenChangeBeiAnService amsJibenChangeBeiAnService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		// 校验基本户开户许可证是否在他行有久悬户,禁止变更
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
		}else{
			super.doAmsCheckTypeAcctSynchron(auth, allAcct);
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
		amsJibenChangeBeiAnService.changeAccountFirstStep(auth,allAcct);
		amsJibenChangeBeiAnService.changeAccountSecondStep(auth,allAcct);
		amsJibenChangeBeiAnService.changeAccountLastStep(auth,allAcct);
	}
}
