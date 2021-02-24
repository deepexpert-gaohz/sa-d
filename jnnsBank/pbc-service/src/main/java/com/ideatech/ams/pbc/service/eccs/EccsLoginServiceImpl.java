package com.ideatech.ams.pbc.service.eccs;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.EccsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.EccsAuth;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EccsLoginServiceImpl implements EccsLoginService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected final static String eccsChart = "utf-8";

	@Autowired
	PbcMockService pbcMockService;

	@Override
	public LoginAuth eccsLogin(String ip, String eccsUserName, String eccsPassword) {
		// 登录参数
		if (StringUtils.isNotBlank(eccsUserName)) {
			eccsUserName = StringUtils.trimToEmpty(eccsUserName);
		}
		LoginAuth auth = new EccsAuth(ip, eccsUserName, eccsPassword);
		//2018年7月19日 如果挡板打开，此处返回登录成功
		if (pbcMockService.isLoginMockOpen()) {
			logger.info("人行挡板开启，默认返回登录成功");
			auth.setLoginStatus(LoginStatus.Success);
			auth.setCookie(new BasicCookieStore());
			return auth;
		}
		return login(auth, 0);
	}

	/**
	 * 登录 ，若有异常情况可登录多次
	 * 
	 * @param auth
	 * @param tryNum
	 * @return
	 */
	private LoginAuth login(LoginAuth auth, int tryNum) {
		if (tryNum > 3) {
			auth.setLoginStatus(LoginStatus.PageUnknow);
			return auth;
		}
		if (tryNum > 0) {
			try {
				Thread.sleep(500);// 暂停0.5秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String url = auth.getDomain() + EccsConfig.XYDMZ_URL_LOGIN;
		String urlPars = "userAuditorgId=0&op=logon&userId={0}&userPassword={1}";
		urlPars = MessageFormat.format(urlPars, auth.getLoginName(), auth.getLoginPwd());
		HttpResult result = null;
		try {
			if (StringUtils.isNotEmpty(auth.getLoginPwd())) {
				auth.setLoginPwd(StringUtils.trimToEmpty(EncodeUtils.encodStr(auth.getLoginPwd(), eccsChart)));
			}
			result = HttpUtils.post(url, urlPars, EccsConfig.ENCODING, null, null);
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (result.getStatuCode() == 200) {
				if (html.indexOf("登录用户名或密码不正确") > -1) { // 用户名或密码错误
					auth.setLoginStatus(LoginStatus.PasswordError);
					logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>密码错误");
				} else if (html.indexOf("<frame id=rightFrame name=\"rightFrame\"") > -1) { // 登录成功
					auth.setLoginStatus(LoginStatus.Success);
					auth.setCookie(result.getCookies());
				} else if (html.indexOf("账号已锁定") > -1) {
					auth.setLoginStatus(LoginStatus.UserNameLock);
					logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>账号已锁定");
				} else {
					Pattern p = Pattern.compile("var error = \\'\\s(.*?)\\';");
					Matcher m = p.matcher(html);
					if (m.find()) {
						if (m.group(1).trim().equals("past")) {
							auth.setCookie(result.getCookies());
							auth.setLoginStatus(LoginStatus.PasswordExpire);
							logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>密码已过期");
						} else if (m.group(1).trim().equals("first")) {
							auth.setCookie(result.getCookies());
							auth.setLoginStatus(LoginStatus.FirstLogin);
							logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>账号首次登录需修改密码");
						} else if (m.group(1).trim().contains("此用户已被停用")) {
							auth.setLoginStatus(LoginStatus.UserStop);
							logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>此用户已经被停用");
						} else if (m.group(1).trim().contains("&nbsp")) {
							Thread.sleep(500);// 暂停0.5秒钟
							return login(auth, tryNum + 1);
						} else {
							auth.setLoginStatus(LoginStatus.PageUnknow);
							logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>登录页异常信息捕获异常" + html);
						}
					} else {
						auth.setLoginStatus(LoginStatus.PageUnknow);
						logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>未知异常" + html);
					}
				}
			} else {
				auth.setLoginStatus(LoginStatus.PageUnknow);
				logger.info("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录返回错误>>>未知异常" + html);
			}
		} catch (Exception e) {
			if (e instanceof HttpHostConnectException || e instanceof ConnectException || e instanceof SocketException) {
				logger.error("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录异常>>>网络不通", e);
				auth.setLoginStatus(LoginStatus.NetNotConn);
			} else if (e instanceof SocketTimeoutException) {
				logger.error("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录异常>>>网络超时", e);
				auth.setLoginStatus(LoginStatus.NetTimeOut);
			} else {
				logger.error("[" + auth.getLoginName() + ":" + auth.getLoginPwd() + "]登录异常>>>未知异常", e);
				auth.setLoginStatus(LoginStatus.NetUnknow);
			}
		}
		return auth;
	}

	@Override
	public PwdModifyStatus modifyEccsPassWordBxpire(LoginAuth loginAuth, String oldPassWord, String newPassword, String userId) {
		HttpResult result = null;
		String url = "";
		if (StringUtils.isNotBlank(userId)) {
			url = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_UpdatePassword;
		} else {
			url = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_UpdatePasswordResult;
		}
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("oldpwd=").append(oldPassWord);
		urlPars.append("&newpwd=").append(newPassword);
		if (StringUtils.isNotBlank(userId)) {
			urlPars.append("&userid=").append(userId);
		}
		urlPars.append("&ran=").append(new Date().getTime());
		try {
			result = HttpUtils.post(url, urlPars.toString(), EccsConfig.ENCODING, loginAuth.getCookie(), null);
		} catch (Exception e) {
			logger.error("[" + loginAuth.getLoginName() + ":" + loginAuth.getLoginPwd() + "]登录异常>>>密码修改失败", e);
			return PwdModifyStatus.PwdMidfyFail;
		}
		result.makeHtml(EccsConfig.ENCODING);
		String html = result.getHtml();
		if (html.indexOf("userPassword") > -1 || StringUtils.isEmpty(html.trim()) || StringUtils.equals("1", html.trim())) {
			logger.info("密码修改成功");
		} else if (html.indexOf("v1") > -1) {
			logger.info("[原密码：" + oldPassWord + "新密码:" + newPassword + "] 原始密码输入错误");
			return PwdModifyStatus.OldPwdError;
		} else if (html.indexOf("v2") > -1) {
			logger.info("[原密码：" + oldPassWord + "新密码:" + newPassword + "] 新密码不能与前四次密码重复！");
			return PwdModifyStatus.PwdSameBeforeFour;
		} else {
			logger.info("密码修改失败" + html);
			return PwdModifyStatus.PwdMidfyFail;
		}
		return PwdModifyStatus.Success;
	}

	@Override
	public PwdModifyStatus modifyEccsPassWord(LoginAuth loginAuth, String oldPassWord, String newPassword) {
		String userId = null;
		if (loginAuth.getLoginStatus() == LoginStatus.FirstLogin) {
			userId = loginAuth.getLoginName();
		}
		return modifyEccsPassWordBxpire(loginAuth, oldPassWord, newPassword, userId);
	}

	@Override
	public void logout(LoginAuth loginAuth) {
		String url = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_LOGOUT;
		HttpResult result = null;
		try {
			result = HttpUtils.get(url, loginAuth.getCookie(), null);
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (result.getStatuCode() == 200) {
				if (html.indexOf("欢迎登陆机构信用代码系统") > -1) { // 用户名或密码错误
					logger.info("机构信用代码证系统退出成功");
				} else {
					logger.info("机构信用代码证系统退出失败" + html);
				}
			}
		} catch (Exception e) {
			logger.error("机构信用代码证系统退回异常", e);
		}

	}

	@Override
	public LoginStatus validateEccsLogin(PbcUserAccount eccsUserAccount) {
		if (eccsUserAccount == null) {
			return LoginStatus.PassWordEmpty;
		}
		if (StringUtils.isBlank(eccsUserAccount.getLoginIp())) {
			return LoginStatus.PbcIpEmpty;
		}
		if (StringUtils.isBlank(eccsUserAccount.getLoginUserName()) || StringUtils.isBlank(eccsUserAccount.getLoginPassWord())) {
			return LoginStatus.PassWordEmpty;
		}
		return LoginStatus.Success;
	}

	@Override
	public String getEccsUserId(String eccsIp, String eccsUserName, String eccsPassword) {
		LoginAuth auth = new EccsAuth(eccsIp, eccsUserName, eccsPassword);
		String userId = "";
		// 登录参数
		String urlPars = "userAuditorgId=undefined&op=logon&userId={0}&userPassword={1}";
		String url = auth.getDomain() + EccsConfig.XYDMZ_URL_LOGIN;
		urlPars = MessageFormat.format(urlPars, eccsUserName, eccsPassword);
		HttpResult result = null;
		try {
			result = HttpUtils.post(url, urlPars, EccsConfig.ENCODING, HttpConfig.HTTP_SOCKET_TIMEOUT, null, HeadsCache.getXydmzCommon(), null);
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (result.getStatuCode() == 200) {
				Pattern p = Pattern.compile("var error = \\'\\s(.*?)\\';");
				Matcher m = p.matcher(html);
				if (m.find()) {
					auth.setCookie(result.getCookies());
					userId = html.substring(html.indexOf("jsp?userid="), html.indexOf("\",window"));
					userId = userId.substring(userId.indexOf("=") + 1);
				}
			}
		} catch (Exception e) {
			if (e instanceof HttpHostConnectException) {
				logger.error("[" + eccsUserName + ":" + eccsPassword + "]获取UserId异常>>>网络不通", e);
			} else if (e instanceof SocketTimeoutException) {
				logger.error("[" + eccsUserName + ":" + eccsPassword + "]获取UserId异常>>>网络超时", e);
			} else {
				logger.error("[" + eccsUserName + ":" + eccsPassword + "]获取UserId异常>>>未知异常", e);
			}
		}
		return userId;
	}

}
