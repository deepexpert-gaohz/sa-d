package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
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
public class AmsFeilinshiOpenBeiAnServiceImpl implements AmsFeilinshiOpenBeiAnService {
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
        //validate(condition);

        // 登录用户所在银行机构号和银行机构名称
        String[] bankInfo = amsSearchService.getBankNameAndRegAreaCodeForUserType4(auth);

        String bankAreaCode = "";
        if (ArrayUtils.isNotEmpty(bankInfo)) {
            if(StringUtils.isNotBlank(bankInfo[1])){
                condition.setBankCode(bankInfo[1]);
            }
            if(StringUtils.isNotBlank(bankInfo[0])){
                condition.setBankName(bankInfo[0]);
            }
            // 银行所在地地区代码
            bankAreaCode = bankInfo[2];
            log.info("银行所在地地区代码bankAreaCode=" + bankAreaCode);
            condition.setBankAreaCode(bankAreaCode);
        }

        //此参数中的regAreaCode不是url参数中的regAreaCode，而是sdepregareahidden与accEntAccountInfo.saccbasearea
        log.info("非临时开立时,输入的地区代码为{}", condition.getRegAreaCode());

        // 根据基本开户许可证获取注册地地区代码
        String accountKeyAreaCode = amsSearchService.getAreaCodeByAccountKey(auth, condition.getAccountKey());
        if (StringUtils.isNotBlank(accountKeyAreaCode)) {
            //此处不应该覆盖，应该还是用填写的地区代码
            //condition.setRegAreaCode(accountKeyAreaCode);
        }
        log.info("非临时开立时，根据基本户反显的地区代码为{}", accountKeyAreaCode);


        //这个字段需要远程获取
        String remoteRegAreaCode = amsSearchService.getRegAreaCodeForRemoteOpen(auth, condition.getRegAreaCode());
        if (StringUtils.isNotBlank(remoteRegAreaCode)) {
            condition.setSDepRegAreaHidden(remoteRegAreaCode);
        }else{
            condition.setSDepRegAreaHidden(condition.getRegAreaCode());
        }
        log.info("非临时开立时，根据获取远程地区代码为{}", remoteRegAreaCode);

