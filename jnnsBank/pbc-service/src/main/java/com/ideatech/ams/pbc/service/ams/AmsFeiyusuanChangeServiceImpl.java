package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsFeiyusuanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.List;

@Component
public class AmsFeiyusuanChangeServiceImpl implements AmsFeiyusuanChangeService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AmsSearchService amsSearchService;

	@Autowired
	AmsSyncOperateService amsSyncOperateService;

	@Override
	public AmsFeiyusuanSyncCondition changeAccountFirstStep(LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws SyncException, Exception {
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
		String urlPars = getFirstStepUrlParams(condition);
		logger.info("非预算变更上报第一步拼接参数打印：{}",urlPars);
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGEFYSACCOUNTVALIDATE;
		HttpResult result = HttpUtils.post(url, urlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			String validaStr = amsSyncOperateService.validateOp(html, "变更信息录入", auth, condition);
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			if (StringUtils.indexOf(html, "您确认发送征询吗") == -1) {
				getFysAcctountData(html, condition);
			}
		} else {
			logger.info("账户" + condition.getAcctNo() + "校验是否可以变更异常" + html);
			throw new SyncException("用户登录信息失效");
		}
		return condition;
	}

	@Override
	public AmsAccountInfo changeAccountSecondStep(LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws SyncException, Exception {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		String urlPars = getSecondStepUrlParams(condition);
		logger.info("非预算上报第二步拼接参数打印：{}",urlPars);
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGEFYSACCOUNTSHOWBASEINFO;
		HttpResult result = HttpUtils.post(url, urlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		logger.info("非预算变更上报第二步返回页面："+html);
		if (result.getStatuCode() == 200) {
			// 验证操作是否成功
			String validaStr = amsSyncOperateService.validateOp(html, "变更信息核对");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			amsAccountInfo = getAmsAccountByAcctNoReturnHtml(html);
		} else {
			logger.info("账号" + condition.getAcctNo() + "变更时反显客户信息时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
		return amsAccountInfo;
	}

	@Override
	public void changeAccountLastStep(LoginAuth auth) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CHANGEFYSACCOUNT;
		HttpResult result = null;
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		result = HttpUtils.post(queryUrl, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			// 验证操作是否成功
			String validaStr = amsSyncOperateService.validateOp(html, "变更成功");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
		} else {
			logger.info("账户变更提交时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	/**
	 * 获取账户变更第一步接口参数
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private String getFirstStepUrlParams(AmsFeiyusuanSyncCondition condition) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("authorizationFlag=0&alertFlag=0&errorMsg=");
		urlPars.append("&authorizationMsg=&msgSingleId=&operateType=");
		urlPars.append("&saccno=").append(condition.getAcctNo());
		urlPars.append("&FD_OpenBankCode=102301000010");
		urlPars.append("&FD_OpenBankName=%D6%D0%B9%FA%B9%A4%C9%CC%D2%F8%D0%D0%BD%AD%CB%D5%CA%A1%B7%D6%D0%D0");
		urlPars.append("&saccbankcode=").append(condition.getBankCode());
		urlPars.append("&saccbanknamehidden=");
		urlPars.append(URLEncoder.encode(condition.getBankName(), "gbk"));
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		return urlPars.toString();
	}

	/**
	 * 获取账户变更第二步接口参数
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private String getSecondStepUrlParams(AmsFeiyusuanSyncCondition condition) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		// 账户类型
		urlPars.append("sacckind=3");
		// 证明文件1种类
		urlPars.append("&saccfiletype1=").append(condition.getAccountFileType());
		// 证明文件1编号
		urlPars.append("&saccfilecode1=");
		urlPars.append(EncodeUtils.encodStr(condition.getAccountFileNo(), "gbk"));
		// 资金性质
		urlPars.append("&saccfundkind=");
		urlPars.append(EncodeUtils.encodStr(condition.getCapitalProperty(), "gbk"));
		// 帐号名称
		urlPars.append("&saccname2=");
		urlPars.append(EncodeUtils.encodStr(condition.getAcctName(), "gbk"));
		// 账户名称构成方式
		urlPars.append("&accountNameType=").append(condition.getAccountNameFrom());
		String accountinfoimode = "";
		if (condition.getAccountNameFrom().equals("1")) {
			accountinfoimode = "3";
		} else if (condition.getAccountNameFrom().equals("2")) {
			accountinfoimode = "4";
		} else {
			accountinfoimode = "4";
		}
		// 资金管理人\内设部门
		urlPars.append("&accEntAccountInfo.imode=").append(accountinfoimode);
		urlPars.append("&sremark=");
		urlPars.append(EncodeUtils.encodStr(condition.getRemark(), "gbk"));
		urlPars.append("&saccdepname=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideDepartmentName(), "gbk"));
		// 内设部门负责人名称
		urlPars.append("&saccdepmanname=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideSaccdepmanName(), "gbk"));
		// 内设部门负责人身份证件种类
		urlPars.append("&saccdepcrekind=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideSaccdepmanKind(), "gbk"));
		// 内设部门负责人身份证件编号
		urlPars.append("&saccdepcrecode=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideSaccdepmanNo(), "gbk"));
		// 内设部门电话
		urlPars.append("&saccdeptel=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideTelphone(), "gbk"));
		// 内设部门邮编
		urlPars.append("&saccdeppostcode=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideZipCode(), "gbk"));
		// 内设部门地址
		urlPars.append("&saccdepaddress=");
		urlPars.append(EncodeUtils.encodStr(condition.getInsideAddress(), "gbk"));
		// 账户名称构成方式：与存款人名称一致 资金管理人名称
		urlPars.append("&saccdepnametmp=");
		urlPars.append(EncodeUtils.encodStr(condition.getMoneyManager(), "gbk"));
		// 账户名称构成方式：与存款人名称一致 资金管理人身份证件种类
		urlPars.append("&saccdepcrekindtmp=").append(condition.getMoneyManagerCtype());
		urlPars.append("&zmbh=&zmbh2=");
		// 账户名称构成方式：与存款人名称一致 资金管理人身份证件编号
		urlPars.append("&saccdepcrecodetmp=").append(condition.getMoneyManagerCno());
		// 加资金性质 账户名称前缀值
		urlPars.append("&saccprefix=");
		if ("2".equals(condition.getAccountNameFrom())) { // 选择 存款人名称加资金性质
			if("14".equals(condition.getCapitalProperty())){
				if(StringUtils.isNotBlank(condition.getSaccprefix())){
					urlPars.append(EncodeUtils.encodStr(condition.getSaccprefix(), "gbk"));
				}
			}
		}
		// 加 资金性质 账户名称
		urlPars.append("&saccname=");
		urlPars.append(EncodeUtils.encodStr(condition.getAcctName(), "gbk"));
		// 加 资金性质 账户名称2
		urlPars.append("&saccname=");
		urlPars.append(EncodeUtils.encodStr(condition.getAcctName(), "gbk"));
		// 加 资金性质 账户名称后缀值
		urlPars.append("&saccpostfix=");
		urlPars.append(EncodeUtils.encodStr(condition.getSaccpostfix(), "gbk"));
		// 加 资金性质 前缀选择值
		urlPars.append("&accnameprefix=");
		// 加 资金性质 后缀选着值
		urlPars.append("&accnamesuffix=");
		// 加 资金性质 资金管理人名称
		urlPars.append("&saccdepnametmp2=");
		urlPars.append(EncodeUtils.encodStr(condition.getMoneyManager(), "gbk"));
		// 加 资金性质 资金管理人身份证件种类
		urlPars.append("&saccdepcrekindtmp2=").append(condition.getMoneyManagerCtype());
		// 加 资金性质 资金管理人身份证件编号
		urlPars.append("&saccdepcrecodetmp2=").append(condition.getMoneyManagerCno());
		urlPars.append("&accEntAccountInfo.scurtype=1");
		urlPars.append("&cruArray=1");

		return urlPars.toString();
	}

	/**
	 * 获取变更非预算 账户名称构成方式 与资金管理人一致
	 * 
	 * @param html
	 * @return
	 */
	private AmsAccountInfo getAmsAccountByAcctNoReturnHtml(String html) {
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		// 存款人名称
		amsAccountInfo.setDepositorName(eleValues.get(0).html().replace("&nbsp;", "").trim());
		// 电话
		amsAccountInfo.setTelephone(eleValues.get(1).html().replace("&nbsp;", "").trim());
		// 地址
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
		// 法定代表人或单位负责人")) {
		String legalType = eleValues.get(6).getElementsByAttribute("checked").val();
		String legalTypeValue = "";
		if (legalType.equals("1")) {
			legalTypeValue = "法定代表人";
		} else if (legalType.equals("2")) {
			legalTypeValue = "单位负责人";
		}
		amsAccountInfo.setLegalType(legalTypeValue);
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
		// 证明文件种类")) {
		amsAccountInfo.setFileType(eleValues.get(16).html().replace("&nbsp;", "").trim());
		// 证明文件编号")) {
		amsAccountInfo.setFileNo(eleValues.get(17).html().replace("&nbsp;", "").trim());
		// 经营范围
		amsAccountInfo.setBusinessScope(eleValues.get(18).html().replace("&nbsp;", "").trim());
		// 证明文件种类2
		amsAccountInfo.setFileType2(eleValues.get(19).html().replace("&nbsp;", "").trim());
		// 证明文件编号2
		amsAccountInfo.setFileNo2(eleValues.get(20).html().replace("&nbsp;", "").trim());
		// 无需办理税务登记证的文件或税务机关出具的证明
		amsAccountInfo.setNoTaxProve(eleValues.get(22).html().replace("&nbsp;", "").trim());
		// 国税登记证号")) {
		amsAccountInfo.setStateTaxRegNo(eleValues.get(24).html().replace("&nbsp;", "").trim());
		// 地税登记证号")) {
		amsAccountInfo.setTaxRegNo(eleValues.get(25).html().replace("&nbsp;", "").trim());
		// 工商营业执照有效期
		// 单位名称")) {
		amsAccountInfo.setParCorpName(eleValues.get(33).html().replace("&nbsp;", "").trim());
		// 基本存款账户开户许可证核准号")) {
		amsAccountInfo.setParAccountKey(eleValues.get(34).html().replace("&nbsp;", "").trim());
		// 组织机构代码") && i == 24) {
		amsAccountInfo.setParOrgCode(eleValues.get(35).html().replace("&nbsp;", "").trim());
		String parlegalTypeValue = "";
		if (eleValues.get(36).getElementsByAttribute("checked").val().equals("1")) {
			parlegalTypeValue = "法定代表人";
		} else if (eleValues.get(36).getElementsByAttribute("checked").val().equals("2")) {
			parlegalTypeValue = "单位负责人";
		}
		// 法定代表人或单位负责人")) {
		amsAccountInfo.setParLegalType(parlegalTypeValue);
		// "姓名") && i == 26) {
		amsAccountInfo.setParLegalName(eleValues.get(37).html().replace("&nbsp;", "").trim());
		// 证件种类") && i == 27) {
		amsAccountInfo.setParLegalIdcardType(eleValues.get(38).html().replace("&nbsp;", "").trim());
		// 证件号码") && i == 28) {
		amsAccountInfo.setParLegalIdcardNo(eleValues.get(39).html().replace("&nbsp;", "").trim());
		// 开户时间
		amsAccountInfo.setAcctCreateDate(eleValues.get(41).html().replace("&nbsp;", "").trim());
		// 开户银行名称")) {
		// allAcct.setBankName(eleValues.get(43).html().replace("&nbsp;",
		// "").trim());
		// 开户银行代码")) {
		amsAccountInfo.setBankCode(eleValues.get(44).html().replace("&nbsp;", "").trim());
		// 账号")) {
		amsAccountInfo.setAcctNo(eleValues.get(45).html().replace("&nbsp;", "").trim());
		// 非预算专用存款账户编号
		amsAccountInfo.setAccountFileNo(eleValues.get(47).html().replace("&nbsp;", "").trim());
		// 账户名称
		amsAccountInfo.setAcctName(eleValues.get(48).html().replace("&nbsp;", "").trim());
		// 备注")) {
		amsAccountInfo.setRemark(eleValues.get(51).html().replace("&nbsp;", "").trim());
		// // 资金性质
		// allAcct.setCapitalProperty(eleValues.get(49).html().replace("&nbsp;",
		// "").trim());
		// // 资金管理人姓名
		// allAcct.setMoneyManager(eleValues.get(52).html().replace("&nbsp;",
		// "").trim());
		// // 资金管理人身份证种类
		// allAcct.setMoneyManagerCtype(eleValues.get(53).html().replace("&nbsp;",
		// "").trim());
		// // 资金管理人身份证件编号
		// allAcct.setMoneyManagerCno(eleValues.get(54).html().replace("&nbsp;",
		// "").trim());
		return amsAccountInfo;
	}

	/**
	 * 获取账户变更前的账户信息
	 * 
	 * @param html
	 * @return
	 */
	private AmsFeiyusuanSyncCondition getFysAcctountData(String html, AmsFeiyusuanSyncCondition amsFeiyusuanSyncCondition) {
		Document doc = Jsoup.parse(html);
		Element commColumn = doc.getElementById("myTable");
		Elements commColumns = commColumn.getElementsByClass("EdtTbLCell");

		// 非预算证明文件1类型
		if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountFileType())) {
			amsFeiyusuanSyncCondition.setAccountFileType(commColumns.get(0).getElementsByAttribute("selected").val());
		}
		// 非预算证明文件1编号
		if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountFileNo())) {
			amsFeiyusuanSyncCondition.setAccountFileNo(commColumns.get(1).getElementsByClass("StdEditBox").val());
		}
		// 资金性质
		if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getCapitalProperty())) {
			amsFeiyusuanSyncCondition.setCapitalProperty(commColumns.get(2).getElementsByAttribute("selected").val());
		}
		// 备注
		if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getRemark())) {
			amsFeiyusuanSyncCondition.setRemark(commColumns.get(9).getElementsByClass("StdEditBox").val());
		}
		// 账户名称构成方式
		if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountNameFrom())) {
			String accountNameType = commColumns.get(8).getElementsByAttribute("selected").val();
			amsFeiyusuanSyncCondition.setAccountNameFrom(accountNameType);
		}
		String accountNameType = amsFeiyusuanSyncCondition.getAccountNameFrom();
		// 与存款人名称一致
		if (accountNameType.equals("0")) {
			Element elementsyz = doc.getElementById("mytableFundManager");
			Elements eleValues = elementsyz.getElementsByClass("EdtTbLCell");
			amsFeiyusuanSyncCondition.setMoneyManager(eleValues.get(0).getElementsByClass("StdEditBox").val());
			amsFeiyusuanSyncCondition.setMoneyManagerCtype(eleValues.get(1).getElementsByAttribute("selected").val());
			amsFeiyusuanSyncCondition.setMoneyManagerCno(eleValues.get(2).getElementsByClass("StdEditBox").val());
		} else if (accountNameType.equals("1")) {// 存款人名称加内设部门
			Element elementsns = doc.getElementById("mytableInterDep");
			Elements eleValues = elementsns.getElementsByClass("EdtTbLCell");
			// 内设部门名称
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideDepartmentName())) {
				amsFeiyusuanSyncCondition.setInsideDepartmentName(eleValues.get(0).getElementsByClass("StdEditBox").val());
			}
			// 内设部门负责人名称
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanName())) {
				amsFeiyusuanSyncCondition.setInsideSaccdepmanName(eleValues.get(1).getElementsByClass("StdEditBox").val());
			}
			// 内设部门非洲人身份证件类型
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanKind())) {
				amsFeiyusuanSyncCondition.setInsideSaccdepmanName(eleValues.get(2).getElementsByAttribute("selected").val());
			}
			// 内设部门负责人身份证件编号
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanNo())) {
				amsFeiyusuanSyncCondition.setInsideSaccdepmanNo(eleValues.get(3).getElementsByClass("StdEditBox").val());
			}
			// 内设部门电话
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideTelphone())) {
				amsFeiyusuanSyncCondition.setInsideTelphone(eleValues.get(4).getElementsByClass("StdEditBox").val());
			}
			// 内设部门邮编
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideZipCode())) {
				amsFeiyusuanSyncCondition.setInsideZipCode(eleValues.get(5).getElementsByClass("StdEditBox").val());
			}
			// 内设部门地址
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideAddress())) {
				amsFeiyusuanSyncCondition.setInsideAddress(eleValues.get(6).getElementsByClass("StdEditBox").val());
			}
		} else if (accountNameType.equals("2")) { // 存款人名称加资金性质
			Element elementzj = doc.getElementById("mytableFundandAcc");
			Elements eleValues = elementzj.getElementsByClass("EdtTbLCell");
			// 后缀值
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getSaccpostfix())) {
				amsFeiyusuanSyncCondition.setSaccpostfix(eleValues.get(1).getElementsByClass("StdEditBox").val());
			}
			// 资金管理人姓名
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManager())) {
				amsFeiyusuanSyncCondition.setMoneyManager(eleValues.get(5).getElementsByClass("StdEditBox").val());
			}
			// 资金管理人身份证件种类
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCtype())) {
				amsFeiyusuanSyncCondition.setMoneyManagerCtype(eleValues.get(6).getElementsByAttribute("selected").val());
			}
			// 资金管理人身份证件编号
			if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCno())) {
				amsFeiyusuanSyncCondition.setMoneyManagerCno(eleValues.get(7).getElementsByClass("StdEditBox").val());
			}
		}
		return amsFeiyusuanSyncCondition;
	}

}
