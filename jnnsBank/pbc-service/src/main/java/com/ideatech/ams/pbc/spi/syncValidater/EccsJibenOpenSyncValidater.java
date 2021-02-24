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
public class EccsJibenOpenSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException, Exception {
		// 企业名称
		if (StringUtils.isBlank(allAcct.getDepositorName())) {
			throw new SyncException("存款人名称不能为空");
		} else if (allAcct.getDepositorName().getBytes("gbk").length > 128) {
			throw new SyncException("存款人名称”长度不能大于128个字符（64个汉字）！");
		}
		// 工商注册类型
		if (StringUtils.isBlank(allAcct.getRegType()) || StringUtils.isBlank(allAcct.getRegNo())) {
			throw new SyncException("工商注册类型与编号不能为空");
		}
		String[] regTypeArray = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "99" };

		if(StringUtils.isNotBlank(allAcct.getRegType())){
            if (!ArrayUtils.contains(regTypeArray, allAcct.getRegType())) {
                logger.info("工商注册类型当前值：" + allAcct.getRegType());
                logger.info("工商注册类型值应为：\"01\", \"02\", \"03\", \"04\", \"05\", \"06\", \"07\", \"08\", \"99\"");
                throw new SyncException("工商注册类型值不正确");
			}
		}

		// 省市区 地址
		if (StringUtils.isBlank(allAcct.getRegProvince())) {
			throw new SyncException("注册（登记）地址的“省份”必选！");
		}
		if (StringUtils.isBlank(allAcct.getRegCity())) {
			throw new SyncException("注册（登记）地址的“市”必选！");
		}
		if (StringUtils.isBlank(allAcct.getRegArea())) {
			throw new SyncException("注册（登记）地址的“区/县”必选！");
		}
		if (StringUtils.isBlank(allAcct.getRegAddress())) {
			throw new SyncException("注册（登记）地址的“详细地址”必填！");
		} else if (allAcct.getRegAddress().getBytes("gbk").length > 128) {
			throw new SyncException("注册（登记）地址的“详细地址”长度不能大于128个字符（64个汉字）！");
		}
		// 登记部门
		if (StringUtils.isBlank(allAcct.getRegOffice())) {
			throw new SyncException("“登记部门”必选");
		}

		String[] RegOffice = new String[] { "G","R","M","B","S","W","Z","Q"};
		if(StringUtils.isNotBlank(allAcct.getRegOffice())){
			if (!ArrayUtils.contains(RegOffice, allAcct.getRegOffice())) {
				throw new SyncException("登记部门类型值不正确");
			}
		}
		// 证件到日期与证件成立日
		if (StringUtils.isNotEmpty(allAcct.getSetupDate()) && StringUtils.isNotEmpty(allAcct.getTovoidDate())) {
			if (DateUtils.parse(allAcct.getTovoidDate(), "yyyy-MM-dd").getTime() <= DateUtils.parse(allAcct.getSetupDate(), "yyyy-MM-dd").getTime()) {
				throw new SyncException("“证书到期日”必须大于“成立日期”！");
			}
		}
		// 经营范围
		if (StringUtils.isNotEmpty(allAcct.getBusinessScopeEccs())) {
//			if (StringUtils.isBlank(allAcct.getBusinessScopeEccs())) {
//				allAcct.setBusinessScopeEccs(allAcct.getBusinessScopeEccs());
//			}
			if (allAcct.getBusinessScopeEccs().getBytes("gbk").length > 400) {
				throw new SyncException("“信用代码证经营（业务）范围”的长度不能大于400个字符（200个汉字）！");
			}
		}
		// 注册资金 与注册币种
		if ((StringUtils.isBlank(allAcct.getRegCurrencyTypeEccs()) && StringUtils.isNotEmpty(allAcct.getRegisteredCapital()))
				|| (StringUtils.isNotEmpty(allAcct.getRegCurrencyTypeEccs()) && StringUtils.isBlank(allAcct.getRegisteredCapital()))) {
			throw new SyncException("“注册币种”和“注册资本”必须同时填写！");
		}
		//基本户币种类型值判断
		String[] regCurrencyTypeAms = new String[] { "AUD", "CAD", "CNY", "EUR", "GBP", "HKD", "JPY", "KRW", "SGD", "USD", "XEU"};
		if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeEccs())){
			if (!ArrayUtils.contains(regCurrencyTypeAms, allAcct.getRegCurrencyTypeEccs())) {
				logger.info("基本户币种类型当前值：" + allAcct.getRegCurrencyTypeEccs());
				logger.info("基本户币种类型值应为：\"AUD\", \"CAD\", \"CNY\", \"EUR\", \"GBP\", \"HKD\", \"JPY\", \"KRW\", \"SGD\", \"USD\", \"XEU\"");
				throw new SyncException("基本户币种类型值不正确");
			}
		}
		// 组织机构类别与细分
		if (StringUtils.isBlank(allAcct.getOrgType()) || StringUtils.isBlank(allAcct.getOrgTypeDetail())) {
			throw new SyncException("“组织机构类别”及“细分”必选！");
		}
		// 若登记注册号类型为统一社会信用代码\商业与非商业登记证号则无需判读
		if (StringUtils.isNotBlank(allAcct.getRegType()) && !allAcct.getRegType().equals("07") && !allAcct.getRegType().equals("08")) {
			if (allAcct.getOrgType().equals("1")) {// 组织机构类别选择”企业“
				if (!(StringUtils.isNotEmpty(allAcct.getOrgCode()) && StringUtils.isNotEmpty(allAcct.getRegNo()) && (StringUtils.isNotEmpty(allAcct.getStateTaxRegNo()) || StringUtils
						.isNotEmpty(allAcct.getTaxRegNo())))) {
					throw new SyncException("“组织机构类别”选择了“企业”，必须填写“工商注册号”、“组织机构代码”、“纳税人识别号（国税或地税）”！");
				}
			} else if (allAcct.getOrgType().equals("2")) {
				if (!(StringUtils.isNotEmpty(allAcct.getRegNo()) && StringUtils.isNotEmpty(allAcct.getOrgCode()))) {
					throw new SyncException("“组织机构类别”选择了“事业单位”，必须填写“事业单位登记号”、“组织机构代码”！");
				}
			} else if (allAcct.getOrgType().equals("3")) {
				if (StringUtils.isBlank(allAcct.getRegNo())) {
					throw new SyncException("“组织机构类别”选择了“机关”，必须填写“机关登记号");
				}
			} else if (allAcct.getOrgType().equals("4")) {
				if (StringUtils.isBlank(allAcct.getRegNo())) {
					throw new SyncException("“组织机构类别”选择了“社会团体”，必须填写“社会团体登记号”");
				}
			} else if (allAcct.getOrgType().equals("7")) {
				if (StringUtils.isBlank(allAcct.getRegNo()) || (StringUtils.isBlank(allAcct.getStateTaxRegNo()) && StringUtils.isBlank(allAcct.getTaxRegNo()))) {
					throw new SyncException("“组织机构类别”选择了“个体工商户”，必须填写“工商注册号”、“纳税人识别号（国税或地税）”！");
				}
			}
		}
		/* 法人信息 */
		if (StringUtils.isBlank(allAcct.getLegalName())) {
			throw new SyncException("“法定代表人（负责人）姓名”必填！");
		} else if (allAcct.getLegalName().getBytes("gbk").length > 30) {
			throw new SyncException("“法定代表人（负责人）名称”长度不能大于30个字符（15个汉字）！");
		}
		if (StringUtils.isBlank(allAcct.getLegalIdcardTypeEccs())) {
			throw new SyncException("“法定代表人（负责人）证件类型”必选！");
		}
		if (StringUtils.isBlank(allAcct.getLegalIdcardNo())) {
			throw new SyncException("“法定代表人（负责人）证件号码”必填！");
		} else if (allAcct.getLegalIdcardNo().getBytes("gbk").length > 20) {
			throw new SyncException("“法定代表人（负责人）证件号码”长度不能大于20个字符！");
		}
		if (allAcct.getLegalIdcardTypeEccs().equals("0")) {
			if (StringUtils.isNotEmpty(IDCardUtils.IDCardValidate(allAcct.getLegalIdcardNo()))) {
				throw new SyncException("法人身份证无效，不是合法的身份证号码！");
			}
		}


		//法人类型值判断
		String[] legalType = new String[] { "1", "2"};
		if(StringUtils.isNotBlank(allAcct.getLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
				logger.info("基本户法人类型值当前值：" + allAcct.getLegalType());
				logger.info("基本户法人类型值应为：\"1\", \"2\"");
				throw new SyncException("基本户法人类型值不正确");
			}
		}

		//法人证件类型值判断
		String[] legalIdcardTypeEccs = new String[] { "0","3","X","9","4","2","5","1"};
		if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeEccs())){
			if (!ArrayUtils.contains(legalIdcardTypeEccs, allAcct.getLegalIdcardTypeEccs())) {
				logger.info("基本户法人证件类型当前值：" + allAcct.getLegalIdcardTypeEccs());
				logger.info("基本户法人证件类型值应为确：\"0\",\"3\",\"X\",\"9\",\"4\",\"2\",\"5\",\"1\"");
				throw new SyncException("基本户法人证件类型值不正确");
			}
		}

		// 机构状态
		if (StringUtils.isBlank(allAcct.getOrgStatus())) {
			throw new SyncException("“机构状态”必选！");
		}
		//机构状态
		String[] orgStatus = new String[] { "1", "2", "9"};
		if(StringUtils.isNotBlank(allAcct.getOrgStatus())){
			if (!ArrayUtils.contains(orgStatus, allAcct.getOrgStatus())) {
				logger.info("机构状态类型当前值：" + allAcct.getOrgStatus());
				logger.info("机构状态类型值应为确：\"1\", \"2\", \"9\"");
				throw new SyncException("机构状态类型值不正确");
			}
		}


		// 基本户状态
		if (StringUtils.isBlank(allAcct.getBasicAccountStatus())) {
			throw new SyncException("“基本户状态”必选！");
		}
		//基本户状态
		String[] basicAccountStatu = new String[] { "1", "2","3", "9"};
		if(StringUtils.isNotBlank(allAcct.getBasicAccountStatus())){
			if (!ArrayUtils.contains(basicAccountStatu, allAcct.getBasicAccountStatus())) {
				logger.info("基本户状态类型当前值：" + allAcct.getBasicAccountStatus());
				logger.info("机基本户状态类型值应为确：\"1\", \"2\",\"3\", \"9\"");
				throw new SyncException("基本户状态类型值不正确");
			}
		}
		// 办公地址
		if ((StringUtils.isBlank(allAcct.getWorkProvince()) || StringUtils.isBlank(allAcct.getWorkCity()) || StringUtils.isBlank(allAcct.getWorkArea()) || StringUtils
				.isBlank(allAcct.getWorkAddress()))
				&& (StringUtils.isNotEmpty(allAcct.getWorkCity()) || StringUtils.isNotEmpty(allAcct.getWorkArea()) || StringUtils.isNotEmpty(allAcct.getWorkProvince())
						|| StringUtils.isNotEmpty(allAcct.getWorkAddress()))) {
			throw new SyncException("“办公（生产、经营）地址”必须填写完整！（包含市区字段）");
		}
		if (StringUtils.isNotEmpty(allAcct.getWorkAddress())) {
			if (allAcct.getWorkAddress().getBytes(EccsConfig.ENCODING).length > 128) {
				throw new SyncException("“办公（生产、经营）地址”的“地址”长度不能大于128字符（64个汉字）！");
			}
		}
		// 联系电话
		if (StringUtils.isNotEmpty(allAcct.getTelephone())) {
			if (allAcct.getTelephone().getBytes("gbk").length > 35) {
				throw new SyncException("“联系电话”长度不能大于35字符！");
			}
		}
		// 财务部联系电话
		if (StringUtils.isNotEmpty(allAcct.getFinanceTelephone())) {
			if (allAcct.getFinanceTelephone().getBytes("gbk").length > 35) {
				throw new SyncException("“财务部联系电话”长度不能大于35字符！");
			}
		}
		//企业规模
		String[] corpScale = new String[] { "2","3","4","5","9"};
		if(StringUtils.isNotBlank(allAcct.getCorpScale())){
			if (!ArrayUtils.contains(corpScale, allAcct.getCorpScale())) {
				logger.info("企业规模类型当前值：" + allAcct.getCorpScale());
				logger.info("企业规模类型值应为确：\"2\",\"3\",\"4\",\"5\",\"9\"");
				throw new SyncException("企业规模类型值不正确");
			}
		}

		//基本户币种类型值判断
		String[] regCurrencyTypeEccs = new String[] { "AUD","CAD","CNY","EUR","GBP","HKD","JPY","KRW","SGD","USD","XEU"};
		if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeEccs())){
			if (!ArrayUtils.contains(regCurrencyTypeEccs, allAcct.getRegCurrencyTypeEccs())) {
				logger.info("信用代码证基本户币种类型当前值：" + allAcct.getRegCurrencyTypeEccs());
				logger.info("信用代码证基本户币种类型值应为：\"AUD\",\"CAD\",\"CNY\",\"EUR\",\"GBP\",\"HKD\",\"JPY\",\"KRW\",\"SGD\",\"USD\",\"XEU\"");
				throw new SyncException("信用代码证基本户币种类型值不正确");
			}
		}
	}
}
