package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsYibanSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class AmsYiBanOpenServiceImpl implements AmsYibanOpenService {

    public Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    AmsSearchService amsSearchService;

    @Autowired
    AmsSyncOperateService amsSyncOpearateService;

	@Autowired
	PbcCommonService pbcCommonService;

	@Override
	public void openAccountFirstStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		// 登录用户所在机构的机构号
		String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
		if (ArrayUtils.isNotEmpty(bankInfo)) {
			condition.setBankCode(bankInfo[1]);
			condition.setBankName(bankInfo[0]);
		}
		// 银行所在地地区代码
		String bankAreaCode = amsSearchService.getBankAreCode(auth, bankInfo[1])[1];
		condition.setBankAreaCode(bankAreaCode);
		// 根据开户许可证获取注册地地区代码
		String accountKeyAreaCode = amsSearchService.getAreaCodeByAccountKey(auth, condition.getAccountKey());
		accountKeyAreaCode = amsSearchService.getRegAreaCodeForRemoteOpen(auth,condition.getRegAreaCode());
		logger.info("accountKeyAreaCode : " + accountKeyAreaCode);
		// 校验
		validYibanOpenFirstStep(condition);
		HttpResult result = null;
		String urlPars = getUrlParmsByFirstStep(auth, condition, accountKeyAreaCode);
		logger.info("一般户上报拼接参数：" + urlPars);
		if (urlPars.contains("sacctype=2")) {
			result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_TOGENERAL + "&stype=2", auth.getCookie(), null);
		} else {
			result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_TOGENERAL, auth.getCookie(), null);
		}
		Thread.sleep(1000);
		result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_TOOPENGENERALPAGE, auth.getCookie(), null);
		//20200917  人行更新后增加页面解析  clx
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			//回调9月15号人行更新后函数
			amsSearchService.callbackMethod(auth,html);
		}
		Thread.sleep(1000);
		result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_GENERALVALIDATE, urlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
//		String html = result.getHtml();
		html = result.getHtml();
		if (result.getStatuCode() == 200) {
			pbcCommonService.writeLog(condition.getAcctNo()+"一般户第一步页面打印：",html);
			if (html.indexOf("基本存款账户开户地地区代码录入不正确，请核实后重新录入") > -1) {
				// 部分地区代码录入错误，根据许可证本身地区代码对应的地区代码重新尝试
				if (StringUtils.isNotBlank(accountKeyAreaCode) && !accountKeyAreaCode.equals(condition.getRegAreaCode())) {
					condition.setRegAreaCode(accountKeyAreaCode);
					openAccountFirstStep(auth, condition);
					return;
				}
				throw new SyncException("基本存款账户开户地地区代码录入不正确，请核实后重新录入");
			}
			String validaStr = "";
			if (urlPars.contains("sacctype=2")) {
				validaStr = amsSyncOpearateService.validateOp(html, "录入证明文件信息", auth, condition);
				if (validaStr.contains("基本存款账户不存在")) {
//					if (!condition.getRegAreaCode().substring(4, 6).equals("00")) {// 部分由于地区代码不正确，后面2位改成00则部分会成功
                    if (!StringUtils.substring(condition.getRegAreaCode(), 4, 6).equals("00")) {// 部分由于地区代码不正确，后面2位改成00则部分会成功
                        condition.setRegAreaCode(condition.getRegAreaCode().subSequence(0, 4) + "00");
                        openAccountFirstStep(auth, condition);
                        return;
                    }
                }
            } else {
				//回调9月15号人行更新后函数
				amsSearchService.callbackMethod(auth,html);
                validaStr = amsSyncOpearateService.validateOp(html, "录入证明文件信息");
            }
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
		} else {
            logger.info("基本户开户许可证" + condition.getAccountKey() + "校验异常" + html);
            throw new SyncException("用户登录信息失效");
        }
    }

	@Override
	public AmsAccountInfo openAccountSecondStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		validYibanOpenSecondStep(condition);// 校验
		String urlParms = getUrlParmsBySecondStep(auth, condition);
		HttpResult result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_SHOWBASEINFO, urlParms, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			pbcCommonService.writeLog(condition.getAcctNo()+"一般户第二步页面打印：",html);
			String validaStr = amsSyncOpearateService.validateOp(html, "信息核对");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
