/**
 * 
 */
package com.ideatech.ams.account.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhailiang
 *
 */
@Component
public class TransactionUtilsImpl implements TransactionUtils {

	@Autowired
	private TransactionUtils transactionUtils;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void executeInTransaction(TransactionCallback callback) throws Exception {
		callback.execute();
	}

	@Override
	public void executeInNoTransaction(TransactionCallback callback) throws Exception {
		callback.execute();
	}

	@Override
	public void exexuteInByType(TransactionCallback callback, Propagation propagation) throws Exception {
		if(propagation == Propagation.REQUIRES_NEW){
			transactionUtils.executeInNewTransaction(callback);
		}else if(propagation == Propagation.NOT_SUPPORTED){
			transactionUtils.executeInNOTSUPPORTEDTransaction(callback);
		}else if(propagation == Propagation.REQUIRED){
			transactionUtils.executeInTransaction(callback);
		}else{
			transactionUtils.executeInNoTransaction(callback);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idea.data.jpa.utils.TransactionUtils#executeInNewTransaction(com.idea.data.jpa.utils.TransactionCallback)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeInNewTransaction(TransactionCallback callback) throws Exception {
		callback.execute();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void executeInNOTSUPPORTEDTransaction(TransactionCallback callback) throws Exception {
		callback.execute();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <T> T executeInNewTransaction(TransactionCallbackParm callback, Class<T> clazz) throws Exception {
		return (T) callback.execute(clazz);
	}
}
