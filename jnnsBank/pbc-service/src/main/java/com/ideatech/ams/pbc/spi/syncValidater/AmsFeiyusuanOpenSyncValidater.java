package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非预算单位专用存款账户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeiyusuanOpenSyncValidater extends AmsYusuanOpenSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		super.doValidater(allAcct);
	}

}
