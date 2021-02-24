package com.ideatech.ams.pbc.service.eccs;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.EccsConfig;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EccsSearchServiceImpl implements EccsSearchService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public EccsAccountInfo getEccsAccountInfo(LoginAuth auth, StringBuffer eccsSearchBuffer) throws Exception, SyncException {
		EccsAccountInfo eccsAccountInfo = new EccsAccountInfo();
		StringBuffer diffent = new StringBuffer();
		diffent.append("&ran=").append(new Date().getTime());
		HttpResult result = null;
		// 查询是否存在
		// 查询url
		String queryUrl = auth.getDomain() + EccsConfig.XYDMZ_URL_MANAGE_PRESERVE;
		// 查询参数
		String urlStr = eccsSearchBuffer.append(diffent).toString();
		try {
			result = HttpUtils.post(queryUrl, urlStr, EccsConfig.ENCODING, auth.getCookie(), HeadsCache.getXydmzCommon());
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (html.indexOf("查询结果为空") > -1) {
				throw new SyncException("在机构信用代码证系统中未找到该企业");
			}
			if (html.indexOf("暂停服务") > -1) {
				throw new SyncException("机构信用代码证系统暂停服务");
			}
			Document document = Jsoup.parse(html);
			String assetProjId = document.select("input[name=assetProjId]").first().val();
			eccsAccountInfo.setAssetProjId(assetProjId);
			// 发放状态标志位 0未发放，大于0已发放
			String dispatchtimes = document.select("input[id=dispatchtimes0]").first().val();
			diffent = new StringBuffer();
			diffent.append("&flag=&intPage=1");
			urlStr = eccsSearchBuffer.append(diffent).toString();
			if (Integer.valueOf(dispatchtimes) > 0) {// 获取已发放的信用代码证信息
				searchAccountInfo(auth, eccsAccountInfo, urlStr, assetProjId);
				eccsAccountInfo.setInfoisOk(true);
			} else {// 获取未发放的代码证信息
				searchGetOrgCodeInfo(auth, eccsAccountInfo, urlStr, assetProjId);
				eccsAccountInfo.setInfoisOk(false);
			}
		} catch (SyncException e) {
			throw e;
		} catch (ConnectException e) {
			logger.error("根据条件查询机构代码证系统超时", e);
			throw new SyncException("根据条件查询机构代码证系统超时");
		} catch (Exception e) {
			logger.error("根据条件查询机构代码证系统异常", e);
			throw new SyncException("根据条件查询机构代码证系统异常");
		}
		return eccsAccountInfo;
	}

	/**
	 * 获取未发放的机构信用代码证的信用代码证信息
	 * 
	 * @param auth
	 * @param model
	 * @param urlPars
	 * @param crcCode
	 * @throws SyncException
	 * @throws Exception
	 */
	private void searchGetOrgCodeInfo(LoginAuth auth, EccsAccountInfo model, String urlPars, String crcCode) throws SyncException, Exception {

		HttpResult result = null;
		String queryUrl = auth.getDomain() + EccsConfig.XYDMZ_URL_FaFangModifyDetail + crcCode;
		result = HttpUtils.post(queryUrl, urlPars, EccsConfig.ENCODING, auth.getCookie(), HeadsCache.getXydmzCommon());
		if (result.getStatuCode() == 200) {
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			Document doc = Jsoup.parse(html);
			Elements tables = doc.select("form");
			Element table = tables.get(0);
			// 公共变更客户信息
			getCommonChangeInfo(model, table);
		} else {
			throw new SyncException("获取原先未变更前机构信用代码证信息页面异常");
		}
	}

	/**
	 * 获取查询条件
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public StringBuffer getEccsSearchUrlParams(EccsSearchCondition model) {
		model.setOrgCode(PbcBussUtils.getStandardOrgCode(model.getOrgCode()));// 组织机构代码证转换
		String[] regTypeArray = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "99" };
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("&flag=1&intPage=1");
		urlPars.append("&LoanCode=&SelReasonType=");
		if (StringUtils.isBlank(model.getOrgCode())) {
			urlPars.append("&SdepOrgcode=");
		} else {
			urlPars.append("&SdepOrgcode=").append(model.getOrgCode());
		}
		if (StringUtils.isBlank(model.getOrgEccsNo())) {
			urlPars.append("&CreditCode=");
		} else {
			urlPars.append("&CreditCode=").append(model.getOrgEccsNo());
		}
		if (StringUtils.isBlank(model.getAccountKey()) || model.getAccountKey().length() < 14) {
			urlPars.append("&SaccBaseLicNo=");
		} else {
			urlPars.append("&SaccBaseLicNo=").append(model.getAccountKey());
		}
		if (StringUtils.isBlank(model.getStateTaxRegNo())) {
			urlPars.append("&SdepNationalTaxCode=");
		} else {
			urlPars.append("&SdepNationalTaxCode=").append(model.getStateTaxRegNo());
		}
		if (StringUtils.isBlank(model.getTaxRegNo())) {
			urlPars.append("&SdepLandTaxCode=");
		} else {
			urlPars.append("&SdepLandTaxCode=").append(model.getTaxRegNo());
		}
		if (StringUtils.isNotBlank(model.getRegNo()) && ArrayUtils.contains(regTypeArray, model.getRegType()) && StringUtils.isNotBlank(model.getRegType())) {
			if (model.getRegType().equals("01") && model.getRegNo().substring(0, 1).equals("9") && model.getRegNo().length() == 18) {
				model.setRegType("07");
			}
			urlPars.append("&RegisterType=").append(model.getRegType());
			urlPars.append("&RegisterCode=").append(model.getRegNo());
		} else {
			urlPars.append("&RegisterType=");
			urlPars.append("&RegisterCode=");
		}
		return urlPars;
	}

	/**
	 * 获取原先未变更前机构信用代码证信息（信用代码证已发放）
	 * 
	 * @param auth
	 *            登录用户信息
	 * @param model
	 *            存放原先信息的机构信用代码证对象
	 * @param urlPars
	 *            获取账户详细页面参数Pars
	 * @param crcCode
	 * @return
	 * @exception
	 */
	private void searchAccountInfo(LoginAuth auth, EccsAccountInfo model, String urlPars, String crcCode) throws SyncException, Exception {
		HttpResult result = null;
		String queryUrl = auth.getDomain() + EccsConfig.XYDMZ_URL_MODIFYDETAIL + crcCode;
		result = HttpUtils.post(queryUrl, urlPars, EccsConfig.ENCODING, auth.getCookie(), HeadsCache.getXydmzCommon());
		if (result.getStatuCode() == 200) {
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			Document doc = Jsoup.parse(html);
			Elements tables = doc.select("form");
			Element table = tables.get(0);
			/* 机构标识信息 */
			Pattern p = Pattern.compile("modUnit\\('rad1','(.*?)'\\)");
			Matcher m = p.matcher(table.html());
			if (m.find()) {
				model.setEccsBankCode(m.group(1));
			}
			// 公共变更客户信息
			getCommonChangeInfo(model, table);
			model.setCreditCode(StringUtils.trim(table.select("input[name=idBean.creditcode]").val()));
			// 中文名称
			model.setDepositorNameInfoSourceCode(table.select("input[name=idBean.sdepnamecdatafrom]").val());
			model.setDepositorNameUpdateTime(StringUtils.trim(table.select("input[name=idBean.sdepnamecuptime]").val()));
			// 英文名称
			model.setOrgEnNameInfoSourceCode(StringUtils.trim(table.select("input[name=idBean.sdepnameedatafrom]").val()));
			model.setOrgEnNameUpdateTime(StringUtils.trim(table.select("input[name=idBean.sdepnameeuptime]").val()));
			// 省市区
			model.setRegAddressInfoSourceCode(StringUtils.trim(table.select("input[name=areaaddrdatafrom]").val()));
			model.setRegAddressUpdateTime(StringUtils.trim(table.select("input[name=areaaddruptime]").val()));
			// 登记部门
			model.setRegOfficeInfoSourceCode(StringUtils.trim(table.select("input[name=idBean.departkinddatafrom]").val()));
			model.setRegOfficeUpdateTime(StringUtils.trim(table.select("input[name=idBean.departkinduptime]").val()));
			// 成立时间
			model.setSetupDateInfoSourceCode(StringUtils.trim(table.select("input[name=builddatedatafrom]").val()));
			model.setSetupDateUpdateTime(StringUtils.trim(table.select("input[name=builddateuptime]").val()));
			// 证书到期日
			model.setEffectiveDateInfoSourceCode(StringUtils.trim(table.select("input[name=maturitydatedatafrom]").val()));
			model.setEffectiveDateUpdateTime(StringUtils.trim(table.select("input[name=maturitydateuptime]").val()));
			// 经营范围
			model.setBusinessScopeInfoSourceCode(StringUtils.trim(table.select("input[name=sdepworkdatafrom]").val()));
			model.setBusinessScopeUpdateTime(StringUtils.trim(table.select("input[name=sdepworkuptime]").val()));
			// 注册资本
			model.setRegisteredCapitalInfoSourceCode(StringUtils.trim(table.select("input[name=sdepfunddatafrom]").val()));
			model.setRegisteredCapitalUpdateTime(StringUtils.trim(table.select("input[name=sdepworkuptime]").val()));
			// 组织机构类别
			model.setOrgTypeInfoSourceCode(StringUtils.trim(table.select("input[name=orgkinddatafrom]").val()));
			model.setOrgTypeUpdateTime(StringUtils.trim(table.select("input[name=orgkinduptime]").val()));
			// 经济行业分类
			model.setEconomyIndustryInfoSourceCode(StringUtils.trim(table.select("input[name=vocationtypedatafrom]").val()));
			model.setEconomyIndustryUpdateTime(StringUtils.trim(table.select("input[name=vocationtypeuptime]").val()));
			// 经济类型
			model.setEconomyTypeInfoSourceCode(StringUtils.trim(table.select("input[name=economytypedatafrom]").val()));
			model.setEconomyTypeUpdateTime(StringUtils.trim(table.select("input[name=economytypeuptime]").val()));
			// 法人信息
			model.setControlid(table.select("input[name=controlList[0].controlid]").val());
			model.setLegalInfoSourceCode(table.select("input[name=managerList[0].inte_manager_datafrom]").val());
			model.setLegalUpdateTime(StringUtils.trim(table.select("input[name=managerList[0].uploadtimef]").val()));
			// 机构状态
			model.setOrgStatusInfoSourceCode(StringUtils.trim(table.select("input[name=orgstatedatafrom]").val()));
			model.setOrgStatusUpdateTime(StringUtils.trim(table.select("input[name=orgstateuptime]").val()));
			// 基本户状态
			model.setAccountStatusInfoSourceCode(StringUtils.trim(table.select("input[name=statedatafrom]").val()));
			model.setAccountStatusUpdateTime(StringUtils.trim(table.select("input[name=stateuptime]").val()));
			// 企业规模
			model.setCorpScaleInfoSourceCode(StringUtils.trim(table.select("input[name=vocationamountdatafrom]").val()));
			model.setCorpScaleUpdateTime(StringUtils.trim(table.select("input[name=vocationamountuptime]").val()));
			/* 联络信息 */
			// 办公（生产、经营）地址
			model.setWorkAddressInfoSourceCode(StringUtils.trim(table.select("input[name=workaddressdatafrom]").val()));
			model.setWorkAddressUpdateTime(StringUtils.trim(table.select("input[name=workaddressuptime]").val()));
			// 联系电话
			model.setWorkTelephoneInfoSourceCode(StringUtils.trim(table.select("input[name=worktelephonedatafrom]").val()));
			model.setWorkTelephoneUpdateTime(StringUtils.trim(table.select("input[name=worktelephoneuptime]").val()));
			// 财务部联系电话
			model.setFinanceTelephoneInfoSourceCode((table.select("input[name=financetelephonedatafrom]").val()));
			model.setFinanceTelephoneUpdateTime(StringUtils.trim(table.select("input[name=financetelephoneuptime]").val()));
			/* 高管及主要关系人信息 */
			// 董事长
			model.setManagerName1(table.select("input[name=managerList[1].managername]").val());
			String setCertifiTypes1[] = findSelected(table.select("select[name=managerList[1].certifitype]").first());
			model.setCertifiType1(setCertifiTypes1[0]);
			model.setCertifiTypeStr1(setCertifiTypes1[1]);
			model.setCertificode1(table.select("input[name=managerList[1].certificode]").val());

			model.setManagerInfoSourceCode1(table.select("input[name=managerList[1].inte_manager_datafrom]").val());
			model.setManagerUpdateTime1(StringUtils.trim(table.select("input[name=managerList[1].uploadtimef]").val()));
			// 总经理/主要负责人
			model.setManagerName2(table.select("input[name=managerList[2].managername]").val());
			String setCertifiTypes2[] = findSelected(table.select("select[name=managerList[2].certifitype]").first());
			model.setCertifiType2(setCertifiTypes2[0]);
			model.setCertifiTypeStr2(setCertifiTypes2[1]);
			model.setCertificode2(table.select("input[name=managerList[2].certificode]").val());

			model.setManagerInfoSourceCode2(table.select("input[name=managerList[2].inte_manager_datafrom]").val());
			model.setManagerUpdateTime2(StringUtils.trim(table.select("input[name=managerList[2].uploadtimef]").val()));
			// 财务负责人
			model.setManagerName3(table.select("input[name=managerList[3].managername]").val());
			String setCertifiTypes3[] = findSelected(table.select("select[name=managerList[3].certifitype]").first());
			model.setCertifiType3(setCertifiTypes3[0]);
			model.setCertifiTypeStr3(setCertifiTypes3[1]);
			model.setCertificode3(table.select("input[name=managerList[3].certificode]").val());

			model.setManagerInfoSourceCode3(table.select("input[name=managerList[3].inte_manager_datafrom]").val());
			model.setManagerUpdateTime3(StringUtils.trim(table.select("input[name=managerList[3].uploadtimef]").val()));
			// 监事长
			model.setManagerName4(table.select("input[name=managerList[4].managername]").val());
			String setCertifiTypes4[] = findSelected(table.select("select[name=managerList[4].certifitype]").first());
			model.setCertifiType4(setCertifiTypes4[0]);
			model.setCertifiTypeStr4(setCertifiTypes4[1]);
			model.setCertificode4(table.select("input[name=managerList[4].certificode]").val());

			model.setManagerInfoSourceCode4(table.select("input[name=managerList[4].inte_manager_datafrom]").val());
			model.setManagerUpdateTime4(StringUtils.trim(table.select("input[name=managerList[4].uploadtimef]").val()));
			/* 上级机构（主管单位）信息 */
			// 上级企业名称
			model.setParCorpName(table.select("input[name=uporgname]").val());
			// 上级机构信用代码证编号
			model.setParOrgEccsNo(table.select("input[name=upcreditcode]").val());
			// 上级组织机构代码
			model.setParOrgCode(table.select("input[name=upsdeporgcode]").val());
			// 上级登记注册类型
			String parRegTypes[] = findSelected(table.select("select[name=upsaccfiletype]").first());
			model.setParRegType(parRegTypes[0]);
			model.setParRegTypeStr(parRegTypes[1]);
			// 上级登记注册编号
			model.setParRegNo(table.select("input[name=upsaccfilecode]").val());

			model.setParCorpInfoSourceCode(table.select("input[name=uporginformationdatafrom]").val());
			model.setParCorpUpdateTime(table.select("input[name=uporginformationuptime]").val());
			/*
			 * // 实际控制人 TODO待编写 String[] controlType1 =
			 * findSelected(table.select
			 * ("select[name=controlList[0].controltype]").first());
			 * model.setControlType1(controlType1[0]);
			 * model.setControlTypeStr1(controlType1[1]);
			 * model.setControlName1(table
			 * .select("input[name=controlList[0].controlername]").val()); //
			 * 重要控股人 TODO待编写
			 */
		} else {
			throw new SyncException("获取原先未变更前机构信用代码证信息页面异常");
		}
	}

	/**
	 * 获取变更公共的对象
	 * 
	 * @param model
	 * @param table
	 */
	private void getCommonChangeInfo(EccsAccountInfo model, Element table) {
		// 组织机构代码证
		model.setOrgCode(StringUtils.trim(table.select("input[name=idBean.sdeporgcode]").val()));
		// 贷款卡编码
		model.setBankCardNo(StringUtils.trim(table.select("input[name=idBean.loancardcode]").val()));
		// 开户许可证
		model.setAccountKey(StringUtils.trim(table.select("input[name=idBean.saccbaselicno]").val()));
		// 登记注册号类型
		String regTypes[] = findSelected(table.select("select[name=idBean.registertype").get(0));
		model.setRegType(regTypes[0]);
		model.setRegTypeStr(regTypes[1]);
		// 登记注册号
		model.setRegNo(StringUtils.trim(table.select("input[name=idBean.registercode]").val()));
		// 地税
		model.setStateTaxRegNo(StringUtils.trim(table.select("input[name=idBean.sdepnationaltaxcode]").val()));
		// 国税
		model.setTaxRegNo(StringUtils.trim(table.select("input[name=idBean.sdeplandtaxcode]").val()));
		// 存款人名称
		model.setDepositorName(StringUtils.trim(table.select("input[name=idBean.sdepnamec]").val()));
		// 英文名称
		model.setOrgEnName(StringUtils.trim(table.select("input[name=idBean.sdepnamee]").val()));
		// 省市区
		String countrys[] = findSelected(table.select("select[name=country").get(0));
		model.setCountry(countrys[0]);
		model.setCountryStr(selectValue(countrys[1]));
		String provCodes[] = findSelected(table.select("select[name=provCode").get(0));
		model.setProvCode(provCodes[0]);
		model.setProvCodeStr(selectValue(provCodes[1]));
		String cityCodes[] = findSelected(table.select("select[name=cityCode").get(0));
		model.setCityCode(cityCodes[0]);
		model.setCityCodeStr(selectValue(cityCodes[1]));
		String countyCodes[] = findSelected(table.select("select[name=countyCode").get(0));
		model.setCountyCode(countyCodes[0]);
		model.setCountyCodeStr(selectValue(countyCodes[1]));
		model.setRegAddress(StringUtils.trim(table.select("textarea[name=areaaddr]").text()));
		// 登记部门
		String regOffices[] = findSelected(table.select("select[name=idBean.departkind").get(0));
		model.setRegOffice(regOffices[0]);
		model.setRegOfficeStr(selectValue(regOffices[1]));
		// 成立时间
		model.setSetupDate(table.select("input[name=builddate]").val());
		// 证书到期日
		model.setEffectiveDate(table.select("input[name=maturitydate]").val());
		// 经营范围
		model.setBusinessScope(table.select("input[name=sdepwork]").val());
		// 注册资本
		model.setRegisteredCapital(table.select("input[name=sdepfund]").val());
		String regCurrencyTypes[] = findSelected(table.select("select[name=sdepfundkind").first());
		model.setRegCurrencyType(regCurrencyTypes[0]);
		model.setRegCurrencyTypeStr(regCurrencyTypes[1]);
		if (true) {
			model.setRegCurrencyType("CNY");
			model.setRegCurrencyTypeStr("人民币");
		}
		// 组织机构类别
		String orgTypes[] = findSelected(table.select("select[name=idBean.orgkind]").first());
		model.setOrgType(orgTypes[0]);
		model.setOrgTypeStr(selectValue(orgTypes[1]));
		String orgTypeDetails[] = findSelected(table.select("select[name=orgkindsub]").first());
		model.setOrgTypeDetails(orgTypeDetails[0]);
		model.setOrgTypeDetailsStr(orgTypeDetails[1]);
		// 经济行业分类
		model.setEconomyIndustry(table.select("input[name=vocationCode]").val());
		model.setEconomyIndustryStr(table.select("input[name=vocationName]").val());
		// 经济类型
		String economyTypes[] = findSelected(table.select("select[name=economytype]").first());
		model.setEconomyType(economyTypes[0]);
		model.setEconomyTypeStr(selectValue(economyTypes[1]));
		// 法人信息
		model.setLegalName(table.select("input[name=managerList[0].managername]").val());
		String legalIdcardTypes[] = findSelected(table.select("select[name=managerList[0].certifitype]").get(0));
		model.setLegalIdcardType(legalIdcardTypes[0]);
		model.setLegalIdcardTypeStr(selectValue(legalIdcardTypes[1]));
		model.setLegalIdcardNo(table.select("input[name=managerList[0].certificode]").val());
		// 机构状态
		String orgStatuses[] = findSelected(table.select("select[name=orgstate]").first());
		model.setOrgStatus(orgStatuses[0]);
		model.setOrgStatusStr(orgStatuses[1]);
		// 基本户状态
		String accountStatuses[] = findSelected(table.select("select[name=state]").first());
		model.setAccountStatus(accountStatuses[0]);
		model.setAccountStatusStr(accountStatuses[1]);
		// 企业规模
		String corpScales[] = findSelected(table.select("select[name=vocationamount]").first());
		model.setCorpScale(corpScales[0]);
		String corpScaleStr = corpScales[1];
		model.setCorpScaleStr(selectValue(corpScaleStr));
		// 办公（生产、经营）地址
		String provCodeWs[] = findSelected(table.select("select[name=provCodeW]").first());
		model.setProvCodeW(provCodeWs[0]);
		model.setProvCodeWStr(selectValue(provCodeWs[1]));
		String cityCodeWs[] = findSelected(table.select("select[name=cityCodeW]").first());
		model.setCityCodeW(cityCodeWs[0]);
		model.setCityCodeWStr(selectValue(cityCodeWs[1]));
		String countyCodeWs[] = findSelected(table.select("select[name=countyCodeW]").first());
		model.setCountyCodeW(countyCodeWs[0]);
		model.setCountyCodeWStr(selectValue(countyCodeWs[1]));
		model.setWorkAddress(table.select("textarea[name=workaddress]").text());
		// 联系电话
		model.setWorkTelephone(table.select("input[name=worktelephone]").val());
		// 财务部联系电话
		model.setFinanceTelephone(table.select("input[name=financetelephone]").val());
		// 董事长
		model.setManagerName1(table.select("input[name=managerList[1].managername]").val());
		String setCertifiTypes1[] = findSelected(table.select("select[name=managerList[1].certifitype]").first());
		model.setCertifiType1(setCertifiTypes1[0]);
		model.setCertifiTypeStr1(setCertifiTypes1[1]);
		model.setCertificode1(table.select("input[name=managerList[1].certificode]").val());
		// 总经理/主要负责人
		model.setManagerName2(table.select("input[name=managerList[2].managername]").val());
		String setCertifiTypes2[] = findSelected(table.select("select[name=managerList[2].certifitype]").first());
		model.setCertifiType2(setCertifiTypes2[0]);
		model.setCertifiTypeStr2(setCertifiTypes2[1]);
		model.setCertificode2(table.select("input[name=managerList[2].certificode]").val());
		// 财务负责人
		model.setManagerName3(table.select("input[name=managerList[3].managername]").val());
		String setCertifiTypes3[] = findSelected(table.select("select[name=managerList[3].certifitype]").first());
		model.setCertifiType3(setCertifiTypes3[0]);
		model.setCertifiTypeStr3(setCertifiTypes3[1]);
		model.setCertificode3(table.select("input[name=managerList[3].certificode]").val());
		// 监事长
		model.setManagerName4(table.select("input[name=managerList[4].managername]").val());
		String setCertifiTypes4[] = findSelected(table.select("select[name=managerList[4].certifitype]").first());
		model.setCertifiType4(setCertifiTypes4[0]);
		model.setCertifiTypeStr4(setCertifiTypes4[1]);
		model.setCertificode4(table.select("input[name=managerList[4].certificode]").val());
		// 上级企业名称
		model.setParCorpName(table.select("input[name=uporgname]").val());
		// 上级机构信用代码证编号
		model.setParOrgEccsNo(table.select("input[name=upcreditcode]").val());
		// 上级组织机构代码
		model.setParOrgCode(table.select("input[name=upsdeporgcode]").val());
		// 上级登记注册类型
		String parRegTypes[] = findSelected(table.select("select[name=upsaccfiletype]").first());
		model.setParRegType(parRegTypes[0]);
		model.setParRegTypeStr(selectValue(parRegTypes[1]));
		// 上级登记注册编号
		model.setParRegNo(table.select("input[name=upsaccfilecode]").val());
	}

	/**
	 * 查询 select下拉框下 当前选中的option
	 * 
	 * @param select
	 * @return str[0]值 str[1]显示的字
	 */
	private static String[] findSelected(Element select) {
		String[] str = new String[2];
		if (select == null) {
			return str;
		}
		Elements options = select.getElementsByTag("option");

		if (options.size() > 0) {
			str[0] = options.get(0).val();
			str[1] = options.get(0).html();
			for (Element option : options) {
				if (option.toString().indexOf("selected") > -1) {
					str[0] = option.val();
					str[1] = option.html();
					break;
				}
			}
		}
		return str;
	}
	
	private String selectValue(String val){
		if (org.apache.commons.lang.StringUtils.indexOf(val, "请选择") > -1)
			val = "";
		return val;
	}
	
}
