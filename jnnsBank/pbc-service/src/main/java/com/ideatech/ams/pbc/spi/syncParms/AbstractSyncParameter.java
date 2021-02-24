package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.SyncParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractSyncParameter implements SyncParameter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected final static String amsChart = "gbk";

	protected final static String eccsChart = "utf-8";

	@Override
	public String getParams(AllAcct allAcct, SyncSystem syncSystem) throws SyncException {
		String parms = getSyncParams(allAcct);
		printStartlog(allAcct, parms, syncSystem);
		return parms;
	}

	protected abstract String getSyncParams(AllAcct allAcct) throws SyncException;

	private void printStartlog(AllAcct allAcct, String parms, SyncSystem syncSystem) {
		StringBuffer logComm = new StringBuffer();
		logComm.append(allAcct.getAcctType().getFullName());
		logComm.append(allAcct.getAcctNo());
		logComm.append(allAcct.getOperateType().getFullName());
		logComm.append("报备").append(syncSystem.getFullName());
		logComm.append("的参数为：").append(parms);
		logger.info(logComm.toString());
	}

	protected String getAmsThrowExceptionStr(AllAcct allAcct) {
		return getThrowExceptionStrBySyncSystem(allAcct, SyncSystem.ams);
	}

	protected String getEccsThrowExceptionStr(AllAcct allAcct) {
		return getThrowExceptionStrBySyncSystem(allAcct, SyncSystem.eccs);
	}

	private String getThrowExceptionStrBySyncSystem(AllAcct allAcct, SyncSystem syncSystem) {
		return "账户" + allAcct.getAcctNo() + allAcct.getOperateType().getFullName() + "报备" + syncSystem.getFullName() + "时参数拼接错误";
	}
}
