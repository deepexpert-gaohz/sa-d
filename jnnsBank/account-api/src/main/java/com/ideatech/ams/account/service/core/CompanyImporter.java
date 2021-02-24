/**
 * 
 */
package com.ideatech.ams.account.service.core;

/**
 * @author zhailiang
 *
 */
public interface CompanyImporter {
	
	/*List<CompanyAccountInfo> importData(File file) throws Exception;*/
	
	void processData(Boolean isFirst) throws Exception;
}
