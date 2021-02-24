package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import java.io.File;
import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

@Component
@Slf4j
public class AmsJibenChangeBeiAnServiceImpl implements AmsJibenChangeBeiAnService {
    private static final String amsChart = "gbk";
    @Autowired
    private AmsSearchService amsSearchService;
    @Autowired
    AmsSyncOperateService amsSyncOpearateService;
    @Autowired
    PbcCommonService pbcCommonService;
    @Override
    public void changeAccountFirstStep(LoginAuth auth, AllAcct condition)
            throws SyncException, Exception {
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
        // 上报
        HttpResult result = null;
        // 请求正文携带的接口参数
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        log.info(condition.getAcctNo()+"(取消核准)变更基本存款账户开户资料信息－>变更审核信息录入参数:{}", bodyUrlPars);
        result = HttpUtils.get(auth.getDomain() + "/ams/changeBasicAccountPilot.do", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + "/ams/viewChangeBasicPilot.do", auth.getCookie(), null);
        //20200917  人行更新后增加页面解析  clx
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            //回调9月15号人行更新后函数
            amsSearchService.callbackMethod(auth,html);
        }
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
//        String html = result.getHtml();
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更基本存款账户开户资料信息－>变更审核信息录入 ",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void changeAccountSecondStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception {
        HttpResult result = null;
        String html = "";
        String bodyUrlPars = getBodyUrlPars(condition);// 参数变更
        log.info(condition.getAcctNo()+"(取消核准)变更基本存款账户开户资料信息－>信息核对参数：{}",bodyUrlPars);
        if (StringUtils.isNotEmpty(condition.getIndustryCode())) {
            // 行业归属
            domainCode(auth, condition.getIndustryCode());
        }
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change_succsess, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更基本存款账户开户资料信息－>信息核对",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            //获取html相关数据，作为第三步请求的条件
            getParms(html,condition);
            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void changeAccountLastStep(LoginAuth auth, AllAcct condition) throws SyncException,Exception {
        String bodyUrlPars = getBodyUrlParsLast(condition);// 参数
        log.info(condition.getAcctNo()+"(取消核准)基本户存款账户是否涉及打印参数：{}",bodyUrlPars);
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/viewPrintLicenseCertificatePilot.do", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本户存款账户是否涉及打印",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            if(StringUtils.isBlank(html) || html.equals("")){
                throw new SyncException("(取消核准)账号" + condition.getAcctNo() + "变更时同步数据不正确，请重新输入");
            }
            if(html.contains("成功")){
                log.info("此次变更不涉及到打印，变更完成!");
            }else{
                log.info("此次变更涉及到打印，开始执行后续操作!");
                if(html.indexOf("name=\"slicno\" ") > -1){
                    Document doc = Jsoup.parse(html);
                    Elements slicnoInput = doc.select("input[name=slicno]");
                    String value = slicnoInput.attr("value");
                    if(StringUtils.isBlank(value)){
                        throw new SyncException("开户许可证号获取失败");
                    }
                    condition.setOpenKey(value);
                }
                amsSearchService.callbackMethod(auth,html);
                changeAccountFourStep(auth,condition);
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoFromChangeAccountFirstStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception {
        AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
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
        // 请求正文携带的接口参数
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        log.info(condition.getAcctNo()+"(取消核准)基本户变更查询参数:{}", bodyUrlPars);
        result = HttpUtils.get(auth.getDomain() + "/ams/changeBasicAccountPilot.do", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + "/ams/viewChangeBasicPilot.do", auth.getCookie(), null);
        //20200917  人行更新后增加页面解析  clx
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            //回调9月15号人行更新后函数
            amsSearchService.callbackMethod(auth,html);
        }
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
//        String html = result.getHtml();
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本户变更查询",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            amsAccountInfo = getBaseData(html);
        } else {
            log.info("{}备案制基本户变更查询失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "备案制基本户变更查询失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        return amsAccountInfo;
    }

    //打印后续操作
    private void changeAccountFourStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=&printTypeList=REPORT_BASIC_LIC_PILOT&printTime=&fromNotChecked=0&authorize_result=&slicno=");
        urlPars.append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        log.info(condition.getAcctNo()+"(取消核准)变更基本存款账户－>基本存款账户开户信息参数：{}",urlPars.toString());
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/saveBasicChangePilot.do", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if(result.getStatuCode() == 200){
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更基本存款账户－>基本存款账户开户信息",html);
            String mgs = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(mgs)) {
                throw new SyncException(mgs);
            }
            amsSearchService.callbackMethod(auth,html);
            changeAccountFiveStep(auth,condition);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }
    private void changeAccountFiveStep(LoginAuth auth, AllAcct condition) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=&printTypeList=ACCOUNT_REPORT_INFO&printTime=2&fromNotChecked=0&authorize_result=&slicno=");
        urlPars.append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        log.info(condition.getAcctNo()+"(取消核准)变更基本存款账户－>完成变更参数：{}",urlPars.toString());
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/forRePrintLic_COPY_PWDPilot.do", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if(result.getStatuCode() == 200){
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)变更基本存款账户－>完成变更",html);
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
                    if(linkText1.contains("J")){
                        log.info("变更类型后基本户核准号：" + linkText1);
                        condition.setAccountKey(linkText1);
                        return;
                    }else{
                        log.info("基本存款账户编号获取失败");
                    }
                }
            }
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getAcctNo(), html });
            throw new SyncException("账号" + condition.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }
    private String getBodyUrlParsLast(AllAcct allAcct) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("fromNotChecked=0");
            urlPars.append("&sdepfundkind=");
            urlPars.append(allAcct.getRegCurrencyTypeAms());
            if(StringUtils.isNotBlank(allAcct.getIaccState())){
                urlPars.append("&iaccstate=").append(allAcct.getIaccState());
            }
            /**
             * fdepfund 注册资金
             */
            urlPars.append("&fdepfund=");
            urlPars.append(allAcct.getRegisteredCapital());
            //TODO:不确定参数作用
            if(StringUtils.isNotBlank(allAcct.getAcctFileType())){
                urlPars.append("&entDepositorInfo.saccfiletype1=").append(allAcct.getAcctFileType());
            }else{
                urlPars.append("&entDepositorInfo.saccfiletype1=1");
            }
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
            urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        } catch (Exception e) {

            log.error("AmsJibenBeiAnChangeServiceImpl#getBodyUrlParsLast#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }
    private String getFirstBodyUrlParmsByFirstStep(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try {
            urlPars.append("fromNotChecked=0&choiceType=0");
            // 账号
            urlPars.append("&saccno=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            urlPars.append("&sacclicno=");
            // 银行机构代码
            urlPars.append("&saccbankcode=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        } catch (Exception e) {

            log.error("AmsJibenBeiAnChangeServiceImpl#getFirstBodyUrlParmsByFirstStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }

    private void domainCode(LoginAuth auth, String domainCodes) throws Exception {
        StringBuffer urlPar = new StringBuffer();
        for (String str : domainCodes.split(",")) {
            urlPar.append("&selectTrades=1");
            urlPar.append("&stradecodes=").append(URLEncoder.encode(str, amsChart));
            urlPar.append("&sdomaincodes=");
            urlPar.append(str.substring(str.length() - 1, str.length()));
        }
        HttpResult result = HttpUtils.post(auth.getDomain() + "/ams/addCallingPilot.do", urlPar.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html =result.getHtml();
        if(result.getStatuCode()==200){
            pbcCommonService.writeLog("(取消核准)行业归属录入",html);
        }

    }
    private String getBodyUrlPars(AllAcct allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer("");
        try {
            //-----------------------隐藏域信息-----------------------------------------
            // 是否从待核准过来
            urlPars.append("fromNotChecked=0");
            // 征询结束后需要的域值
            urlPars.append("&authorizationFlag=0");
            urlPars.append("&alertFlag=0");
            urlPars.append("&errorMsg=");
            urlPars.append("&authorizationMsg=");
            urlPars.append("&msgSingleId=");
            urlPars.append("&operateType=");
            urlPars.append("&tradeanddomainhidden=");
            urlPars.append("&zmbh=");
            urlPars.append("&sdepkindhidden=").append(allAcct.getDepositorType());
            urlPars.append("&saccno=").append(allAcct.getAcctNo());
            urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
            //未标明注册资金不为空证明做过变更，如果变更值为1则需要把注册资金币种和注册资金置为空
            if(StringUtils.isNotBlank(allAcct.getIsIdentification())){
                // 未标明注册资金,本系统1代表勾选
                if (StringUtils.equals(allAcct.getIsIdentification(), "1")) {
                    urlPars.append("&nofundflag=on");
                }else{
                    /**
                     * sdepfundkind 注册资金币种
                     */
                    urlPars.append("&sdepfundkind=");
                    if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())){
                        urlPars.append(allAcct.getRegCurrencyTypeAms());
                    }
                    /**
                     * fdepfund 注册资金
                     */
                    urlPars.append("&fdepfund=");
                    if(StringUtils.isNotBlank(allAcct.getRegisteredCapital())){
                        urlPars.append(allAcct.getRegisteredCapital());
                    }
                }
            }else{
                /**
                 * sdepfundkind 注册资金币种
                 */
                urlPars.append("&sdepfundkind=");
                if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())){
                    urlPars.append(allAcct.getRegCurrencyTypeAms());
                }
                /**
                 * fdepfund 注册资金
                 */
                urlPars.append("&fdepfund=");
                if(StringUtils.isNotBlank(allAcct.getRegisteredCapital())){
                    urlPars.append(allAcct.getRegisteredCapital());
                }
            }
            urlPars.append("&deffectdate=").append(allAcct.getTovoidDate());
            urlPars.append("&sremark=");
            // ----------------------存款人基本信息---------------------------------------
            /**
             * sdepregareahidden accEntDepositorInfo sdepname存款人名称
             */
            urlPars.append("&sdepname=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
            /**
             * sdeptel 电话
             */
            urlPars.append("&sdeptel=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), amsChart));
            /**
             * sdepaddress 地址 包含省市区
             */
            StringBuffer deatialAddress = new StringBuffer("");
            //去除拼接。跟开户保持一致
//            if (!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegProvinceCHName())) {
//                deatialAddress.append(allAcct.getRegProvinceCHName());
//            }
//            if (!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegCityCHName())) {
//                deatialAddress.append(allAcct.getRegCityCHName());
//            }
//            if(!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegAreaCHName())){
//                deatialAddress.append(allAcct.getRegAreaCHName());
//            }
            deatialAddress.append(allAcct.getIndusRegArea());
            urlPars.append("&sdepaddress=");
            urlPars.append(EncodeUtils.encodStr(deatialAddress.toString(), amsChart));
            /**
             * sdeppostcode 邮政编码
             */
            urlPars.append("&sdeppostcode=").append(allAcct.getZipCode());
            /**
             * sdepkind 存款人类别
             */
            //urlPars.append("&sdepkind=").append(allAcct.getDepositorType());
            /**
             * sdeporgcode 组织机构代码
             */
            urlPars.append("&sdeporgcode=");
            urlPars.append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            /**
             * idepmanagestype 法定代表人或单位负责人
             */
            if (StringUtils.isNotEmpty(allAcct.getLegalType()) && !allAcct.getLegalType().equals("0")) {
                urlPars.append("&idepmanagestype=").append(allAcct.getLegalType());
            }
            /**
             * sdepmanagername 法人姓名
             */
            urlPars.append("&sdepmanagername=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));

            if("14".equals(allAcct.getDepositorType())){
                /**
                 * sdepmancrekind 证明文件种类
                 */
                urlPars.append("&sdepcerkind=").append(allAcct.getLegalIdcardTypeAms());
                /**
                 * sdepmancrecode 证明文件编号
                 */
                urlPars.append("&sdepcercode=");
                urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
            }
                /**
                 * sdepmancrekind 身份证件种类
                 */
                urlPars.append("&sdepmancrekind=").append(allAcct.getLegalIdcardTypeAms());
                /**
                 * sdepmancrecode 身份证件编号
                 */
                urlPars.append("&sdepmancrecode=");
                urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));


            /**
             * sdepregarea 注册地地区代码
             */
            //urlPars.append("&sdepregarea=").append(allAcct.getRegAreaCode());
            /**
             * FD_DomainCode 行业归属
             */
            urlPars.append("&FD_DomainCode=");


            /**
             * saccfiletype1 证明文件1种类
             */
            if (StringUtils.isNotEmpty(allAcct.getFileType()) && allAcct.getFileType().equals("07")) {
                allAcct.setFileType("01");
            }
            urlPars.append("&saccfiletype1=");
            if(StringUtils.isNotBlank(allAcct.getFileType())){
                urlPars.append(allAcct.getFileType());
            }

            /**
             * &saccfilecode1 证明文件1编号
             */
            urlPars.append("&saccfilecode1=");
            if (StringUtils.isNotBlank(allAcct.getFileNo())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            }
            /**
             * saccfiletype2 证明文件2种类
             */
            urlPars.append("&saccfiletype2=");
            if (StringUtils.isNotBlank(allAcct.getFileType2())){
                urlPars.append(allAcct.getFileType2());
            }
            /**
             * saccfilecode2 证明文件2编号
             */
            urlPars.append("&saccfilecode2=");
            if (StringUtils.isNotBlank(allAcct.getFileNo2())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo2(), amsChart));
            }
            /**
             * sdepwork 经营范围
             */
            urlPars.append("&sdepwork=");
            if (StringUtils.isNotBlank(allAcct.getBusinessScope())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));
            }
            /**
             * sdepnotaxfile 无需办理税务登记证的文件或税务机关出具的证明
             */
            urlPars.append("&sdepnotaxfile=");
            if(StringUtils.isNotBlank(allAcct.getNoTaxProve())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));
            }

            /**
             * sdepcountaxcode 国税登记证号
             */
            urlPars.append("&sdepcountaxcode=");
            if (StringUtils.isNotBlank(allAcct.getStateTaxRegNo())){
                urlPars.append(allAcct.getStateTaxRegNo());
            }
            /**
             * sdepareataxcode 地税登记证号
             */
            urlPars.append("&sdepareataxcode=");
            if (StringUtils.isNotBlank(allAcct.getTaxRegNo())){
                urlPars.append(allAcct.getTaxRegNo());
            }
            // ----------------------上级法人或主管单位信息---------------------------------------
            /**
             * sdeptname 上级单位名称
             */
            urlPars.append("&sdeptname=");
            if(StringUtils.isNotBlank(allAcct.getParCorpName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
            }
            /**
             * sdeptlic 上级基本存款账户开户许可证核准号
             */
            urlPars.append("&sdeptlic=");
            if(StringUtils.isNotBlank(allAcct.getParAccountKey())){
                urlPars.append(allAcct.getParAccountKey());
            }
            /**
             * sdeptmanorgcode 上级组织机构代码
             */
            urlPars.append("&sdeptmanorgcode=");
            urlPars.append(StringUtils.isNotEmpty(allAcct.getParOrgCode()) ? allAcct.getParOrgCode().replace("-", "") : "");
            /**
             * sdeptmanname 上级法人的姓名
             */
            urlPars.append("&sdeptmanname=");
            if(StringUtils.isNotBlank(allAcct.getParLegalName())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
            }
            /**
             * sdeptmankind 上级法定代表人或单位负责人 1法人 2单位负责人
             */
            if (allAcct.getParLegalType() != null && StringUtils.isNotEmpty(allAcct.getParLegalType().toString())) {
                urlPars.append("&sdeptmankind=").append(allAcct.getParLegalType());
            }
            /**
             * sdeptmancrekind 上级身份证件种类 账户信息：
             */
            urlPars.append("&sdeptmancrekind=");
            if (StringUtils.isNotBlank(allAcct.getParLegalIdcardType())){
                urlPars.append(allAcct.getParLegalIdcardType().split(",")[0]);
            }
            /**
             * sdeptmancrecode 上级身份证件编号
             */
            urlPars.append("&sdeptmancrecode=");
            if(StringUtils.isNotBlank(allAcct.getParLegalIdcardNo())){
                urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardNo(), amsChart));
            }
            urlPars.append("&scurtype=1");
            urlPars.append("&cruArray=1");
            urlPars.append("&sDepManRegion=").append(allAcct.getNationality());
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
            urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        } catch (Exception e) {
            log.error(allAcct.getAcctNo() + "参数拼接失败", e);
            throw new SyncException("账户:"+allAcct.getAcctNo()+",存款人名称："+allAcct.getDepositorName()+"报备时，参数拼接异常");
        }
        return urlPars.toString();
    }

    public static AmsAccountInfo getBaseData(String html) throws Exception{
        log.info("开户获取取消核准变更前人行字段====================");
        AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
        Document doc = Jsoup.parse(html);
        Elements eleValues = doc.getElementsByClass("EdtTbLCell");
        log.info("开户获取法定代表人信息====================");
        // 法定代表人或单位负责人")) {
        String legelTypeValue = eleValues.get(6).getElementsByAttribute("checked").val();
        if (legelTypeValue.equals("1")) {
            amsAccountInfo.setLegalType("法定代表人");
        } else if (legelTypeValue.equals("2")) {
            amsAccountInfo.setLegalType("单位负责人");
        }
        log.info("开户获取上级法定代表人信息====================");
        // 上级法定代表人或单位负责人")) {
        String parlegelTypeValue = eleValues.get(30).getElementsByAttribute("checked").val();
        if (parlegelTypeValue.equals("1")) {
            amsAccountInfo.setParLegalType("法定代表人");
        } else if (parlegelTypeValue.equals("2")) {
            amsAccountInfo.setParLegalType("单位负责人");
        }
        log.info("开户获取营业执照有效期信息====================");
        //营业执照有效期
        String date = eleValues.get(26).html().replace("&nbsp;", "").trim();
        if(date.contains("myDate.year") && date.contains("myDate.month") && date.contains("myDate.date")){
            String year = StringUtils.substringAfterLast(date,"myDate.year=").substring(0,4);
            String month = StringUtils.substringAfterLast(date,"myDate.month=").substring(0,2);
            String day = StringUtils.substringAfterLast(date,"myDate.date=").substring(0,2);
            if(StringUtils.isNumeric(year)){
                if(month.contains(";")){
                    month = "0" + month.replace(";","");
                }
                if(day.contains(";")){
                    day = "0" + day.replace(";","");
                }
                String tovoidDate = year + "-" + month + "-" + day;
                if(StringUtils.isNotBlank(tovoidDate)){
                    amsAccountInfo.setTovoidDate(tovoidDate);
                }
            }
        }
        log.info("开户获取未标明注册资金信息====================");
        // 未标明注册资金
        boolean isIdentification = eleValues.get(15).getElementsByAttribute("checked").isEmpty();
        if(isIdentification){
            //ture未勾选   说明是有资金的
            amsAccountInfo.setIsIdentification("0");
        }else{
            //false说明是勾选了  说明是未标明注册资金的
            amsAccountInfo.setIsIdentification("1");
        }
        log.info("开户获取取消核准变更前人行字段结束====================");
        return amsAccountInfo;
    }

    private void getParms(String html,AllAcct allAcct){
        try {
            Document doc2 = Jsoup.parse(html);
            Elements slicnoInput = doc2.select("input[name=iaccstate]");
            if(slicnoInput!=null){
                String value = slicnoInput.attr("value");
                allAcct.setIaccState(value);
            }
            Elements slicnoInput1 = doc2.select("input[name=entDepositorInfo.saccfiletype1]");
            if(slicnoInput1!=null){
                String value = slicnoInput1.attr("value");
                allAcct.setAcctFileType(value);
            }
        }catch (Exception e){
            log.error("获取第二步返回数据异常:{}",e);
        }

    }
    /*//main方法测试变更第一步全页面抓取信息  暂时不删除（基本户）
    public static void main(String[] args) {
        AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
        Document doc = null;
        try {
            doc = Jsoup.parse(FileUtils.readFileToString(new File("/Users/chenluxiang/Desktop/1234.html"),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements eleValues = doc.getElementsByClass("EdtTbLCell");
        // 存款人名称
        amsAccountInfo.setDepositorName(eleValues.get(0).getElementsByClass("StdEditBox").val().trim());
        // 电话
        amsAccountInfo.setTelephone(eleValues.get(1).getElementsByClass("StdEditBox").val().trim());
        // 地址
        //特殊字符转义
        String address = eleValues.get(2).getElementsByClass("StdEditBox").val().trim();
        if(StringUtils.isNotBlank(address)){
            address = StringEscapeUtils.unescapeHtml(address);
        }
        amsAccountInfo.setRegAddress(address);
        // 邮政编码
        amsAccountInfo.setZipCode(eleValues.get(3).getElementsByClass("StdEditBox").val().trim());
        // 存款人类别")) {
        amsAccountInfo.setDepositorType(eleValues.get(4).getElementsByAttribute("selected").html());
        // 组织机构代码") && i == 5) {
        amsAccountInfo.setOrgCode(eleValues.get(5).getElementsByClass("StdEditBox").val().trim());
        // 法定代表人或单位负责人")) {
        String legelTypeValue = eleValues.get(6).getElementsByAttribute("checked").val();
        if (legelTypeValue.equals("1")) {
            amsAccountInfo.setLegalType("法定代表人");
        } else if (legelTypeValue.equals("2")) {
            amsAccountInfo.setLegalType("单位负责人");
        }
        // 姓名") && i == 7) {
        String legalName = StringEscapeUtils.unescapeHtml(eleValues.get(7).getElementsByClass("StdEditBox").val().trim());
        amsAccountInfo.setLegalName(legalName);
        // 证件种类") && i == 8) {
        amsAccountInfo.setLegalIdcardType(eleValues.get(8).getElementsByAttribute("selected").html());
        // 证件号码") && i == 9) {
        amsAccountInfo.setLegalIdcardNo(eleValues.get(9).getElementsByClass("StdEditBox").val().trim());
        // 注册地地区代码")) {
        amsAccountInfo.setRegAreaCode(eleValues.get(10).html().replace("&nbsp;", "").trim());
        // 注册资金币种")) {
        amsAccountInfo.setRegCurrencyType(eleValues.get(13).getElementsByAttribute("selected").html());
        // 注册资金")) {
        amsAccountInfo.setRegisteredCapital(eleValues.get(14).getElementsByClass("StdEditBox").val().trim());

        // 未标明注册资金
        boolean isIdentification = eleValues.get(15).getElementsByAttribute("checked").isEmpty();
        if(isIdentification){
            //ture未勾选   说明是有资金的
            amsAccountInfo.setIsIdentification("0");
        }else{
            //false说明是勾选了  说明是未标明注册资金的
            amsAccountInfo.setIsIdentification("1");
        }
        // 证明文件种类")) {
        String fileType = eleValues.get(16).getElementsByClass("StdEditBox").val().trim();
        if (fileType.equals("01")) {
            amsAccountInfo.setFileType("工商营业执照");
        }
        // 证明文件编号")) {
        amsAccountInfo.setFileNo(eleValues.get(17).getElementsByClass("StdEditBox").val().trim());
        // 经营范围
        amsAccountInfo.setBusinessScope(eleValues.get(18).getElementsByClass("StdEditBox").val().trim());
        // 证明文件种类2
        String fileType2 = eleValues.get(19).getElementsByAttribute("selected").html();
        if (fileType2.equals("02")) {
            amsAccountInfo.setFileType2("批文");
        }else if (fileType2.equals("03")) {
            amsAccountInfo.setFileType2("登记证书");
        }else if (fileType2.equals("04")) {
            amsAccountInfo.setFileType2("开户证明");
        }else if (fileType2.equals("08")) {
            amsAccountInfo.setFileType2("财政部门的批复书");
        }
        // 证明文件编号2
        amsAccountInfo.setFileNo2(eleValues.get(20).getElementsByClass("StdEditBox").val().trim());
        // 无需办理税务登记的证明文件编号
        amsAccountInfo.setNoTaxProve(eleValues.get(21).getElementsByClass("StdEditBox").val().trim());
        // 国税登记证号")) {
        amsAccountInfo.setStateTaxRegNo(eleValues.get(22).getElementsByClass("StdEditBox").val().trim());
        // 地税登记证号")) {
        amsAccountInfo.setTaxRegNo(eleValues.get(23).getElementsByClass("StdEditBox").val().trim());
        //营业执照有效期
        String date = eleValues.get(26).html().replace("&nbsp;", "").trim();
        if(date.contains("myDate.year") && date.contains("myDate.month") && date.contains("myDate.date")){
            String year = StringUtils.substringAfterLast(date,"myDate.year=").substring(0,4);
            String month = StringUtils.substringAfterLast(date,"myDate.month=").substring(0,2);
            String day = StringUtils.substringAfterLast(date,"myDate.date=").substring(0,2);
            if(StringUtils.isNumeric(year)){
                if(month.contains(";")){
                    month = "0" + month.replace(";","");
                }
                if(day.contains(";")){
                    day = "0" + day.replace(";","");
                }
                String tovoidDate = year + "-" + month + "-" + day;
                if(StringUtils.isNotBlank(tovoidDate)){
                    amsAccountInfo.setTovoidDate(tovoidDate);
                }
            }
        }
        // 上级单位名称")) {
        amsAccountInfo.setParCorpName(eleValues.get(27).getElementsByClass("StdEditBox").val().trim());
        // 上级基本存款账户开户许可证核准号")) {
        amsAccountInfo.setParAccountKey(eleValues.get(28).getElementsByClass("StdEditBox").val().trim());
        // 上级组织机构代码") && i == 24) {
        amsAccountInfo.setParOrgCode(eleValues.get(29).getElementsByClass("StdEditBox").val().trim());
        // 上级法定代表人或单位负责人")) {
        String parlegelTypeValue = eleValues.get(30).getElementsByAttribute("checked").val();
        if (parlegelTypeValue.equals("1")) {
            amsAccountInfo.setParLegalType("法定代表人");
        } else if (parlegelTypeValue.equals("2")) {
            amsAccountInfo.setParLegalType("单位负责人");
        }
        // 上级姓名"
        amsAccountInfo.setParLegalName(eleValues.get(31).getElementsByClass("StdEditBox").val().trim());
        // 上级证件种类")
        amsAccountInfo.setParLegalIdcardType(eleValues.get(32).getElementsByAttribute("selected").html());
        // 上级证件号码")
        amsAccountInfo.setParLegalIdcardNo(eleValues.get(33).getElementsByClass("StdEditBox").val().trim());
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
        amsAccountInfo.setAcctCreateDate(eleValues.get(40).html().replace("&nbsp;", "").trim());
    }*/
}
