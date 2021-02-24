package com.ideatech.ams.account.service.core;

import java.io.File;

/**
 * 
 * @ClassName: CompanyImportPreproccess
 * @Description: TODO(对公报备预处理器接口)
 * @author zoulang
 * @date 2015年11月25日 上午10:02:10
 *
 */
public interface CompanyImportListener {

	/**
	 * 
	 * @Title: preproccess
	 * @Description: TODO(t+1数据预处理)
	 * @param 设定文件
	 * @return boolean 处理则返回 true,未处理返回false
	 * @throws
	 */
	public boolean preListener() throws Exception;
	
	public Boolean preFileListener() throws Exception;

	Boolean saveFile2Core(File file) throws Exception;


	/**
	 * @throws Exception
	 * @Description: 更新核心流水表、账户主表、对公账户表。（人行覆盖核心）
	 * @return void 返回类型
	 * @throws
	 */
	void afterListener() throws Exception;

}