        if(StringUtils.equals(bankAreaCode,accountKeyAreaCode)){
            //许可证对应的注册地区代码如果基本开户许注册地地区代码一致，则证明开本地临时户
            condition.setOpenAccountSiteType("1");
        }else{
            //许可证对应的注册地区代码如果基本开户许注册地地区代码不一致，则证明开异地临时户
            condition.setOpenAccountSiteType("2");
        }
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        // 上报
        HttpResult result = null;
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+"?method=forFrameForward&stype="+condition.getOpenAccountSiteType(), auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+"?method=forShowBasicImportantInfo", auth.getCookie(), null);

        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html1 = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>审核及控制信息录入",html1);
            String msg = amsSyncOpearateService.validate(html1);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
        }

        Thread.sleep(1000);
        log.info(condition.getAcctNo()+"（取消核准）非临时机构临时存款账户开立－>信息录入参数：{}",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forInputBasicImportantInfo", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>信息录入",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            //判断当前页面是否进入到下一个页面，如果还在原页则还是有问题
            if (html.indexOf("临时存款账户开户原因") == -1) {
                if (html.indexOf("但是和录入的开户地地区代码不一致") > -1) {
                    throw new SyncException("基本存款账户开户地地区代码录入不正确，请核实后重新录入。");
                } else if (html.indexOf("您确认发送征询吗") > -1) {
                    msg = amsSyncOpearateService.validateOp(html, auth, condition);
                    if (StringUtils.isNotBlank(msg)) {
                        throw new SyncException(msg);
                    }
                } else {
                    throw new SyncException("非临时提交后进行下一步异常,请联系系统管理员");
                }
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
        log.info(condition.getAcctNo()+"非临时机构临时存款账户开立－>信息核对参数：{}",bodyUrlPars);

        PbcBussUtils.printObject(condition);

        //根据申请开户原因不同，method不同
        String url = AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forInputBasicOpenAccInfo";
        if ("1".equals(condition.getCreateAccountReason())) {
            url = AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot + "?method=forInputBasicOpenAccInfo_1";
        }
        result = HttpUtils.post(auth.getDomain() + url, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>信息核对",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            if(html.contains("未知异常")){
                throw new SyncException("报送失败,出现未知错误，请求页面如下：\n{}",html);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountThirdStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsByThirdStep(condition);
        log.info(condition.getAcctNo()+"非临时机构临时存款账户开立－>核发开户信息参数：{}",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forSureBasicOpenAccInfo", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>核发开户信息",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }
            if(html.indexOf("name=\"slicno\"") > -1){
                Document doc = Jsoup.parse(html);
                Elements slicnoInput = doc.select("input[name=slicno]");
                String value = slicnoInput.attr("value");
                if(StringUtils.isBlank(value)){
                    throw new SyncException("非临时机构临时存款账户开户许可证编号获取失败");
                }
                condition.setOpenKey(value);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountLastStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception {
        if(StringUtils.isBlank(condition.getOpenKey())){
            throw new SyncException("非临时机构临时存款账户开户许可证编号");
        }
        HttpResult result = null;
        String bodyUrlPars = getUrlParmsByLastStep(condition);
        log.info(condition.getAcctNo()+"非临时机构临时存款账户开立－>核发开户信息参数1：{}",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forPrintLic", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        //打印账户信息
        bodyUrlPars=getUrlParmsByLastStepAccoun(condition);
        log.info(condition.getAcctNo()+"非临时机构临时存款账户开立－>核发开户信息参数2：",bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forRePrintLic_COPY_PWD", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>核发开户信息(打印)",html);
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
                        log.info("非临时开户成功，临时存款账户编号：{}",linkText1);
                        condition.setAccountLicenseNo(linkText1);
                        return;
                    }else{
                        log.info("临时存款账户编号获取失败");
                    }
                }
            }
        }else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        bodyUrlPars = getUrlParmsBySureStep(condition);
        log.info("非临时机构临时存款账户开立－>核发开户信息(确认)参数3："+bodyUrlPars);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot+ "?method=forComplete", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)非临时机构临时存款账户开立－>核发开户信息(确认)",html);
        }
    }
    private String getUrlParmsBySureStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("operateType=01&printTypeList=ACCOUNT_REPORT_INFO");
            urlPars.append("&printActionType=&authorize_result=");
            urlPars.append("&fromNotChecked=0");
            urlPars.append("&slicno=").append(EncodeUtils.encodStr(allAcct.getOpenKey(), amsChart));
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsBySureStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }



    @Override
    public String getFirstBodyUrlParmsByFirstStep(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try{
            urlPars.append("fromNotChecked=0&authorizationFlag=0&alertFlag=0");
            urlPars.append("&errorMsg=&authorizationMsg=&msgSingleId=&operateType=");

            // 网点所在地区代码
            urlPars.append("&regAreaCode=").append(allAcct.getBankAreaCode());
            //基本存款账户编号
            urlPars.append("&accEntAccountInfo.saccbaselicno=");
            urlPars.append(allAcct.getAccountKey());
            if(StringUtils.equals("2",allAcct.getOpenAccountSiteType())){
                urlPars.append("&sacctype=").append(allAcct.getOpenAccountSiteType());
                urlPars.append("&accEntAccountInfo.saccbasearea=").append(allAcct.getRegAreaCode());
                urlPars.append("&sdepregareahidden=").append(allAcct.getSDepRegAreaHidden());
            }else{
                urlPars.append("&sacctype=");
                urlPars.append("&accEntAccountInfo.saccbasearea=");
                urlPars.append("&sdepregareahidden=");
            }
            // 银行机构代码
            urlPars.append("&accEntAccountInfo.saccbankcode=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));

            // 账号
            urlPars.append("&accEntAccountInfo.saccno=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            // 开户日期
            urlPars.append("&accEntAccountInfo.daccbegindate=").append(allAcct.getAcctCreateDate());

            // 到期日
            urlPars.append("&accEntAccountInfo.daccvailddate=").append(allAcct.getEffectiveDate());
            urlPars.append("&accEntAccountInfo.scurtype=1");
            urlPars.append("&cruArray=1");
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
            //开户原因
            urlPars.append("&accEntAccountInfo.saccreason=").append(allAcct.getCreateAccountReason());
            //备注
            urlPars.append("&accEntAccountInfo.sremark=");
            if(StringUtils.isNotBlank(allAcct.getRemark())){
                urlPars.append(allAcct.getRemark());
            }
            //前缀
            urlPars.append("&saccprefix=");
            if(StringUtils.isNotBlank(allAcct.getSaccprefix())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getSaccprefix(), amsChart));
            }
            //后缀
            urlPars.append("&saccpostfix=");
            if (StringUtils.isNotBlank(allAcct.getSaccpostfix())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getSaccpostfix(), amsChart));
            }
            //暂时不知道
            urlPars.append("&accnameprefix=");
            urlPars.append("&accnamesuffix=");
            urlPars.append("&accEntAccountInfo.saccfiletype2=");
            urlPars.append("&accEntAccountInfo.saccfilecode2=");
            //证明文件种类
            urlPars.append("&accEntAccountInfo.saccfiletype1=").append(allAcct.getAccountFileType());
            //证明文件编号
            urlPars.append("&accEntAccountInfo.saccfilecode1=").append(allAcct.getAccountFileNo());
            //项目部名称
            urlPars.append("&accEntAccountInfo.saccdepname=");
            if(StringUtils.isNotBlank(allAcct.getFlsProjectName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsProjectName(), amsChart));
            }
            //负责人身份证件种类
            urlPars.append("&accEntAccountInfo.saccdepcrekind=");
            if (StringUtils.isNotBlank(allAcct.getFlsFzrLegalIdcardType())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalIdcardType(), amsChart));
            }
            //负责人姓名
            urlPars.append("&accEntAccountInfo.saccdepmanname=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrLegalName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalName(), amsChart));
            }
            //负责人身份证件号码
            urlPars.append("&accEntAccountInfo.saccdepcrecode=");
            if (StringUtils.isNotBlank(allAcct.getFlsFzrLegalIdcardNo())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalIdcardNo(), amsChart));
            }
            //地址
            urlPars.append("&accEntAccountInfo.saccdepaddress=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrAddress())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrAddress(), amsChart));
            }

            //邮编
            urlPars.append("&accEntAccountInfo.saccdeppostcode=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrZipCode())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrZipCode(), amsChart));
            }
            //电话
            urlPars.append("&accEntAccountInfo.saccdeptel=");
            if(StringUtils.isNotBlank(allAcct.getFlsFzrTelephone())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrTelephone(), amsChart));
            }
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsBySecondStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getUrlParmsByThirdStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("sdepkindhidden=&sdepnamehidden=");
            urlPars.append("&saccfilecode1hidden=&deffectdatehidden=");
            urlPars.append("&sdeporgcodehidden=&saccbankcodehidden=");
            urlPars.append("&daccbegindatehidden=&saccnohidden=");
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            urlPars.append("&nofundflag=false&fromNotChecked=0");
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsByThirdStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getUrlParmsByLastStep(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("operateType=01&printTypeList=REPORT_BASIC_LIC_PILOT");
            urlPars.append("&printTime=&authorize_result=");
            urlPars.append("&fromNotChecked=0");
            urlPars.append("&slicno=").append(EncodeUtils.encodStr(allAcct.getOpenKey(), amsChart));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsByLastStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getUrlParmsByLastStepAccoun(AmsFeilinshiSyncCondition allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("operateType=01&printTypeList=ACCOUNT_REPORT_INFO");
            urlPars.append("&printTime=2&authorize_result=");
            urlPars.append("&fromNotChecked=0");
            urlPars.append("&slicno=").append(EncodeUtils.encodStr(allAcct.getOpenKey(), amsChart));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
            urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        }catch (Exception e){
            log.error("AmsFeilinshiOpenBeiAnServiceImpl#getUrlParmsByLastStep#查询校验参数拼接异常", e);
            throw new SyncException("信息核对参数拼接异常");
        }
        return urlPars.toString();
    }
    private void validate(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception {
        if (StringUtils.isBlank(allAcct.getOpenAccountSiteType())){
            throw new SyncException("请确认本异地");
        }
        if(StringUtils.equals("2",allAcct.getOpenAccountSiteType())){
            if(StringUtils.isBlank(allAcct.getAccountKeyRegAreaCode())){
                throw new SyncException("请填写异地开户时异地的地区代码");
            }
        }
    }
}
