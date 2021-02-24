package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
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

import java.util.Arrays;
@Component
@Slf4j
public class AmsFeilinshiChangeBeiAnServiceImpl implements AmsFeilinshiChangeBeiAnService {
    private static final String amsChart = "gbk";

    @Autowired
    AmsSearchService amsSearchService;
    @Autowired
    AmsSyncOperateService amsSyncOperateService;
    @Autowired
    AmsSyncOperateService amsSyncOpearateService;
    @Autowired
    PbcCommonService pbcCommonService;
    @Override
    public void openAccountFirstStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        //校验
        validate(condition);
        // 登录用户所在银行机构号和银行机构名称
        String[] bankInfo = amsSearchService.getBankNameAndRegAreaCodeForUserType4(auth);
        log.info("根据网点获取机构号以及地区代码信息如下{}", Arrays.toString(bankInfo));
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
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_changeOrgTempAccountPilot+"?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_changeOrgTempAccountPilot+"?method=forShowSeachChangeOrgTempAccount", auth.getCookie(), null);
        Thread.sleep(1000);
        log.info(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－变更信息录入 参数:{}", bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeOrgTempAccountPilot+ "?method=forShowChangeOrgTempAccount", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－变更信息录入 ",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountSecondStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsBySecondStep(condition);
        log.info(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－变更信息核对参数：{}",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeOrgTempAccountPilot+ "?method=forShowConfirmSeachChangeOrgTempAccount", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－变更信息核对",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountLastStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsByThirdStep(condition);
        log.info(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－>完成变更参数：{}",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_changeOrgTempAccountPilot+ "?method=forPrintChangeOrgTempAccount", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户－>完成变更",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            if(StringUtils.isBlank(html) || html.equals("")){
                throw new SyncException("(取消核准)账号" + condition.getAcctNo() + "变更时同步数据不正确，请重新输入");
            }
            if(html.contains("成功")){
                log.info("变更不涉及到打印，变更完成!");
            }else{
                log.info("变更涉及到打印，开始执行后续操作!");
                if(html.indexOf("name=\"slicno\" ") > -1){
                    Document doc = Jsoup.parse(html);
                    Elements slicnoInput = doc.select("input[name=slicno]");
                    String value = slicnoInput.attr("value");
                    if(StringUtils.isBlank(value)){
                        throw new SyncException("非临时开户许可证号获取失败");
                    }
                    condition.setOpenKey(value);
                }
                changeAccountFourStep(auth,condition);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }
    private void changeAccountFourStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=&printTypeList=REPORT_BASIC_LIC_PILOT&printTime=&fromNotChecked=0&authorize_result=&slicno=");
        urlPars.append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        log.info(condition.getAcctNo()+"(取消核准)变更非临时机构临时存款账户－>完成变更(确认)参数：{}",urlPars.toString());
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/changeOrgTempAccountPilot.do?method=saveChangeOrgTempAccount", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if(result.getStatuCode() == 200){
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更非临时机构临时存款账户－>完成变更(确认)",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            changeAccountFiveStep(auth,condition);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }
    private void changeAccountFiveStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=&printTypeList=ACCOUNT_REPORT_INFO&printTime=2&fromNotChecked=0&authorize_result=&slicno=");
        urlPars.append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        log.info(condition.getAcctNo()+"(取消核准)变更非临时机构临时存款账户－>完成变更(打印)参数：{}",urlPars.toString());
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/changeOrgTempAccountPilot.do?method=forRePrintLic_COPY_PWD", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if(result.getStatuCode() == 200){
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更非临时机构临时存款账户－>完成变更(打印)",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            if(html.contains("成功")){
                Document doc2 = Jsoup.parse(html);
                Elements trs2 = doc2.select("font");
                for (Element tr:trs2) {
                    String linkText1 = tr.text();
                    linkText1 = com.ideatech.ams.pbc.utils.StringUtils.trimSpace(linkText1);
                    if(linkText1.contains("L")){
                        log.info("变更后非临时机构临时存款账户编号：" + linkText1);
                        condition.setAccountLicenseNo(linkText1);
                        return;
                    }else{
                        log.info("非临时机构临时存款账户编号获取失败");
                    }
                }
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }
    private String getUrlParmsByThirdStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("fromNotChecked=0");
            urlPars.append("&add=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&check=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsByThirdStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getFirstBodyUrlParmsByFirstStep(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try{
            urlPars.append("fromNotChecked=0&authorizationFlag=0&alertFlag=0");
            urlPars.append("&errorMsg=&authorizationMsg=&msgSingleId=&operateType=");

            if(StringUtils.isNotBlank(allAcct.getAcctNo())){
                urlPars.append("&choiceType=0");
                //按照账号变更
                urlPars.append("&saccno=");
                urlPars.append(allAcct.getAcctNo());
                urlPars.append("&sacclicno=");
            }else if(StringUtils.isNotBlank(allAcct.getAccountLicenseNo())){
                urlPars.append("&choiceType=1");
                //按照非临时开户许可证号（有字母L）变更
                urlPars.append("&saccno=");
                urlPars.append("&sacclicno=");
                urlPars.append(allAcct.getAccountLicenseNo());
            }else{
                throw new SyncException("账号和非临时开户许可证号（有字母L）不能同时为空");
            }
            // 银行机构代码
            urlPars.append("&saccbankcode=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        } catch (Exception e) {
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getFirstBodyUrlParmsByFirstStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getUrlParmsBySecondStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("fromNotChecked=0&sacckind=");
            //账户名称
            urlPars.append("&saccname=").append(allAcct.getAcctName());
            urlPars.append("&saccfundkind=");
            //证明文件种类
            urlPars.append("&saccfiletype1=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileType(), amsChart));
            //证明文件种类编号
            urlPars.append("&saccfilecode1=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo(), amsChart));
            //备注
            urlPars.append("&sremark=");
            if(StringUtils.isNotBlank(allAcct.getRemark())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getRemark(), amsChart));
            }
            //负责人姓名
            urlPars.append("&saccdepmanname=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrLegalName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalName(), amsChart));
            }
            //负责人身份证件种类
            urlPars.append("&saccdepcrekind=");
            if (StringUtils.isNotBlank(allAcct.getFlsFzrLegalIdcardType())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalIdcardType(), amsChart));
            }
            //项目部名称
            urlPars.append("&saccdepname=");
            if(StringUtils.isNotBlank(allAcct.getFlsProjectName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsProjectName(), amsChart));
            }
            //负责人身份证件号码
            urlPars.append("&saccdepcrecode=");
            if (StringUtils.isNotBlank(allAcct.getFlsFzrLegalIdcardNo())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalIdcardNo(), amsChart));
            }
            //电话
            urlPars.append("&saccdeptel=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrTelephone())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrTelephone(), amsChart));
            }
            //地址
            urlPars.append("&saccdepaddress=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrAddress())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrAddress(), amsChart));
            }
            //邮编
            urlPars.append("&saccdeppostcode=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrZipCode())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrZipCode(), amsChart));
            }
            urlPars.append("&accEntAccountInfo.scurtype=1");
            urlPars.append("&cruArray=1");
            urlPars.append("&add=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&check=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsBySecondStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private void validate(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        if (StringUtils.isBlank(allAcct.getAcctName())){
            throw new SyncException("账户名称不能为空");
        }
        if (StringUtils.isBlank(allAcct.getAccountFileType())){
            throw new SyncException("证明文件种类不能为空");
        }
        if (StringUtils.isBlank(allAcct.getAccountFileNo())){
            throw new SyncException("证明文件种类编号不能为空");
        }
        if(StringUtils.isBlank(allAcct.getAcctNo())&& StringUtils.isBlank(allAcct.getAccountLicenseNo())){
            throw new SyncException("账号和非临时开户许可证号（有字母L）不能同时为空");
        }
    }
}
