package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
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
public class AmsRevokeServiceImpl implements AmsRevokeService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AmsSearchService amsSearchService;

	@Autowired
	AmsSyncOperateService amsSyncOperateService;

	@Override
	public AmsAccountInfo revokeAccountFirstStep(LoginAuth auth, AmsRevokeSyncCondition condition) throws SyncException, Exception {
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
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CLOSEBASIC;
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		HttpResult result = HttpUtils.post(url, urlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			String validaStr = amsSyncOperateService.validateOp(html, "信息核对");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			// 获取现撤销对应的基本户信息
			amsAccountInfo = getCloseBaseData(html);
		} else {
			logger.info("账号" + condition.getAcctNo() + "校验是否可以销户时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
		return amsAccountInfo;
	}

	@Override
	public void revokeAccountLastStep(LoginAuth auth, AmsRevokeSyncCondition condition) throws SyncException, Exception {
		if (StringUtils.isBlank(condition.getCancenReason())) {
			throw new SyncException("账户销户原因不能为空");
		}
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("sdeletereason=").append(condition.getCancenReason());
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_CLOSECOMMIT;
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
			logger.info("账号销户提交时异常" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	private String getfirstStepUrlParms(AmsRevokeSyncCondition condition) throws Exception {
		StringBuffer urlPars = new StringBuffer();
		// 帐号
		urlPars.append("&saccno=").append(condition.getAcctNo());
		// 开户许可证号
		urlPars.append("&sacclicno=");
		urlPars.append("&saccbankcode=").append(condition.getBankCode());
		urlPars.append("&saccbanknamehidden=");
		urlPars.append(EncodeUtils.encodStr(condition.getBankName(), "gbk"));
		urlPars.append("&sdeletereason=").append(condition.getCancenReason());
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		return urlPars.toString();
	}

	/**
	 * 校验销户条件是否满足
	 * 
	 * @param condition
	 * @throws SyncException
	 */
	private void validFirstStep(AmsRevokeSyncCondition condition) throws SyncException {
		if (StringUtils.isBlank(condition.getAcctNo())) {
			throw new SyncException("账号不能为空");
		}
		if (StringUtils.isBlank(condition.getBankCode()) || StringUtils.isBlank(condition.getBankName())) {
			throw new SyncException("人行机构代码、名称不能为空");
		}
		if (StringUtils.isBlank(condition.getCancenReason())) {
			throw new SyncException("账户销户原因不能为空");
		}
		if (!PbcBussUtils.canCancel(condition.getAcctType())) {
			throw new SyncException("人行系统只支持报备类账户进行销户，核准类账户请直接到当地人行直接进行销户");
		}
	}

	/**
	 * 获取撤销报备类账户的基本户信息
	 * @param html
	 * @return
	 */
	private AmsAccountInfo getCloseBaseData(String html) {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
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
		// 法定代表人或单位负责人")) {
		String legelTypeValue = eleValues.get(6).getElementsByAttribute("checked").val();
		if (legelTypeValue.equals("1")) {
			amsAccountInfo.setLegalType("法定代表人");
		} else if (legelTypeValue.equals("2")) {
			amsAccountInfo.setLegalType("单位负责人");
		}
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
		// 无需办理税务登记的证明文件编号
		amsAccountInfo.setNoTaxProve(eleValues.get(21).html().replace("&nbsp;", "").trim());
		// 国税登记证号")) {
		amsAccountInfo.setStateTaxRegNo(eleValues.get(23).html().replace("&nbsp;", "").trim());
		// 地税登记证号")) {
		amsAccountInfo.setTaxRegNo(eleValues.get(24).html().replace("&nbsp;", "").trim());
		// 资金性质
		amsAccountInfo.setCapitalProperty(eleValues.get(25).html().replace("&nbsp;", "").trim());
		// 上级单位名称")) {
		amsAccountInfo.setParCorpName(eleValues.get(28).html().replace("&nbsp;", "").trim());
		// 上级基本存款账户开户许可证核准号")) {
		amsAccountInfo.setParAccountKey(eleValues.get(29).html().replace("&nbsp;", "").trim());
		// 上级组织机构代码") && i == 24) {
		amsAccountInfo.setParOrgCode(eleValues.get(30).html().replace("&nbsp;", "").trim());
		// 上级法定代表人或单位负责人")) {
		String parlegelTypeValue = eleValues.get(31).getElementsByAttribute("checked").val();
		if (parlegelTypeValue.equals("1")) {
			amsAccountInfo.setParLegalType("法定代表人");
		} else if (parlegelTypeValue.equals("2")) {
			amsAccountInfo.setParLegalType("单位负责人");
		}
		// 上级姓名"
		amsAccountInfo.setParLegalName(eleValues.get(32).html().replace("&nbsp;", "").trim());
		// 上级证件种类")
		amsAccountInfo.setParLegalIdcardType(eleValues.get(33).html().replace("&nbsp;", "").trim());
		// 上级证件号码")
		amsAccountInfo.setParLegalIdcardNo(eleValues.get(34).html().replace("&nbsp;", "").trim());
		// 开户银行名称")) {
		amsAccountInfo.setBankName(eleValues.get(35).html().replace("&nbsp;", "").trim());
		// 开户银行代码")) {
		amsAccountInfo.setBankCode(eleValues.get(36).html().replace("&nbsp;", "").trim());
		// 账号")) {
		amsAccountInfo.setAcctNo(eleValues.get(37).html().replace("&nbsp;", "").trim());
		// 账户性质
		String acctType = eleValues.get(38).html().replace("&nbsp;", "").trim();
		if (StringUtils.isNotEmpty(acctType)) {
			if (acctType.contains("一般")) {
				amsAccountInfo.setAcctType(AccountType.yiban);
			} else if (acctType.contains("非预算")) {
				amsAccountInfo.setAcctType(AccountType.feiyusuan);
			}
		} else {
			amsAccountInfo.setAcctType(AccountType.yiban);
		}
		// 开户日期")) {
		amsAccountInfo.setAcctCreateDate(eleValues.get(39).html().replace("&nbsp;", "").trim());
		// 账户名称
		amsAccountInfo.setAcctName(eleValues.get(41).html().replace("&nbsp;", "").trim());
		return amsAccountInfo;
	}
}