//			logger.info("开户许可证" + condition.getAccountKey() + "获取客户客户信息{}", html);
            amsAccountInfo = getAmsAccountByAccountKeyReturnHtml(html);
			amsSearchService.callbackMethod(auth,html);
        } else {
            logger.info("开户许可证" + condition.getAccountKey() + "获取客户客户信息异常{}", html);
            throw new SyncException("用户登录信息失效");
        }
        return amsAccountInfo;
    }

	@Override
	public void openAccountLastStep(LoginAuth auth) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		urlPars.append("&sdepkindhidden=&sdepnamehidden=&saccfilecode1hidden=&deffectdatehidden=");
		urlPars.append("&sdeporgcodehidden=&saccbankcodehidden=&daccbegindatehidden=&saccnohidden=");
		urlPars.append("&saccbanknamehidden=");
		urlPars.append("&nofundflag=false");
		urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPENGENERALACCTINFO;
		HttpResult result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
		result.makeHtml(HttpConfig.HTTP_ENCODING);
		String html = result.getHtml();
		if (result.getStatuCode() == 200) {
			pbcCommonService.writeLog("一般户第三步页面打印：",html);
			// 验证操作是否成功
			String validaStr = amsSyncOpearateService.validateOp(html, "备案成功");
			if (StringUtils.isNotEmpty(validaStr)) {
				throw new SyncException(validaStr);
			}
		} else {
			logger.info("报备一般账户开户页面访问不正确" + html);
			throw new SyncException("用户登录信息失效");
		}
	}

	/**
	 * 开一般户获取第一步时接口参数
	 * 
	 * @param auth
	 * @param condition
	 * @return
	 * @throws SyncException
	 * @throws Exception
	 */
	private String getUrlParmsByFirstStep(LoginAuth auth, AmsYibanSyncCondition condition, String accountKeyRegAreaCode) throws SyncException, Exception {
		StringBuffer urlPars = new StringBuffer();
		try {
			urlPars.append("susertype=2");
			urlPars.append("&sacctype=");
			if (StringUtils.isBlank(accountKeyRegAreaCode) || !accountKeyRegAreaCode.equals(condition.getBankAreaCode())) {// 异地
				urlPars.append("2");
				urlPars.append("&accEntAccountInfo.saccbasearea=").append(condition.getRegAreaCode());
				urlPars.append("&sdepregareahidden=").append(condition.getRegAreaCode());
				//增加一般户本地异地标识
				condition.setOpenAccountSiteType("2");
			} else {// 本地开户，基本户注册地地区代码可以为空
				urlPars.append("&accEntAccountInfo.saccbasearea=").append("");
				urlPars.append("&sdepregareahidden=").append("");
				condition.setOpenAccountSiteType("1");
			}
			urlPars.append("&authorizationFlag=0&alertFlag=0");
			urlPars.append("&errorMsg=&authorizationMsg=&msgSingleId=&operateType=");
			urlPars.append("&regAreaCode=").append(condition.getBankAreaCode());
			urlPars.append("&accEntAccountInfo.saccbaselicno=").append(condition.getAccountKey());
			urlPars.append("&accEntAccountInfo.saccbankcode=").append(condition.getBankCode());
			urlPars.append("&saccbanknamehidden=").append(URLEncoder.encode(condition.getBankName(), "gbk"));
			urlPars.append("&accEntAccountInfo.saccno=").append(condition.getAcctNo());
			urlPars.append("&accEntAccountInfo.daccbegindate=").append(condition.getAcctCreateDate());
			urlPars.append("&accEntAccountInfo.scurtype=1");
			urlPars.append("&cruArray=1");
			urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp");
			urlPars.append("%3B%26nbsp%3B%B6%A8%26nbsp%3B");
		} catch (Exception e) {

            logger.error("查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }

    /**
     * 获取开户第二步接口参数
     *
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    private String getUrlParmsBySecondStep(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("&accEntAccountInfo.saccbaselicno=").append(condition.getAccountKey());
        urlPars.append("&accEntAccountInfo.saccbasearea=").append(condition.getRegAreaCode());
        urlPars.append("&accEntAccountInfo.saccno=").append(condition.getAcctNo());
        urlPars.append("&accEntAccountInfo.saccfiletype1=").append(condition.getAccountFileType());
        urlPars.append("&accEntAccountInfo.saccfilecode1=");
        urlPars.append(EncodeUtils.encodStr(condition.getAccountFileNo(), "gbk"));
        urlPars.append("&accEntAccountInfo.sremark=");
        urlPars.append(EncodeUtils.encodStr(condition.getRemark(), "gbk"));
		//20200917更新后没有这个字段的报送了
        //urlPars.append("&accEntAccountInfo.daccbegindate=").append(condition.getAcctCreateDate());
        return urlPars.toString();
    }

    /**
     * 一般户开户信息校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validYibanOpenFirstStep(AmsYibanSyncCondition condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("开户对象为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("账号不能为空");
        }
        if (StringUtils.isBlank(condition.getAccountKey())) {
            throw new SyncException("基本户开户许可证不能为空");
        }
        if (StringUtils.isEmpty(condition.getAcctCreateDate())) {
            condition.setAcctCreateDate(DateUtils.getNowDateShort());
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("银行所在地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("银行机构号不能为空");
        }
        if (StringUtils.isBlank(condition.getRegAreaCode())) {
            throw new SyncException("基本户注册地地区代码不能为空");
        }
    }

    /**
     * 一般户开户反显基本信息校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validYibanOpenSecondStep(AmsYibanSyncCondition condition) throws SyncException {
        if (StringUtils.isBlank(condition.getAccountKey())) {
            throw new SyncException("基本户开户许可和不能为空");
        }
        if (StringUtils.isBlank(condition.getRegAreaCode())) {
            throw new SyncException("基本户注册地地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getAccountFileType())) {
            throw new SyncException("开户证明文件种类不能为空");
        }
        if (StringUtils.isBlank(condition.getAccountFileNo()) && !StringUtils.equals(condition.getAccountFileType(), "07")) {// 其他证明
            throw new SyncException("开户证明文件编号不能为空");
        }
    }

	/**
	 * 根据开户许可证开户时返回页面得到人行账管信息
	 * 
	 * @param html
	 *            请求后返回的页面
	 * @return
	 */
	private AmsAccountInfo getAmsAccountByAccountKeyReturnHtml(String html) throws SyncException {
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		Document doc = Jsoup.parse(html);
		Elements eleValues = doc.getElementsByClass("EdtTbLCell");
		Elements eleCommonNames = doc.getElementsByClass("EdtTbRCell");
		if (eleCommonNames == null || eleCommonNames.size() < 1) {
			logger.info("一般户反显人行客户信息时，页面异常:" + html);
			return amsAccountInfo;
		}
		// 经营范围
		Element elerop = eleCommonNames.get(19).getElementsByClass("DataTable").get(0);
		String businessScope = elerop.html().replace("&nbsp;", "").trim();
		//包含注释信息则获取label中的内容
		if(businessScope.contains("!--")){
			businessScope = eleCommonNames.get(19).getElementsByTag("label").text().replace("&nbsp;", "").trim();
		}
		amsAccountInfo.setBusinessScope(businessScope);
		// 存款人名称
		amsAccountInfo.setDepositorName(eleValues.get(0).html().replace("&nbsp;", "").trim());
		// 电话
		amsAccountInfo.setTelephone(eleValues.get(1).html().replace("&nbsp;", "").trim());
		// 地址
		//特殊字符转义
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
		amsAccountInfo.setLegalType(eleValues.get(6).html().replace("&nbsp;", "").trim());
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
		// 无需办理税务登记证的文件或税务机关出具的证明
		amsAccountInfo.setNoTaxProve(eleValues.get(21).html().replace("&nbsp;", "").trim());
		// 证明文件种类2
		amsAccountInfo.setFileType2(eleValues.get(18).html().replace("&nbsp;", "").trim());
		// 证明文件编号2
		amsAccountInfo.setFileNo2(eleValues.get(19).html().replace("&nbsp;", "").trim());
		// 国税登记证号")) {
		amsAccountInfo.setStateTaxRegNo(eleValues.get(22).html().replace("&nbsp;", "").trim());
		// 地税登记证号")) {
		amsAccountInfo.setTaxRegNo(eleValues.get(23).html().replace("&nbsp;", "").trim());
		// 单位名称")) {
		amsAccountInfo.setParCorpName(eleValues.get(24).html().replace("&nbsp;", "").trim());
		// 基本存款账户开户许可证核准号")) {
		amsAccountInfo.setParAccountKey(eleValues.get(25).html().replace("&nbsp;", "").trim());
		// 组织机构代码") && i == 24) {
		amsAccountInfo.setParOrgCode(eleValues.get(26).html().replace("&nbsp;", "").trim());
		// 法定代表人或单位负责人")) {
		amsAccountInfo.setParLegalType(eleValues.get(27).html().replace("&nbsp;", "").trim());
		// "姓名") && i == 26) {
		amsAccountInfo.setParLegalName(eleValues.get(28).html().replace("&nbsp;", "").trim());
		// 证件种类") && i == 27) {
		amsAccountInfo.setParLegalIdcardType(eleValues.get(29).html().replace("&nbsp;", "").trim());
		// 证件号码") && i == 28) {
		amsAccountInfo.setParLegalIdcardNo(eleValues.get(30).html().replace("&nbsp;", "").trim());
//		// 账号")) {//返回的是错误的数据
//		amsAccountInfo.setAcctNo(eleValues.get(33).html().replace("&nbsp;", "").trim());
        return amsAccountInfo;
    }

}
