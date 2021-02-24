package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class AmsResetJibenPrintImpl implements AmsResetJiBenPrintService {

    private static final String amsChart = "gbk";

    @Autowired
    AmsSyncOperateService amsSyncOpearateService;

    @Override
    public AmsAccountInfo resetJiBenPrintFristStep(LoginAuth auth, String acctNo, String pbcCode, String bankName) throws Exception {
        AmsAccountInfo accountModel = new AmsAccountInfo();
        log.info("------------------------------补打基本存款账户信息开始");
        if (StringUtils.isBlank(acctNo)) {
            throw new SyncException("账号不能为空！");
        }
        if (StringUtils.isBlank(pbcCode)) {
            throw new SyncException("银行联行号不能为空！");
        }
        StringBuffer urlPars = getUrlPars(acctNo, pbcCode, bankName);
        log.info("urlPars" + urlPars);
        HttpResult result = null;
        String html = "";
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forShowSeachChangeAccount", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forSeachChangeAccount", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
//        html = readfile("/Users/chenluxiang/Desktop/forSeachChangeAccount.html");
        if (result.getStatuCode() == 200) {

            log.info("==============补打进入基本户详情页面:{}" + html);
            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }

            if (html.contains("补打开户信息")) {
                log.info("查询基本存款账户开户信息成功：" + html);
                Document doc1 = Jsoup.parse(html);
                Element div1 = doc1.getElementById("myTable");
                Elements EdtTbLCells = div1.getElementsByClass("EdtTbLCell");
                accountModel.setDepositorName(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(0).html()).replace("&nbsp;", ""));
                accountModel.setTelephone(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(1).html()).replace("&nbsp;", ""));
                accountModel.setRegAddress(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(2).html()).replace("&nbsp;", ""));
                accountModel.setZipCode(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(3).html()).replace("&nbsp;", ""));
                accountModel.setDepositorType(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(4).html()).replace("&nbsp;", ""));
                accountModel.setOrgCode(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(6).html()).replace("&nbsp;", ""));
