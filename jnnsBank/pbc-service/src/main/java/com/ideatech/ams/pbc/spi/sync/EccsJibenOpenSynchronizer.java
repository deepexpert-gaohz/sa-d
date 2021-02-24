package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.EccsConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.spi.SyncParameter;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

/**
 * 机构信用代码证系统基本户开户接口
 * 
 * @author zoulang
 *
 */
@Component
public class EccsJibenOpenSynchronizer extends AbstractSynchronizer {
	protected final static String eccsChart = "utf-8";

	@Override
	protected void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
		String queryUrl = auth.getDomain() + EccsConfig.XYDMZ_URL_PREENTERCODE;
		SyncParameter syncAccountParameter = getSyncAccountParameter(allAcct, SyncSystem.eccs);
		// 接口参数
		String urlPars = syncAccountParameter.getParams(allAcct, SyncSystem.eccs);
		String searchUrlPars = getSearchUrlPars(allAcct);// 开户前需查询是否需要开户
		logger.info("基本户上报信用代码证拼接参数打印：" + searchUrlPars);
		HttpResult result = null;
		List<Header> headers = HeadsCache.getXydmzCommon();
		try {
			result = HttpUtils.post(queryUrl, searchUrlPars.toString().replaceAll("null", ""), EccsConfig.ENCODING, auth.getCookie(), headers);
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (html.indexOf('|') > -1) {
				String status = html.substring(0, html.indexOf('|', 0));
				String hasCrcCode = html.substring(html.indexOf('&') + 1, html.length());
				String modifyType = html.substring(html.indexOf('&') + 1, html.length());// 修改类型1:已发放信息；0：未发放发放信息,
				if (StringUtils.isNotEmpty(hasCrcCode) && hasCrcCode.length() > 1) {
					hasCrcCode = hasCrcCode.substring(0, hasCrcCode.indexOf('&'));
					modifyType = modifyType.substring(modifyType.indexOf("&") + 1, modifyType.indexOf("&") + 2);
				}
				addEccsInfo(auth, allAcct, status, hasCrcCode, searchUrlPars, urlPars);
			} else if (html.indexOf("暂停服务") > -1) {
				logger.info("机构信用代码证系统暂停服务");
				throw new SyncException("机构信用代码系统暂停服务");
			} else {
				logger.info("账户" + allAcct.getAcctNo() + "机构信用代码开户校验异常,返回结果为：" + html);
				throw new SyncException("机构信用代码证系统报备异常");
			}
		} catch (SyncException e) {
			throw e;
		} catch (Exception e) {
			logger.error("账户" + allAcct.getAcctNo() + "请求同步" + syncSystem.getFullName() + "出现异常", e);
			if (e instanceof HttpHostConnectException || e instanceof SocketTimeoutException) {
				throw new SyncException("账号" + allAcct.getAcctNo() + "请求同步" + SyncSystem.eccs.getFullName() + "时网络连接异常");
			} else {
				throw new SyncException("账号" + allAcct.getAcctNo() + "请求同步" + SyncSystem.eccs.getFullName() + "时异常,请重试");
			}
		}
	}

	/**
	 * 新增企业保存到机构信用代码证中
	 * 
	 * @param loginAuth
	 *            登录信息
	 * @param allAcct
	 * @param status
	 * @param hasCrcCode
	 * @param serarchUrlParms
	 * @param urlParms
	 * @return
	 * @throws Exception
	 */
	private void addEccsInfo(LoginAuth loginAuth, AllAcct allAcct, String status, String hasCrcCode, String serarchUrlParms, String urlParms) throws SyncException, Exception {
		if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
			return;
		}
		List<Header> headers = HeadsCache.getXydmzCommon();
		// 机构未存在新加
		if (status.equals("0")) {
			commitEccsOpenAcct(serarchUrlParms, urlParms, loginAuth, headers);
		} else if (status.equals("1")) { // 机构已存在
			throw new SyncException("机构信用代码证系统中已经存在该机构，请查看对应信息！");
		} else if (status.equals("2")) { // 在 页面会展现根据查询条件进行匹配的机构列表
			HttpResult result = new HttpResult();
			String queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MATCH;
			result = HttpUtils.post(queryUrl, getExitsAcctNoParms(allAcct), EccsConfig.ENCODING, loginAuth.getCookie(), headers);
			result.makeHtml(EccsConfig.ENCODING);
			String html = result.getHtml();
			if (html.indexOf("已匹配上的机构列表") > -1) {
				// 若开户时使用统一社会信用代码 并检测到系统已经存在统一社会信用代码证时，标志位机构信息已经存在
				if (html.contains(allAcct.getRegNo())) {
					throw new SyncException("机构信用代码证系统中已经存在该机构，请查看对应信息！");
				}
				queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_MATCH_ADD;
				result = HttpUtils.post(queryUrl, serarchUrlParms.toString().replaceAll("null", ""), EccsConfig.ENCODING, loginAuth.getCookie(), headers);
				result.makeHtml(EccsConfig.ENCODING);
				html = result.getHtml();
				if (html.indexOf("代码申请资料录入") > -1) {
					commitEccsOpenAcct(serarchUrlParms, urlParms, loginAuth, headers);
					return;
				}
			}
			throw new SyncException("参数不正确,组织机构代码或登记注册号码必填或联系系统管理员");
		} else if (status.equals("no") || status.equals("")) {
			throw new SyncException("标识信息匹配失败!");
		} else {
			logger.info("新增机构信用代码证校验失败,信用代码证状态为:", status);
			throw new SyncException("新增机构信用代码证校验失败:" + status);
		}
	}


	/**
	 * 提交机构信用代码证开户
	 * @param serarchUrlPars
	 * @param urlParms
	 * @param loginAuth
	 * @param headers
	 * @throws SyncException
	 * @throws Exception
	 */
	private void commitEccsOpenAcct(String serarchUrlPars, String urlParms, LoginAuth loginAuth, List<Header> headers) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer("");
		urlPars.append(serarchUrlPars);

		String urlParsDetail = urlPars.append(urlParms).toString().replaceAll("null", "");
		String queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_CODECOMMIT;
		HttpResult result = HttpUtils.post(queryUrl, urlParsDetail, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
		result.makeHtml(EccsConfig.ENCODING);
		String html = result.getHtml();
		String status = html.substring(0, html.indexOf('|', 0));
		String hasCrcCode = html.substring(html.indexOf('|') + 1, html.length() - 1);
		if (status.equals("0")) {
			queryUrl = loginAuth.getDomain() + EccsConfig.XYDMZ_URL_CODESUCCESS;
			urlParsDetail = urlParsDetail.replace("???", hasCrcCode);
			for (int i = 0; i < 5; i++) {
				urlParsDetail = urlParsDetail.replace("[" + i + "]", EncodeUtils.encodStr("[" + i + "]", "utf-8"));
			}
			result = HttpUtils.post(queryUrl, urlParsDetail, EccsConfig.ENCODING, loginAuth.getCookie(), headers);
			result.makeHtml(EccsConfig.ENCODING);
			html = result.getHtml();
			if (html.indexOf("信息保存成功") < 0) {
				throw new SyncException("账户信用代码报送失败");
			}
		} else if (html.indexOf("请重新登录") > -1) {
			throw new SyncException("账户信用代码访问时间超时，请重新提交");
		} else if (status.equals("1")) {
			throw new SyncException("信用代码系统中已经存在该机构，请查看对应信息！");
		} else if (status != "") {
			logger.info("新增代码证异常,状态是" + status + ",请求的结果页面为：" + html);
			throw new SyncException(status);
		} else {
			logger.info("新增机构信用证失败:\n{}", html);
			throw new SyncException("新增机构信用证失败");
		}
	}

	/**
	 * 根据国地税查询出重复账户 ，继续新增
	 * 
	 * @param allAcct
	 * @return
	 */
	private String getExitsAcctNoParms(AllAcct allAcct) {
		StringBuffer matchUrlPars = new StringBuffer("");
		matchUrlPars.append("intPage=1").append("&flag=1");
		matchUrlPars.append("&saccbaselicno=");// 开户许可证核准号
		matchUrlPars.append("&RegisterType=").append(allAcct.getRegType());
		matchUrlPars.append("&RegisterCode=").append(allAcct.getRegNo());
		matchUrlPars.append("&SdepNationalTaxCode=").append(allAcct.getStateTaxRegNo());
		matchUrlPars.append("&SdepLandTaxCode=").append(allAcct.getTaxRegNo());
		matchUrlPars.append("&SdepNationalTaxCode1=").append(allAcct.getStateTaxRegNo());
		matchUrlPars.append("&SdepLandTaxCode1=").append(allAcct.getTaxRegNo());
		matchUrlPars.append("&ran=").append(new Date().getTime());
		return matchUrlPars.toString();
	}

	/**
	 * 获取开户时查询是否开户接口参数
	 * 
	 * @param allAcct
	 * @return
	 * @throws Exception
	 */
	private String getSearchUrlPars(AllAcct allAcct) throws Exception {
		StringBuffer striBuffer = new StringBuffer();
		String orgCode = allAcct.getOrgCode();
		if (StringUtils.isNotEmpty(allAcct.getRegType()) && StringUtils.isNotEmpty(allAcct.getRegNo())) {
			if (allAcct.getRegType().equals("01") && allAcct.getRegNo().substring(0, 1).equals("9") && allAcct.getRegNo().length() == 18) {
				allAcct.setRegType("07");
			}
		}
		if (allAcct.getRegType().equals("07")) {// 若开户工商营业执照为统一社会信用代码,税务登记信息、组织机构代码为空
			striBuffer.append("&idBean.sdeporgcode=");
			striBuffer.append("&idBean.sdepnationaltaxcode=");
			striBuffer.append("&idBean.sdeplandtaxcode=");
		} else {
			if (StringUtils.isNotEmpty(orgCode) && !orgCode.contains("-")) {
				orgCode = PbcBussUtils.getStandardOrgCode(allAcct.getOrgCode());
			}
			striBuffer.append("&idBean.sdeporgcode=").append(orgCode);
			/**
			 * idBean.sdepnationaltaxcode纳税人识别号（国税）
			 */
			striBuffer.append("&idBean.sdepnationaltaxcode=");
			striBuffer.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), eccsChart));
			/**
			 * idBean.sdeplandtaxcode纳税人识别号（地税）
			 */
			striBuffer.append("&idBean.sdeplandtaxcode=");
			striBuffer.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), eccsChart));
		}
		/**
		 * idBean.loanCardCode 贷款卡编码
		 */
		striBuffer.append("&idBean.loancardcode=").append(allAcct.getBankCardNo());
		/**
		 * idBean.saccBaseLicNo 开户许可证核准号
		 */
		striBuffer.append("&idBean.saccbaselicno=").append(allAcct.getAccountKey());
		/**
		 * idBean.registerType 登记注册号类型
		 */
		striBuffer.append("&idBean.registertype=").append(allAcct.getRegType());
		/**
		 * idBean.registerCode 登记注册号码
		 */
		striBuffer.append("&idBean.registercode=");
		striBuffer.append(EncodeUtils.encodStr(allAcct.getRegNo(), eccsChart));

		return striBuffer.toString();
	}
}
