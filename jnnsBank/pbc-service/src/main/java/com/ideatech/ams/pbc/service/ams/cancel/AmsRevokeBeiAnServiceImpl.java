package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeBeiAnSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AmsRevokeBeiAnServiceImpl implements AmsRevokeBeiAnService {
	private static final String amsChart = "gbk";

	@Autowired
    AmsSearchService amsSearchService;
	@Autowired
	PbcCommonService pbcCommonService;
	@Autowired
    AmsSyncOperateService amsSyncOperateService;
	@Autowired
    AmsSyncOperateService amsSyncOpearateService;

	@Override
	public void revokeAccountFirstStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception {
		log.info("<<<<<=====取消核准销户上报第一步开始");
		//校验
		validate(condition);
		// 登录用户所在银行机构号和银行机构名称
		String[] bankInfo = amsSearchService.getBankNameAndRegAreaCodeForUserType4(auth);
		if (ArrayUtils.isNotEmpty(bankInfo)) {
			if(StringUtils.isNotBlank(bankInfo[1])){
				condition.setBankCode(bankInfo[1]);
			}
			if(StringUtils.isNotBlank(bankInfo[0])){
				condition.setBankName(bankInfo[0]);
			}
		}
		// 上报
		HttpResult result = null;
		String html = "";
		result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_closeBasicPilot+"?method=forFrameForward", auth.getCookie(), null);
		Thread.sleep(1000);
		result = HttpUtils.get(auth.getDomain() +  AmsConfig.ZG_URL_closeBasicPilot+"?method=forSearchCloseBasic", auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		if (result.getStatuCode() == 200) {
			String bodyUrlPars = getFirstBodyUrlParmsBySecondStep(condition);
			result = HttpUtils.post(auth.getDomain() +  AmsConfig.ZG_URL_closeBasicPilot+"?method=forSearchCloseBasicSubmit", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
			result.makeHtml(HttpConfig.HTTP_ENCODING);
			html = result.getHtml();
			if (result.getStatuCode() == 200) {
				log.info("<<<<<=====取消核准销户上报第一步返回页面:{}", html);
				String mgs = amsSyncOpearateService.validate(html);
				if (StringUtils.isNotBlank(mgs)) {
					throw new SyncException(mgs);
				}
				// 获取现撤销对应的基本户信息
			} else {
				log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
				throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
			}
		} else {
			log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
			throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
		}
		log.info("<<<<<=====取消核准销户上报第一步结束");
	}

	@Override
	public void revokeAccountSecondStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception {
		log.info("<<<<<=====取消核准销户上报第二步开始");
		if (StringUtils.isBlank(condition.getCancenReason())) {
			throw new SyncException("账户销户原因不能为空");
		}
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("sdeletereason=").append(condition.getCancenReason());
		urlPars.append("&iaccstate=4");
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		String url = auth.getDomain() + AmsConfig.ZG_URL_closeBasicPilot+ "?method=forShowConfirmSeachCloseBasic";
		HttpResult result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		log.info("取消核准销户第二步：" + html);
		log.info("取消核准销户第二步，销户类型为：" + condition.getCancenReason());
		if (result.getStatuCode() == 200) {
			// 验证操作是否成功
			String validaStr = amsSyncOperateService.validateOp(html, "成功");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
			//如果基本户销户时转户，需打印
			if(condition.getCancenReason().equals("1")){
				getResult(html,condition);
			}
		} else {
			log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
			throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
		}
		log.info("<<<<<=====取消核准销户上报第二步结束");
	}
	private String getFirstBodyUrlParmsBySecondStep(AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		try {
			if (StringUtils.isBlank(condition.getCancenReason())) {
				throw new SyncException("账户销户原因不能为空");
			}
			// 账号
			if(StringUtils.isNotBlank(condition.getAcctNo())){
				urlPars.append("saccno=").append(EncodeUtils.encodStr(condition.getAcctNo(), amsChart));
				urlPars.append("&sacclicno=");
			}else if(StringUtils.isNotBlank(condition.getAccountKey())){
				urlPars.append("saccno=");
				urlPars.append("&sacclicno=").append(EncodeUtils.encodStr(condition.getAccountKey(), amsChart));
			}else{
				throw new SyncException("账号和开户许可证号必须填其中一个");
			}

			// 银行机构代码
			urlPars.append("&saccbankcode=").append(condition.getBankCode());
			// 银行机构名称
			urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(condition.getBankName(), amsChart));
			// 销户原因
			urlPars.append("&sdeletereason=").append(condition.getCancenReason());
			urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
		} catch (Exception e) {
			log.error("AmsJibenRevokeBeiAnServiceImpl#getSecondBodyUrlParmsBySecondStep#查询校验参数拼接异常", e);
			throw new SyncException("查询校验参数拼接异常");
		}
		return urlPars.toString();
	}


	@Override
	public AmsAccountInfo getAmsAccountInfoRevokeAccountFirstStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		//校验
		validate(condition);
		// 登录用户所在银行机构号和银行机构名称
		String[] bankInfo = amsSearchService.getBankNameAndRegAreaCodeForUserType4(auth);
		if (ArrayUtils.isNotEmpty(bankInfo)) {
			if(StringUtils.isNotBlank(bankInfo[1])){
				condition.setBankCode(bankInfo[1]);
			}
			if(StringUtils.isNotBlank(bankInfo[0])){
				condition.setBankName(bankInfo[0]);
			}
		}
		// 上报
		HttpResult result = null;
		String html = "";
		result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_closeBasicPilot+"?method=forFrameForward", auth.getCookie(), null);
		Thread.sleep(1000);
		result = HttpUtils.get(auth.getDomain() +  AmsConfig.ZG_URL_closeBasicPilot+"?method=forSearchCloseBasic", auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		if (result.getStatuCode() == 200) {
			String bodyUrlPars = getFirstBodyUrlParmsBySecondStep(condition);
			result = HttpUtils.post(auth.getDomain() +  AmsConfig.ZG_URL_closeBasicPilot+"?method=forSearchCloseBasicSubmit", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
			result.makeHtml(HttpConfig.HTTP_ENCODING);
			html = result.getHtml();
			pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)撤销银行结算账户－>信息核对(查询)",html);
			if (result.getStatuCode() == 200) {
				String mgs = amsSyncOpearateService.validate(html);
				if (StringUtils.isNotBlank(mgs)) {
					throw new SyncException(mgs);
				}
				// 获取现撤销对应的基本户信息
				amsAccountInfo = getCloseBaseData(html);
			} else {
				log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
				throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
			}
		} else {
			log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
			throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
		}
		return amsAccountInfo;
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
		if (com.ideatech.common.utils.StringUtils.isNotEmpty(acctType)) {
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
		return amsAccountInfo;
	}
	private void validate(AmsRevokeBeiAnSyncCondition condition) throws SyncException {

		if(StringUtils.isBlank(condition.getCancenReason())){
			throw new SyncException("销户原因不能为空");
		}
		if(StringUtils.isBlank(condition.getAcctNo()) && StringUtils.isBlank(condition.getAccountKey())){
			throw new SyncException("账号和开户许可证号必填其中一个");
		}

	}
	private void getResult(String html, AmsRevokeBeiAnSyncCondition allAcct){
		Document doc = Jsoup.parse(html);
		Element elements1 = doc.getElementById("defaultprinttagname");
		Elements trs = elements1.select("tr");
		List<AmsPrintInfo> aLLAccountData = new ArrayList<>();
		for (Element tr:trs) {
			Elements tds = tr.select("td");
			if(tds.size()==9){
				for (Element td:tds) {
					String tdText = td.text();
					if(StringUtils.equals(tdText,"顺序")){
						continue;
					}else{
						AmsPrintInfo account = new AmsPrintInfo();
						//序号
						account.setNum(tds.get(0).text());
						//开户地区代码
						account.setBankAreaCode(tds.get(1).text());
						account.setBankName(tds.get(2).text());
						account.setAcctType(tds.get(3).text());

						account.setAcctNo(tds.get(4).text());
						account.setAcctName(tds.get(5).text());
						account.setAcctCreateDate(tds.get(6).text());
						account.setAccountStatus(tds.get(7).text());
						account.setCancelDate(tds.get(8).text());
						aLLAccountData.add(account);
					}
				}
			}else{
				continue;
			}
		}
		allAcct.setALLAccountData(aLLAccountData);
	}
}
