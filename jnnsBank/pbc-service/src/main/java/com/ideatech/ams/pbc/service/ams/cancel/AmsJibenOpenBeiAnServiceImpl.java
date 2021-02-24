package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcCommonService;
import com.ideatech.ams.pbc.service.PbcSyncConfigService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


@Component
@Slf4j
public class AmsJibenOpenBeiAnServiceImpl implements AmsJibenOpenBeiAnService {

    private static final String amsChart = "gbk";
    @Autowired
    AmsSearchService amsSearchService;
    @Autowired
    AmsSyncOperateService amsSyncOpearateService;
    @Autowired
    PbcSyncConfigService pbcSyncConfigService;
    @Autowired
    PbcCommonService pbcCommonService;
    @Override
    public void openAccountFirstStep(LoginAuth auth, AllAcct condition) throws Exception {
        //每次上报第一次调用都查询一次配置
        //开户信息校验
        validJIbenOpenFirstStep(condition);
        // 登录用户所在银行机构号和银行机构名称
        String[] bankInfo = amsSearchService.getBankNameAndRegAreaCodeForUserType4(auth);
        log.info("根据网点获取机构号以及地区代码信息如下{}", Arrays.toString(bankInfo));
        String bankAreaCode = "";
        if (ArrayUtils.isNotEmpty(bankInfo)) {
            if (StringUtils.isNotBlank(bankInfo[1])) {
                condition.setBankCode(bankInfo[1]);
            }
            if (StringUtils.isNotBlank(bankInfo[0])) {
                condition.setBankName(bankInfo[0]);
            }
            // 银行所在地地区代码
            bankAreaCode = bankInfo[2];
            log.info("银行所在地地区代码bankAreaCode=" + bankAreaCode);
            condition.setBankAreaCode(bankAreaCode);
        }
        if (StringUtils.isBlank(bankAreaCode)) {
            condition.setOpenAccountSiteType("1");
        } else if (StringUtils.equals(bankAreaCode, condition.getRegAreaCode())) {
            //银行所在地地区代码如果基本开户许注册地地区代码一致，则证明开本地基本户
            condition.setOpenAccountSiteType("1");
        } else {
            //许可证对应的注册地区代码如果基本开户许注册地地区代码不一致，则证明开异地基本户
            condition.setOpenAccountSiteType("2");
        }
        log.info("本异值判断值为：{}", condition.getOpenAccountSiteType());
        PbcBussUtils.printObject(condition);
        // 上报
        HttpResult result = null;
        // 请求头携带的接口参数
        String headUrlPars = getFirstHeadUrlParmsByFirstStep(condition);
        // 请求正文携带的接口参数
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        // 异地开户
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forFrameForward&stype=" + condition.getOpenAccountSiteType(), auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forShowBasicImportantInfo", auth.getCookie(), null);

        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本存款账户开立－>审核及控制信息录入",html);
            String msg = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(msg)) {
                log.info("人行页面打印：" + html);
                throw new SyncException(msg);
            }
            amsSearchService.callbackMethod(auth,html);
        }
        log.info(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>开户资料信息录入（head）参数:{}", headUrlPars);
        log.info(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>开户资料信息录入（body）参数:{}", bodyUrlPars);
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forInputBasicImportantInfo" + headUrlPars, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
         html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>开户资料信息录入 ",html);
            String msg = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(msg)) {
                log.info("人行页面打印：" + html);
                throw new SyncException(msg);
            }
            //获取开户过程中页面隐藏域operatetType值。用于后面传值
            condition.setOperateTypeValue(getOperateType(html));

            //判断当前页面是否进入到下一个页面，如果还在原页则还是有问题
            if (html.indexOf("存款人基本信息") == -1) {
                if (html.indexOf("您确认发送征询吗") > -1) {
                    //如果是省外异地，则该页面返回当前页面并页面内包含征询字样
                    msg = amsSyncOpearateService.validateOp(html, auth, condition);
                    if (StringUtils.isNotBlank(msg)) {
                        throw new SyncException(msg);
                    }
                } else if (html.indexOf("是否以录入的数据为准") > -1) {
                    revokeOpen(html, auth, condition, bodyUrlPars, headUrlPars);
                } else {
                    throw new SyncException("基本户提交后进行下一步异常,请联系系统管理员");
                }
            }
            //回调9月15号人行更新后函数
            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountSecondStep(LoginAuth auth, AllAcct condition) throws Exception {
        validJIbenOpenSecondStep(condition);
        HttpResult result = null;
        String html = "";
        // 请求头携带的接口参数
        String headUrlPars = getSecondHeadUrlParmsBySecondStep(condition);
        log.info(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>信息核对请求头（head）参数:{}", headUrlPars);
        String bodyUrlPars = getUrlParmsBySecondStep(condition);// 参数
        log.info(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>信息核对请求体（body）参数:{}", bodyUrlPars);
       if (StringUtils.isNotEmpty(condition.getIndustryCode())) {
            // 行业归属
            domainCode(auth, condition.getIndustryCode());
        }
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forInputBasicOpenAccInfo" + headUrlPars, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>信息核对 ",html);
            String validaStr = amsSyncOpearateService.validateOp(html, "信息核对");
            if (StringUtils.isNotEmpty(validaStr)) {
                log.info("人行页面打印：" + html);
                throw new SyncException(validaStr);
            }
            //获取开户过程中页面隐藏域operatetType值。用于后面传值
            condition.setOperateTypeValue(getOperateType(html));
            condition.setSaccfiletype1(saccfiletype1(html));
            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void openAccountLastStep(LoginAuth auth, AllAcct condition) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        // 存款人类别
        urlPars.append("&sdepkindhidden=").append(condition.getDepositorType());
        // 存款人名称
        urlPars.append("&sdepnamehidden=").append(EncodeUtils.encodStr(condition.getDepositorName(), amsChart));
        // 工商注册号
        urlPars.append("&saccfilecode1hidden=").append(EncodeUtils.encodStr(condition.getFileNo(), amsChart));
        // 工商执照有效期
        urlPars.append("&deffectdatehidden=").append(condition.getTovoidDate());
        // 组织机构代码
        urlPars.append("&sdeporgcodehidden=").append(StringUtils.isNotEmpty(condition.getOrgCode()) ? condition.getOrgCode().replaceAll("-", "") : "");
        // 银行机构代码
        urlPars.append("&saccbankcodehidden=").append(condition.getBankCode());
        // 开户日期
        urlPars.append("&daccbegindatehidden=").append(condition.getAcctCreateDate());
        // 账号
        urlPars.append("&saccnohidden=").append(EncodeUtils.encodStr(condition.getAcctNo(), amsChart));
        // 银行机构名称
        urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(condition.getBankName(), amsChart));
        // 未标明注册资金,本系统1代表勾选
        if (StringUtils.equals(condition.getIsIdentification(), "1")) {
            urlPars.append("&nofundflag=true");
        } else {
            urlPars.append("&nofundflag=false");
        }
        urlPars.append("&operateType=").append(condition.getOperateTypeValue());
        urlPars.append("&fromNotChecked=0");
        // 地区代码,本异地不同
        if ("1".equals(condition.getOpenAccountSiteType())) {
            urlPars.append("&areaStr=").append(condition.getRegAreaCode());
        } else {
            urlPars.append("&areaStr=");
        }
        urlPars.append("&accEntDepositorInfo.saccfiletype1=").append(condition.getSaccfiletype1());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        log.info(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>基本存款账户开户信息请求体（body）参数:{}", urlPars.toString());
        String url = auth.getDomain() +  AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT+"?method=forSureBasicOpenAccInfo";
        HttpResult result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)基本存款账户新开户－>基本存款账户开户信息 ",html);
            if(html.indexOf("name=\"slicno\" ") > -1){
                Document doc = Jsoup.parse(html);
                Elements slicnoInput = doc.select("input[name=slicno]");
                String value = slicnoInput.attr("value");
                if (StringUtils.isBlank(value)) {
                    throw new SyncException("基本存款账户开户许可证号获取失败");
                }
                condition.setOpenKey(value);
            }
            //获取开户过程中页面隐藏域operatetType值。用于后面传值
            condition.setOperateTypeValue(getOperateType(html));
            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void getPrintInfo(LoginAuth auth, AllAcct condition) throws Exception {
        if (StringUtils.isBlank(condition.getOpenKey())) {
            throw new SyncException("基本存款账户开户许可证号不能为空");
        }
        //forPrintLic执行步骤
        String forPrintLicUrl =getForPrintLicUrlParms(condition);
        log.info(condition.getAcctNo()+"(取消核准)(forPrintLic)基本存款账户新开户－>基本存款账户开户信息请求参数:{}", forPrintLicUrl);
        String url = auth.getDomain() +  AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT+"?method=forPrintLic";
        //打印账户信息
       // HttpResult result = HttpUtils.post(url, forPrintLicUrl, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        List<Header> headers = amsSearchService.getHeaderForShowResult(auth,auth.getDomain() +  AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT+"?method=forSureBasicOpenAccInfo");
        HttpResult result = HttpUtils.post(url, forPrintLicUrl, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准forPrintLic)基本存款账户新开户－>基本存款账户开户信息 ",html);
            String msg = amsSyncOpearateService.validate(html);
            if (StringUtils.isNotBlank(msg)) {
                log.info("人行页面打印：" + html);
                throw new SyncException(msg);
            }
            //获取开户过程中页面隐藏域operatetType值。用于后面传值
            condition.setOperateTypeValue(getOperateType(html));

            amsSearchService.callbackMethod(auth,html);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        String bodyUrlPars = getInfoParms(condition);
        log.info(condition.getAcctNo()+"(取消核准)基本户开户上报打印【账户信息】请求参数:{}", bodyUrlPars);
        url = auth.getDomain() +  AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT+"?method=forRePrintLic_COPY_PWD";
        //打印账户信息
        result = HttpUtils.post(url, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        String[] resultStr = null;
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)(账户信息)基本存款账户新开户－>基本存款账户开户信息 ",html);
            resultStr = dealPrintHtml(html);
            if (resultStr == null || resultStr.length <= 0) {
                throw new SyncException("查询打印账户信息异常");
            }
            //账户名称
            condition.setAcctName(resultStr[0]);
            //账号
            condition.setAcctNo(resultStr[1]);
            //开户银行
            condition.setBankName(resultStr[2]);
            //法定代表人（单位负责人）姓名
            condition.setLegalName(resultStr[3]);
            //基本开户许可证号编号
            condition.setAccountKey(resultStr[4]);
            //日期
            condition.setPrintDate(resultStr[5] + "-" + resultStr[6] + "-" + resultStr[7]);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        //打印查询密码
        bodyUrlPars = getPwdParms(condition);
        log.info(condition.getAcctNo()+"(取消核准)基本户开户上报打印【查询密码】请求参数:{}", bodyUrlPars);
        result = HttpUtils.post(url, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)(查询密码)基本存款账户新开户－>基本存款账户开户信息 ",html);
            if(resultStr ==null || resultStr.length<=0){
                throw new SyncException("打印查询密码异常");
            }
            resultStr = dealPrintHtml(html);
            //查询密码
            condition.setSelectPwd(resultStr[1]);
        } else {
            log.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{condition.getAcctNo(), html});
            throw new SyncException("账号" + condition.getAcctNo() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        log.info("取消核准AccountsAllInfo保存基本户查询密码;{}，开户许可证号:{},基本存款账户编号：{}", condition.getSelectPwd(), condition.getOpenKey(), condition.getAccountKey());
    }

    /**
     * 获取打印信息
     *
     * @param html
     * @return
     */
    private String[] dealPrintHtml(String html) {
        String[] array = null;
        if (StringUtils.isBlank(html)) {
            return array;
        }
        log.info("原始html：" + html);
        Matcher matcher = Pattern.compile("webPrint.SetXML\\(\"(.*?)\"\\)").matcher(html);
        while (matcher.find()) {
            html = matcher.group(1);
            if (StringUtils.isBlank(html)) {
                continue;
            }
            if (!html.startsWith("H4s")) {
                continue;
            }
            log.info("抓取后的html截取前：" + html);
            html = html.replaceAll("webPrint.SetXML", "")
                    .replaceAll("webPrint.SetXLS", "")
                    .replaceAll("\\(", "")
                    .replaceAll("\\);", "")
                    .replaceAll("\"", "").replaceAll(" ", "");
            log.info("截取后且没解压的html：" + html);
            //System.out.println("解压后："+unzipString(str));
            try {
                html = unzipString(html);
            } catch (IOException e) {
                log.error("解压打印信息异常：" + e.toString());
                return array;
            }
            Document doc = Jsoup.parse(html);
            Elements slicnoInputs = doc.getElementsByTag("textContent");
            array = new String[slicnoInputs.size()];
            int i = 0;
            for (Element slicnoInput1 : slicnoInputs) {
                String linkText = slicnoInput1.text();
                array[i] = linkText;
                log.info("参数：" + linkText);
                i++;
            }
        }
        return array;
    }

    private String unzipString(String compressedStr) throws IOException {
        InputStream bais = new ByteArrayInputStream(compressedStr.getBytes());
        Base64InputStream b64io = new Base64InputStream(bais);
        GZIPInputStream gin = new GZIPInputStream(b64io);
        //toString 方法建议制定编码，否则采用系统默认编码，出现中文编码错误的问题
        return IOUtils.toString(gin, "utf-8");
    }

    /**
     * forPrintLic的参数
     *
     * @param condition
     * @return
     */
    private String getForPrintLicUrlParms(AllAcct condition) {
        StringBuffer urlPars = new StringBuffer();

        urlPars.append("&operateType=").append(condition.getOperateTypeValue());
        urlPars.append("&fromNotChecked=0");

        urlPars.append("&printTypeList=REPORT_BASIC_LIC_PILOT&printTime=&authorize_result=");
        // 地区代码
        urlPars.append("&areaStr=").append(condition.getRegAreaCode());
        urlPars.append("&slicno=").append(condition.getOpenKey());
//        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
//        urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        return urlPars.toString();
    }

    /**
     * 获取打印账户信息
     *
     * @param condition
     * @return
     */
    private String getInfoParms(AllAcct condition) {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("&operateType=").append(condition.getOperateTypeValue());
        urlPars.append("&fromNotChecked=0");
        urlPars.append("&printTypeList=ACCOUNT_REPORT_INFO&printTime=2&authorize_result=");
        // 地区代码
        urlPars.append("&areaStr=").append(condition.getRegAreaCode());
        urlPars.append("&slicno=").append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        log.info("账户信息url：" + urlPars.toString());
        return urlPars.toString();
    }

    /**
     * 打印查询密码
     *
     * @param condition
     * @return
     */
    private String getPwdParms(AllAcct condition) {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("&operateType=").append(condition.getOperateTypeValue());
        urlPars.append("&fromNotChecked=0");
        urlPars.append("&printTypeList=REPORT_LIC_PWD&printTime=3&authorize_result=");
        // 地区代码
        urlPars.append("&areaStr=").append(condition.getRegAreaCode());
        urlPars.append("&slicno=").append(condition.getOpenKey());
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        log.info("密码信息url：" + urlPars.toString());
        return urlPars.toString();
    }

    /**
     * 备案制基本户开户---请求头接口参数
     *
     * @param
     * @return
     * @throws SyncException
     * @throws Exception
     */
    private String getFirstHeadUrlParmsByFirstStep(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try {
            // 存款人类别
            urlPars.append("&sdepkindhidden=").append(allAcct.getDepositorType());
            // 存款人名称
            urlPars.append("&sdepnamehidden=").append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
            // 工商注册号
            urlPars.append("&saccfilecode1hidden=").append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            // 工商执照有效期
            urlPars.append("&deffectdatehidden=").append(allAcct.getTovoidDate());
            // 组织机构代码
            urlPars.append("&sdeporgcodehidden=").append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            // 银行机构代码
            urlPars.append("&saccbankcodehidden=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            // 账号
            urlPars.append("&saccnohidden=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            // 开户日期
            urlPars.append("&daccbegindatehidden=").append(allAcct.getAcctCreateDate());
        } catch (Exception e) {
            log.error("AmsJibenBeiAnOpenServiceImpl#getFirstHeadUrlParmsByFirstStep#查询校验参数拼接异常", e);
            throw new SyncException("接口参数拼接异常");
        }
        return urlPars.toString();

    }

    /**
     * 备案制基本户开户---请求正文接口参数
     *
     * @param
     * @return
     * @throws SyncException
     * @throws Exception
     */
    @Override
    public String getFirstBodyUrlParmsByFirstStep(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try {
            //本地异地有些参数有区别
            //本地
            if ("1".equals(allAcct.getOpenAccountSiteType())) {
                urlPars.append("sdepregareahidden=");
                // 地区代码
                urlPars.append("&areaStr=").append(allAcct.getRegAreaCode());
            } else if ("2".equals(allAcct.getOpenAccountSiteType())) {
                //异地
                urlPars.append("sdepregareahidden=").append(allAcct.getRegAreaCode());
                urlPars.append("&accEntDepositorInfo.sdepregarea=").append(allAcct.getRegAreaCode());
            }

            urlPars.append("&ori_sdepname=&ori_sdeporgcode=");
            urlPars.append("&ori_saccfilecode1=&authorize_result=0&warnLevel=");
            // 注册地区代码
            urlPars.append("&regAreaCode=").append(allAcct.getBankAreaCode());
            urlPars.append("&sacctype=" + allAcct.getOpenAccountSiteType());
            // 异地
            /*if (!allAcct.getBankAreaCode().equals(allAcct.getRegAreaCode())) {
                urlPars.append("2");
            }*/

            /*if (StringUtils.equals("2",allAcct.getOpenAccountSiteType())) {
                urlPars.append("2");
            }*/
            urlPars.append("&authorizationFlag=0&alertFlag=0&errorMsg=&authorizationMsg=&msgSingleId=");
            // 注册地区 --先默认台州市.后测试发现不写也可以
            urlPars.append("&areaNameStr=");
            urlPars.append("&operateType=01&accEntDepositorInfo.spilot=1&accEntAccountInfo.spilot=1");
            // 存款人名称
            urlPars.append("&accEntDepositorInfo.sdepname=").append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
            // 存款人类别
            urlPars.append("&accEntDepositorInfo.sdepkind=").append(allAcct.getDepositorType());
            // 无字号个体工商才会在第一页面显示证件种类和证件号码
            if ("14".equals(allAcct.getDepositorType())) {
                urlPars.append("&accEntDepositorInfo.sdepcerkind=").append(allAcct.getLegalIdcardTypeAms());
                urlPars.append("&accEntDepositorInfo.sdepcercode=").append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
            } else {
                urlPars.append("&accEntDepositorInfo.sdepcerkind=");
                urlPars.append("&accEntDepositorInfo.sdepcercode=");
            }
            // 工商注册号
            urlPars.append("&accEntDepositorInfo.saccfilecode1=").append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            // 工商执照有效期
            urlPars.append("&accEntDepositorInfo.deffectdate=").append(allAcct.getTovoidDate());
            // 组织机构代码
            urlPars.append("&accEntDepositorInfo.sdeporgcode=").append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            // 银行机构代码
            urlPars.append("&accEntAccountInfo.saccbankcode=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            // 账号
            urlPars.append("&accEntAccountInfo.saccno=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            // 开户日期
            urlPars.append("&accEntAccountInfo.daccbegindate=").append(allAcct.getAcctCreateDate());
            urlPars.append("&accEntAccountInfo.saccbaselicno=");
            if (StringUtils.isNotBlank(allAcct.getOldAccountKey())) {
                urlPars.append(allAcct.getOldAccountKey());
            }
            urlPars.append("&accEntAccountInfo.scurtype=1");
            urlPars.append("&cruArray=1");
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");

        } catch (Exception e) {
            log.error("AmsJibenBeiAnOpenServiceImpl#getFirstBodyUrlParmsByFirstStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        return urlPars.toString();
    }

    private String getSecondHeadUrlParmsBySecondStep(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        try {
            // 存款人类别
            urlPars.append("&sdepkindhidden=").append(allAcct.getDepositorType());
            // 存款人名称
            urlPars.append("&sdepnamehidden=").append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
            // 工商注册号
            urlPars.append("&saccfilecode1hidden=").append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            // 工商执照有效期
            urlPars.append("&deffectdatehidden=").append(allAcct.getTovoidDate());
            // 组织机构代码
            urlPars.append("&sdeporgcodehidden=").append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            // 银行机构代码
            urlPars.append("&saccbankcodehidden=").append(allAcct.getBankCode());
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            // 开户日期
            urlPars.append("&daccbegindatehidden=").append(allAcct.getAcctCreateDate());
            // 账号
            urlPars.append("&saccnohidden=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            // 未标明注册资金
            // 该字段本系统1代表勾选，当为1时即nofundflag=true, body中的nofundflag=on
            if (StringUtils.isNotBlank(allAcct.getIsIdentification())) {
                if (StringUtils.equals(allAcct.getIsIdentification(), "0")) {
                    urlPars.append("&nofundflag=false");
                } else {
                    urlPars.append("&nofundflag=true");
                }
            } else {
                urlPars.append("&nofundflag=false");
            }
        } catch (Exception e) {
            log.error("AmsJibenBeiAnOpenServiceImpl#getSecondHeadUrlParmsBySecondStep#查询校验参数拼接异常", e);
            throw new SyncException("查询校验参数拼接异常");
        }
        log.info("变更参数拼接：" + urlPars.toString());
        return urlPars.toString();
    }

    /**
     * 校验
     */
    private void validJIbenOpenFirstStep(AllAcct condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("开户对象为空");
        }
        if (StringUtils.isBlank(condition.getDepositorType())) {
            throw new SyncException("存款人类别不能为空");
        }
        if (StringUtils.isBlank(condition.getDepositorName())) {
            throw new SyncException("存款人名称不能为空");
        }
        if (StringUtils.isBlank(condition.getFileNo())) {
            throw new SyncException("工商注册号不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctCreateDate())) {
            throw new SyncException("开户日期不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("账号不能为空");
        }
        if (StringUtils.isBlank(condition.getRegAreaCode())) {
            throw new SyncException("地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("银行机构代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankName())) {
            throw new SyncException("银行机构名称不能为空");
        }
    }

    private void validJIbenOpenSecondStep(AllAcct condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("开户对象为空");
        }
        if (StringUtils.isBlank(condition.getDepositorType())) {
            throw new SyncException("存款人类别不能为空");
        }
        if (StringUtils.isBlank(condition.getDepositorName())) {
            throw new SyncException("存款人名称不能为空");
        }
        if (StringUtils.isBlank(condition.getFileNo())) {
            throw new SyncException("工商注册号不能为空");
        }

        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("银行机构代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankName())) {
            throw new SyncException("银行机构名称不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctCreateDate())) {
            throw new SyncException("开户日期不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("账号不能为空");
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("注册地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getRegAreaCode())) {
            throw new SyncException("地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getTelephone())) {
            throw new SyncException("电话不能为空");
        }
        if (StringUtils.isBlank(condition.getIndusRegArea())) {
            throw new SyncException("地址不能为空");
        }
        if (StringUtils.isBlank(condition.getZipCode())) {
            throw new SyncException("邮政编码不能为空");
        }
        if (StringUtils.isBlank(condition.getLegalType())) {
            throw new SyncException("法定代表人或单位负责人不能为空");
        }
        if (StringUtils.isBlank(condition.getLegalName())) {
            throw new SyncException("法人姓名不能为空");
        }
        if ("14".equals(condition.getDepositorType())) {
            if (StringUtils.isBlank(condition.getLegalIdcardTypeAms())) {
                throw new SyncException("身份证件种类不能为空");
            }
            if (StringUtils.isBlank(condition.getLegalIdcardNo())) {
                throw new SyncException("身份证件编号不能为空");
            }
        }
    }

    private void domainCode(LoginAuth auth, String domainCodes) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        for (String str : domainCodes.split(",")) {
            urlPars.append("&selectTrades=1");
            urlPars.append("&stradecodes=").append(EncodeUtils.encodStr(str, amsChart));
            urlPars.append("&sdomaincodes=");
            urlPars.append(str.substring(str.length() - 1, str.length()));
        }
        HttpResult result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=addCalling", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html =result.getHtml();
        if(result.getStatuCode()==200){
            pbcCommonService.writeLog("（取消核准）行业归属录入",html);
        }
    }

    protected String getUrlParmsBySecondStep(AllAcct allAcct) throws SyncException {
        StringBuffer urlPars = new StringBuffer("");
        try {
            /*
             * 页面隐藏域信息---与第一页面headurl参数一致
             */
            urlPars.append("sdepkindhidden=").append(allAcct.getDepositorType());
            urlPars.append("&sdepnamehidden=").append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
            urlPars.append("&saccfilecode1hidden=").append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            urlPars.append("&deffectdatehidden=").append(allAcct.getTovoidDate());
            urlPars.append("&sdeporgcodehidden=").append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            // 银行机构代码
            urlPars.append("&saccbankcodehidden=").append(allAcct.getBankCode());
            // 开户日期
            urlPars.append("&daccbegindatehidden=").append(allAcct.getAcctCreateDate());
            // 账号
            urlPars.append("&saccnohidden=").append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            // 银行机构名称
            urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(allAcct.getBankName(), amsChart));
            // 隐藏字段
            urlPars.append("&rltenthidden=&tradeanddomainhidden=&tradeanddomainhiddentemp=");
            // 是否从待核准过来
            urlPars.append("&fromNotChecked=0");
            urlPars.append("&operateType=").append(allAcct.getOperateTypeValue());
            urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
            // 地区代码,本地报送才有该字段
            if (allAcct.getOpenAccountSiteType().equals("1")) {
                urlPars.append("&areaStr=").append(allAcct.getRegAreaCode());
            }
            // ----------------------存款人基本信息---------------------------------------
            /**
             * sdepregareahidden accEntDepositorInfo sdepname存款人名称
             */
            urlPars.append("&accEntDepositorInfo.sdepname=");

            urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));


            /**
             * accEntDepositorInfo.sdeptel 电话
             */
            urlPars.append("&accEntDepositorInfo.sdeptel=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), amsChart));
            /**
             * accEntDepositorInfo.sdepaddress 地址 包含省市区
             */
            /*StringBuffer deatialAddress = new StringBuffer("");
            if (!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegProvinceCHName())) {
                deatialAddress.append(allAcct.getRegProvinceCHName());
            }
            if (!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegCityCHName())) {
                deatialAddress.append(allAcct.getRegCityCHName());
            }
            if(!StringUtils.contains(allAcct.getIndusRegArea(), allAcct.getRegAreaCHName())){
                deatialAddress.append(allAcct.getRegAreaCHName());
            }*/
            //deatialAddress.append(allAcct.getIndusRegArea());
            urlPars.append("&accEntDepositorInfo.sdepaddress=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getIndusRegArea(), amsChart));
            /**
             * accEntDepositorInfo.sdeppostcode 邮政编码
             */
            urlPars.append("&accEntDepositorInfo.sdeppostcode=").append(allAcct.getZipCode());
            /**
             * accEntDepositorInfo.sdepkind 存款人类别
             */
            urlPars.append("&accEntDepositorInfo.sdepkind=").append(allAcct.getDepositorType());
            /**
             * accEntDepositorInfo.sdeporgcode 组织机构代码
             */
            urlPars.append("&accEntDepositorInfo.sdeporgcode=");
            urlPars.append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
            /**
             * accEntDepositorInfo.idepmanagestype 法定代表人或单位负责人
             */
            urlPars.append("&accEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
            /**
             * accEntDepositorInfo.sdepmanagername 法人姓名
             */
            urlPars.append("&accEntDepositorInfo.sdepmanagername=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
            /**
             * accEntDepositorInfo.sdepmancrekind 身份证件种类
             */
            urlPars.append("&accEntDepositorInfo.sdepmancrekind=").append(allAcct.getLegalIdcardTypeAms());
            /**
             * accEntDepositorInfo.sdepmancrecode 身份证件编号
             */
            urlPars.append("&accEntDepositorInfo.sdepmancrecode=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
            /**
             * accEntDepositorInfo.sdepregarea 注册地地区代码
             */
            urlPars.append("&accEntDepositorInfo.sdepregarea=").append(allAcct.getRegAreaCode());

            //如果为未标明，则资金不传送
            if (StringUtils.equals(allAcct.getIsIdentification(), "1")) {
                // 未标明直接传送该字段为on
                urlPars.append("&nofundflag=on");
            } else {
                /**
                 * accEntDepositorInfo.sdepfundkind 注册资金币种
                 */
                urlPars.append("&accEntDepositorInfo.sdepfundkind=");
                urlPars.append(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms()) ? allAcct.getRegCurrencyTypeAms() : "");
                /**
                 * accEntDepositorInfo.fdepfund 注册资金
                 */
                urlPars.append("&accEntDepositorInfo.fdepfund=");
                urlPars.append(allAcct.getRegisteredCapital());
            }
            /**
             * accEntDepositorInfo.saccfiletype1 证明文件1种类
             */
            if (StringUtils.isNotEmpty(allAcct.getFileType()) && allAcct.getFileType().equals("07")) {
                allAcct.setFileType("01");
            }
            urlPars.append("&accEntDepositorInfo.saccfiletype1=").append(StringUtils.isNotBlank(allAcct.getFileType()) ? allAcct.getFileType() : "");
            /**
             * &accEntDepositorInfo.saccfilecode1 证明文件1编号
             */
            urlPars.append("&accEntDepositorInfo.saccfilecode1=");
            if (StringUtils.isNotBlank(allAcct.getFileNo())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
            }
            /**
             * accEntDepositorInfo.saccfiletype2 证明文件2种类
             */
            urlPars.append("&accEntDepositorInfo.saccfiletype2=").append(StringUtils.isNotBlank(allAcct.getFileType2()) ? allAcct.getFileType2() : "");
            /**
             * accEntDepositorInfo.saccfilecode2 证明文件2编号
             */
            urlPars.append("&accEntDepositorInfo.saccfilecode2=");
            if (StringUtils.isNotBlank(allAcct.getFileNo2())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo2(), amsChart));
            }

            /**
             * accEntDepositorInfo.sdepwork 经营范围
             */
            urlPars.append("&accEntDepositorInfo.sdepwork=");
            if (StringUtils.isNotBlank(allAcct.getBusinessScope())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));//去掉取消开户许可证核发字样，这个会在人行页面中添加<font>标签进行展示
            }

            /**
             * accEntDepositorInfo.sdepnotaxfile 无需办理税务登记证的文件或税务机关出具的证明
             */
            urlPars.append("&accEntDepositorInfo.sdepnotaxfile=");
            if (StringUtils.isNotBlank(allAcct.getNoTaxProve())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));
            }
            /**
             * accEntDepositorInfo.sdepcountaxcode 国税登记证号
             */
            urlPars.append("&accEntDepositorInfo.sdepcountaxcode=").append(StringUtils.isNotBlank(allAcct.getStateTaxRegNo()) ? allAcct.getStateTaxRegNo() : "");
            /**
             * accEntDepositorInfo.sdepareataxcode 地税登记证号
             */
            urlPars.append("&accEntDepositorInfo.sdepareataxcode=").append(StringUtils.isNotBlank(allAcct.getTaxRegNo()) ? allAcct.getTaxRegNo() : "");
            urlPars.append("&accEntAccountInfo.sremark=");
            // ----------------------上级法人或主管单位信息---------------------------------------
            /**
             * accEntDepositorInfo.sdeptname 上级单位名称
             */
            urlPars.append("&accEntDepositorInfo.sdeptname=");
            if (StringUtils.isNotBlank(allAcct.getParCorpName())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
            }
            /**
             * accEntDepositorInfo.sdeptlic 上级基本存款账户开户许可证核准号
             */
            urlPars.append("&accEntDepositorInfo.sdeptlic=").append(StringUtils.isNotBlank(allAcct.getParAccountKey()) ? allAcct.getParAccountKey() : "");
            /**
             * accEntDepositorInfo.sdeptmanorgcode 上级组织机构代码
             */
            urlPars.append("&accEntDepositorInfo.sdeptmanorgcode=");
            urlPars.append(StringUtils.isNotEmpty(allAcct.getParOrgCode()) ? allAcct.getParOrgCode().replace("-", "") : "");
            /**
             * accEntDepositorInfo.sdeptmanname 上级法人的姓名
             */
            urlPars.append("&accEntDepositorInfo.sdeptmanname=");
            if (StringUtils.isNotBlank(allAcct.getParLegalName())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
            }

            /**
             * accEntDepositorInfo.sdeptmankind 上级法定代表人或单位负责人 1法人 2单位负责人
             */
            if (allAcct.getParLegalType() != null && StringUtils.isNotEmpty(allAcct.getParLegalType().toString())) {
                urlPars.append("&accEntDepositorInfo.sdeptmankind=").append(allAcct.getParLegalType());
            }
            /**
             * accEntDepositorInfo.sdeptmancrekind 上级身份证件种类 账户信息：
             */
            urlPars.append("&accEntDepositorInfo.sdeptmancrekind=");
            if (StringUtils.isNotBlank(allAcct.getParLegalIdcardType())) {
                urlPars.append(allAcct.getParLegalIdcardType().split(",")[0]);
            }
            /**
             * accEntDepositorInfo.sdeptmancrecode 上级身份证件编号
             */
            urlPars.append("&accEntDepositorInfo.sdeptmancrecode=");
            if (StringUtils.isNotBlank(allAcct.getParLegalIdcardNo())) {
                urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardNo(), amsChart));
            }
            /**
             * accEntAccountInfo.saccbankcode 开户银行代码
             */
            urlPars.append("&accEntAccountInfo.saccbankcode=").append(allAcct.getBankCode());
            /**
             * accEntAccountInfo.saccno 账号
             */
            urlPars.append("&accEntAccountInfo.saccno=");
            urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
            urlPars.append("&accEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B&check=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        } catch (Exception e) {
            log.error(allAcct.getAcctNo() + "参数拼接失败", e);
            throw new SyncException("账户:" + allAcct.getAcctNo() + ",存款人名称：" + allAcct.getDepositorName() + "报备时，参数拼接异常");
        }
        return urlPars.toString();
    }
    private void revokeOpen(String html,LoginAuth auth,AllAcct condition,String bodyUrlPars,String headUrlPars) throws Exception {
        boolean jibenSync=pbcSyncConfigService.isJibenSync();
        log.info("先销后开是否以人行数据为准："+jibenSync);
        //  出现这这一行字证明存款人是先销户后开户
        log.info("body替换前参数:{}", bodyUrlPars);
        log.info("head替换前参数:{}", headUrlPars);
        String url;
        String depName = condition.getDepositorName();
        String fileNo = condition.getFileNo();
        String orgCode = StringUtils.isNotEmpty(condition.getOrgCode()) ? condition.getOrgCode().replaceAll("-", "") : "";
        log.info("账管录入的数据-->depositorName:{},fileNo:{},orgCode:{}", depName, fileNo, orgCode);

        Document doc = Jsoup.parse(html);
        Elements ori = doc.select("input[name=ori_sdeporgcode]");
        String ori_sdeporgcode = ori.attr("value");
        ori = doc.select("input[name=ori_sdepname]");
        String ori_sdepname = ori.attr("value");
        ori = doc.select("input[name=ori_saccfilecode1]");
        String ori_saccfilecode1 = ori.attr("value");

        log.info("人行原来数据-->depositorName:{},fileNo:{},orgCode:{}", ori_sdepname, ori_saccfilecode1, ori_sdeporgcode);

        condition.setOrgCode(ori_sdeporgcode);
        condition.setDepositorName(ori_sdepname);
        condition.setFileNo(ori_saccfilecode1);
        bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        headUrlPars = getFirstHeadUrlParmsByFirstStep(condition);
        log.info("head替换后参数:{}", headUrlPars);
        if (jibenSync) {
            //以人行数据为准
            bodyUrlPars = bodyUrlPars.replaceAll("warnLevel=", "warnLevel=1");
            log.info("body替换后参数{}", bodyUrlPars);
            log.info("以人行原来的数据为准，body的三个参数替换成人行的数据然后请求再次走一遍");
            url = auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forInputBasicImportantInfo" + headUrlPars;
        } else {
            //已录入的数据为准
            bodyUrlPars = bodyUrlPars.replaceAll("ori_sdepname=", "ori_sdepname=" + EncodeUtils.encodStr(depName, amsChart));
            bodyUrlPars = bodyUrlPars.replaceAll("ori_sdeporgcode=", "ori_sdeporgcode=" + orgCode);
            bodyUrlPars = bodyUrlPars.replaceAll("ori_saccfilecode1=", "ori_saccfilecode1=" + fileNo);
            bodyUrlPars = bodyUrlPars.replaceAll("warnLevel=", "warnLevel=1");
            log.info("body替换后参数{}", bodyUrlPars);
            //以录入的数据为准，head里面没有参数
            log.info("以账管录入的数据为准，url里面不带参数数据");
            url = auth.getDomain() +AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT+"?method=forInputBasicImportantInfo";
            condition.setOrgCode(orgCode);
            condition.setDepositorName(depName);
            condition.setFileNo(fileNo);
            log.info("以账管录入的数据为准，url参数拼接完成，condition对象放回原来数据");
        }
        //先销后开都要执行forInputBasicImportantInfo请求
        HttpResult result = HttpUtils.post(url, bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            pbcCommonService.writeLog(condition.getAcctNo()+"(取消核准)(先销后开)基本存款账户重新开户－>核对修改开户资料信息 ",html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                log.info("【取消核准】基本户开户(先销后开)异常信息：{}",msg);
                throw new SyncException(msg);
            }
            //回调9月15号人行更新后函数
            amsSearchService.callbackMethod(auth,html);
            condition.setOperateTypeValue(getOperateType(html));
            //判断当前页面是否进入到下一个页面，如果还在原页则还是有问题
            if (html.indexOf("存款人基本信息") == -1) {
                if (html.indexOf("您确认发送征询吗") > -1) {
                    //如果是省外异地，则该页面返回当前页面并页面内包含征询字样
                    msg = amsSyncOpearateService.validateOp(html, auth, condition);
                    if (StringUtils.isNotBlank(msg)) {
                        log.info("【取消核准】基本户开户征询异常信息：{}",msg);
                        throw new SyncException(msg);
                    }
                } else {
                    throw new SyncException("基本户提交后进行下一步异常,请联系系统管理员");
                }
            }
        }
    }

    public String getOperateType(String html){
        String operateType = "";
        if(html.indexOf("operateType") > -1){
            Document doc = Jsoup.parse(html);
            operateType = doc.select("input[name=operateType]").attr("value");
            log.info("账户开户，operateType值：" + operateType);
            return operateType;
        }
        return "";
    }

    public String saccfiletype1(String html){
        String saccfiletype1 = "";
        if(html.indexOf("accEntDepositorInfo.saccfiletype1") > -1){
            Document doc = Jsoup.parse(html);
            saccfiletype1 = doc.select("input[name=accEntDepositorInfo.saccfiletype1]").attr("value");
            log.info("账户开户，saccfiletype1值：" + saccfiletype1);
            return saccfiletype1;
        }
        return "";
    }
}
