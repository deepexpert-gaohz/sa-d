package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

@Component
@Slf4j
public class AmsSelectPwdResetServiceImpl implements AmsSelectPwdResetService {
    @Override
    public String[] resetSelectPwd(LoginAuth auth, String accountKey, String pwd) throws Exception {
        log.info("重置密码开始----->");
        if(StringUtils.isBlank(accountKey)){
            throw new SyncException("基本存款账户编号不能为空");
        }
        if(StringUtils.isBlank(pwd)){
            throw new SyncException("查询密码不能为空");
        }
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("depositorpwdmodformvo.saccbaselicno=").append(accountKey);
        urlPars.append("&depositorpwdmodformvo.newpassword1=").append(pwd);
        urlPars.append("&depositorpwdmodformvo.newpassword2=").append(pwd);
        HttpResult result = null;
        String html = "";
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_depositorPwdReset+"?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_depositorPwdReset+"?method=forResetDepositorPwd", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_depositorPwdReset+"?method=forResetDepositorPwdSubmit", urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        html = result.getHtml();
        if(result.getStatuCode() == 200){
            if(html.contains("成功")){
                log.info("{}存款人查询密码重置成功", new Object[] { accountKey});
                return dealPrintHtml(html);
            }else{
                //处理错误信息
                Document doc = Jsoup.parse(html);
                Elements slicnoInput = doc.getElementsByClass("error");
                if (slicnoInput != null && slicnoInput.size() > 0) {
                    String mesg = slicnoInput.get(0).html();
                    log.info("{}存款人查询密码重置错误，错误信息：\n{}", new Object[] { accountKey, mesg });
                    throw new SyncException("基本开户许可证号：" + accountKey + "存款人查询密码重置错误,错误信息：" + mesg);
                }else{
                    log.info("{}存款人查询密码重置错误，请求页面如下：\n{}", new Object[] { accountKey, html });
                    throw new SyncException("基本开户许可证号" + accountKey + "存款人查询密码重置错误,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
                }
            }
        }else{
            log.info("{}存款人查询密码重置错误，请求页面如下：\n{}", new Object[] { accountKey, html });
            throw new SyncException("基本开户许可证号" + accountKey + "存款人查询密码重置错误,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");

        }
    }
    private String[] dealPrintHtml(String html){
        String[] array = null;
        if(StringUtils.isBlank(html)){
            return array;
        }
        log.info("抓取html："+html);
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
            log.info("处理后且没解压的html："+html);
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
