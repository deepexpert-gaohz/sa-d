package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import org.springframework.stereotype.Component;



/**
 * 默认报备系统校验器
 * 
 * @author zoulang
 *
 */
@Component
public class DefaultSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		// TODO 默认为校验成功
	}
}
