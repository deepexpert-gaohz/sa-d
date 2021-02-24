package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeiyusuanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsFeiyusuanOpenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 非预算单位专用存款账户报备人行接口,用以报备人行账管系统
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeiyusuanOpenSynchronizer extends AbstractSynchronizer {

	@Autowired
	AmsFeiyusuanOpenService amsFeiyusuanOpenService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		AmsFeiyusuanSyncCondition amsFeiyusuanOpenCondition = new AmsFeiyusuanSyncCondition();
		BeanUtils.copyProperties(allAcct, amsFeiyusuanOpenCondition);
		amsFeiyusuanOpenService.openAccountFirstStep(auth, amsFeiyusuanOpenCondition);
		allAcct.setRegAreaCode(amsFeiyusuanOpenCondition.getRegAreaCode());// 在第一步校验时，地区代码可能发生变化
		amsFeiyusuanOpenService.openAccountSecondStep(auth, amsFeiyusuanOpenCondition);
		if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
			return;
		}
		amsFeiyusuanOpenService.openAccountLastStep(auth);
		// 校验人行是否存在
		if (!checkSyncStatus(auth, allAcct.getAcctNo(), allAcct.getOperateType())) {
			throw new SyncException("账户" + allAcct.getAcctNo() + "报备人行成功,但人行账管系统无法查到该账户报备后信息，请联系系统管理员.");
		}
	}

}
