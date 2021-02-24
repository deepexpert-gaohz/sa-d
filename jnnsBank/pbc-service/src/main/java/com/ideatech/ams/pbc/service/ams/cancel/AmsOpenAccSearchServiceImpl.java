package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
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
public class AmsOpenAccSearchServiceImpl implements AmsOpenAccSearchService {
    private static final String amsChart = "gbk";
    @Autowired
    AmsSyncOperateService amsSyncOpearateService;
    @Override
    public void openAccSearch(LoginAuth auth, AllAcct condition) throws Exception {
        //参数校验
        validCondition(condition);
        // 请求正文携带的接口参数
        String bodyUrlPars = getFirstBodyUrlParmsByFirstStep(condition);
        //log.info("AmsJibenBeiAnOpenServiceImpl#openAccountFirstStep#请求正文携带接口参数:{}", bodyUrlPars);
        HttpResult result = null;
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPEN_SEARCH+"?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPEN_SEARCH+"?method=forInputSaccInfo", auth.getCookie(), null);
        Thread.sleep(1000);
        result = HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_ACCOUNT_OPEN_SEARCH+ "?method=forCheckSdeppasswork", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            //todo:处理异常情况
            log.info("已开立其他银行账户情况查询>第一步页面>>>{}",html);
            String validaStr = amsSyncOpearateService.validateOp(html, "信息核对");
            if (StringUtils.isNotEmpty(validaStr)) {
                throw new SyncException(validaStr);
            }

        } else {
            log.info("{}查询失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getDepositorName(), html });
            throw new SyncException("存款人名称：" + condition.getDepositorName() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    @Override
    public void checkSdepPassword(LoginAuth auth, AllAcct condition) throws Exception {
        //参数校验
        validCondition(condition);
        // 请求正文携带的接口参数
        String bodyUrlPars = getFirstBodyUrlParmsByCheckSdepPassword(condition);
        HttpResult result =HttpUtils.post(auth.getDomain() +AmsConfig.ZG_URL_ACCOUNT_OPEN_SEARCH+ "?method=forSearchPrintBasicSubmit", bodyUrlPars, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("已开立其他银行账户情况查询>存款人密码检验返回页面>>>{}",html);
            String validaStr = amsSyncOpearateService.validateOp(html, "信息核对");
            if (StringUtils.isNotEmpty(validaStr)) {
                throw new SyncException(validaStr);
            }
            if (html.contains("存款人查询密码错误")) {
                throw new SyncException("存款人查询密码错误，不予打印");
            }else{
                getResult(html,condition);
            }
        } else {
            log.info("{}查询失败,出现未知错误，请求页面如下：\n{}", new Object[] { condition.getDepositorName(), html });
            throw new SyncException("存款人名称：" + condition.getDepositorName() + "报送失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
    }

    private String getFirstBodyUrlParmsByFirstStep(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("accEntDepositorInfo.sdepname=").append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
        urlPars.append("&accEntAccountInfo.sacclicno=").append(EncodeUtils.encodStr(allAcct.getAccountKey(), amsChart));
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        return urlPars.toString();
    }
    private String getFirstBodyUrlParmsByCheckSdepPassword(AllAcct allAcct) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("sdeletereasone=");
        urlPars.append("&accEntAccountInfo.sacclicno=").append(EncodeUtils.encodStr(allAcct.getAccountKey(), amsChart));
        urlPars.append("&sdeppasswork=").append(EncodeUtils.encodStr(allAcct.getSelectPwd(), amsChart));
        urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        urlPars.append("&back=%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B%26nbsp%3B%EF%BF%BD%EF%BF%BD%26nbsp%3B");
        return urlPars.toString();
    }
    private void validCondition(AllAcct condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("查询对象为空");
        }
        if(StringUtils.isBlank(condition.getDepositorName())){
            throw new SyncException("存款人名称不能为空");
        }
        if(StringUtils.isBlank(condition.getAccountKey())){
            throw new SyncException("基本存款账户开户许可证核准号不能为空");
        }
        if(StringUtils.isBlank(condition.getSelectPwd())){
            throw new SyncException("存款人查询密码不能为空");
        }
    }
    private void getResult(String html, AllAcct allAcct){
        Document doc = Jsoup.parse(html);
        Element elements1 = doc.getElementById("defaulttabletagname");
        Elements trs = elements1.select(".classicLook").select("tr");
        List<AmsPrintInfo> aLLAccountData = new ArrayList<>();
        for (Element tr:trs) {
            String trText = tr.text();
            //去除标题行
            if(trText.contains("开户地地区代码")){
                continue;
            }
            AmsPrintInfo account = new AmsPrintInfo();
            Elements tds = tr.select("td");
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
            account.setAcctSuspenDate(tds.get(8).text());
            account.setCancelDate(tds.get(9).text());
            aLLAccountData.add(account);
        }
        allAcct.setALLAccountData(aLLAccountData);
    }
}
