package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 机构信用代码证基本户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class EccsJibenOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			/**
			 * idBean.registerType 登记注册号类型
			 */
			if (StringUtils.isNotEmpty(allAcct.getRegType()) && StringUtils.isNotEmpty(allAcct.getRegNo())) {
				if (allAcct.getRegType().equals("01") && allAcct.getRegNo().substring(0, 1).equals("9") && allAcct.getRegNo().length() == 18) {
					allAcct.setRegType("07");
				}
			}
			/**
			 * 判断Code代码
			 */
			urlPars.append("&crccode=???");
			/**
			 * idBean.sdepNameC 机构中文名称 idBean.sdepNameE机构英文名称
			 * idBean.sdepOrgCode组织机构代码
			 */
			urlPars.append("&idBean.sdepnamec=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), eccsChart));
			urlPars.append("&idBean.sdepnamee=").append(allAcct.getOrgEnName());
			/**
			 * country国家
			 */
			urlPars.append("&country=CHN");
			/**
			 * provCode省份
			 */
			urlPars.append("&provCode=").append(allAcct.getRegProvince().substring(0, 2));
			/**
			 * cityCode城市
			 */

			urlPars.append("&cityCode=").append(allAcct.getRegCity().substring(0, 4));
			/**
			 * countyCode 地区
			 */
			urlPars.append("&countyCode=").append(allAcct.getRegArea());
			/**
			 * 详细地址
			 */
			urlPars.append("&areaaddr=");
			if (StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegProvinceCHName())) {
				allAcct.setRegAddress(StringUtils.replace(allAcct.getRegAddress(), allAcct.getRegProvinceCHName(), ""));
			}
			if (StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegCityCHName())) {
				allAcct.setRegAddress(StringUtils.replace(allAcct.getRegAddress(), allAcct.getRegCityCHName(), ""));
			}
			if (StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegAreaCHName())) {
				allAcct.setRegAddress(StringUtils.replace(allAcct.getRegAddress(), allAcct.getRegAreaCHName(), ""));
			}
			urlPars.append(EncodeUtils.encodStr(allAcct.getRegAddress(), eccsChart));
			/**
			 * idBean.departKind 登记部门
			 */
			urlPars.append("&idBean.departkind=").append(allAcct.getRegOffice());
			/**
			 * buildDate成立日期
			 */
			urlPars.append("&builddate=").append(allAcct.getSetupDate());
			/**
			 * maturityDate证书到期日
			 */
			urlPars.append("&maturitydate=").append(allAcct.getTovoidDate());
			/**
			 * sdepManagerName法定代表人名称 managerList[0].managertype=5新增隐藏域
			 */
			urlPars.append("&managerList[0].managertype=5");
			urlPars.append("&managerList[0].managername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), eccsChart));
			/**
			 * sdepManagerCreKind证件类型
			 */
			urlPars.append("&managerList[0].certifitype=").append(allAcct.getLegalIdcardTypeEccs());
			/**
			 * sdepManagerCreCode证件号码
			 */
			urlPars.append("&managerList[0].certificode=").append(allAcct.getLegalIdcardNo());
			/*
			 * 注册资金与注册币种
			 */
			String regCurrencyType = allAcct.getRegCurrencyTypeEccs();
			String regCurrentFund = allAcct.getRegisteredCapital();
			// 注册币种
			if (StringUtils.isEmpty(regCurrencyType)) {
				regCurrencyType = "";
			}
			// 注册资金
			if (StringUtils.isEmpty(regCurrentFund)) {
				regCurrentFund = "";
			}
			/**
			 * sdepFundKind注册资本币种
			 */
			urlPars.append("&sdepfundkind=").append(regCurrencyType);
			/**
			 * sdepFund注册资本(万元)
			 */
			//转换为万元
			regCurrentFund = PbcBussUtils.getEccsRegisteredCapital(regCurrentFund);
			urlPars.append("&sdepfund=").append(regCurrentFund);

			// 经营范围 不能出现 半角 , ;
			if (StringUtils.isNotEmpty(allAcct.getBusinessScopeEccs())) {
				String sdepWork = allAcct.getBusinessScopeEccs();
				sdepWork = sdepWork.replace(",", "，").replace(";", "；").replace("*", "×");
				allAcct.setBusinessScopeEccs(sdepWork);
			}
			/**
			 * sdepWork经营（业务）范围
			 */
			urlPars.append("&sdepwork=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScopeEccs(), eccsChart));
			/**
			 * idBean.orgKind 组织机构类别
			 */
			urlPars.append("&idBean.orgkind=").append(allAcct.getOrgType());
			/**
			 * orgkindsub 组织机构类别细分
			 */
			urlPars.append("&orgkindsub=").append(allAcct.getOrgTypeDetail());
			/**
			 * vocationName经济行业分类 显示值
			 */
			urlPars.append("&vocationName=").append(EncodeUtils.encodStr(allAcct.getEconomyIndustryName(), eccsChart));
			/**
			 * vocationCode经济行业分类 值
			 */
			urlPars.append("&vocationCode=").append(allAcct.getEconomyIndustryCode());
			/**
			 * economyType经济类型
			 */
			urlPars.append("&economytype=").append(allAcct.getEconomyType());
			/**
			 * vocationAmount企业规模
			 */
			urlPars.append("&vocationamount=").append(allAcct.getCorpScale());
			/**
			 * orgState机构状态
			 */
			urlPars.append("&orgstate=").append(allAcct.getOrgStatus());
			/**
			 * state基本户状态
			 */
			urlPars.append("&state=").append(allAcct.getBasicAccountStatus());
			/**
			 * upOrgName 上级机构名称
			 */
			urlPars.append("&uporgname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), eccsChart));
			/**
			 * upCreditCode 上级机构信用代码
			 */
			urlPars.append("&upcreditcode=").append(allAcct.getParOrgEccsNo());
			/**
			 * upSdepOrgCode 上级组织机构代码
			 */

			StringBuffer parOrgCodeStr = new StringBuffer();
			if (StringUtils.isEmpty(allAcct.getParOrgCode())) {
				urlPars.append("&upsdeporgcode=");
			} else if (!allAcct.getParOrgCode().contains("-")) {
				parOrgCodeStr.append(allAcct.getParOrgCode().substring(0, allAcct.getParOrgCode().length() - 1));
				parOrgCodeStr.append("-");
				parOrgCodeStr.append(allAcct.getParOrgCode().substring(allAcct.getParOrgCode().length() - 1));
				urlPars.append("&upsdeporgcode=").append(parOrgCodeStr);
			} else {
				urlPars.append("&upsdeporgcode=").append(allAcct.getParOrgCode());
			}
			/**
			 * upSaccFileType上级登记注册号类型
			 */
			urlPars.append("&upsaccfiletype=").append(allAcct.getParRegType());
			/**
			 * upSaccFileCode上级登记注册号码
			 */
			urlPars.append("&upsaccfilecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParRegNo(), eccsChart));
			/*
			 * 办公地址
			 */
			/**
			 * provCodeW办公（生产、经营）地址 省份
			 */
			if (StringUtils.isEmpty(allAcct.getWorkProvince())) {
				urlPars.append("&provCodeW=0");
			} else {
				urlPars.append("&provCodeW=").append(allAcct.getWorkProvince().substring(0, 2));
			}
			/**
			 * cityCodeW办公（生产、经营）地址 城市
			 */
			if (StringUtils.isEmpty(allAcct.getWorkCity())) {
				urlPars.append("&cityCodeW=0");
			} else {
				urlPars.append("&cityCodeW=").append(allAcct.getWorkCity().substring(0, 4));
			}
			/**
			 * countyCodeW办公（生产、经营）地址 地区
			 */
			if (StringUtils.isEmpty(allAcct.getWorkArea())) {
				urlPars.append("&countyCodeW=0");
			} else {
				urlPars.append("&countyCodeW=").append(allAcct.getWorkArea());
			}
			/**
			 * workAddress 办公详细地址
			 */
			urlPars.append("&workaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getWorkAddress(), eccsChart));
			/**
			 * workTelephone联系电话
			 */
			urlPars.append("&worktelephone=").append(allAcct.getTelephone());
			/**
			 * financeTelephone财务部联系电话
			 */
			urlPars.append("&financetelephone=").append(allAcct.getFinanceTelephone());
			/*
			 * 高管及主要关系人信息 TODO
			 */
			/**
			 * 董事长 姓名、证件类型、证件号码
			 */
			urlPars.append("&managerList[1].managertype=1");
			urlPars.append("&managerList[1].managername=").append("");
			urlPars.append("&managerList[1].certifitype=").append("");
			urlPars.append("&managerList[1].certificode=").append("");
			/**
			 * 总经理/主要负责人 姓名、证件类型、证件号码
			 */
			urlPars.append("&managerList[2].managertype=2");
			urlPars.append("&managerList[2].managername=").append("");
			urlPars.append("&managerList[2].certifitype=").append("");
			urlPars.append("&managerList[2].certificode=").append("");
			/**
			 * 财务负责人 姓名、证件类型、证件号码
			 */
			urlPars.append("&managerList[3].managertype=3");
			urlPars.append("&managerList[3].managername=").append("");
			urlPars.append("&managerList[3].certifitype=").append("");
			urlPars.append("&managerList[3].certificode=").append("");
			/**
			 * 监事长 姓名、证件类型、证件号码
			 */
			urlPars.append("&managerList[4].managertype=4");
			urlPars.append("&managerList[4].managername=").append("");
			urlPars.append("&managerList[4].certifitype=").append("");
			urlPars.append("&managerList[4].certificode=").append("");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "信用代码证新开户参数拼接失败", e);
			throw new SyncException(getEccsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
