package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.EccsConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.eccs.EccsChangeAccountParamService;
import com.ideatech.ams.pbc.service.eccs.EccsSearchService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 机构信用代码证系统基本户变更接口
 * 
 * @author zoulang
 *
 */
@Component
public class EccsJibenChangeSynchronizer extends AbstractSynchronizer {
	protected final static String eccsChart = "utf-8";

	@Autowired
	EccsSearchService eccsSearchService;

	@Autowired
	EccsChangeAccountParamService eccsChangeAccountParamService;

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		StringBuffer urlParamsBuffer = new StringBuffer();
		EccsSearchCondition eccsSearchCondition = allAcct.getEccsSerarchCondition();
		if (allAcct.getEccsSerarchCondition() != null) {
			PbcBussUtils.printObjectColumn(allAcct.getEccsSerarchCondition());
		}
		validEccsSearchCondtion(eccsSearchCondition);// 校验查询条件
		// 查询条件
		StringBuffer stringSearchBuffer = eccsSearchService.getEccsSearchUrlParams(eccsSearchCondition);
		// 查询出对象
		EccsAccountInfo eccsAccountInfo = eccsSearchService.getEccsAccountInfo(auth, stringSearchBuffer);
		if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
			return;
		}
		if (eccsAccountInfo.isInfoisOk()) {// 账户信息已发放
			urlParamsBuffer = eccsChangeAccountParamService.getChangeXydmzUrl(eccsAccountInfo, allAcct); // 变更详细参数
			modifyExistEccsInfo(auth, stringSearchBuffer.toString(), urlParamsBuffer, eccsAccountInfo);
		} else {
			urlParamsBuffer = eccsChangeAccountParamService.getNotFaFangChangeXydmzUrl(eccsAccountInfo, allAcct); // 变更详细参数
			modifyNoFaFangEccsInfo(auth, eccsAccountInfo.getAssetProjId(), stringSearchBuffer.toString(), urlParamsBuffer);
		}
	}

	/**
	 * 修改机构信用代码证已发放账户信息
	 * @param loginAuth
	 * @param urlPars
	 * @param urlParsDeatils
	 * @param xydmzAccountModel
	 * @throws SyncException
	 * @throws Exception
	 */
	private void modifyExistEccsInfo(LoginAuth loginAuth, String urlPars, StringBuffer urlParsDeatils, EccsAccountInfo xydmzAccountModel) throws SyncException, Exception {
		String html = "";
		HttpResult result = new HttpResult();
		List<Header> headers = HeadsCache.getXydmzCommon();
		String queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MODIFYDETAIL + xydmzAccountModel.getAssetProjId();
		result = HttpUtils.post(queryUrl, urlPars.toString(), EccsConfig.ENCODING, loginAuth.getCookie(), headers);
		result.makeHtml(EccsConfig.ENCODING);
		html = result.getHtml();
		Document doc = Jsoup.parse(html);
		if (html.indexOf("代码信息维护") < 0 && html.indexOf("机构信息更新") < 0) {
			throw new SyncException("接口链接失败,数据无法修改，请重试，再不行请联系管理员！");
		}
		Elements el = doc.getElementsByTag("input");
		int i = 0;
		for (Element element : el) {
			if (i > 13) {
				break;
			}
			if ("hidden".equals(element.attr("type"))) {
				String columName = element.attr("name").toString().trim();
				String columValue = element.val().trim();
				urlParsDeatils.append("&").append(columName).append("=");
				urlParsDeatils.append(EncodeUtils.encodStr(columValue, "utf-8"));
			}
			i++;
		}
		String urlParsDeatil = urlParsDeatils.toString();
		// 机构维护 跳转到维护页面
		queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MODIFYCOMMIT;
		result = HttpUtils.post(queryUrl, urlParsDeatil, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
		result.makeHtml(EccsConfig.ENCODING);
		html = result.getHtml();
		String status = html.substring(0, html.indexOf('|', 0));
		if (status.equals("0")) {
			queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MODIFYSUCCESS;
			result = HttpUtils.post(queryUrl, urlParsDeatil, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
			result.makeHtml(EccsConfig.ENCODING);
			html = result.getHtml();
			if (html.indexOf("修改信息保存成功") < 0) {
				logger.info("变更机构信用代码证失败:\n{}", html);
				throw new SyncException("信用代码机构变更失败");
			}
		} else if (html.indexOf("请重新登录") > -1) {
			throw new SyncException("访问时间超时，请重新提交");
		} else if (status.equals("1")) {
			throw new SyncException("您修改后的标识信息在系统中已经存在");
		} else if (status != "") {
			throw new SyncException(status);
		} else {
			logger.info("机构信用代码证变更操作失败:\n", html);
			throw new SyncException("代码信息维护操作失败");
		}
	}


	/**
	 * 修改未发放的机构信用代码证信息
	 * @param loginAuth
	 * @param hasCrcCode
	 * @param urlPars
	 * @param urlParsDeatils
	 * @throws SyncException
	 * @throws Exception
	 */
	private void modifyNoFaFangEccsInfo(LoginAuth loginAuth, String hasCrcCode, String urlPars, StringBuffer urlParsDeatils) throws SyncException, Exception {
		String html = "";
		HttpResult result = new HttpResult();
		List<Header> headers = HeadsCache.getXydmzCommon();
		String queryUrl = "";
		queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_FaFangModifyDetail + hasCrcCode;
		result = HttpUtils.post(queryUrl, urlPars.toString(), EccsConfig.ENCODING, loginAuth.getCookie(), headers);
		result.makeHtml(EccsConfig.ENCODING);
		html = result.getHtml();
		if (html.indexOf("代码信息更新") > -1) {
			Document document = Jsoup.parse(html);
			urlParsDeatils.append("&crccode=").append(document.select("input[name=crccode]").val());
			urlParsDeatils.append("&operationtimeold=").append(EncodeUtils.encodStr(document.select("input[name=operationtimeold]").val(), "utf-8"));
			commitEccsNoFaFangChangeAcct(urlParsDeatils.toString(), loginAuth);
		} else {
			throw new SyncException("参数不正确,组织机构代码或登记注册号码必填或联系系统管理员");
		}
	}

	/**
	 * 提交未发放信用代码证变更方法
	 * @param urlParsDeatils
	 * @param loginAuth
	 * @throws SyncException
	 * @throws Exception
	 */
	private void commitEccsNoFaFangChangeAcct(String urlParsDeatils, LoginAuth loginAuth) throws SyncException, Exception {
		List<Header> headers = HeadsCache.getXydmzCommon();
		String queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MODIFYCOMMIT;
		HttpResult result = HttpUtils.post(queryUrl, urlParsDeatils, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
		result.makeHtml(EccsConfig.ENCODING);
		String html = result.getHtml();
		String status = html.substring(0, html.indexOf('|', 0));
		if (status.equals("0")) {
			queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_CODESUCCESS;
			result = HttpUtils.post(queryUrl, urlParsDeatils, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
			result.makeHtml(EccsConfig.ENCODING);
			html = result.getHtml();
			if (html.indexOf("信息保存成功") < 0) {
				throw new SyncException("账户信用代码报送失败");
			}
		} else if (html.indexOf("请重新登录") > -1) {
			throw new SyncException("账户信用代码访问时间超时，请重新提交");
		} else if (status != "") {
			throw new SyncException(status);
		} else {
			logger.info("变更机构信用证失败:\n{}", html);
			throw new SyncException("变更机构信用证失败");
		}
	}

	/**
	 * 变更、查询机构信用代码证系统时校验查询条件
	 * 
	 * @param condition
	 * @throws SyncException
	 */
	private void validEccsSearchCondtion(EccsSearchCondition condition) throws SyncException {
		int num = 0;
		if (condition == null) {
			throw new SyncException("机构代码证查询对象不能为空");
		}
		if (StringUtils.isNotBlank(condition.getAccountKey())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getOrgCode())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getOrgEccsNo())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getRegNo()) && StringUtils.isNotBlank(condition.getRegType())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getStateTaxRegNo())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getTaxRegNo())) {
			num++;
		}
		if (num < 1) {
			throw new SyncException("机构信用代码证变更时,查询条件不能为空");
		}
	}
}
