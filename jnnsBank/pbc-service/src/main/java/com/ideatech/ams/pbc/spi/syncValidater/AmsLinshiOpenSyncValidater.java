package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统临时户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsLinshiOpenSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException, Exception {
		super.validateCustomerInfoCommonOpen(allAcct);
		super.validateEffectiveDate(allAcct);

			//法人类型值判断
			String[] legalType = new String[] { "1", "2"};
			if(StringUtils.isNotBlank(allAcct.getLegalType())){
				if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
					logger.info("临时户法人类型当前值：" + allAcct.getLegalType());
					logger.info("临时户法人类型值应为：\"1\", \"2\"");
					throw new SyncException("临时户法人类型值不正确");
				}
			}

			//上级法人类型值判断
			if(StringUtils.isNotBlank(allAcct.getParLegalType())){
				if (!ArrayUtils.contains(legalType, allAcct.getParLegalType())) {
					logger.info("临时户上级法人类型当前值：" + allAcct.getLegalType());
					logger.info("临时户上级法人类型值应为：\"1\", \"2\"");
					throw new SyncException("临时户上级法人类型值不正确");
				}
			}
			//法人证件类型值判断
			String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
			if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeAms())){
				if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getLegalIdcardTypeAms())) {
					logger.info("临时户法人证件类型当前值：" + allAcct.getLegalIdcardTypeAms());
					logger.info("临时户法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
					throw new SyncException("临时户法人证件类型值不正确");
				}
			}

			//上级法人证件类型值判断
			if(StringUtils.isNotBlank(allAcct.getParLegalIdcardType())){
				if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getParLegalIdcardType())) {
					logger.info("临时户上级法人证件类型当前值：" + allAcct.getParLegalIdcardType());
					logger.info("临时户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
					throw new SyncException("临时户上级法人证件类型值不正确");
				}
			}

			//证明文件种类1类型值判断
			String[] fileType = new String[] { "01", "09"};
			if(StringUtils.isNotBlank(allAcct.getFileType())){
				if (!ArrayUtils.contains(fileType, allAcct.getFileType())) {
					logger.info("临时户证明文件种类1证件类型当前值：" + allAcct.getParLegalIdcardType());
					logger.info("临时户证明文件种类1证件类型值应为：\"01\", \"09\"");
					throw new SyncException("临时户证明文件种类1证件类型值不正确");
				}
			}

			//临时户币种类型值判断
			String[] regCurrencyTypeAms = new String[] { "1","2","3","4","5","A","B","C","D","E","F"};
			if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())){
				if (!ArrayUtils.contains(regCurrencyTypeAms, allAcct.getRegCurrencyTypeAms())) {
					logger.info("临时户币种类型当前值：" + allAcct.getRegCurrencyTypeAms());
					logger.info("临时户币种类型值应为：\"1\",\"2\",\"3\",\"4\",\"5\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"");
					throw new SyncException("临时户币种类型值不正确");
				}
			}

			//临时户行业归属类型值判断
			String[] industryCode = new String[] {
					"A$$农、林、牧、渔业$$第一产业            $$1",
					"B$$采矿业$$第二产业            $$2",
					"C$$制造业$$第二产业            $$2",
					"D$$电力、煤气及水的生产和供应业$$第二产业            $$2",
					"E$$建筑业$$第二产业            $$2",
					"F$$交通运输、仓储及邮政业$$第一层次            $$3",
					"G$$信息传输、计算机服务和软件业$$第二层次            $$4",
					"H$$批发和零售业$$第二层次            $$4",
					"I$$住宿和餐饮业$$第二层次            $$4",
					"J$$金融业$$第二层次            $$4",
					"K$$房地产业$$第二层次            $$4",
					"L$$租赁和商务服务业$$第二层次            $$4",
					"M$$科学研究、技术服务和地质勘查业$$第三层次            $$5",
					"N$$水利、环境和公共设施管理业$$第三层次            $$5",
					"O$$居民服务和其他服务业$$第三层次            $$5",
					"P$$教育$$第三层次            $$5",
					"Q$$卫生、社会保障和社会福利业$$第三层次            $$5",
					"R$$文化、体育和娱乐业$$第三层次            $$5",
					"S$$公共管理和社会组织$$第四层次            $$6",
					"T$$国际组织（其他行业）$$第四层次            $$6",
					"U$$其他$$第一层次            $$3"
			};
			if(StringUtils.isNotBlank(allAcct.getIndustryCode())){
				if (!ArrayUtils.contains(industryCode, allAcct.getIndustryCode())) {
					throw new SyncException("临时户行业归属类型值不正确");
				}
			}
	}
}
