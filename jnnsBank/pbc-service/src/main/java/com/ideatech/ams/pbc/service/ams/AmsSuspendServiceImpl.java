package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsSuspendSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
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

import java.util.List;

@Component
public class AmsSuspendServiceImpl implements AmsSuspendService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AmsSearchService amsSearchService;

	@Autowired
	AmsSyncOperateService amsSyncOperateService;

	@Override
	public AmsAccountInfo suspendAccountFirstStep(LoginAuth auth, AmsSuspendSyncCondition condition) throws SyncException, Exception {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		// 获取人行机构代码
		String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
		if (ArrayUtils.isNotEmpty(bankInfo)) {
			condition.setBankCode(bankInfo[1]);
			condition.setBankName(bankInfo[0]);
		}
		// 校验
		validFirstStep(condition);
		String urlPars = getfirstStepUrlParms(condition);
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_SUPSENDTOBASIC;
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());

		HttpResult result = HttpUtils.post(url, urlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			String validaStr = amsSyncOperateService.validateOp(html, "信息核对");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			// 获取现久悬对应的基本户信息
			amsAccountInfo = getSuspendBaseData(html);
		} else {
			logger.info("账号" + condition.getAcctNo() + "校验是否可以久悬时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
		return amsAccountInfo;
	}

	@Override
	public void suspendAccountLastStep(LoginAuth auth) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_SUSPENDCOMMIT;
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
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
			logger.info("账号久悬提交时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	/**
	 * 变成久悬账户 获取对应的基本户信息
	 * @param html
	 * @return
	 */
	private AmsAccountInfo getSuspendBaseData(String html) {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
		// 基本户中 行业归属（行业分类 产业分类) 上级法定代表人 开户银行名称 这几个没有获取到
		// 一般化户、非预算 行业归属（行业分类 产业分类) 上级法定代表人
		// 存款人名称
		amsAccountInfo.setDepositorName(eleValues.get(0).html().replace("&nbsp;", "").trim());
		// 电话
		amsAccountInfo.setTelephone(eleValues.get(1).html().replace("&nbsp;", "").trim());
		// 地址
		amsAccountInfo.setRegAddress(eleValues.get(2).html().replace("&nbsp;", "").trim());
		// 邮政编码
		amsAccountInfo.setZipCode(eleValues.get(3).html().replace("&nbsp;", "").trim());
		// 存款人类别")) {
		amsAccountInfo.setDepositorType(eleValues.get(4).html().replace("&nbsp;", "").trim());
		// 组织机构代码") && i == 5) {
		amsAccountInfo.setOrgCode(eleValues.get(5).html().replace("&nbsp;", "").trim());
		// 姓名") && i == 7) {
		String legalName = StringEscapeUtils.unescapeHtml(eleValues.get(6).html().replace("&nbsp;", "").trim());
		amsAccountInfo.setLegalName(legalName);
		// 证件种类") && i == 8) {
		amsAccountInfo.setLegalIdcardType(eleValues.get(7).html().replace("&nbsp;", "").trim());
		// 证件号码") && i == 9) {
		amsAccountInfo.setLegalIdcardNo(eleValues.get(8).html().replace("&nbsp;", "").trim());
		// 注册地地区代码")) {
		amsAccountInfo.setRegAreaCode(eleValues.get(9).html().replace("&nbsp;", "").trim());
		// 注册资金币种")) {
		amsAccountInfo.setRegCurrencyType(eleValues.get(10).html().replace("&nbsp;", "").trim());
		// 注册资金")) {
		amsAccountInfo.setRegisteredCapital(eleValues.get(11).html().replace("&nbsp;", "").trim());
		// 经营范围
		amsAccountInfo.setBusinessScope(eleValues.get(12).html().replace("&nbsp;", "").trim());
		// 证明文件种类")) {
		amsAccountInfo.setFileType(eleValues.get(13).html().replace("&nbsp;", "").trim());
		// 证明文件编号")) {
		amsAccountInfo.setFileNo(eleValues.get(14).html().replace("&nbsp;", "").trim());
		// 证明文件种类2
		amsAccountInfo.setFileType2(eleValues.get(15).html().replace("&nbsp;", "").trim());
		// 证明文件编号2
		amsAccountInfo.setFileNo2(eleValues.get(16).html().replace("&nbsp;", "").trim());
		// 无需办理税务登记的证明文件编号
		amsAccountInfo.setNoTaxProve(eleValues.get(18).html().replace("&nbsp;", "").trim());
		// 国税登记证号")) {
		amsAccountInfo.setStateTaxRegNo(eleValues.get(19).html().replace("&nbsp;", "").trim());
		// 地税登记证号")) {
		amsAccountInfo.setTaxRegNo(eleValues.get(20).html().replace("&nbsp;", "").trim());
		// 上级单位名称")) {
		amsAccountInfo.setParCorpName(eleValues.get(21).html().replace("&nbsp;", "").trim());
		// 上级基本存款账户开户许可证核准号")) {
		amsAccountInfo.setParAccountKey(eleValues.get(22).html().replace("&nbsp;", "").trim());
		// 上级组织机构代码") && i == 24) {
		amsAccountInfo.setParOrgCode(eleValues.get(23).html().replace("&nbsp;", "").trim());
		// 上级姓名"
		amsAccountInfo.setParLegalName(eleValues.get(24).html().replace("&nbsp;", "").trim());
		// 上级证件种类")
		amsAccountInfo.setParLegalIdcardType(eleValues.get(25).html().replace("&nbsp;", "").trim());
		// 上级证件号码")
		amsAccountInfo.setParLegalIdcardNo(eleValues.get(26).html().replace("&nbsp;", "").trim());
		if (eleValues.html().indexOf("一般存款账户") > -1) {
			// 开户银行名称")) {
			amsAccountInfo.setBankName(eleValues.get(27).html().replace("&nbsp;", "").trim());
			// 开户银行代码")) {
			amsAccountInfo.setBankCode(eleValues.get(28).html().replace("&nbsp;", "").trim());
			// 账号")) {
			amsAccountInfo.setAcctNo(eleValues.get(29).html().replace("&nbsp;", "").trim());
			// 账户性质
			amsAccountInfo.setAcctType(AccountType.yiban);
			// 开户日期")) {
			amsAccountInfo.setAcctCreateDate(eleValues.get(32).html().replace("&nbsp;", "").trim());
			// 基本户的开户许可证
			amsAccountInfo.setAccountKey(eleValues.get(33).html().replace("&nbsp;", "").trim());
			// 一般户 证明文件编号
			amsAccountInfo.setAccountFileNo(eleValues.get(35).html().replace("&nbsp;", "").trim());
		}
		if (eleValues.html().indexOf("基本存款账户") > -1) {
			// 开户银行代码")) {
			amsAccountInfo.setBankCode(eleValues.get(27).html().replace("&nbsp;", "").trim());
			// 账号")) {
			amsAccountInfo.setAcctNo(eleValues.get(28).html().replace("&nbsp;", "").trim());
			// 账户性质
			amsAccountInfo.setAcctType(AccountType.jiben);
			// 开户日期")) {
			amsAccountInfo.setAcctCreateDate(eleValues.get(30).html().replace("&nbsp;", "").trim());
		}
		// 非预算专用存款账户 账户名称加内设部门
		if (eleValues.html().indexOf("非预算单位专用存款账户") > -1) {
			// 开户银行名称")) {
			amsAccountInfo.setBankName(eleValues.get(27).html().replace("&nbsp;", "").trim());
			// 开户银行代码")) {
			amsAccountInfo.setBankCode(eleValues.get(28).html().replace("&nbsp;", "").trim());
			// 账号")) {
			amsAccountInfo.setAcctNo(eleValues.get(29).html().replace("&nbsp;", "").trim());
			// 非预算 账户名称")) {
			amsAccountInfo.setAcctName(eleValues.get(30).html().replace("&nbsp;", "").trim());
			// 资金性质
			// allAcct.setCapitalProperty(eleValues.get(31).html().replace("&nbsp;",
			// "").trim());
			// 账户性质
			amsAccountInfo.setAcctType(AccountType.feiyusuan);
			// 开户日期")) {
			amsAccountInfo.setAcctCreateDate(eleValues.get(35).html().replace("&nbsp;", "").trim());
			// 基本户的开户许可证
			amsAccountInfo.setAccountKey(eleValues.get(36).html().replace("&nbsp;", "").trim());
			// 非预算 证明文件种类
			// allAcct.setFileGeneralType(eleValues.get(37).html().replace("&nbsp;",
			// "").trim());
			// 非预算 证明文件编号
			amsAccountInfo.setAccountFileNo(eleValues.get(38).html().replace("&nbsp;", "").trim());
			if (doc.html().indexOf("内设部门") > -1) {// 内设部门
				// 内设部门名称
				amsAccountInfo.setInsideDepartmentName(eleValues.get(41).html().replace("&nbsp;", "").trim());
				// 负责人身份证件编号
				amsAccountInfo.setInsideSaccdepmanNo(eleValues.get(43).html().replace("&nbsp;", "").trim());
				// 负责人姓名
				amsAccountInfo.setInsideSaccdepmanName(eleValues.get(44).html().replace("&nbsp;", "").trim());
				// 负责人地址
				amsAccountInfo.setInsideAddress(eleValues.get(45).html().replace("&nbsp;", "").trim());
				// 内设部门电话
				amsAccountInfo.setInsideTelphone(eleValues.get(46).html().replace("&nbsp;", "").trim());
				// 内设部门邮编
				amsAccountInfo.setInsideZipCode(eleValues.get(47).html().replace("&nbsp;", "").trim());
			}
			// 非预算专用存款账户 账户名称加资金管理人
			if (doc.html().indexOf("内设部门") < 0 && doc.html().indexOf("资金管理人") > -1) {
				// 资金管理人姓名
				amsAccountInfo.setMoneyManager(eleValues.get(42).html().replace("&nbsp;", "").trim());
				// 资金管理人身份证件种类
				amsAccountInfo.setMoneyManagerCno(eleValues.get(44).html().replace("&nbsp;", "").trim());
			}
		}
		return amsAccountInfo;
	}

	/**
	 * 获取久悬第一步接口参数
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private String getfirstStepUrlParms(AmsSuspendSyncCondition condition) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		// 帐号
		urlPars.append("&searchAccFormVO.saccid=").append(condition.getAcctNo());
		// 开户许可证号
		urlPars.append("&searchAccFormVO.sbankcode=").append(condition.getBankCode());
		urlPars.append("&searchAccFormVO.sbankname=");
		urlPars.append(EncodeUtils.encodStr(condition.getBankName(), "gbk"));
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		return urlPars.toString();
	}

	/**
	 * 校验久悬条件是否满足
	 * 
	 * @param condition
	 * @throws SyncException
	 */
	private void validFirstStep(AmsSuspendSyncCondition condition) throws SyncException {
		if (StringUtils.isBlank(condition.getAcctNo())) {
			throw new SyncException("账号不能为空");
		}
		if (StringUtils.isBlank(condition.getBankCode()) || StringUtils.isBlank(condition.getBankName())) {
			throw new SyncException("人行机构代码、名称不能为空");
		}
	}
}
