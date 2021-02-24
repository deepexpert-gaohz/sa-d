package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AmsFeilinshiExtensionBeiAnServiceImpl implements AmsFeilinshiExtensionBeiAnService {
    private static final String amsChart = "gbk";

    @Autowired
    AmsSearchService amsSearchService;
    @Autowired
    AmsSyncOperateService amsSyncOperateService;
    @Autowired
    AmsSyncOperateService amsSyncOpearateService;

    @Override
    public void openAccountFirstStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
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
        /*// 银行所在地地区代码
        String bankAreaCode = amsSearchService.getBankAreCode(auth, bankInfo[1])[1];
        condition.setBankAreaCode(bankAreaCode);*/
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        HttpResult result = null;
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_changeAccountTempLimitPilot+"?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_changeAccountTempLimitPilot+"?method=forShowSeachChangeAccountTempLimit", auth.getCookie(), null);

        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html1 = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("<<<<<=====备案制非临时机构临时存款账户展期点击进入字段填写页面:{}",html1);
            String msg = amsSyncOpearateService.validate(html1);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
        }

        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeAccountTempLimitPilot+ "?method=changeAccountTempLimit", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("<<<<<=====备案制非临时机构临时存款账户展期上报第一步返回页面:{}",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            if(html.indexOf("name=\"slicno\"") > -1){
                Document doc = Jsoup.parse(html);
                Elements slicno= doc.select("input[name=slicno]");
                String value = slicno.attr("value");
                if(StringUtils.isBlank(value)){
                    throw new SyncException("开户许可证编号获取失败");
                }
                condition.setOpenKey(value);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        log.info("<<<<<=====备案制非临时机构临时存款账户展期上报第一步结束");
    }

    @Override
    public void openAccountSecondStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsBySecondStep(condition);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeAccountTempLimitPilot+"?method=saveChangeTempLimit", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("=====备案制非临时机构临时存款账户展期上报第二步返回页面:{}",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        log.info("=====备案制非临时机构临时存款账户展期上报第二步结束");
    }

    @Override
    public void openAccountLastStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsByLastStep(condition);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeAccountTempLimitPilot+"?method=forRePrintLic_COPY_PWD", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("=====备案制非临时机构临时存款账户展期上报第三步返回页面:{}",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            if(html.contains("成功")){
                Document doc2 = Jsoup.parse(html);
                Elements trs2 = doc2.select("font");
                for (Element tr:trs2) {
                    String linkText1 = tr.text().replaceAll(" ", "");
                    if(linkText1.contains("L")){
                        condition.setAccountLicenseNo(linkText1);
                        return;
                    }else{
                        log.info("临时存款账户编号获取失败!");
                    }
                }
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        log.info("=====备案制非临时机构临时存款账户展期上报第三步结束");
    }
    private String getUrlParmsBySecondStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("operateType=&printTypeList=REPORT_BASIC_LIC_PILOT&printTime=&authorize_result=");
            urlPars.append("&slicno=").append(allAcct.getOpenKey());
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiExtensionBeiAnServiceImpl#getUrlParmsBySecondStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getUrlParmsByLastStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("operateType=&printTypeList=REPORT_BASIC_LIC_PILOT&printTime=&authorize_result=");
            urlPars.append("&slicno=").append(allAcct.getOpenKey());
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiExtensionBeiAnServiceImpl#getUrlParmsByLastStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getFirstBodyUrlParmsByFirstStep(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try{
            urlPars.append("authorizationFlag=0&alertFlag=0&errorMsg=");
            urlPars.append("&authorizationMsg=&msgSingleId=&operateType=");
            if(StringUtils.isNotBlank(allAcct.getAcctNo())){
                urlPars.append("&choiceType=0");
                //按照账号展期
                urlPars.append("&sacclicno=");
                urlPars.append("&saccno=");
                urlPars.append(allAcct.getAcctNo());

            }else if(StringUtils.isNotBlank(allAcct.getAccountLicenseNo())){
                urlPars.append("&choiceType=1");
                //按照非临时开户许可证号（有字母L）展期
                urlPars.append("&sacclicno=");
                urlPars.append(allAcct.getAccountLicenseNo());
                urlPars.append("&saccno=");
            }else{
                throw new SyncException("账号和非临时开户许可证号（有字母L）不能同时为空");
            }
            // 银行机构代码
            urlPars.append("&saccbankcode=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            urlPars.append("&SysYear=2004");
            urlPars.append("&daccvailddate=").append(allAcct.getEffectiveDate());
            urlPars.append("&iaccstate=11");
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        } catch (Exception e) {
            log.error("AmsFeilinshiExtensionBeiAnServiceImpl#getFirstBodyUrlParmsByFirstStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }
    private void validate(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        if (StringUtils.isBlank(allAcct.getEffectiveDate())){
            throw new SyncException("申请展期的临时存款账户有效日期不能为空");
        }
        if(StringUtils.isBlank(allAcct.getAcctNo())&& StringUtils.isBlank(allAcct.getAccountLicenseNo())){
            throw new SyncException("账号和非临时开户许可证号（有字母L）不能同时为空");
        }
    }
}
