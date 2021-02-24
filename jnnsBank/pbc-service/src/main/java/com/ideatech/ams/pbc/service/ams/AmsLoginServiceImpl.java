package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.AmsAuth;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



@Component
public class AmsLoginServiceImpl implements AmsLoginService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	PbcMockService pbcMockService;

	@Override
	public LoginAuth amsLogin(String ip, String amsUserName, String amsUserPwd) {
		return login(ip, amsUserName, amsUserPwd, 0);
	}

	private LoginAuth login(String ip, String amsUserName, String amsUserPwd, int tryNum) {
		LoginAuth auth = new AmsAuth(ip, amsUserName, amsUserPwd);
		//2018年7月19日 如果挡板打开，此处返回登录成功
		if (pbcMockService.isLoginMockOpen()) {
			logger.info("人行挡板开启，默认返回登录成功");
			auth.setLoginStatus(LoginStatus.Success);
			auth.setCookie(new BasicCookieStore());
			return auth;
		}
		if (tryNum > 0) {
			try {
				Thread.sleep(500);// 暂停1秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (tryNum > 2) {
			auth.setLoginStatus(LoginStatus.PageUnknow);
			return auth;
		}
		String url = auth.getDomain() + AmsConfig.ZG_URL_LOGIN;
		if (StringUtils.isNotBlank(amsUserName)) {
			amsUserName = StringUtils.trimToEmpty(amsUserName);
		}
		if (StringUtils.isNotEmpty(amsUserPwd)) {
			amsUserPwd = StringUtils.trimToEmpty(amsUserPwd);
		}
		// 密码长度最大为8位
		if (StringUtils.isNotEmpty(amsUserPwd) && StringUtils.length(amsUserPwd) > 8) {
			amsUserPwd = amsUserPwd.substring(0, 8);
		}
		// 用户名最大长度为6
		if (StringUtils.isNotEmpty(amsUserName) && StringUtils.length(amsUserName) > 6) {
			amsUserName = amsUserName.substring(0, 6);
		}
		// 登录参数
		String urlPars = "txcode=login&clientDate={0}&loginInfo.slogincode={1}&loginInfo.spassword={2}";
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		urlPars = MessageFormat.format(urlPars, date, amsUserName, amsUserPwd);
		HttpResult result = null;
		try {
			result = HttpUtils.post(url, urlPars, HttpConfig.HTTP_ENCODING, null, null);
			result.makeHtml(HttpConfig.HTTP_ENCODING);
			String html = result.getHtml();
			if (result.getStatuCode() == 200) {
				if (html.indexOf("/ams/showBoard.do?method=showBoard") > -1) {// 登录成功
					auth.setLoginStatus(LoginStatus.Success);
					auth.setCookie(result.getCookies());
					// 判断人行是否已经关闭
				} else if (html.indexOf(AmsConfig.ZG_URL_LOGIN) > -1) { // 用户名、密码错误
					Document doc = Jsoup.parse(html);
					String errorInfo = doc.select("font[color=red]").text();
					if (errorInfo.indexOf("用户不存在") > -1 || errorInfo.indexOf("查询信息出现异常") > -1) {
						logger.info("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>账户不存在");
						auth.setLoginStatus(LoginStatus.UserNameError);
					} else if (errorInfo.indexOf("用户被锁定") > -1) {
						logger.info("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>用户被锁");
						auth.setLoginStatus(LoginStatus.UserNameLock);
					} else if (errorInfo.indexOf("密码错误") > -1) {
						logger.info("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>密码错误");
						auth.setLoginStatus(LoginStatus.PasswordError);
					} else if (errorInfo.indexOf("用户被停用") > -1) {
						logger.info("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>用户被停用");
						auth.setLoginStatus(LoginStatus.UserStop);
					} else if (errorInfo.indexOf("客户机的系统日期") > -1) {
						logger.info("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>" + errorInfo);
						auth.setLoginStatus(LoginStatus.LoginTimeError);
					} else if (errorInfo.indexOf("Transaction is already completed") > 0) {
						auth.setLoginStatus(LoginStatus.AmsException);
						logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回人行账管后台异常>>" + html);
					} else {
						logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回未知处理异常>>" + html);
						auth.setLoginStatus(LoginStatus.PageUnknow);
					}
				} else if (html.indexOf("modifyOperatorPassword.do") > -1) { // 密码过期
					logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>密码过期");
					auth.setLoginStatus(LoginStatus.PasswordExpire);
					auth.setCookie(result.getCookies());
				} else if (html.indexOf("Internal Server Error") > -1) {
					logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回错误>>>人行账管系统无法访问");
					auth.setLoginStatus(LoginStatus.AmsClose);
				} else {
					logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回未知异常页面>>" + html);
					return login(ip, amsUserName, amsUserPwd, tryNum + 1);
				}
			} else {
				logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录返回未知异常,状态为" + result.getStatuCode() + ">>" + html);
				return login(ip, amsUserName, amsUserPwd, tryNum + 1);
			}
		} catch (Exception e) {
			if (e instanceof HttpHostConnectException || e instanceof ConnectException || e instanceof SocketException) {
				logger.error("[" + amsUserName + ":" + amsUserPwd + " ]登录异常>>>网络不通", e);
				auth.setLoginStatus(LoginStatus.NetNotConn);
			} else if (e instanceof SocketTimeoutException) {
				logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录异常>>>网络超时", e);
				auth.setLoginStatus(LoginStatus.NetTimeOut);
			} else {
				logger.error("[" + amsUserName + ":" + amsUserPwd + "]登录异常>>>网络异常", e);
				auth.setLoginStatus(LoginStatus.NetUnknow);
			}
		}
		return auth;
	}

	@Override
	public PwdModifyStatus modifyPassWord(LoginAuth loginAuth, String oldPassWord, String newPassword) {
		HttpResult result = null;
		String url = loginAuth.getDomain() + "/ams/modifyOperatorPassword.do?method=forModifyPasswordCommit";
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("txcode=&suserpass=").append(oldPassWord);
		urlPars.append("&snewuserpass=").append(newPassword);
		urlPars.append("&snewuserpass2=").append(newPassword);
		List<Header> headers = HeadsCache.getRhzgCommon(loginAuth.getDomain());
		try {
			result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, loginAuth.getCookie(), headers);
		} catch (Exception e) {
			logger.error("修改密码异常", e);
			return PwdModifyStatus.NetException;
		}
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (StringUtils.isNotBlank(html) && html.indexOf("密码修改成功") > -1) {
			logger.info("人行用户名、密码修改成功！！原先密码：" + oldPassWord + "》》现有密码：" + newPassword);
			return PwdModifyStatus.Success;
		} else {
			logger.info("人行用户名、密码修改失败！！ 请求页面" + html);
			return PwdModifyStatus.PwdMidfyFail;
		}
	}

	@Override
	public LoginStatus validateAmsLogin(PbcUserAccount amsUserAccount) {
		if (amsUserAccount == null) {
			return LoginStatus.PassWordEmpty;
		}
		if (StringUtils.isBlank(amsUserAccount.getLoginIp())) {
			return LoginStatus.PbcIpEmpty;
		}
		if (StringUtils.isBlank(amsUserAccount.getLoginUserName()) || StringUtils.isBlank(amsUserAccount.getLoginPassWord())) {
			return LoginStatus.PassWordEmpty;
		}
		if (!amsUserAccount.getLoginUserName().substring(0, 1).equals("2") && !amsUserAccount.getLoginUserName().substring(0, 1).equals("4")) {
			return LoginStatus.MustTwoLevelRoleUser;
		}
		return LoginStatus.Success;
	}
}
