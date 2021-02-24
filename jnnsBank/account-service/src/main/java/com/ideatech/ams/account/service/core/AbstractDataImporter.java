/**
 * 
 */
package com.ideatech.ams.account.service.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;


/**
 * @author zoulang
 *
 */
@Slf4j
public abstract class AbstractDataImporter implements CompanyImporter {

	/*@Override
	public List<CompanyAccountInfo> importData(File file) throws Exception {
		logger.info("使用" + getClass().getSimpleName() + "导入数据文件:" + file.getAbsolutePath());
		List<CompanyAccountInfo> accountInfo = doImport(file);
		logger.info("使用" + getClass().getSimpleName() + "导入数据成功");
		return accountInfo;
	}

	protected abstract List<CompanyAccountInfo> doImport(File file) throws Exception;*/
	
	@Override
	public void processData(Boolean isFirst) throws Exception {
		log.info("使用" + getClass().getSimpleName() + "处理数据");
		if (isFirst) {
			log.info("处理第一次数据初始化");
			firstProcess();
		} else {
			doProcess();
		}
		log.info("使用" + getClass().getSimpleName() + "处理数据成功");
	}

	protected abstract void doProcess() throws Exception;
	
	protected abstract void firstProcess() throws Exception;

	
}
