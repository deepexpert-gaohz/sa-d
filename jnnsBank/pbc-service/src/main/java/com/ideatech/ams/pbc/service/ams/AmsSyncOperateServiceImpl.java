package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.common.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AmsSyncOperateServiceImpl implements AmsSyncOperateService {
	public Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AmsSearchService amsSearchService;

	@Override
	public String validateOp(String html, String rightMatchStr) throws SyncException {
		String resultStr = "";
		if (!StringUtils.contains(html, rightMatchStr)) {
			if (html.indexOf("id=\"errormsg\"") > -1) {
				Document doc = Jsoup.parse(html);
				Element errorDiv = doc.getElementById("errormsg");
				Elements errorFont = errorDiv.getElementsByTag("font");
				if (errorFont != null && errorFont.size() > 0) {
					resultStr = errorFont.get(0).html();
				} else {
					resultStr = errorDiv.html();
				}
			} else if (html.indexOf("class=\"error\"") > -1) {
				Document doc = Jsoup.parse(html);
				Elements errorDiv = doc.getElementsByClass("error");
				if (errorDiv != null && errorDiv.size() > 0) {
					resultStr = errorDiv.get(0).html();
				} else {
					if (errorDiv != null && errorDiv.html() != null) {
						resultStr = errorDiv.html();
					}
				}
			}else if (html.indexOf("异常信息") > -1) {
				resultStr = "人行账管系统异常,未知异常,请重试！";
			}
		}
		if (StringUtils.isNotEmpty(resultStr)) {
			resultStr = resultStr.replaceAll("<br />", "").replaceAll("\\n", "").replaceAll("&nbsp;", "");
		}
		return resultStr;
	}

	@Override
	public String validateOp(String html, String rightMatchStr, LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		return retrunValidateOp(html, rightMatchStr, auth, condition);
	}

	@Override
	public String validateOp(String html, String rightMatchStr, LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws SyncException, Exception {
		return retrunValidateOp(html, rightMatchStr, auth, condition);
	}

	@Override
	public String validate(String html) throws SyncException, Exception {
			String resultStr = "";
			if (html.indexOf("id=\"errormsg\"") > -1) {
				Document doc = Jsoup.parse(html);
				Element error = doc.getElementById("errormsg");
				Elements errorFont = error.getElementsByTag("font");
				if (errorFont != null && errorFont.size() > 0) {
					for (Element element : errorFont) {
						if (org.apache.commons.lang.StringUtils.isNotBlank(resultStr)) {
							resultStr += ";";
						}
						resultStr += element.html();
					}
				} else {
					resultStr = error.html();
				}
			} else if (html.indexOf("class=\"error\"") > -1) {
				Document doc = Jsoup.parse(html);
				Elements error = doc.getElementsByClass("error");
				if (error != null && error.size() > 0) {
					for (Element element : error) {
						if (org.apache.commons.lang.StringUtils.isNotBlank(resultStr)) {
							resultStr += ";";
						}
						resultStr += element.html();
					}
				} else {
					if (error != null && error.html() != null) {
						resultStr = error.html();
					}
				}
			}
		if (StringUtils.isNotEmpty(resultStr)) {
			resultStr = resultStr.replaceAll("<br />", "").replaceAll("\\n", "").replaceAll("&nbsp;", "");
		} else {
			if (html.indexOf("异常信息") > -1) {
				resultStr = "人行账管系统异常,未知异常,请重试!";
			}
		}
		return resultStr;
	}

	@Override
	public String validateOp(String html, LoginAuth auth, AllAcct condition) throws SyncException, Exception {
		String resultStr = "";
		// 征询页面
		if (StringUtils.indexOf(html, "您确认发送征询吗") != -1) {
			logger.info("账号" + condition.getAcctNo() + "进入征询页面...");
			// 如果征询结果是同意开户则开户  默认征询3次
			ResultVO resultVO = null;
			for (int i = 0; i < 5; i++) {
				resultVO = amsSearchService.consultRemoteServer(auth, condition);
				if (resultVO.isSuccess()) {
					break;
				} else if (resultVO.getMsg().indexOf("存款人已开立基本存款账户") > -1) {
					resultStr = resultVO.getMsg();
					break;
				} else if (resultVO.getMsg().indexOf("账户已撤销") > -1) {
					resultStr = resultVO.getMsg();
					break;
				} else if (resultVO.getMsg().indexOf("不存在") > -1) {
					resultStr = resultVO.getMsg();
					break;
				} else if (resultVO.getMsg().indexOf("异地征询结果为空") > -1) {
					resultStr = "异地征询无响应 ,请稍后再试或手工到人行账管系统操作";
					logger.info("异地征询失败，当前返回内容为:{}", resultVO.getMsg());
					break;
				} else if (i == 4 && StringUtils.isNotBlank(resultVO.getMsg())) {
					resultStr = resultVO.getMsg();
					break;
				} else {
					Thread.sleep(2000L);
				}
			}
		}
		return resultStr;
	}

	@Override
	public String validateOp(String html, LoginAuth auth, AmsFeilinshiSyncCondition condition) throws SyncException, Exception {
		String resultStr = "";
		// 征询页面
		if (StringUtils.indexOf(html, "您确认发送征询吗") != -1) {
			logger.info("账号" + condition.getAcctNo() + "进入征询页面...");
			// 如果征询结果是同意开户则开户  默认征询3次
			ResultVO result = null;
			for (int i = 0; i < 5; i++) {
				result = amsSearchService.consultRemoteServer(auth, condition);
				if (result.isSuccess()) {
					break;
				} else if (result.getMsg().indexOf("基本存款账户不存在") > -1) {
					resultStr = result.getMsg();
					break;
				} else if (result.getMsg().indexOf("账户已撤销") > -1) {
					resultStr = result.getMsg();
					break;
				} else if (result.getMsg().indexOf("不存在") > -1) {
					resultStr = result.getMsg();
					break;
				} else if (result.getMsg().indexOf("异地征询结果为空") > -1) {
					resultStr = "异地征询无响应 , 请稍后再试或手工到人行账管系统操作";
					break;
				} else if (i == 4 && StringUtils.isNotBlank(result.getMsg())) {
					resultStr = result.getMsg();
					break;
				} else {
					Thread.sleep(2000L);
				}
			}
		}
		return resultStr;
	}

	/**
	 * 校验错误信息
	 * 
	 * @param html
	 *            请求响应的页面
	 * @param auth
	 * @param rightMatchStr
	 *            响应要求的包含的正确字符串,若html包含此字段，则认为处理正常
	 * @param condition
	 * @return 若html未包含rightMatchStr字符串，返回错误信息；若包含则返回“”
	 * @throws SyncException
	 * @throws Exception
	 */
	private String retrunValidateOp(String html, String rightMatchStr, LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		String resultStr = "";
		logger.info("进入异地征询......");
		if (!StringUtils.contains(html, rightMatchStr)) {
			resultStr = validateOp(html, rightMatchStr);
			if (StringUtils.isBlank(resultStr)) {
				// 征询页面
				if (StringUtils.indexOf(html, "您确认发送征询吗") != -1) {
					logger.info("账号" + condition.getAcctNo() + "进入征询页面...");
					// 如果征询结果是同意开户则开户 默认征询3次
					ResultVO resultVO = null;
					for (int i = 0; i < 5; i++) {
						resultVO = amsSearchService.consultRemoteServer(auth, condition);
						if (resultVO.isSuccess()) {
							break;
						} else if (resultVO.getMsg().indexOf("存款人有其他久悬") > -1) {
							resultStr = resultVO.getMsg();
							break;
						} else if (resultVO.getMsg().indexOf("账户已撤销") > -1) {
							resultStr = resultVO.getMsg();
							break;
						} else if (resultVO.getMsg().indexOf("不存在") > -1) {
							resultStr = resultVO.getMsg();
							break;
						} else if (i == 4 && resultVO.getMsg().indexOf("异地征询结果为空") > -1) {// 当最后一次还是异地郑旭结果为空，则提示异常征询失败
							resultStr = "异地征询无响应,请稍后再试或手工到人行账管系统操作";
							break;
						} else {
							Thread.sleep(500L);
						}
					}
				}
			}
		}else {
			logger.info("1、省内异地调用回调函数......");
			amsSearchService.callbackMethod(auth,html);
		}
		return resultStr;
	}

	@Override
	public boolean deleteAccountInCheckList(LoginAuth auth, String delAccountNo) throws SyncException, Exception {
		if (StringUtils.isBlank(delAccountNo)) {
			throw new SyncException("账号不能为空");
		}
		HttpResult result = null;
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPEN_LIST;
		String html = "";
		String tempUrl = queryUrl;
		int findCount = 0;
		int pageSize = 10;// 默认10页
		for (int i = 1; i <= pageSize; i++) {
			if (i < 2) {
				result = HttpUtils.get(queryUrl.toString(), auth.getCookie(), null);
				result.makeHtml(HttpConfig.HTTP_ENCODING);
				html = result.getHtml();
				// 找到共几页
				Matcher matcher = Pattern.compile("共\\d+行,  第\\d+页 共(\\d+)页").matcher(html);
				if (matcher.find()) {
					pageSize = Integer.parseInt(matcher.group(1));
				}
			} else {
				queryUrl = queryUrl + "&ispage=true&pagingPage=" + i;
				result = HttpUtils.get(queryUrl.toString(), auth.getCookie(), null);
				result.makeHtml(HttpConfig.HTTP_ENCODING);
				html = result.getHtml();
				queryUrl = tempUrl;
			}
			// 若查询的html页面存在账号，则对账号进行删除
			if (html.indexOf(delAccountNo) > -1) {
				findCount = findCount + doDelete(auth, html, delAccountNo);
			}
		}
		if (findCount < 1) {
			return false;
			// throw new SyncException("待核准列表中不存在要删除的账号(" + delAccountNo + ")");
		} else {
			return true;
		}
	}

	/**
	 * 在待核准列表中删除账号
	 * 
	 * @param auth
	 * @param html
	 *            存在要删除账号的html页面
	 * @param delAccountNo
	 *            要删除的账号
	 * @return 返回删除的数量(一个账号可能存在多个)
	 */
	@SuppressWarnings("rawtypes")
	private int doDelete(LoginAuth auth, String html, String delAccountNo) throws SyncException, Exception {
		String icheckno = "";
		String accountNo = "";
		int deleteCount = 0;
		HttpResult result = null;
		Document doc = Jsoup.parse(html);
		// 匹配列表中要删除的账号、可能是多个
		Elements elms = doc.select("#defaulttabletagname tr");
		Map<String, String> map = new HashMap<String, String>();
		for (Element element : elms) {
			Elements tds = element.select("td");
			if (tds.size() <= 0)
				continue;
			icheckno = tds.get(0).select("input[name=icheckno]").attr("value");
			accountNo = tds.get(3).text();
//			accountNo = accountNo.substring(1, accountNo.length() - 1);
			accountNo = StringUtils.substring(accountNo, 1, accountNo.length() - 1);
			map.put(icheckno, accountNo);
		}
		String[] delIchecknos = new String[map.entrySet().size()];
		Iterator iterator = map.entrySet().iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (entry.getValue().equals(delAccountNo))
				delIchecknos[i] = entry.getKey() + "";
		}
		// 账户删除
		String delUrl = auth.getDomain() + "/ams/unApproveAcc.do?method=forDeleteAccApproveSubmit";
		for (String delIcheckno : delIchecknos) {
			if (StringUtils.isNotEmpty(delIcheckno)) {
				String delPars = "&accNotCheckedEntDepositorInfo.icheckkind=0&icheckno=" + delIcheckno;
				HttpUtils.post(delUrl, delPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), HeadsCache.getRhzgCommon(auth.getDomain()));
				deleteCount++;
			}
		}
		// 再次查询待核准页面查询是否存在账户信息
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPEN_LIST;
		result = HttpUtils.get(queryUrl, auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		html = result.getHtml();
		if (html.indexOf(delAccountNo) > -1) {
			throw new SyncException("账号" + delAccountNo + "删除失败");
		}
		return deleteCount;
	}

}
