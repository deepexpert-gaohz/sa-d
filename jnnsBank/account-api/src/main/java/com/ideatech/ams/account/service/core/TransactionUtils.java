/**
 * 
 */
package com.ideatech.ams.account.service.core;

import org.springframework.transaction.annotation.Propagation;

/**
 * @author zhailiang
 *
 */
public interface TransactionUtils {

	void executeInNewTransaction(TransactionCallback callback) throws Exception;

	void executeInNOTSUPPORTEDTransaction(TransactionCallback callback) throws Exception;

	<T> T executeInNewTransaction(TransactionCallbackParm callback,Class<T> clazz) throws Exception;

	void executeInTransaction(TransactionCallback callback) throws Exception;

	void exexuteInByType(TransactionCallback callback, Propagation propagation) throws Exception;

	void executeInNoTransaction(TransactionCallback callback) throws Exception;
}
