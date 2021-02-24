package com.ideatech.ams.account.service.core;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 
 * @ClassName: DefaultCompanyImportAccess
 * @Description: TODO(T+1文件处理入口)
 * @author zoulang
 * @date 2015年11月25日 上午10:26:14
 *
 */
@Component
@Slf4j
public class DefaultCompanyImportAccess implements CompanyImportAccess {

	@Autowired
	CompanyImportListener preproccesser;

	@Autowired
	private CompanyImporter dbDataImporter;
	
	/*@Autowired
	private CompanyImporter txtDataImporter;*/

	@Value(("${import.file.location}"))
	private String importFileLocation;

	@Value(("${import.file.locationFinish}"))
	private String importFileLocationForFinish;

	@Value(("${import.file.pbcCoverCore:false}"))
	private boolean pbcCoverCore;

	@Override
	public void mainAccess() throws Exception {
		boolean preProcessResult = false;
		Boolean isFirst = null;
		// 文件预处理
		try {
			log.info("======================存量数据导入开始======================");
			Long startTime = System.currentTimeMillis();
			// 原有逻辑：将核心数据File导入核心账户表YD_CORE_PUBLIC_ACCOUNT（导入的同时可以逐条进行人行数据覆盖）
			// 先有逻辑：将核心数据File导入核心账户表YD_CORE_PUBLIC_ACCOUNT（不进行人行数据覆盖，人行覆盖可以单独线程执行。）
			isFirst = preproccesser.preFileListener();
			Long endTime = System.currentTimeMillis();
			log.info("处理导入文件总耗时" + (endTime - startTime) / 1000 + "秒");
			log.info("======================存量数据导入结束======================");
		} catch (Exception e) {
			log.error("核心数据初始化上报文件预处理模块异常", e);
			throw new Exception("核心数据初始化自动上报文件预处理模块异常");
		}

		// 处理核心账户表YD_CORE_PUBLIC_ACCOUNT的数据（增加流水表数据，增加账户主表、账户对公表数据，增加客户主表、对公表、日志表的数据，最后删除核心账户表）
		if (isFirst != null) {
			processCoreData(isFirst);
		}

		try{
			//除补全外：账户状态、法定代表人、法人证件类型、币种、资金转换BigDecimal、行业归属、邮政编码、账户名称构成方式、资金性质、上级法人证件类型等
			if (isFirst != null && pbcCoverCore){
				log.info("======================核心流水更新（人行覆盖核心）开始。======================");
				Long startTime = System.currentTimeMillis();
				preproccesser.afterListener();
				Long endTime = System.currentTimeMillis();
				log.info("======================核心流水更新（人行覆盖核心）结束。======================");
				log.info("核心流水更新（人行覆盖核心）总耗时{}秒",(endTime - startTime) / 1000);
			}
		}catch (Exception e){
			log.error("核心流水更新（人行覆盖核心）异常。", e);
		}

	}
	
	public void processCoreData(Boolean isFirst) {
		try {
			dbDataImporter.processData(isFirst);
		} catch (Exception e) {
			log.error("t+1自动处理异常。。。", e);
		}
	}
}
