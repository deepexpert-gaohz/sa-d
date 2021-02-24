package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统基本户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
@Slf4j
public class AmsJibenOpenSyncValidater extends AbstractSyncValidater {

//	@Value("${ams.company.pbc.eccs:true}")
//	private boolean eccsSyncEnabled;

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException, Exception {
		super.validateCustomerInfoCommonOpen(allAcct);

//		if(eccsSyncEnabled) {
//			if (StringUtils.isBlank(allAcct.getRegProvince())) {
//				throw new SyncException("注册地址中“省份”Code不能为空");
//			}
//			if (StringUtils.isBlank(allAcct.getRegCity())) {
//				throw new SyncException("注册地址中“城市”Code不能为空");
//			}
//			if (StringUtils.isBlank(allAcct.getRegArea())) {
//				throw new SyncException("注册地址中“地区”Code不能为空");
//			}
//		}
		/*
		 * if (StringUtils.isBlank(allAcct.getRegProvinceCHName())) { throw new
		 * SyncException("注册地址中“省份”中文名不能为空"); }
		 */
		// 存款人类型
		if (StringUtils.isBlank(allAcct.getDepositorType())) {
			throw new SyncException("存款人类型不能为空");
		} else if (allAcct.getDepositorType().equals("14")) {
			if (allAcct.getDepositorName().indexOf("个体户") < 0) {
				allAcct.setDepositorName("个体户" + allAcct.getDepositorName());
			}
			if (!allAcct.getDepositorName().equals("个体户" + allAcct.getLegalName())) {
				throw new SyncException("当存款人类别为'无字号个体工商户'时,法定代表人或单位负责人姓名与存款人名称要求一致!");
			}
			if (allAcct.getDepositorName().equals("个体户")) {
				throw new SyncException("当存款人类别为'无字号个体工商户'时,存款人名称不能为‘个体户’!");
			}
		}
		// 当存款人类别为企业法人、非企业法人、个体工商户时 国地税获与无需办理税务登记证明必须有个必填
		if (StringUtils.isBlank(allAcct.getNoTaxProve())) {
			String[] arrays = { "01", "02", "13", "14" };
			if (ArrayUtils.contains(arrays, allAcct.getDepositorType())) {
				if (StringUtils.isBlank(allAcct.getStateTaxRegNo()) && StringUtils.isBlank(allAcct.getTaxRegNo())) {
					throw new SyncException("请录入国税登记证号或地税登记证号,或填写无需办理税务登记证的文件或税务机关出具的证明");
				}
				// 经营范围
				if (StringUtils.isBlank(allAcct.getBusinessScope())) {
					throw new SyncException("经营范围不能为空");
				} else if (allAcct.getBusinessScope().length() > 489) {
					throw new SyncException("经营范围超长(978个字符或489个汉字),请重新录入");
				}
			}
		}
		//存款人类别（基本户）校验
		String[] depositorType = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "20" };
		if (StringUtils.isNotBlank(allAcct.getDepositorType())) {
			if (!ArrayUtils.contains(depositorType, allAcct.getDepositorType())) {
				logger.info("基本户存款人类别（基本户）类型当前值：" + allAcct.getDepositorType());
				logger.info("基本户存款人类别（基本户）类型值应为：\"01\", \"02\", \"03\", \"04\", \"05\", \"06\", \"07\", \"08\", \"09\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"20\"");
				throw new SyncException("基本户存款人类别（基本户）类型值不正确");
			}
		}

		//法人类型值判断
		String[] legalType = new String[] { "1", "2"};
		if (StringUtils.isNotBlank(allAcct.getLegalType())) {
			if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
				logger.info("基本户法人类型当前值：" + allAcct.getLegalType());
				logger.info("基本户法人类型值应为：\"1\", \"2\"");
				throw new SyncException("基本户法人类型值不正确");
			}
		}

		//上级法人类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getParLegalType())) {
				logger.info("基本户上级法人类型当前值：" + allAcct.getParLegalType());
				logger.info("基本户上级法人类型值应为：\"1\", \"2\"");
				throw new SyncException("基本户上级法人类型值不正确");
			}
		}
		//法人证件类型值判断
		String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		if (StringUtils.isNotBlank(allAcct.getLegalIdcardTypeAms())) {
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getLegalIdcardTypeAms())) {
				logger.info("基本户法人证件类型当前值：" + allAcct.getLegalIdcardTypeAms());
				logger.info("基本户法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("基本户法人证件类型值不正确");
			}
		}

		//上级法人证件类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalIdcardType())){
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getParLegalIdcardType())) {
				logger.info("基本户上级法人证件类型当前值：" + allAcct.getLegalIdcardTypeAms());
				logger.info("基本户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("基本户上级法人证件类型值不正确");
			}
		}

		//基本户币种类型值判断
		String[] regCurrencyTypeAms = new String[] { "1","2","3","4","5","A","B","C","D","E","F"};
		if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())){
			if (!ArrayUtils.contains(regCurrencyTypeAms, allAcct.getRegCurrencyTypeAms())) {
				logger.info("基本户币种类型当前值：" + allAcct.getRegCurrencyTypeAms());
				logger.info("基本户币种类型值应为：\"1\",\"2\",\"3\",\"4\",\"5\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"");
				throw new SyncException("基本户币种类型值不正确");
			}
		}

		//基本户证明文件1种类类型值判断
		String[] fileType = new String[] { "01", "02", "03", "04"};
		if(StringUtils.isNotBlank(allAcct.getFileType())){
			if (!ArrayUtils.contains(fileType, allAcct.getFileType())) {
				logger.info("基本户证明文件1种类当前值：" + allAcct.getFileType());
				logger.info("基本户证明文件1种类类型值应为：\"01\", \"02\", \"03\", \"04\"");
				throw new SyncException("基本户证明文件1种类类型值不正确");
			}
		}


		//基本户证明文件2种类类型值判断
		String[] fileType2 = new String[] {"02", "03", "04","08"};
		if(StringUtils.isNotBlank(allAcct.getFileType2())){
			if (!ArrayUtils.contains(fileType2, allAcct.getFileType2())) {
				logger.info("基本户证明文件2种类当前值：" + allAcct.getFileType2());
				logger.info("基本户证明文件2种类类型值应为：\"02\", \"03\", \"04\", \"08\"");
				throw new SyncException("基本户证明文件2种类类型值不正确");
			}
		}

		//基本户行业归属类型值判断
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
				logger.info("基本户行业归属类型当前值：" + allAcct.getIndustryCode());
				throw new SyncException("基本户行业归属类型值不正确");
			}
		}

	}
}