//                accountModel.setLegalType(EdtTbLCells.select("input[name=idepmanagestype]:checked").html());
                //TODO 法定代表人
                Elements elements = EdtTbLCells.select("input[name=idepmanagestype]");
                for(Element element : elements){
                    String check = element.attr("checked");
                    System.out.println(check);
                }
                accountModel.setLegalName(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(8).html()).replace("&nbsp;", ""));
                accountModel.setLegalIdcardType(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(9).html()).replace("&nbsp;", ""));
                accountModel.setLegalIdcardNo(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(10).html()).replace("&nbsp;", ""));
                accountModel.setRegAreaCode(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(11).html()).replace("&nbsp;", ""));
                accountModel.setRegCurrencyType(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(14).html()).replace("&nbsp;", ""));
                accountModel.setRegisteredCapital(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(15).html()).replace("&nbsp;", ""));
                //TODO 未标明注册资金
                accountModel.setFileType1En(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.select("input[name=entDepositorInfo.saccfiletype1]").attr("value")).replace("&nbsp;", ""));
                accountModel.setFileType(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(17).html()).replace("&nbsp;", ""));
                accountModel.setFileNo(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(18).html()).replace("&nbsp;", ""));
                accountModel.setBusinessScope(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(19).html()).replace("&nbsp;", ""));
                accountModel.setFileType2(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.select("input[name=entDepositorInfo.saccfiletype2]").attr("value")).replace("&nbsp;", ""));
                accountModel.setFileNo2(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(21).html()).replace("&nbsp;", ""));
                accountModel.setNoTaxProve(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(23).html()).replace("&nbsp;", ""));
                accountModel.setStateTaxRegNo(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(24).html()).replace("&nbsp;", ""));
                accountModel.setTaxRegNo(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(25).html()).replace("&nbsp;", ""));
                accountModel.setIndustryCode(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(26).html()).replace("&nbsp;", ""));
                accountModel.setTovoidDate(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(30).html()).replace("&nbsp;", ""));
                accountModel.setAccountKey(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(42).html()).replace("&nbsp;", ""));
                // 19 空
                accountModel.setParCorpName(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(31).html()).replace("&nbsp;", ""));
                accountModel.setParAccountKey(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(32).html()).replace("&nbsp;", ""));
                accountModel.setParOrgCode(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(33).html()).replace("&nbsp;", ""));
                accountModel.setParLegalName(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(35).html()).replace("&nbsp;", ""));
                accountModel.setParLegalIdcardType(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(36).html()).replace("&nbsp;", ""));
                accountModel.setParLegalIdcardNo(com.ideatech.common.utils.StringUtils.trim(EdtTbLCells.get(37).html()).replace("&nbsp;", ""));
            } else {
                log.error("{}补打基本户存款人信息错误，查询基本存款账户开户信息错误，请求页面如下：{}", new Object[]{html});
                throw new SyncException("补打基本户存款人信息错误，查询基本存款账户开户信息错误");
            }
        }
        return accountModel;
    }

    @Override
    public String[] resetJiBenPrintSecondStep(LoginAuth auth,String fileType) throws Exception {

        String[] str = new String[3];
        String[] selectPwd = null;
        //第一部获取基本户编号
        String[] slicno = getSlicno(auth,fileType);
        String slicno2 = getSlicno2(auth, slicno[0],slicno[1]);
        log.info("------------------------------获取基本户开户许可证，基本户编号，执行post方法：forRePrintLic_COPY_PWD");
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=");
        urlPars.append("&printTypeList=ACCOUNT_REPORT_INFO");
        urlPars.append("&printTime=2");
        urlPars.append("&accEntAccountInfo.sacckind=").append(fileType);
        urlPars.append("&authorize_result=");
        urlPars.append("&slicno=").append(slicno2);
        urlPars.append("&sub=%26nbsp%3B%B4%F2%26nbsp%3B%26nbsp%3B%D3%A1%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        HttpResult result = null;
        String html = "";
        log.info("urlPars" + urlPars);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forRePrintLic_COPY_PWD", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
//        html = readfile("/Users/chenluxiang/Desktop/forRePrintLic_COPY_PWD.html");
        if (result.getStatuCode() == 200) {

            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }

            if (html.contains("开户许可证核准号")) {
                Document doc = Jsoup.parse(html);
                Elements elements = doc.getElementsByClass("RltShow");
                String accountKey = elements.get(3).select("font").html().replace("&nbsp;","").trim();
                String openKey = elements.get(5).select("font").select("input[name=slicno]").attr("value");
                //基本户开户许可证
                if(StringUtils.isNotBlank(accountKey)){
                    str[0] = accountKey;
                }
                //基本户编号
                if(StringUtils.isNotBlank(openKey)){
                    str[1] = openKey;
                }
                return str;
            }
        } else {
            log.info("{}存款人查询密码重置错误，请求页面如下：\n{}", new Object[]{html});
            throw new SyncException("获取开户许可证编号错误");
        }
        return null;
    }

    private String[] getSlicno(LoginAuth auth,String fileType) throws Exception {
        String[] arr = new String[2];
        String sacckind = "";
        log.info("------------------------------获取基本户编号，执行post方法：forShowPrintChange");
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("fromNotChecked=0");
        urlPars.append("&iaccstate=11");
        urlPars.append("&entDepositorInfo.saccfiletype1=").append(fileType);
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        urlPars.append("&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        HttpResult result = null;
        String html = "";
        log.info("urlPars" + urlPars);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forShowPrintChange", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
//        html = readfile("/Users/chenluxiang/Desktop/forShowPrintChange.html");
        if (result.getStatuCode() == 200) {

            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }

            if (html.contains("请录入开户许可证编号")) {
                Document doc = Jsoup.parse(html);

                Elements sacckind1 = doc.select("input[name=accEntAccountInfo.sacckind]");
                String sacckind1Value = sacckind1.attr("value");
                if(sacckind1Value != null){
                    sacckind = sacckind1Value;
                    arr[1] = sacckind;
                }
                Elements slicnoInput = doc.select("input[name=slicno]");
                String value = slicnoInput.attr("value");
                if (StringUtils.isBlank(value)) {
                    log.error("-------------获取基本户编号失败，请求页面：" + html);
                    throw new SyncException("基本存款账户开户许可证号获取失败");
                }else{
                    arr[0] = value;
                }
                return arr;
            }
        } else {
            log.info("{}存款人查询密码重置错误，请求页面如下：\n{}", new Object[]{html});
            throw new SyncException("getSlicno获取开户许可证编号错误");
        }
        return null;
    }

    private String getSlicno2(LoginAuth auth, String slicno,String sacckind) throws Exception {
        log.info("------------------------------执行post方法：forPrintLic");
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("operateType=");
        urlPars.append("&printTypeList=REPORT_BASIC_LIC_PILOT");
        urlPars.append("&printTime=");
        urlPars.append("&accEntAccountInfo.sacckind=").append(sacckind);
        urlPars.append("&authorize_result=");
        urlPars.append("&slicno=").append(slicno);
        urlPars.append("&sub=%26nbsp%3B%B4%F2%26nbsp%3B%26nbsp%3B%D3%A1%26nbsp%3B");
        urlPars.append("&res=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
        HttpResult result = null;
        String html = "";
        log.info("urlPars" + urlPars);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_resetJiBenPrint + "?method=forPrintLic", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
//        html = readfile("/Users/chenluxiang/Desktop/forPrintLic.html");
        if (result.getStatuCode() == 200) {

            String msg = amsSyncOpearateService.validate(html);
            if(StringUtils.isNotBlank(msg)){
                throw new SyncException(msg);
            }

            if (html.contains("请录入开户许可证编号")) {
                Document doc = Jsoup.parse(html);
                Elements slicnoInput = doc.select("input[name=slicno]");
                String value = slicnoInput.attr("value");
                if (StringUtils.isBlank(value)) {
                    log.error("-------------获取基本户编号失败，请求页面：" + html);
                    throw new SyncException("基本存款账户开户许可证号获取失败");
                }
                return value;
            }
        } else {
            log.info("{}存款人查询密码重置错误，请求页面如下：\n{}", new Object[]{html});
            throw new SyncException("getSlicno2获取开户许可证编号错误");
        }
        return null;
    }


    private StringBuffer getUrlPars(String acctNo, String pbcCode, String bankName) throws Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("authorizationFlag=0");
        urlPars.append("&alertFlag=0");
        urlPars.append("&errorMsg=");
        urlPars.append("&authorizationMsg=");
        urlPars.append("&msgSingleId=");
        urlPars.append("&operateType=");
        urlPars.append("&saccno=").append(acctNo);
        urlPars.append("&saccbankcode=").append(pbcCode);
        urlPars.append("&saccbanknamehidden=").append(EncodeUtils.encodStr(bankName, amsChart));
        urlPars.append("&iaccstate=11");
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        return urlPars;
    }

    public String readfile(String filePath) {
        File file = new File(filePath);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        byte[] bytes = new byte[1024];
        try {
            for (int n; (n = input.read(bytes)) != -1; ) {
                buffer.append(new String(bytes, 0, n, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 获取打印信息
     * @param html
     * @return
     */
    private String[] dealPrintHtml(String html){
        String[] array = null;
        if(StringUtils.isBlank(html)){
            return array;
        }
        log.info("原始html："+html);
        Matcher matcher = Pattern.compile("webPrint.SetXML\\(\"(.*?)\"\\)").matcher(html);
        while (matcher.find()) {
            html = matcher.group(1);
            if(StringUtils.isBlank(html)){
                continue;
            }
            if(!html.startsWith("H4s")){
                continue;
            }
            log.info("抓取后的html截取前："+html);
            html = html.replaceAll("webPrint.SetXML","")
                    .replaceAll("webPrint.SetXLS","")
                    .replaceAll("\\(","")
                    .replaceAll("\\);","")
                    .replaceAll("\"","").replaceAll(" ","");
            log.info("截取后且没解压的html："+html);
            //System.out.println("解压后："+unzipString(str));
            try {
                html = unzipString(html);
            } catch (IOException e) {
                log.error("解压打印信息异常："+e.toString());
                return array;
            }
            Document doc = Jsoup.parse(html);
            Elements slicnoInputs = doc.getElementsByTag("textContent");
            array = new String[slicnoInputs.size()];
            int i = 0;
            for (Element slicnoInput1 : slicnoInputs) {
                String linkText = slicnoInput1.text();
                array[i] = linkText;
                log.info("参数："+linkText);
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
        return IOUtils.toString(gin,"utf-8");
    }
}