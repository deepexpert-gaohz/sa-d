package com.ideatech.ams.pbc.service.eccs;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EccsChangeAccountParamServiceImpl implements EccsChangeAccountParamService {

	public Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public StringBuffer getChangeXydmzUrl(EccsAccountInfo model, AllAcct allAcct) throws SyncException {
		String updateTime = DateUtils.getNowDateShort();
		StringBuffer urlPars = new StringBuffer();
		if (StringUtils.isNotBlank(allAcct.getFileType())) {
			allAcct.setRegType(allAcct.getFileType());
		}
		if (StringUtils.isNotEmpty(allAcct.getFileNo())) {
			allAcct.setRegNo(allAcct.getFileNo());
		}
		try {
			/**
			 * idBean.sdepnamec 机构中文名称
			 */
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("idBean.sdepnamec&idBean.sdepnamecdatafrom&idBean.sdepnamecuptime", "utf-8"));
			urlPars.append("&idBean.sdepnamec=");
			if (StringUtils.isNotEmpty(allAcct.getDepositorName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), "utf-8"));
				urlPars.append("&idBean.sdepnamecdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&idBean.sdepnamecuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getDepositorName(), "utf-8"));
				urlPars.append("&idBean.sdepnamecdatafrom=");
				urlPars.append(model.getDepositorNameInfoSourceCode());
				urlPars.append("&idBean.sdepnamecuptime=");
				urlPars.append(model.getDepositorNameUpdateTime());
			}
			/**
			 * idBean.sdepNameE机构英文名称
			 */
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("idBean.sdepnamee&idBean.sdepnameedatafrom&idBean.sdepnameeuptime", "utf-8"));
			urlPars.append("&idBean.sdepnamee=");
			if (StringUtils.isNotEmpty(allAcct.getOrgEnName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgEnName(), "utf-8"));
				urlPars.append("&idBean.sdepnameedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&idBean.sdepnameeuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgEnName(), "utf-8"));
				urlPars.append("&idBean.sdepnameedatafrom=");
				urlPars.append(model.getOrgEnNameInfoSourceCode());
				urlPars.append("&idBean.sdepnameeuptime=");
				urlPars.append(model.getOrgEnNameUpdateTime());
			}
			/**
			 * idBean.sdepOrgCode组织机构代码
			 */
			urlPars.append("&idBean.sdeporgcode=");
			if (StringUtils.isNotEmpty(allAcct.getOrgCode())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgCode(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgCode(), "utf-8"));
			}

			// /**
			// * idBean.loanCardCode贷款卡编码
			// */
			// urlPars.append("&idBean.loancardcode=");
			// if (StringUtils.isNotEmpty(allAcct.getBankCardNo())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getBankCardNo(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getBankCardNo(),
			// "utf-8"));
			// }
			/**
			 * idBean.saccBaseLicNo开户许可证核准号
			 */
			urlPars.append("&idBean.saccbaselicno=");
			if (StringUtils.isNotEmpty(allAcct.getAccountKey())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getAccountKey(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getAccountKey(), "utf-8"));
			}
			/**
			 * idBean.registerType登记注册号类型
			 */
			urlPars.append("&idBean.registertype=");
			if (StringUtils.isNotEmpty(allAcct.getRegType())) {
				if (StringUtils.isNotEmpty(allAcct.getRegNo()) && allAcct.getRegNo().substring(0, 1).equals("9") && allAcct.getRegNo().length() >= 18) {
					urlPars.append(EncodeUtils.encodStr("07", "utf-8"));// 若为统一社会信用代码证
																		// 则证件类型为统一社会信用代码
				} else {
					urlPars.append(EncodeUtils.encodStr(allAcct.getRegType(), "utf-8"));
				}
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegType(), "utf-8"));
			}
			/**
			 * idBean.registerCode登记注册号码
			 */
			urlPars.append("&idBean.registercode=");
			if (StringUtils.isNotEmpty(allAcct.getRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegNo(), "utf-8"));
			}
			/**
			 * idBean.sdepNationaltaxCode纳税人识别号（国税）
			 */
			urlPars.append("&idBean.sdepnationaltaxcode=");
			if (StringUtils.isNotEmpty(allAcct.getStateTaxRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getStateTaxRegNo(), "utf-8"));
			}
			/**
			 * idBean.sdepLandtaxCode纳税人识别号（地税）
			 */
			urlPars.append("&idBean.sdeplandtaxcode=");
			if (StringUtils.isNotEmpty(allAcct.getTaxRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getTaxRegNo(), "utf-8"));
			}
			/*
			 * 注册地址（省市区等）
			 */
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("country&provCode&cityCode&countyCode&areaaddr&areaaddrdatafrom&areaaddruptime", "utf-8"));
			if (StringUtils.isEmpty(model.getProvCode())) {
				model.setProvCode("0");
			}
			if (StringUtils.isEmpty(model.getCityCode())) {
				model.setCityCode("0");
			}
			if (StringUtils.isEmpty(model.getCountyCode())) {
				model.setCountyCode("0");
			}
			/**
			 * country国家
			 */
			urlPars.append("&country=CHN");
			/**
			 * provCode省份
			 */
			urlPars.append("&provCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegProvince())) {
				urlPars.append(allAcct.getRegProvince().substring(0, 2));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getProvCode(), "utf-8"));
			}
			/**
			 * cityCode城市
			 */
			urlPars.append("&cityCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegCity())) {
				urlPars.append(allAcct.getRegCity().substring(0, 4));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCityCode(), "utf-8"));
			}
			/**
			 * countyCode 地区
			 */
			urlPars.append("&countyCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegArea())) {
				urlPars.append(allAcct.getRegArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCountyCode(), "utf-8"));
			}
			/**
			 * 详细地址
			 */
			urlPars.append("&areaaddr=");
			if (StringUtils.isNotEmpty(allAcct.getRegAddress())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegAddress(), "utf-8"));
				urlPars.append("&areaaddrdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&areaaddruptime=");
				urlPars.append(updateTime);

			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegAddress(), "utf-8"));
				urlPars.append("&areaaddrdatafrom=");
				urlPars.append(model.getRegAddressInfoSourceCode());
				urlPars.append("&areaaddruptime=");
				urlPars.append(model.getRegAddressUpdateTime());
			}
			// idBean.departKind 登记部门
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("idBean.departkind&idBean.departkinddatafrom&idBean.departkinduptime", "utf-8"));
			urlPars.append("&idBean.departkind=");
			if (StringUtils.isNotEmpty(allAcct.getRegOffice())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegOffice(), "utf-8"));
				urlPars.append("&idBean.departkinddatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&idBean.departkinduptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegOffice(), "utf-8"));
				urlPars.append("&idBean.departkinddatafrom=");
				urlPars.append(model.getRegOfficeInfoSourceCode());
				urlPars.append("&idBean.departkinduptime=");
				urlPars.append(model.getRegOfficeUpdateTime());
			}
			// buildDate成立日期
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("builddate&builddatedatafrom&builddateuptime", "utf-8"));
			urlPars.append("&builddate=");
			if (StringUtils.isNotEmpty(allAcct.getSetupDate())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getSetupDate(), "utf-8"));
				urlPars.append("&builddatedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&builddateuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getSetupDate(), "utf-8"));
				urlPars.append("&builddatedatafrom=");
				urlPars.append(model.getSetupDateInfoSourceCode());
				urlPars.append("&builddateuptime=");
				urlPars.append(model.getSetupDateUpdateTime());
			}
			// maturityDate证书到期日
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("maturitydate&maturitydatedatafrom&maturitydateuptime", "utf-8"));
			urlPars.append("&maturitydate=");
			if (StringUtils.isNotEmpty(allAcct.getTovoidDate())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTovoidDate(), "utf-8"));
				urlPars.append("&maturitydatedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&maturitydateuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getEffectiveDate(), "utf-8"));
				urlPars.append("&maturitydatedatafrom=");
				urlPars.append(model.getEffectiveDateInfoSourceCode());
				urlPars.append("&maturitydateuptime=");
				urlPars.append(model.getEffectiveDateUpdateTime());
			}
			// sdepManagerName法定代表人名称
			urlPars.append("&managerList[0].managertype=5");
			urlPars.append("&controlList[0].controlid").append(model.getControlid());
			urlPars.append("&managerList[0].managername=");
			if (StringUtils.isNotEmpty(allAcct.getLegalName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalName(), "utf-8"));
			}
			// sdepManagerCreKind证件类型
			urlPars.append("&managerList[0].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getLegalIdcardTypeEccs())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardTypeEccs(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalIdcardType(), "utf-8"));
			}
			// sdepManagerCreCode证件号码
			urlPars.append("&managerList[0].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getLegalIdcardNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalIdcardNo(), "utf-8"));
			}
			// sdepManager信息来源和信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getLegalName()) || StringUtils.isNotEmpty(allAcct.getLegalIdcardTypeEccs()) || StringUtils.isNotEmpty(allAcct.getLegalIdcardNo())) {
				urlPars.append("&managerList[0].inte_manager_datafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&managerList[0].uploadtimef=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&managerList[0].inte_manager_datafrom=");
				urlPars.append(model.getLegalInfoSourceCode());
				urlPars.append("&managerList[0].uploadtimef=");
				urlPars.append(model.getLegalUpdateTime());
			}
			// sdepFundKind注册资本币种
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("sdepfundkind&sdepfund&sdepfunddatafrom&sdepfunduptime", "utf-8"));
			urlPars.append("&sdepfundkind=");
			if (StringUtils.isNotEmpty(allAcct.getRegCurrencyTypeEccs())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeEccs(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegCurrencyType(), "utf-8"));
			}
			// sdepFund注册资本(万元)
			urlPars.append("&sdepfund=");
			if (StringUtils.isNotEmpty(allAcct.getRegisteredCapital())) {
				urlPars.append(EncodeUtils.encodStr(PbcBussUtils.getEccsRegisteredCapital(allAcct.getRegisteredCapital()), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegisteredCapital(), "utf-8"));
			}
			// sdepFund信息来源和信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getRegCurrencyTypeEccs()) || StringUtils.isNotEmpty(allAcct.getRegisteredCapital())) {
				urlPars.append("&sdepfunddatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&sdepfunduptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&sdepfunddatafrom=");
				urlPars.append(model.getRegisteredCapitalInfoSourceCode());
				urlPars.append("&sdepfunduptime=");
				urlPars.append(model.getRegisteredCapitalUpdateTime());
			}
			// sdepWork经营（业务）范围
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("sdepwork&sdepworkdatafrom&sdepworkuptime", "utf-8"));
			urlPars.append("&sdepwork=");
			if (StringUtils.isNotEmpty(allAcct.getBusinessScopeEccs())) {
				String sdepWork = allAcct.getBusinessScopeEccs();
				sdepWork = sdepWork.replace(",", "，").replace(";", "；").replace("*", "×");
				allAcct.setBusinessScopeEccs(sdepWork);
				urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScopeEccs(), "utf-8"));
				urlPars.append("&sdepworkdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&sdepworkuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getBusinessScope(), "utf-8"));
				urlPars.append("&sdepworkdatafrom=");
				urlPars.append(model.getBusinessScopeInfoSourceCode());
				urlPars.append("&sdepworkuptime=");
				urlPars.append(model.getBusinessScopeUpdateTime());
			}
			// idBean.orgKind 组织机构类别
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("idBean.orgkind&orgkindsub&orgkinddatafrom&orgkinduptime", "utf-8"));
			urlPars.append("&idBean.orgkind=");
			if (StringUtils.isNotEmpty(allAcct.getOrgType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgType(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgType(), "utf-8"));
			}
			urlPars.append("&orgkindsub=");
			if (StringUtils.isNotEmpty(allAcct.getOrgTypeDetail())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgTypeDetail(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgTypeDetails(), "utf-8"));
			}
			// 组织机构类别信息来源和信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getOrgTypeDetail()) || StringUtils.isNotEmpty(allAcct.getOrgType())) {
				urlPars.append("&orgkinddatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&orgkinduptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&orgkinddatafrom=");
				urlPars.append(model.getOrgTypeInfoSourceCode());
				urlPars.append("&orgkinduptime=");
				urlPars.append(model.getOrgTypeUpdateTime());
			}
			// vocationName 经济行业分类页面显示名称
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("vocation_png&vocationName&vocationCode&vocationtypedatafrom&vocationtypeuptime", "utf-8"));
			urlPars.append("&vocationName=");
			urlPars.append(EncodeUtils.encodStr(model.getEconomyIndustryStr(), "utf-8"));
			// vocationCode 经济行业分类值
			urlPars.append("&vocationCode=");
			urlPars.append(EncodeUtils.encodStr(model.getEconomyIndustry(), "utf-8"));
			// 经济行业分类信息来源和信息获取时间
			urlPars.append("&vocationtypedatafrom=");
			urlPars.append(model.getEconomyIndustryInfoSourceCode());
			urlPars.append("&vocationtypeuptime=");
			urlPars.append(model.getEconomyIndustryUpdateTime());
			// economyType经济类型
			urlPars.append("&rad1=");
			urlPars.append(EncodeUtils.encodStr("economytype&economytypedatafrom&economytypeuptime", "utf-8"));
			urlPars.append("&economytype=");
			if (StringUtils.isNotEmpty(allAcct.getEconomyType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getEconomyType(), "utf-8"));
				urlPars.append("&economytypedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&economytypeuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getEconomyType(), "utf-8"));
				urlPars.append("&economytypedatafrom=");
				urlPars.append(model.getEconomyTypeInfoSourceCode());
				urlPars.append("&economytypeuptime=");
				urlPars.append(model.getEconomyTypeUpdateTime());
			}
			// vocationAmount企业规模
			urlPars.append("&rad3=");
			urlPars.append(EncodeUtils.encodStr("vocationamount&vocationamountdatafrom&vocationamountuptime", "utf-8"));
			urlPars.append("&vocationamount=");
			if (StringUtils.isNotEmpty(allAcct.getCorpScale())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCorpScale(), "utf-8"));
				urlPars.append("&vocationamountdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&vocationamountuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCorpScale(), "utf-8"));
				urlPars.append("&vocationamountdatafrom=");
				urlPars.append(model.getCorpScaleInfoSourceCode());
				urlPars.append("&vocationamountuptime=");
				urlPars.append(model.getCorpScaleUpdateTime());
			}
			// orgState机构状态
			urlPars.append("&rad3=");
			urlPars.append(EncodeUtils.encodStr("orgstate&orgstatedatafrom&orgstateuptime", "utf-8"));
			urlPars.append("&orgstate=");
			if (StringUtils.isNotEmpty(allAcct.getOrgStatus())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgStatus(), "utf-8"));
				urlPars.append("&orgstatedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&orgstateuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgStatus(), "utf-8"));
				urlPars.append("&orgstatedatafrom=");
				urlPars.append(model.getOrgStatusInfoSourceCode());
				urlPars.append("&orgstateuptime=");
				urlPars.append(model.getOrgStatusUpdateTime());
			}
			// state基本户状态
			urlPars.append("&rad3=");
			urlPars.append(EncodeUtils.encodStr("state&statedatafrom&stateuptime", "utf-8"));
			urlPars.append("&state=");
			if (StringUtils.isNotEmpty(allAcct.getBasicAccountStatus())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getBasicAccountStatus(), "utf-8"));
				urlPars.append("&statedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&stateuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getAccountStatus(), "utf-8"));
				urlPars.append("&statedatafrom=");
				urlPars.append(model.getAccountStatusInfoSourceCode());
				urlPars.append("&stateuptime=");
				urlPars.append(model.getAccountStatusUpdateTime());
			}
			// upOrgName 上级机构名称
			urlPars.append("&uporgname=");
			if (StringUtils.isNotEmpty(allAcct.getParCorpName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParCorpName(), "utf-8"));
			}
			// upCreditCode 上级机构信用代码
			urlPars.append("&upcreditcode=");
			if (StringUtils.isNotEmpty(allAcct.getParOrgEccsNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgEccsNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParOrgEccsNo(), "utf-8"));
			}
			// upSdepOrgCode 上级组织机构代码
			urlPars.append("&upsdeporgcode=");
			if (StringUtils.isNotEmpty(allAcct.getParOrgCode())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgCode(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParOrgCode(), "utf-8"));
			}
			// upSaccFileType登记注册号类型
			urlPars.append("&upsaccfiletype=");
			if (StringUtils.isNotEmpty(allAcct.getParRegType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParRegType(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParRegType(), "utf-8"));
			}
			// upSaccFileCode登记注册号码
			urlPars.append("&upsaccfilecode=");
			if (StringUtils.isNotEmpty(allAcct.getParRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParRegNo(), "utf-8"));
			}
			if (StringUtils.isNotEmpty(allAcct.getParCorpName()) || StringUtils.isNotEmpty(allAcct.getParOrgEccsNo()) || StringUtils.isNotEmpty(allAcct.getParOrgCode())
					|| StringUtils.isNotEmpty(allAcct.getParRegType()) || StringUtils.isNotEmpty(allAcct.getParRegNo())) {
				urlPars.append("&uporginformationdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&uporginformationuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&uporginformationdatafrom=");
				urlPars.append(model.getParCorpInfoSourceCode());
				urlPars.append("&uporginformationuptime=");
				urlPars.append(model.getParCorpUpdateTime());
			}
			// provCodeW办公（生产、经营）地址 省份 cityCodeW办公（生产、经营）地址 城市
			// countyCodeW办公（生产、经营）地址 地区 workAddress 地址
			urlPars.append("&rad4=");
			urlPars.append(EncodeUtils.encodStr("provCodeW&cityCodeW&countyCodeW&workaddress", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&workaddressdatafrom&workaddressuptime", "utf-8"));
			if (StringUtils.isEmpty(model.getProvCodeW())) {
				model.setProvCodeW("0");
			}
			if (StringUtils.isEmpty(model.getCityCodeW())) {
				model.setCityCodeW("0");
			}
			if (StringUtils.isEmpty(model.getCountyCodeW())) {
				model.setCountyCodeW("0");
			}
			/**
			 * provCodeW办公（生产、经营）地址 省份
			 */
			urlPars.append("&provCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkProvince())) {
				urlPars.append(allAcct.getWorkArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getProvCodeW(), "utf-8"));
			}
			/**
			 * cityCodeW办公（生产、经营）地址 城市
			 */
			urlPars.append("&cityCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkCity())) {
				urlPars.append(allAcct.getWorkCity());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCityCodeW(), "utf-8"));
			}
			/**
			 * countyCodeW办公（生产、经营）地址 地区
			 */
			urlPars.append("&countyCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkArea())) {
				urlPars.append(allAcct.getWorkArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCountyCodeW(), "utf-8"));
			}
			/**
			 * workAddress 办公详细地址
			 */
			urlPars.append("&workaddress=");
			if (StringUtils.isNotEmpty(allAcct.getWorkAddress())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getWorkAddress(), "utf-8"));
				urlPars.append("&workaddressdatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&workaddressuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getWorkAddress(), "utf-8"));
				urlPars.append("&workaddressdatafrom=");
				urlPars.append(model.getWorkAddressInfoSourceCode());
				urlPars.append("&workaddressuptime=");
				urlPars.append(model.getWorkAddressUpdateTime());
			}
			// workTelephone联系电话
			urlPars.append("&rad4=");
			urlPars.append(EncodeUtils.encodStr("worktelephone&worktelephonedatafrom&worktelephoneuptime", "utf-8"));
			urlPars.append("&worktelephone=");
			if (StringUtils.isNotEmpty(allAcct.getTelephone())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), "utf-8"));
				urlPars.append("&worktelephonedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&worktelephoneuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getWorkTelephone(), "utf-8"));
				urlPars.append("&worktelephonedatafrom=");
				urlPars.append(model.getWorkTelephoneInfoSourceCode());
				urlPars.append("&worktelephoneuptime=");
				urlPars.append(model.getWorkTelephoneUpdateTime());
			}
			// financeTelephone财务部联系电话
			urlPars.append("&rad4=");
			urlPars.append(EncodeUtils.encodStr("financetelephone&financetelephonedatafrom&financetelephoneuptime", "utf-8"));
			urlPars.append("&financetelephone=");
			if (StringUtils.isNotEmpty(allAcct.getFinanceTelephone())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getFinanceTelephone(), "utf-8"));
				urlPars.append("&financetelephonedatafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&financetelephoneuptime=");
				urlPars.append(updateTime);
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getFinanceTelephone(), "utf-8"));
				urlPars.append("&financetelephonedatafrom=");
				urlPars.append(model.getFinanceTelephoneInfoSourceCode());
				urlPars.append("&financetelephoneuptime=");
				urlPars.append(model.getFinanceTelephoneUpdateTime());
			}
			// 高管及主要关系人信息--董事长信息
			urlPars.append("&rad5=");
			urlPars.append(EncodeUtils.encodStr("managerList[1].managername&managerList[1].certifitype", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[1].certificode", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[1].inte_manager_datafrom&managerList[1].uploadtimef", "utf-8"));
			// 董事长姓名
			urlPars.append("&managerList[1].managertype=1");
			urlPars.append("&managerList[1].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName1(), "utf-8"));
			}
			// 董事长证件类型
			urlPars.append("&managerList[1].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType1(), "utf-8"));
			}
			// 董事长证件号码
			urlPars.append("&managerList[1].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode1(), "utf-8"));
			}
			// 董事长信息来源及信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getManagerName1()) || StringUtils.isNotEmpty(allAcct.getCertifiType1()) || StringUtils.isNotEmpty(allAcct.getCertificode1())) {
				urlPars.append("&managerList[1].inte_manager_datafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&managerList[1].uploadtimef=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&managerList[1].inte_manager_datafrom=");
				urlPars.append(model.getManagerInfoSourceCode1());
				urlPars.append("&managerList[1].uploadtimef=");
				urlPars.append(model.getManagerUpdateTime1());
			}
			// 高管及主要关系人信息 --总经理/主要负责人信息
			urlPars.append("&rad5=");
			urlPars.append(EncodeUtils.encodStr("managerList[2].managername&managerList[2].certifitype", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[2].certificode&managerList[2].inte_manager_datafrom", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[2].uploadtimef", "utf-8"));
			// 总经理/主要负责人姓名
			urlPars.append("&managerList[2].managertype=2");
			urlPars.append("&managerList[2].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName2(), "utf-8"));
			}
			// 总经理/主要负责人证件类型
			urlPars.append("&managerList[2].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType2(), "utf-8"));
			}
			// 总经理/主要负责人证件号码
			urlPars.append("&managerList[2].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode2(), "utf-8"));
			}
			// 总经理/主要负责人信息来源及信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getManagerName2()) || StringUtils.isNotEmpty(allAcct.getCertifiType2()) || StringUtils.isNotEmpty(allAcct.getCertificode2())) {
				urlPars.append("&managerList[2].inte_manager_datafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&managerList[2].uploadtimef=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&managerList[2].inte_manager_datafrom=");
				urlPars.append(model.getManagerInfoSourceCode2());
				urlPars.append("&managerList[2].uploadtimef=");
				urlPars.append(model.getManagerUpdateTime2());
			}

			// 高管及主要关系人信息--财务负责人信息
			urlPars.append("&rad5=");
			urlPars.append(EncodeUtils.encodStr("managerList[3].managername&managerList[3].certifitype", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[3].certificode", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[3].inte_manager_datafrom&managerList[3].uploadtimef", "utf-8"));
			// 财务负责人姓名
			urlPars.append("&managerList[3].managertype=3");
			urlPars.append("&managerList[3].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName3(), "utf-8"));
			}
			// 财务负责人证件类型
			urlPars.append("&managerList[3].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType3(), "utf-8"));
			}
			// 财务负责人证件号码
			urlPars.append("&managerList[3].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode3(), "utf-8"));
			}
			// 财务负责人信息来源及信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getManagerName3()) || StringUtils.isNotEmpty(allAcct.getCertifiType3()) || StringUtils.isNotEmpty(allAcct.getCertificode3())) {
				urlPars.append("&managerList[3].inte_manager_datafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&managerList[3].uploadtimef=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&managerList[3].inte_manager_datafrom=");
				urlPars.append(model.getManagerInfoSourceCode3());
				urlPars.append("&managerList[3].uploadtimef=");
				urlPars.append(model.getManagerUpdateTime3());
			}
			// 高管及主要关系人信息--监事长信息
			urlPars.append("&rad5=");
			urlPars.append(EncodeUtils.encodStr("managerList[4].managername&managerList[4].certifitype", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[4].certificode", "utf-8"));
			urlPars.append(EncodeUtils.encodStr("&managerList[4].inte_manager_datafrom&managerList[4].uploadtimef", "utf-8"));
			// 监事长姓名
			urlPars.append("&managerList[4].managertype=4");
			urlPars.append("&managerList[4].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName4(), "utf-8"));
			}
			// 监事长证件类型
			urlPars.append("&managerList[4].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType4(), "utf-8"));
			}
			// 监事长证件号码
			urlPars.append("&managerList[4].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode4(), "utf-8"));
			}
			// 监事长信息来源及信息获取时间
			if (StringUtils.isNotEmpty(allAcct.getManagerName4()) || StringUtils.isNotEmpty(allAcct.getCertifiType4()) || StringUtils.isNotEmpty(allAcct.getCertificode4())) {
				urlPars.append("&managerList[4].inte_manager_datafrom=");
				urlPars.append(model.getEccsBankCode());
				urlPars.append("&managerList[4].uploadtimef=");
				urlPars.append(updateTime);
			} else {
				urlPars.append("&managerList[4].inte_manager_datafrom=");
				urlPars.append(model.getManagerInfoSourceCode4());
				urlPars.append("&managerList[4].uploadtimef=");
				urlPars.append(model.getManagerUpdateTime4());
			}

			urlPars.append("&button2=");
			urlPars.append("%E4%BF%9D%E5%AD%98%E6%9C%BA%E6%9E%84%E4%BF%A1%E6%81%AF");

			//
			// // 控制人2类型
			// urlPars.append("&controlList[1].controltype=");
			// if (StringUtils.isNotEmpty(allAcct.getControlType2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlType2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlType2(),
			// "utf-8"));
			// }
			// // 控制人2姓名
			// urlPars.append("&controlList[1].controlername=");
			// if (StringUtils.isNotEmpty(allAcct.getControlName2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlName2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlName2(),
			// "utf-8"));
			// }
			// // 控制人2证件类型
			// urlPars.append("&controlList[1].controlcertitype=");
			// if (StringUtils.isNotEmpty(allAcct.getControlIdType2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlIdType2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlIdType2(),
			// "utf-8"));
			// }
			// // 控制人2证件号码
			// urlPars.append("&controlList[1].controlcerticode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlCode2(),
			// "utf-8"));
			// }
			// // 控制人2组织机构代码
			// urlPars.append("&controlList[1].sdeporgcode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlOrgCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlOrgCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlOrgCode2(),
			// "utf-8"));
			// }
			// // 控制人2机构信用代码
			// urlPars.append("&controlList[1].creditcode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlEccsCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlEccsCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlEccsCode2(),
			// "utf-8"));
			// }
			// // 控件人2信息来源及信息获取时间
			// if (StringUtils.isNotEmpty(allAcct.getControlType2()) ||
			// StringUtils.isNotEmpty(allAcct.getControlName2())
			// || StringUtils.isNotEmpty(allAcct.getControlIdType2()) ||
			// StringUtils.isNotEmpty(allAcct.getControlCode2())
			// || StringUtils.isNotEmpty(allAcct.getControlOrgCode2()) ||
			// StringUtils.isNotEmpty(allAcct.getControlEccsCode2())) {
			// urlPars.append("&controlList[1].reportorgcode=");
			// urlPars.append(model.getEccsBankCode());
			// urlPars.append("&controlList[1].uploadtimeg=");
			// urlPars.append(updateTime);
			// } else {
			// urlPars.append("&controlList[1].reportorgcode=");
			// urlPars.append(model.getControlInfoSourceCode2());
			// urlPars.append("&controlList[1].uploadtimeg=");
			// urlPars.append(model.getControlUpdateTime2());
			// }
			//
			// // 控制人3类型
			// urlPars.append("&controlList[2].controltype=");
			// if (StringUtils.isNotEmpty(allAcct.getControlType3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlType3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlType3(),
			// "utf-8"));
			// }
			// // 控制人3姓名
			// urlPars.append("&controlList[2].controlername=");
			// if (StringUtils.isNotEmpty(allAcct.getControlName3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlName3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlName3(),
			// "utf-8"));
			// }
			// // 控制人3证件类型
			// urlPars.append("&controlList[2].controlcertitype=");
			// if (StringUtils.isNotEmpty(allAcct.getControlIdType3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlIdType3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlIdType3(),
			// "utf-8"));
			// }
			// // 控制人3证件号码
			// urlPars.append("&controlList[2].controlcerticode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlCode3(),
			// "utf-8"));
			// }
			// // 控制人3组织机构代码
			// urlPars.append("&controlList[2].sdeporgcode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlOrgCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlOrgCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlOrgCode3(),
			// "utf-8"));
			// }
			// // 控制人3机构信用代码
			// urlPars.append("&controlList[2].creditcode=");
			// if (StringUtils.isNotEmpty(allAcct.getControlEccsCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getControlEccsCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getControlEccsCode3(),
			// "utf-8"));
			// }
			// // 控件人3信息来源及信息获取时间
			// if (StringUtils.isNotEmpty(allAcct.getControlType3()) ||
			// StringUtils.isNotEmpty(allAcct.getControlName3())
			// || StringUtils.isNotEmpty(allAcct.getControlIdType3()) ||
			// StringUtils.isNotEmpty(allAcct.getControlCode3())
			// || StringUtils.isNotEmpty(allAcct.getControlOrgCode3()) ||
			// StringUtils.isNotEmpty(allAcct.getControlEccsCode3())) {
			// urlPars.append("&controlList[2].reportorgcode=");
			// urlPars.append(model.getEccsBankCode());
			// urlPars.append("&controlList[2].uploadtimeg=");
			// urlPars.append(updateTime);
			// } else {
			// urlPars.append("&controlList[2].reportorgcode=");
			// urlPars.append(model.getControlInfoSourceCode3());
			// urlPars.append("&controlList[2].uploadtimeg=");
			// urlPars.append(model.getControlUpdateTime3());
			// }
			//
			// // 重要股东信息
			// // 股东1类型
			// urlPars.append("&partnerlList[0].partnertype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderType1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderType1(),
			// "utf-8"));
			// }
			// // 股东1姓名
			// urlPars.append("&partnerlList[0].partnername=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderName1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderName1(),
			// "utf-8"));
			// }
			// // 股东1证件类型
			// urlPars.append("&partnerlList[0].certifitype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderIdType1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderIdType1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderIdType1(),
			// "utf-8"));
			// }
			// // 股东1证件号码
			// urlPars.append("&partnerlList[0].certificode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderCode1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderCode1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderCode1(),
			// "utf-8"));
			// }
			// // 股东1组织机构代码
			// urlPars.append("&partnerlList[0].sdeporgcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderOrgCode1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderOrgCode1(),
			// "utf-8"));
			// }
			// // 股东1机构信用代码
			// urlPars.append("&partnerlList[0].partercreditcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderEccsCode1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderEccsCode1(),
			// "utf-8"));
			// }
			// // 股东1持股比例（%）
			// urlPars.append("&partnerlList[0].partnerratio=");
			// if (StringUtils.isNotEmpty(allAcct.getHoldingRatio1())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getHoldingRatio1(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getHoldingRatio1(),
			// "utf-8"));
			// }
			// // 股东1信息来源及信息获取时间
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName1()) ||
			// StringUtils.isNotEmpty(allAcct.getShareHolderName1())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderIdType1())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderCode1())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode1())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode1())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode1())) {
			// urlPars.append("&partnerlList[0].arpartnerdatafrom=");
			// urlPars.append(model.getEccsBankCode());
			// urlPars.append("&partnerlList[0].uploadtimeg=");
			// urlPars.append(updateTime);
			// } else {
			// urlPars.append("&partnerlList[0].arpartnerdatafrom=");
			// urlPars.append(model.getShareHolderInfoSourceCode1());
			// urlPars.append("&partnerlList[0].uploadtimeg=");
			// urlPars.append(model.getShareHolderUpdateTime1());
			// }
			//
			// // 股东2类型
			// urlPars.append("&partnerlList[1].partnertype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderType2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderType2(),
			// "utf-8"));
			// }
			// // 股东2姓名
			// urlPars.append("&partnerlList[1].partnername=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderName2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderName2(),
			// "utf-8"));
			// }
			// // 股东2证件类型
			// urlPars.append("&partnerlList[1].certifitype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderIdType2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderIdType2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderIdType2(),
			// "utf-8"));
			// }
			// // 股东2证件号码
			// urlPars.append("&partnerlList[1].certificode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderCode2(),
			// "utf-8"));
			// }
			// // 股东2组织机构代码
			// urlPars.append("&partnerlList[1].sdeporgcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderOrgCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderOrgCode2(),
			// "utf-8"));
			// }
			// // 股东2机构信用代码
			// urlPars.append("&partnerlList[1].partercreditcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderEccsCode2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderEccsCode2(),
			// "utf-8"));
			// }
			// // 股东2持股比例（%）
			// urlPars.append("&partnerlList[1].partnerratio=");
			// if (StringUtils.isNotEmpty(allAcct.getHoldingRatio2())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getHoldingRatio2(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getHoldingRatio2(),
			// "utf-8"));
			// }
			// // 股东2信息来源及信息获取时间
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName2()) ||
			// StringUtils.isNotEmpty(allAcct.getShareHolderName2())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderIdType2())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderCode2())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode2())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode2())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode2())) {
			// urlPars.append("&partnerlList[1].arpartnerdatafrom=");
			// urlPars.append(model.getEccsBankCode());
			// urlPars.append("&partnerlList[1].uploadtimeg=");
			// urlPars.append(updateTime);
			// } else {
			// urlPars.append("&partnerlList[1].arpartnerdatafrom=");
			// urlPars.append(model.getShareHolderInfoSourceCode2());
			// urlPars.append("&partnerlList[1].uploadtimeg=");
			// urlPars.append(model.getShareHolderUpdateTime2());
			// }
			//
			// // 股东3类型
			// urlPars.append("&partnerlList[2].partnertype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderType3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderType3(),
			// "utf-8"));
			// }
			// // 股东3姓名
			// urlPars.append("&partnerlList[2].partnername=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderName3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderName3(),
			// "utf-8"));
			// }
			// // 股东3证件类型
			// urlPars.append("&partnerlList[2].certifitype=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderIdType3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderIdType3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderIdType3(),
			// "utf-8"));
			// }
			// // 股东3证件号码
			// urlPars.append("&partnerlList[2].certificode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderCode3(),
			// "utf-8"));
			// }
			// // 股东3组织机构代码
			// urlPars.append("&partnerlList[2].sdeporgcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderOrgCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderOrgCode3(),
			// "utf-8"));
			// }
			// // 股东3机构信用代码
			// urlPars.append("&partnerlList[2].partercreditcode=");
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getShareHolderEccsCode3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getShareHolderEccsCode3(),
			// "utf-8"));
			// }
			// // 股东3持股比例（%）
			// urlPars.append("&partnerlList[2].partnerratio=");
			// if (StringUtils.isNotEmpty(allAcct.getHoldingRatio3())) {
			// urlPars.append(EncodeUtils.encodStr(allAcct.getHoldingRatio3(),
			// "utf-8"));
			// } else {
			// urlPars.append(EncodeUtils.encodStr(model.getHoldingRatio3(),
			// "utf-8"));
			// }
			// // 股东3信息来源及信息获取时间
			// if (StringUtils.isNotEmpty(allAcct.getShareHolderName3()) ||
			// StringUtils.isNotEmpty(allAcct.getShareHolderName3())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderIdType3())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderCode3())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderOrgCode3())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode3())
			// || StringUtils.isNotEmpty(allAcct.getShareHolderEccsCode3())) {
			// urlPars.append("&partnerlList[2].arpartnerdatafrom=");
			// urlPars.append(model.getEccsBankCode());
			// urlPars.append("&partnerlList[2].uploadtimeg=");
			// urlPars.append(updateTime);
			// } else {
			// urlPars.append("&partnerlList[2].arpartnerdatafrom=");
			// urlPars.append(model.getShareHolderInfoSourceCode3());
			// urlPars.append("&partnerlList[2].uploadtimeg=");
			// urlPars.append(model.getShareHolderUpdateTime3());
			// }
		} catch (Exception e) {
			logger.error("账号" + allAcct.getAcctNo() + "机构信用代码证变更参数拼接异常", e);
			throw new SyncException("账号" + allAcct.getAcctNo() + "机构信用代码证变更参数拼接异常");
		}
		return urlPars;
	}

	@Override
	public StringBuffer getNotFaFangChangeXydmzUrl(EccsAccountInfo model, AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			/**
			 * idBean.sdepnamec 机构中文名称
			 */
			urlPars.append("&idBean.sdepnamec=");
			if (StringUtils.isNotEmpty(allAcct.getDepositorName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getDepositorName(), "utf-8"));
			}
			/**
			 * idBean.sdepNameE机构英文名称
			 */
			urlPars.append("&idBean.sdepnamee=");
			if (StringUtils.isNotEmpty(allAcct.getOrgEnName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgEnName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgEnName(), "utf-8"));
			}
			/**
			 * idBean.sdepOrgCode组织机构代码 不能改变
			 */
			urlPars.append("&idBean.sdeporgcode=");
			urlPars.append(EncodeUtils.encodStr(model.getOrgCode(), "utf-8"));
			/**
			 * idBean.loanCardCode贷款卡编码 不能改变
			 */
			urlPars.append("&idBean.loancardcode=");
			if (StringUtils.isNotEmpty(allAcct.getBankCardNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getBankCardNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getBankCardNo(), "utf-8"));
			}
			/**
			 * idBean.saccBaseLicNo开户许可证核准号 不能改变
			 */
			urlPars.append("&idBean.saccbaselicno=");
			if (StringUtils.isNotEmpty(allAcct.getAccountKey())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getAccountKey(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getAccountKey(), "utf-8"));
			}
			/**
			 * idBean.registerType登记注册号类型 不能改变
			 */
			urlPars.append("&idBean.registertype=");
			if (StringUtils.isNotEmpty(allAcct.getRegType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegType(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegType(), "utf-8"));
			}
			/**
			 * idBean.registerCode登记注册号码 不能改变
			 */
			urlPars.append("&idBean.registercode=");
			if (StringUtils.isNotEmpty(allAcct.getRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegNo(), "utf-8"));
			}
			/**
			 * idBean.sdepNationaltaxCode纳税人识别号（国税） 不能改变
			 */
			urlPars.append("&idBean.sdepnationaltaxcode=");
			if (StringUtils.isNotEmpty(allAcct.getStateTaxRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getStateTaxRegNo(), "utf-8"));
			}
			/**
			 * idBean.sdepLandtaxCode纳税人识别号（地税） 不能改变
			 */
			urlPars.append("&idBean.sdeplandtaxcode=");
			if (StringUtils.isNotEmpty(allAcct.getTaxRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getTaxRegNo(), "utf-8"));
			}
			/*
			 * 注册地址（省市区等）
			 */
			if (StringUtils.isEmpty(model.getProvCode())) {
				model.setProvCode("0");
			}
			if (StringUtils.isEmpty(model.getCityCode())) {
				model.setCityCode("0");
			}
			if (StringUtils.isEmpty(model.getCountyCode())) {
				model.setCountyCode("0");
			}
			/**
			 * country国家
			 */
			urlPars.append("&country=CHN");
			/**
			 * provCode省份
			 */
			urlPars.append("&provCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegProvince())) {
				urlPars.append(allAcct.getRegProvince().substring(0, 2));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getProvCode(), "utf-8"));
			}
			/**
			 * cityCode城市
			 */
			urlPars.append("&cityCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegCity())) {
				urlPars.append(allAcct.getRegCity().substring(0, 4));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCityCode(), "utf-8"));
			}
			/**
			 * countyCode 地区
			 */
			urlPars.append("&countyCode=");
			if (StringUtils.isNotEmpty(allAcct.getRegArea())) {
				urlPars.append(allAcct.getRegArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCountyCode(), "utf-8"));
			}
			/**
			 * 详细地址
			 */
			urlPars.append("&areaaddr=");
			if (StringUtils.isNotEmpty(allAcct.getRegAddress())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegAddress(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegAddress(), "utf-8"));
			}
			// idBean.departKind 登记部门
			urlPars.append("&idBean.departkind=");
			if (StringUtils.isNotEmpty(allAcct.getRegOffice())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegOffice(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegOffice(), "utf-8"));
			}
			// buildDate成立日期
			urlPars.append("&builddate=");
			if (StringUtils.isNotEmpty(allAcct.getSetupDate())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getSetupDate(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getSetupDate(), "utf-8"));
			}
			// maturityDate证书到期日
			urlPars.append("&maturitydate=");
			if (StringUtils.isNotEmpty(allAcct.getTovoidDate())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTovoidDate(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getEffectiveDate(), "utf-8"));
			}
			// sdepManagerName法定代表人名称
			urlPars.append("&managerList[0].managertype=5");
			urlPars.append("&controlList[0].controlid").append(model.getControlid());
			urlPars.append("&managerList[0].managername=");
			if (StringUtils.isNotEmpty(allAcct.getLegalName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalName(), "utf-8"));
			}
			// sdepManagerCreKind证件类型
			urlPars.append("&managerList[0].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getLegalIdcardTypeEccs())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardTypeEccs(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalIdcardType(), "utf-8"));
			}
			// sdepManagerCreCode证件号码
			urlPars.append("&managerList[0].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getLegalIdcardNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getLegalIdcardNo(), "utf-8"));
			}
			// sdepFundKind注册资本币种
			urlPars.append("&sdepfundkind=");
			if (StringUtils.isNotEmpty(allAcct.getRegCurrencyTypeEccs())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeEccs(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegCurrencyType(), "utf-8"));
			}
			// sdepFund注册资本(万元)
			urlPars.append("&sdepfund=");
			if (StringUtils.isNotEmpty(allAcct.getRegisteredCapital())) {
				urlPars.append(EncodeUtils.encodStr(PbcBussUtils.getEccsRegisteredCapital(allAcct.getRegisteredCapital()), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getRegisteredCapital(), "utf-8"));
			}
			// sdepWork经营（业务）范围
			urlPars.append("&sdepwork=");
			if (StringUtils.isNotEmpty(allAcct.getBusinessScopeEccs())) {
				String sdepWork = allAcct.getBusinessScopeEccs();
				sdepWork = sdepWork.replace(",", "，").replace(";", "；").replace("*", "×");
				allAcct.setBusinessScopeEccs(sdepWork);
				urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScopeEccs(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getBusinessScope(), "utf-8"));
			}
			// idBean.orgKind 组织机构类别
			urlPars.append("&idBean.orgkind=");
			if (StringUtils.isNotEmpty(allAcct.getOrgType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgType(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgType(), "utf-8"));
			}
			urlPars.append("&orgkindsub=");
			if (StringUtils.isNotEmpty(allAcct.getOrgTypeDetail())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgTypeDetail(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgTypeDetails(), "utf-8"));
			}
			// vocationName 经济行业分类页面显示名称
			urlPars.append("&vocationName=");
			urlPars.append(EncodeUtils.encodStr(model.getEconomyIndustryStr(), "utf-8"));
			// vocationCode 经济行业分类值
			urlPars.append("&vocationCode=");
			urlPars.append(EncodeUtils.encodStr(model.getEconomyIndustry(), "utf-8"));
			// economyType经济类型
			urlPars.append("&economytype=");
			if (StringUtils.isNotEmpty(allAcct.getEconomyType())) {
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getEconomyType(), "utf-8"));
			}
			// vocationAmount企业规模
			urlPars.append("&vocationamount=");
			if (StringUtils.isNotEmpty(allAcct.getCorpScale())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCorpScale(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCorpScale(), "utf-8"));
			}
			// orgState机构状态
			urlPars.append("&orgstate=");
			if (StringUtils.isNotEmpty(allAcct.getOrgStatus())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getOrgStatus(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getOrgStatus(), "utf-8"));
			}
			// state基本户状态
			urlPars.append("&state=");
			if (StringUtils.isNotEmpty(allAcct.getBasicAccountStatus())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getBasicAccountStatus(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getAccountStatus(), "utf-8"));
			}
			// upOrgName 上级机构名称
			urlPars.append("&uporgname=");
			if (StringUtils.isNotEmpty(allAcct.getParCorpName())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParCorpName(), "utf-8"));
			}
			// upCreditCode 上级机构信用代码
			urlPars.append("&upcreditcode=");
			if (StringUtils.isNotEmpty(allAcct.getParOrgEccsNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgEccsNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParOrgEccsNo(), "utf-8"));
			}
			// upSdepOrgCode 上级组织机构代码
			urlPars.append("&upsdeporgcode=");
			if (StringUtils.isNotEmpty(allAcct.getParOrgCode())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgCode(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParOrgCode(), "utf-8"));
			}
			// upSaccFileType登记注册号类型
			urlPars.append("&upsaccfiletype=");
			if (StringUtils.isNotEmpty(allAcct.getParRegType())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParRegType(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParRegType(), "utf-8"));
			}
			// upSaccFileCode登记注册号码
			urlPars.append("&upsaccfilecode=");
			if (StringUtils.isNotEmpty(allAcct.getParRegNo())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getParRegNo(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getParRegNo(), "utf-8"));
			}
			// provCodeW办公（生产、经营）地址 省份 cityCodeW办公（生产、经营）地址 城市
			// countyCodeW办公（生产、经营）地址 地区 workAddress 地址
			if (StringUtils.isEmpty(model.getProvCodeW())) {
				model.setProvCodeW("0");
			}
			if (StringUtils.isEmpty(model.getCityCodeW())) {
				model.setCityCodeW("0");
			}
			if (StringUtils.isEmpty(model.getCountyCodeW())) {
				model.setCountyCodeW("0");
			}
			/**
			 * provCodeW办公（生产、经营）地址 省份
			 */
			urlPars.append("&provCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkProvince())) {
				urlPars.append(allAcct.getWorkArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getProvCodeW(), "utf-8"));
			}
			/**
			 * cityCodeW办公（生产、经营）地址 城市
			 */
			urlPars.append("&cityCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkCity())) {
				urlPars.append(allAcct.getWorkCity());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCityCodeW(), "utf-8"));
			}
			/**
			 * countyCodeW办公（生产、经营）地址 地区
			 */
			urlPars.append("&countyCodeW=");
			if (StringUtils.isNotEmpty(allAcct.getWorkArea())) {
				urlPars.append(allAcct.getWorkArea());
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCountyCodeW(), "utf-8"));
			}
			/**
			 * workAddress 办公详细地址
			 */
			urlPars.append("&workaddress=");
			if (StringUtils.isNotEmpty(allAcct.getWorkAddress())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getWorkAddress(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getWorkAddress(), "utf-8"));
			}
			// workTelephone联系电话
			urlPars.append("&worktelephone=");
			if (StringUtils.isNotEmpty(allAcct.getTelephone())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getWorkTelephone(), "utf-8"));
			}
			// financeTelephone财务部联系电话
			urlPars.append("&financetelephone=");
			if (StringUtils.isNotEmpty(allAcct.getFinanceTelephone())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getFinanceTelephone(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getFinanceTelephone(), "utf-8"));
			}
			// 高管及主要关系人信息--董事长信息
			// 董事长姓名
			urlPars.append("&managerList[1].managertype=1");
			urlPars.append("&managerList[1].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName1(), "utf-8"));
			}
			// 董事长证件类型
			urlPars.append("&managerList[1].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType1(), "utf-8"));
			}
			// 董事长证件号码
			urlPars.append("&managerList[1].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode1())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode1(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode1(), "utf-8"));
			}
			// 高管及主要关系人信息 --总经理/主要负责人信息
			// 总经理/主要负责人姓名
			urlPars.append("&managerList[2].managertype=2");
			urlPars.append("&managerList[2].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName2(), "utf-8"));
			}
			// 总经理/主要负责人证件类型
			urlPars.append("&managerList[2].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType2(), "utf-8"));
			}
			// 总经理/主要负责人证件号码
			urlPars.append("&managerList[2].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode2())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode2(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode2(), "utf-8"));
			}
			// 高管及主要关系人信息--财务负责人信息
			// 财务负责人姓名
			urlPars.append("&managerList[3].managertype=3");
			urlPars.append("&managerList[3].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName3(), "utf-8"));
			}
			// 财务负责人证件类型
			urlPars.append("&managerList[3].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType3(), "utf-8"));
			}
			// 财务负责人证件号码
			urlPars.append("&managerList[3].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode3())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode3(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode3(), "utf-8"));
			}
			// 高管及主要关系人信息--监事长信息
			// 监事长姓名
			urlPars.append("&managerList[4].managertype=4");
			urlPars.append("&managerList[4].managername=");
			if (StringUtils.isNotEmpty(allAcct.getManagerName4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getManagerName4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getManagerName4(), "utf-8"));
			}
			// 监事长证件类型
			urlPars.append("&managerList[4].certifitype=");
			if (StringUtils.isNotEmpty(allAcct.getCertifiType4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertifiType4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertifiType4(), "utf-8"));
			}
			// 监事长证件号码
			urlPars.append("&managerList[4].certificode=");
			if (StringUtils.isNotEmpty(allAcct.getCertificode4())) {
				urlPars.append(EncodeUtils.encodStr(allAcct.getCertificode4(), "utf-8"));
			} else {
				urlPars.append(EncodeUtils.encodStr(model.getCertificode4(), "utf-8"));
			}
		} catch (Exception e) {
			logger.error("账号" + allAcct.getAcctNo() + "机构信用代码证获取未发放变更参数拼接异常", e);
			throw new SyncException("账号" + allAcct.getAcctNo() + "机构信用代码证获取未发放变更参数拼接异常");
		}
		return urlPars;
	}

}
