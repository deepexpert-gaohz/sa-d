package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.config.EccsConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.IDCardUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 机构信用代码证系统基本户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class EccsJibenChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException, Exception {
		String[] regTypeArray = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "99" };
		if(StringUtils.isNotBlank(allAcct.getRegType())){
			if (!ArrayUtils.contains(regTypeArray, allAcct.getRegType())) {
				logger.info("机构信用变更工商注册类型值当前值：" + allAcct.getRegType());
				logger.info("机构信用变更工商注册类型值应为确：\"01\", \"02\", \"03\", \"04\", \"05\", \"06\", \"07\", \"08\", \"99\"");
				throw new SyncException("机构信用变更工商注册类型值不正确");
			}
		}

		String[] RegOffice = new String[] { "G","R","M","B","S","W","Z","Q"};
		if(StringUtils.isNotBlank(allAcct.getRegOffice())){
			if (!ArrayUtils.contains(RegOffice, allAcct.getRegOffice())) {
				logger.info("机构信用变更登记部门类型值当前值：" + allAcct.getRegOffice());
				logger.info("机构信用变更登记部门类型值应为确：\"G\",\"R\",\"M\",\"B\",\"S\",\"W\",\"Z\",\"Q\"");
				throw new SyncException("机构信用变更登记部门类型值不正确");
			}
		}

		//法人类型值判断
		String[] legalType = new String[] { "1", "2"};
		if(StringUtils.isNotBlank(allAcct.getLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
				logger.info("机构信用变更基本户法人类型值当前值：" + allAcct.getLegalType());
				logger.info("机构信用变更基本户法人类型值应为：\"1\", \"2\"");
				throw new SyncException("机构信用变更基本户法人类型值不正确");
			}
		}

//		//法人证件类型值判断
//		String[] legalIdcardTypeEccs = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//		if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeEccs())){
//			if (!ArrayUtils.contains(legalIdcardTypeEccs, allAcct.getLegalIdcardTypeEccs())) {
//				logger.info("机构信用变更基本户法人证件类型当前值：" + allAcct.getLegalIdcardTypeEccs());
//				logger.info("机构信用变更基本户法人证件类型值应为确：\"1\", \"2\", \"3\" , \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
//				throw new SyncException("机构信用变更基本户法人证件类型值不正确");
//			}
//		}

		//法人证件类型值判断
		String[] legalIdcardTypeEccs = new String[] { "0","3","X","9","4","2","5","1"};
		if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeEccs())){
			if (!ArrayUtils.contains(legalIdcardTypeEccs, allAcct.getLegalIdcardTypeEccs())) {
				logger.info("机构信用变更基本户法人证件类型当前值：" + allAcct.getLegalIdcardTypeEccs());
				logger.info("机构信用变更基本户法人证件类型值应为确：\"0\",\"3\",\"X\",\"9\",\"4\",\"2\",\"5\",\"1\"");
				throw new SyncException("机构信用变更基本户法人证件类型值不正确");
			}
		}

		//机构状态
		String[] orgStatus = new String[] { "1", "2", "9"};
		if(StringUtils.isNotBlank(allAcct.getOrgStatus())){
			if (!ArrayUtils.contains(orgStatus, allAcct.getOrgStatus())) {
				logger.info("机构信用变更机构状态类型当前值：" + allAcct.getOrgStatus());
				logger.info("机构信用变更机构状态类型值应为确：\"1\", \"2\", \"9\"");
				throw new SyncException("机构信用变更机构状态类型值不正确");
			}
		}

		//基本户状态
		String[] basicAccountStatu = new String[] { "1", "2","3", "9"};
		if(StringUtils.isNotBlank(allAcct.getBasicAccountStatus())){
			if (!ArrayUtils.contains(basicAccountStatu, allAcct.getBasicAccountStatus())) {
				logger.info("机构信用变更基本户状态类型当前值：" + allAcct.getBasicAccountStatus());
				logger.info("机构信用变更机基本户状态类型值应为确：\"1\", \"2\",\"3\", \"9\"");
				throw new SyncException("机构信用变更基本户状态类型值不正确");
			}
		}
		//企业规模
		String[] corpScale = new String[] { "2","3","4","5","9"};
		if(StringUtils.isNotBlank(allAcct.getCorpScale())){
			if (!ArrayUtils.contains(corpScale, allAcct.getCorpScale())) {
				logger.info("机构信用变更企业规模类型当前值：" + allAcct.getCorpScale());
				logger.info("机构信用变更企业规模类型值应为确：\"2\",\"3\",\"4\",\"5\",\"9\"");
				throw new SyncException("机构信用变更企业规模类型值不正确");
			}
		}

		//基本户币种类型值判断
		String[] regCurrencyTypeEccs = new String[] { "AUD","CAD","CNY","EUR","GBP","HKD","JPY","KRW","SGD","USD","XEU"};
		if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeEccs())){
			if (!ArrayUtils.contains(regCurrencyTypeEccs, allAcct.getRegCurrencyTypeEccs())) {
				logger.info("机构信用变更基本户币种类型当前值：" + allAcct.getRegCurrencyTypeEccs());
				logger.info("机构信用变更基本户币种类型值应为：\"AUD\",\"CAD\",\"CNY\",\"EUR\",\"GBP\",\"HKD\",\"JPY\",\"KRW\",\"SGD\",\"USD\",\"XEU\"");
				throw new SyncException("机构信用变更基本户币种类型值不正确");
			}
		}
	}
}
