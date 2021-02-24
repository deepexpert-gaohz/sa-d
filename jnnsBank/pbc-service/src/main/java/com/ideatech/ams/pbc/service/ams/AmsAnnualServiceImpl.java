package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAnnualInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class AmsAnnualServiceImpl implements AmsAnnualService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AmsSearchService amsSearchService;

	@Override
	public AmsAnnualResultStatus gotoAnnuanlPage(LoginAuth auth) throws SyncException, Exception {
		HttpResult result = null;
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ANNUAL_INPUT;
		try {
			result = HttpUtils.get(queryUrl, auth.getCookie(), null);
		} catch (Exception e) {
			logger.error("账户访问待年检页面异常", e);
			return AmsAnnualResultStatus.Exception;
		}
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			if (html.indexOf("待年检账户信息录入") > -1) {
				return AmsAnnualResultStatus.Success;
			} else if (html.indexOf("当前系统正在运行后台批处理") > -1) {
				throw new SyncException("账管系统后台服务已关闭 ");
			} else if (html.indexOf("用户信息失效") > 0) {
				throw new SyncException("用户登录信息失效");
			} else if (html.indexOf("您没有执行<银行结算账户年检>功能的权限") > -1) {
				return AmsAnnualResultStatus.NotPower;
			} else if (html.indexOf("未在年检期间") > -1) {
				return AmsAnnualResultStatus.NotAnnualTime;
			} else {
				logger.info("进入待年检账户信息录入页面异常" + html);
				return AmsAnnualResultStatus.InputAcctError;
			}
		} else {
			logger.info("用户登录信息失效,statuCode:" + result.getStatuCode() + ",返回的html:" + html);
			throw new SyncException("用户登录信息失效");
		}

	}

	@Override
	public AmsAnnualResultStatus validAcctNoAnnual(LoginAuth auth, String acctNo, AmsAnnualInfo amsAnnualInfo) throws Exception {
		if (StringUtils.isBlank(acctNo)) {
			throw new SyncException("年检账号不能为空");
		}
		if (amsAnnualInfo == null) {
			amsAnnualInfo = new AmsAnnualInfo();
			amsAnnualInfo.setAcctNo(acctNo);
		}
		HttpResult result = null;

		// 登录用户所在机构的机构号
		String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ANNUAL_SEARCH;
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("searchAccFormVO.ssearchtype=1");
		urlPars.append("&searchAccFormVO.saccid=").append(acctNo);
		urlPars.append("&searchAccFormVO.sacclicno=");
		urlPars.append("&searchAccFormVO.sbankcode=").append(bankInfo[1]);
		urlPars.append("&searchAccFormVO.sbankname=").append(EncodeUtils.encodStr(bankInfo[0], "gbk"));
		try {
			result = HttpUtils.post(queryUrl, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
			result.makeHtml(HttpConfig.HTTP_ENCODING);
		} catch (Exception e) {
			logger.error("校验账户" + acctNo + "是否可年检异常", e);
			return AmsAnnualResultStatus.Exception;
		}
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			if (html.indexOf("当前系统正在运行后台批处理") > 0) {
				throw new SyncException("账管系统后台服务已关闭 ");
			} else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
				throw new SyncException("用户登录信息失效");
			} else if (html.indexOf("信息核对") > -1) {
				amsAnnualInfo = getAnnnaulAcctNoInfo(html);
				return AmsAnnualResultStatus.Success;
			} else if (html.indexOf("年检账户不存在") > -1) {
				return AmsAnnualResultStatus.NotFind;
			} else if (html.indexOf("该账户已撤销") > -1) {
				return AmsAnnualResultStatus.Revoke;
			} else if (html.indexOf("该账户为久悬状态") > -1) {
				return AmsAnnualResultStatus.Suspend;
			} else if (html.indexOf("开户年份大于年检年份") > -1) {
				return AmsAnnualResultStatus.NewAccount;
			} else if (html.indexOf("不能再次年检") > -1) {
				return AmsAnnualResultStatus.AlreadyAnnual;
			} else if (html.indexOf("逾期") > -1) {
				return AmsAnnualResultStatus.AcctExpire;
			} else if (html.indexOf("未在年检期间") > -1) {
				return AmsAnnualResultStatus.NotAnnualTime;
			} else {
				logger.info("账管系统校验账户是否满足年检出现未知情况:" + html);
				return AmsAnnualResultStatus.Exception;
			}
		} else {
			logger.info("账管系统校验账户是否满足年检出现未知情况,StatusCode为:" + result.getStatuCode() + ",返回结果页面" + html);
			throw new SyncException("用户登录信息失效");
		}

	}

	@Override
	public AmsAnnualResultStatus sumitAnnualAccount(LoginAuth auth, String acctNo) throws SyncException, Exception {
		HttpResult result = null;
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ANNUAL_SUBMIT;
		StringBuffer urlPars = new StringBuffer();
		try {
			urlPars.append("back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
			result = HttpUtils.post(queryUrl, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
			result.makeHtml(HttpConfig.HTTP_ENCODING);
		} catch (Exception e) {
			logger.error("提交账户" + acctNo + "年检异常", e);
			return AmsAnnualResultStatus.Exception;
		}
		String html = result.getHtml();
		if (html.indexOf("当前系统正在运行后台批处理") > 0) {
			throw new SyncException("账管系统后台服务已关闭 ");
		} else if (html.indexOf("用户信息失效") > 0|| html.indexOf("用户登录信息失效") > 0) {
			throw new SyncException("用户登录信息失效");
		}
		if (result.getStatuCode() == 200) {
			if (html.indexOf("年检成功") < 0) {
				logger.info("账号" + acctNo + "年检提交失败:" + html);
				return AmsAnnualResultStatus.SumbitFail;
			}
		} else {
			logger.info("账号" + acctNo + "年检提交访问页面异常" + html);
			throw new SyncException("用户登录信息失效");
		}
		return AmsAnnualResultStatus.Success;

	}

	/**
	 * 获取年检账户的企业信息
	 * 
	 * @param html
	 */
	private AmsAnnualInfo getAnnnaulAcctNoInfo(String html) {
		AmsAnnualInfo amsAnnualInfo = new AmsAnnualInfo();
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
		amsAnnualInfo.setDepositorName(eleValues.get(0).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setTelephone(eleValues.get(1).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setRegAddress(eleValues.get(2).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setZipCode(eleValues.get(3).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setOrgCode(eleValues.get(5).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setLegalIdcardType(eleValues.get(7).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setLegalName(eleValues.get(6).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setLegalIdcardNo(eleValues.get(8).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setRegCurrencyType(eleValues.get(10).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setRegisteredCapital(eleValues.get(11).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setBusinessScope(eleValues.get(12).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setFileType(eleValues.get(13).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setFileNo(eleValues.get(14).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setStateTaxRegNo(eleValues.get(19).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setTaxRegNo(eleValues.get(20).html().replace("&nbsp;", "").trim());
		amsAnnualInfo.setAcctType(eleValues.get(30).html().replace("&nbsp;", "").trim());
		if (eleValues.size() > 33) {
			amsAnnualInfo.setAccountKey(eleValues.get(33).html().replace("&nbsp;", "").trim());
		}
		return amsAnnualInfo;
	}

}
