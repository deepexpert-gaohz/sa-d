package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ideatech.common.utils.StringUtils;

import java.util.List;


@Component
public class AmsYibanChangeServiceImpl implements AmsYibanChangeService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AmsSearchService amsSearchService;

	@Autowired
	AmsSyncOperateService amsSyncOperateService;

	@Override
	public void changeAccountFirstStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		// 登录用户所在机构的机构号
		String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
		if (ArrayUtils.isNotEmpty(bankInfo)) {
			condition.setBankCode(bankInfo[1]);
			condition.setBankName(bankInfo[0]);
		}
		// 银行所在地地区代码
		String bankAreaCode = amsSearchService.getBankAreCode(auth, bankInfo[1])[1];
		condition.setBankAreaCode(bankAreaCode);
		// 校验
		if (StringUtils.isBlank(condition.getAcctNo())) {
			throw new SyncException("账号不能为空");
		}
		if (StringUtils.isBlank(condition.getBankCode()) || StringUtils.isBlank(condition.getBankName())) {
			throw new SyncException("人行机构代码、名称不能为空");
		}
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		HttpResult result = null;
		result = HttpUtils.get(auth.getDomain() + "/ams/changeCommon.do?method=forFrameForWard", auth.getCookie(), null);
		Thread.sleep(500);
		result = HttpUtils.get(auth.getDomain() + "/ams/changeCommon.do?method=forSearchChangeCommon", auth.getCookie(), null);
		Thread.sleep(500);
		//20200917  人行更新后增加页面解析  clx
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			//回调9月15号人行更新后函数
			amsSearchService.callbackMethod(auth,html);
		}
		String urlParms = getYibanChangeFirstStepParams(condition);
		logger.info("一般户变更上报第一步拼接参数打印：{}",urlParms);
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGE_GENERALVALIDATE;
//		HttpResult result = HttpUtils.post(url, urlParms, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result = HttpUtils.post(url, urlParms, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
//		String html = result.getHtml();
		html = result.getHtml();
		if (result.getStatuCode() == 200) {
			// 验证操作是否成功
			String validaStr = amsSyncOperateService.validateOp(html, "变更信息录入", auth, condition);
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			amsSearchService.callbackMethod(auth,html);
		} else {
			logger.info("账户" + condition.getAcctNo() + "校验是否可以变更异常" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	@Override
	public AmsAccountInfo changeAccountSecondStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		String urlParms = getYibanChangeSecondStepParams(condition);
		logger.info("一般户变更上报第二步拼接参数打印：{}",urlParms);
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGE_SHOWBASEINFO;
		HttpResult result = HttpUtils.post(url, urlParms, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			String validaStr = amsSyncOperateService.validateOp(html, "变更信息核对");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			getAmsAccountByAcctNoReturnHtml(html);
			amsSearchService.callbackMethod(auth,html);
		} else {
			logger.info("账号" + condition.getAcctNo() + "变更时反显客户信息时异常" + html);
			throw new SyncException("账号" + condition.getAcctNo() + "变更时反显客户信息时异常");
		}
		return amsAccountInfo;
	}

	@Override
	public void changeAccountLastStep(LoginAuth auth) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGE_OPENGENERALACCTINFO;
		HttpResult result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			// 验证操作是否成功
			String validaStr = amsSyncOperateService.validateOp(html, "成功");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
		} else {
			logger.info("账户变更提交时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	/**
	 * 获取账户变更接口第一步接口参数
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private String getYibanChangeFirstStepParams(AmsYibanSyncCondition condition) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("authorizationFlag=0&alertFlag=0");
		urlPars.append("&errorMsg=&authorizationMsg=&msgSingleId=&operateType=");
		urlPars.append("&saccbankcode=").append(condition.getBankCode());
		urlPars.append("&saccno=").append(condition.getAcctNo());
		urlPars.append("&saccbanknamehidden=");
		urlPars.append(EncodeUtils.encodStr(condition.getBankName(), "gbk"));
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		return urlPars.toString();
	}

	/**
	 * 获取一般户变更第二步接口参数
	 * 
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	private String getYibanChangeSecondStepParams(AmsYibanSyncCondition conditon) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("&saccfiletype1=").append(conditon.getAccountFileType());
		urlPars.append("&saccfilecode1=");
		urlPars.append(EncodeUtils.encodStr(conditon.getAccountFileNo(), "gbk"));
		urlPars.append("&sremark=");
		urlPars.append(EncodeUtils.encodStr(conditon.getRemark(), "gbk"));
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		return urlPars.toString();
	}

	/**
	 * 变更第二步时查看对应账号客户信息
	 * 
	 * @param html
	 * @return
	 * @throws SyncException
	 */
	private AmsAccountInfo getAmsAccountByAcctNoReturnHtml(String html) throws SyncException {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
		// 存款人名称
		amsAccountInfo.setDepositorName(eleValues.get(0).html().replace("&nbsp;", "").trim());
		// 电话
		amsAccountInfo.setTelephone(eleValues.get(1).html().replace("&nbsp;", "").trim());
		// 地址
		//特殊字符转义
		String address = eleValues.get(2).html().replace("&nbsp;", "").trim();
		if(StringUtils.isNotBlank(address)){
			address = StringEscapeUtils.unescapeHtml(address);
		}
		amsAccountInfo.setRegAddress(address);
		// 邮政编码
		amsAccountInfo.setZipCode(eleValues.get(3).html().replace("&nbsp;", "").trim());
		// 存款人类别")) {
		amsAccountInfo.setDepositorType(eleValues.get(4).html().replace("&nbsp;", "").trim());
		// 组织机构代码") && i == 5) {
		amsAccountInfo.setOrgCode(eleValues.get(5).html().replace("&nbsp;", "").trim());
		// 姓名") && i == 7) {
		String legalName = StringEscapeUtils.unescapeHtml(eleValues.get(7).html().replace("&nbsp;", "").trim());
		amsAccountInfo.setLegalName(legalName);
		// 证件种类") && i == 8) {
		amsAccountInfo.setLegalIdcardType(eleValues.get(8).html().replace("&nbsp;", "").trim());
		// 证件号码") && i == 9) {
		amsAccountInfo.setLegalIdcardNo(eleValues.get(9).html().replace("&nbsp;", "").trim());
		// 注册地地区代码")) {
		amsAccountInfo.setRegAreaCode(eleValues.get(10).html().replace("&nbsp;", "").trim());
		// 注册资金币种")) {
		amsAccountInfo.setRegCurrencyType(eleValues.get(13).html().replace("&nbsp;", "").trim());
		// 注册资金")) {
		amsAccountInfo.setRegisteredCapital(eleValues.get(14).html().replace("&nbsp;", "").trim());
		// 经营范围
		amsAccountInfo.setBusinessScope(eleValues.get(15).html().replace("&nbsp;", "").trim());
		// 证明文件种类")) {
		amsAccountInfo.setFileType(eleValues.get(16).html().replace("&nbsp;", "").trim());
		// 证明文件编号")) {
		amsAccountInfo.setFileNo(eleValues.get(17).html().replace("&nbsp;", "").trim());
		// 证明文件种类2
		amsAccountInfo.setFileType2(eleValues.get(18).html().replace("&nbsp;", "").trim());
		// 证明文件编号2
		amsAccountInfo.setFileNo2(eleValues.get(19).html().replace("&nbsp;", "").trim());
		// 国税登记证号")) {
		amsAccountInfo.setStateTaxRegNo(eleValues.get(20).html().replace("&nbsp;", "").trim());
		// 地税登记证号")) {
		amsAccountInfo.setTaxRegNo(eleValues.get(21).html().replace("&nbsp;", "").trim());
		// 单位名称")) {
		amsAccountInfo.setParCorpName(eleValues.get(28).html().replace("&nbsp;", "").trim());
		// 基本存款账户开户许可证核准号")) {
		amsAccountInfo.setParAccountKey(eleValues.get(29).html().replace("&nbsp;", "").trim());
		// 组织机构代码") && i == 24) {
		amsAccountInfo.setParOrgCode(eleValues.get(30).html().replace("&nbsp;", "").trim());
		// 法定代表人或单位负责人")) {
		String parlegelTypeValue = eleValues.get(31).getElementsByAttribute("checked").val();
		if (parlegelTypeValue.equals("1")) {
			amsAccountInfo.setParLegalType("法定代表人");
		} else if (parlegelTypeValue.equals("2")) {
			amsAccountInfo.setParLegalType("单位负责人");
		}
		// "姓名") && i == 26) {
		amsAccountInfo.setParLegalName(eleValues.get(32).html().replace("&nbsp;", "").trim());
		// 证件种类") && i == 27) {
		amsAccountInfo.setParLegalIdcardType(eleValues.get(33).html().replace("&nbsp;", "").trim());
		// 证件号码") && i == 28) {
		amsAccountInfo.setParLegalIdcardNo(eleValues.get(34).html().replace("&nbsp;", "").trim());
		// 开户日期")) {
		amsAccountInfo.setAcctCreateDate(eleValues.get(36).html().replace("&nbsp;", "").trim());
		// 开户银行名称")) {
		// amsAccountInfo.setBankName(eleValues.get(38).html().replace("&nbsp;", "").trim());
		// 开户银行代码")) {
		// amsAccountInfo.setBankCode(eleValues.get(39).html().replace("&nbsp;", "").trim());
		// 账号")) {
		amsAccountInfo.setAcctNo(eleValues.get(40).html().replace("&nbsp;", "").trim());
		// 一般户证明文件种类")) {
		// amsAccountInfo.setFileGeneralType(eleValues.get(41).html().replace("&nbsp;",
		// "").trim());
		// 一般户证明文件编号")) {
		amsAccountInfo.setAccountFileNo(eleValues.get(42).html().replace("&nbsp;", "").trim());
		return amsAccountInfo;
	}
}
