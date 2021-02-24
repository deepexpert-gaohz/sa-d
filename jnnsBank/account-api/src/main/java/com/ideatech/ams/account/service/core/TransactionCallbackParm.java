/**
 * 
 */
package com.ideatech.ams.account.service.core;

/**
 * @author zhailiang
 *
 */
public interface TransactionCallbackParm<T> {

	T execute(Class<T> clazz) throws Exception;

}
