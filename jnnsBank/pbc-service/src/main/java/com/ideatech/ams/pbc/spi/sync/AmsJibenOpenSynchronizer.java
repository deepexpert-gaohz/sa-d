package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenOpenBeiAnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统基本户开户接口
 * 
 * @author zoulang
 *
 */
@Slf4j
@Component
public class AmsJibenOpenSynchronizer extends AbstractSynchronizer {
	@Autowired
	AmsJibenOpenBeiAnService amsJibenOpenBeiAnService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		//TODO:需要判断是否走取消核准接口
		if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
			doSynchronBeian(auth,allAcct);
        }else{
            super.doAmsCheckTypeAcctSynchron(auth, allAcct);
        }
	}
	private void doSynchronBeian(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception{
		amsJibenOpenBeiAnService.openAccountFirstStep(auth,allAcct);
		amsJibenOpenBeiAnService.openAccountSecondStep(auth,allAcct);
		amsJibenOpenBeiAnService.openAccountLastStep(auth,allAcct);
		amsJibenOpenBeiAnService.getPrintInfo(auth,allAcct);
		log.info("取消核准AccountsAllInfo保存基本户查询密码;{}，开户许可证号:{},基本存款账户编号：{}",allAcct.getSelectPwd(),allAcct.getOpenKey(),allAcct.getAccountKey());
		//需要存储打印内容
		//TODO： 需要存储打印内容
	}
}
