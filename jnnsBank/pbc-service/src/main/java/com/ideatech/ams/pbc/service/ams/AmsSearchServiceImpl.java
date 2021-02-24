package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.*;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcSyncConfigService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsFeilinshiOpenBeiAnService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenChangeBeiAnService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenOpenBeiAnService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsRevokeBeiAnService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.NumberUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Slf4j
public class AmsSearchServiceImpl implements AmsSearchService {
    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AmsLoginService amsLoginService;

    @Autowired
    AmsYibanOpenService amsYibanOpenServie;

    @Autowired
    AmsFeiyusuanOpenService amsFeiyusuanOpenService;

    @Autowired
    AmsRevokeService amsRevokeService;

    @Autowired
    AmsSuspendService amsSuspendService;

    @Autowired
    AmsAnnualService amsAnnualService;

    @Autowired
    AmsJibenOpenBeiAnService amsJibenOpenBeiAnService;

    @Autowired
    AmsFeilinshiOpenBeiAnService amsFeilinshiOpenBeiAnService;

    @Autowired
    AmsRevokeBeiAnService amsRevokeBeiAnService;

    @Autowired
    AmsJibenChangeBeiAnService amsJibenChangeBeiAnService;
    @Autowired
    PbcSyncConfigService pbcSyncConfigService;

    @Autowired
    AmsSearchService amsSearchService;

    @Override
    public boolean existAcctInCheckListByAcctNo(LoginAuth auth, String acctNo) throws Exception {
        if (StringUtils.isBlank(acctNo)) {
            throw new SyncException("账号不能为空");
        }
        HttpResult result = null;
        String queryUrl = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_OPEN_LIST;
        result = HttpUtils.get(queryUrl, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            if (html.indexOf(acctNo) > -1) {
                return true;
            } else if (html.indexOf("用户信息失效") > 0) {
                throw new SyncException("用户登录信息失效");
            }
            // logger.info("在待核准类账户未查询到账号" + acctNo + ",访问的页面是:" + html);
        } else {
            throw new SyncException("用 户登录信息失效");
        }
        Pattern p = Pattern.compile("共(\\d*?)行,  第(\\d*?)页 共(\\d*?)页");
        Matcher matcher = p.matcher(html);
        Integer pageNum = 1;
        if (matcher.find()) {
            pageNum = Integer.valueOf(matcher.group(3));
            if (pageNum < 2) {
                logger.info("待核准账户管理列表共" + pageNum + "页面");
                return false;
            }
        } else {
            logger.info("待核准账户页面异常:" + html);
        }
        String tempUrl = queryUrl;
        for (int page = 2; page <= pageNum; page++) { // 有分页，需进行检索
            queryUrl = queryUrl + "&ispage=true&pagingPage=" + page;
            result = HttpUtils.get(queryUrl, auth.getCookie(), null);
            result.makeHtml(HttpConfig.HTTP_ENCODING);
            html = result.getHtml();
            if (result.getStatuCode() == 200) {
                if (html.indexOf(acctNo) > -1) {
                    return true;
                } else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
                    throw new SyncException("用户登录信息失效");
                } else {
                    logger.info("在待核准类账户未查询到账号" + acctNo + ",访问的页面是:" + html);
                }
            } else {
                throw new SyncException("用户登录信息失效");
            }
            queryUrl = tempUrl;
        }
        return false;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception {
        if (StringUtils.isBlank(acctNo)) {
            throw new SyncException("账号不能为空");
        }
        AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
        amsAccountInfo.setAcctNo(acctNo);
        forShowResult(auth, amsAccountInfo);
        //2019年11月27日18:08:51 邹郎 根据账号查询时，区分本异地标识
        if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())) {
            // 登录用户所在机构的机构号
            String[] bankInfo = getBankCodeByLoginName(auth);
            if (ArrayUtils.isNotEmpty(bankInfo) && StringUtils.isNotBlank(bankInfo[0])) {
                // 银行所在地地区代码
                String bankAreaCode = getBankAreCode(auth, bankInfo[1])[1];
                if (StringUtils.isNotBlank(bankAreaCode) && StringUtils.isNotBlank(amsAccountInfo.getRegAreaCode())) {
                    //基本户注册地地区代码和 机构所在地地区代码一致 则是本地
                    if (amsAccountInfo.getRegAreaCode().equals(bankAreaCode)) {
                        amsAccountInfo.setOpenAccountSiteType("1");
                    } else {
                        amsAccountInfo.setOpenAccountSiteType("2");
                    }
                }
            }
        }
        return amsAccountInfo;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNoFromBeiAnChange(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
        return amsJibenChangeBeiAnService.getAmsAccountInfoFromChangeAccountFirstStep(auth, allAcct);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception {
        AmsRevokeSyncCondition conditon = new AmsRevokeSyncCondition();
        conditon.setAcctNo(acctNo);
        conditon.setAcctType(SyncAcctType.yiban);
        conditon.setCancenReason("7");
        return amsRevokeService.revokeAccountFirstStep(auth, conditon);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(LoginAuth auth, String acctNo, SyncAcctType acctType) throws SyncException, Exception {
        AmsRevokeBeiAnSyncCondition conditon = new AmsRevokeBeiAnSyncCondition();
        conditon.setAcctNo(acctNo);
        conditon.setAcctType(acctType);
        conditon.setCancenReason("7");
        return amsRevokeBeiAnService.getAmsAccountInfoRevokeAccountFirstStep(auth, conditon);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception {
        AmsSuspendSyncCondition condition = new AmsSuspendSyncCondition();
        condition.setAcctNo(acctNo);
        condition.setAcctType(SyncAcctType.yiban);
        return amsSuspendService.suspendAccountFirstStep(auth, condition);
    }

    @Override
    public String[] getBankCodeByLoginName(LoginAuth auth) throws SyncException, Exception {
        String[] bankInfo = new String[2];
        String url = auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_TOOPENGENERALPAGE;
        HttpResult result = HttpUtils.get(url, auth.getCookie(), null);
        result.makeHtml("gbk");
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            if (html.indexOf("审核及控制信息录入") > -1) {
                Document document = Jsoup.parse(html);
                bankInfo[0] = document.select("input[name=saccbanknamehidden]").first().val();
                bankInfo[1] = document.select("input[name=accEntAccountInfo.saccbankcode]").first().val();
            } else if (html.indexOf("当前系统正在运行后台批处理，请稍等再进行业务") > -1) {
                throw new SyncException("人行账管系统服务已关闭,请工作时间使用");
            } else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
                throw new SyncException("用户登录信息失效");
            }
        } else {
            throw new SyncException("用户登录信息失效");
        }
        return bankInfo;
    }

    @Override
    public String[] getBankNameAndRegAreaCodeForUserType2(LoginAuth auth) throws SyncException, Exception {
        String[] bankInfo;
        try {
            bankInfo = Arrays.copyOf(getBankCodeByLoginName(auth), 3);
            if (bankInfo != null && bankInfo[1] != null && !bankInfo[1].equals("")) {
                bankInfo[2] = getBankAreCode(auth, bankInfo[1])[1];
            }
        } catch (Exception e) {
            throw new SyncException("根据当前用户获取银行名称异常");
        }
        return bankInfo;
    }

    @Override
    public String[] getBankNameAndRegAreaCodeForUserType4(LoginAuth auth) throws SyncException, Exception {
        String[] bankInfo = new String[3];
        HttpResult result = HttpUtils.get(auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forShowBasicImportantInfo", auth.getCookie(), null);
        result.makeHtml("gbk");
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            if (html.indexOf("审核及控制信息录入") > -1) {
                Document document = Jsoup.parse(html);
                //4级开基本户无此信息反显
                //bankInfo[0] = document.select("input[name=saccbanknamehidden]").first().val();
                bankInfo[1] = document.select("input[name=accEntAccountInfo.saccbankcode]").first().val();
            } else if (html.indexOf("当前系统正在运行后台批处理，请稍等再进行业务") > -1) {
                throw new SyncException("人行账管系统服务已关闭,请工作时间使用");
            } else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
                throw new SyncException("用户登录信息失效");
            }
        } else {
            throw new SyncException("用户登录信息失效");
        }

        try {
            if (bankInfo != null && bankInfo[1] != null && !bankInfo[1].equals("")) {
                String[] areaCode = getBankAreCode(auth, bankInfo[1]);
                bankInfo[0] = areaCode[0];
                bankInfo[2] = areaCode[1];
            }
        } catch (Exception e) {
            throw new SyncException("根据当前用户获取银行名称异常");
        }

        return bankInfo;
    }

    // 根据基本户开户许可证、基本户注册地地区代码查询账户信息
    @Override
    public AmsAccountInfo getAmsAccountInfoByAccountKey(LoginAuth auth, String accountKey, String regAreaCode) throws Exception, SyncException {
        AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
        // 开户许可证
        String validResult = PbcBussUtils.valiDateAccountKey(accountKey);
        if (StringUtils.isNotBlank(validResult)) {
            throw new SyncException(validResult);
        }
        // 基本户存款账户地区代码
        validResult = PbcBussUtils.valiDateRegAreaCode(regAreaCode);
        if (StringUtils.isNotBlank(validResult)) {
            throw new SyncException(validResult);
        }
        //不判断前4位，直接使用传入的值
        //regAreaCode = setRegAreaCodeByAccountKey(accountKey, regAreaCode);// 根据基本户开户许可证，满足条件重新设置基本户开户许可证地区代码
        try {
            AmsYibanSyncCondition condition = getAmsYibanSyncCondition(accountKey, regAreaCode);
            amsYibanOpenServie.openAccountFirstStep(auth, condition);
            amsAccountInfo = amsYibanOpenServie.openAccountSecondStep(auth, condition);
            //2019年10月15日11:39:06 邹郎 设置本异地标识
            if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getLegalName())) {
                amsAccountInfo.setOpenAccountSiteType(condition.getOpenAccountSiteType());
            }
        } catch (Exception e) {
            if (e.getMessage().contains("基本存款账户与一般存款账户不得开立在同一银行机构")) {
                logger.info("基本户与一般户开立在同一家机构中，使用模拟开立非预算账户进行校验");
                try {
                    AmsFeiyusuanSyncCondition feiyusuanCondition = getAmsFeiyusuanCondition(accountKey, regAreaCode);
                    amsFeiyusuanOpenService.openAccountFirstStep(auth, feiyusuanCondition);
                    amsAccountInfo = amsFeiyusuanOpenService.openAccountSecondStep(auth, feiyusuanCondition);
                    //2019年10月15日11:39:06 邹郎 设置本异地标识
                    if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getLegalName())) {
                        amsAccountInfo.setOpenAccountSiteType(feiyusuanCondition.getOpenAccountSiteType());
                    }
                } catch (SyncException e1) {
                    if (e1.getMessage().contains("不能开立非预算单位专用存款账户")) {
                        logger.info("该存款人类别不能开立非预算单位专用存款账户，无法显示基本户信息");
                        throw new SyncException("该企业(社会团体或事业单位等)通过接口无法显示基本户详细信息");
                    } else {
                        throw e1;
                    }
                }
            } else if (e.getMessage().contains("基本存款账户不存在")) {
                throw new SyncException("基本存款账户不存在");
            } else {
                throw e;
            }
        }
        return amsAccountInfo;
    }

    /**
     * 一般户开户校验
     *
     * @param auth        账管用户登录对象
     * @param accountKey  基本户开户许可证
     * @param regAreaCode 基本户注册地地区代码
     * @return
     * @throws SyncException
     * @throws Exception
     */
    @Override
    public AmsAccountInfo getAmsAccountInfoByAccountKeyForYiBanOpen(LoginAuth auth, String accountKey, String regAreaCode) throws SyncException, Exception {
        AmsAccountInfo amsAccountInfo;
        // 开户许可证
        String validResult = PbcBussUtils.valiDateAccountKey(accountKey);
        if (StringUtils.isNotBlank(validResult)) {
            throw new SyncException(validResult);
        }
        // 基本户存款账户地区代码
        validResult = PbcBussUtils.valiDateRegAreaCode(regAreaCode);
        if (StringUtils.isNotBlank(validResult)) {
            throw new SyncException(validResult);
        }
        try {
            AmsYibanSyncCondition condition = getAmsYibanSyncCondition(accountKey, regAreaCode);
            amsYibanOpenServie.openAccountFirstStep(auth, condition);
            amsAccountInfo = amsYibanOpenServie.openAccountSecondStep(auth, condition);
            //2019年10月15日11:39:06 邹郎 设置本异地标识
            if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getLegalName())) {
                amsAccountInfo.setOpenAccountSiteType(condition.getOpenAccountSiteType());
            }
        } catch (Exception e) {
            if (e.getMessage().contains("基本存款账户不存在")) {
                throw new SyncException("基本存款账户不存在");
            } else {
                throw e;
            }
        }
        return amsAccountInfo;
    }

    /**
     * 获取一般户查询条件
     *
     * @param accountkey  基本户开户许可证
     * @param regAreaCode 基本户注册地地区代码
     * @return
     */
    public AmsYibanSyncCondition getAmsYibanSyncCondition(String accountkey, String regAreaCode) {
        AmsYibanSyncCondition condition = new AmsYibanSyncCondition();
        condition.setAccountKey(accountkey);
        condition.setRegAreaCode(regAreaCode);
        condition.setAcctType(SyncAcctType.yiban);
        condition.setOperateType(SyncOperateType.ACCT_OPEN);
        condition.setAccountFileType("07");
        condition.setAcctNo(String.valueOf(new Date().getTime()));
        return condition;
    }

    public AmsFeiyusuanSyncCondition getAmsFeiyusuanCondition(String accountKey, String regAreaCode) {
        AmsFeiyusuanSyncCondition feiyusuanCondition = new AmsFeiyusuanSyncCondition();
        feiyusuanCondition.setAccountFileType("10");
        feiyusuanCondition.setAccountKey(accountKey);
        feiyusuanCondition.setAcctType(SyncAcctType.feiyusuan);
        feiyusuanCondition.setOperateType(SyncOperateType.ACCT_OPEN);
        feiyusuanCondition.setRegAreaCode(regAreaCode);
        feiyusuanCondition.setAccountNameFrom("0");
        feiyusuanCondition.setCapitalProperty("01");
        feiyusuanCondition.setAcctNo(String.valueOf(new Date().getTime()));
        return feiyusuanCondition;
    }

    /**
     * 判断基本户开户许可证与基本户注册地地区代码数字前4位是否相同，若不相同则用基本户开户许可证前6位数字
     *
     * @param accountkey  基本户开户许可证
     * @param regAreaCode 基本户注册地地区代码
     */
    @Deprecated
    protected String setRegAreaCodeByAccountKey(String accountkey, String regAreaCode) {
        if (StringUtils.isNotEmpty(regAreaCode) && StringUtils.isNotBlank(accountkey)) {
            if (!accountkey.substring(1, 5).equals(regAreaCode.substring(0, 4))) {
                regAreaCode = accountkey.substring(1, 7);
            }
        }
        return regAreaCode;
    }

    @Override
    public String[] getBankAreCode(LoginAuth auth, String bankCode) throws SyncException, Exception {
        String url = auth.getDomain() + "/ams/dwr/exec/theIBankAccessBOProxy.getBankNameAndRegAreaCodeForUserType2.dwr";
        StringBuffer body = new StringBuffer("");
//        String c0_id = String.valueOf(Math.random()).replaceAll("0\\.", "");
        String c0_id = getrandom();
        String[] msg = new String[2];
        body.append("callCount=1\n");
        body.append("c0-scriptName=theIBankAccessBOProxy\n");
        body.append("c0-methodName=getBankNameAndRegAreaCodeForUserType2\n");
        body.append("c0-id=").append(c0_id).append("\n");
        body.append("c0-param0=string:").append(bankCode).append("\n");
        body.append("c0-param1=string:").append(bankCode).append("\n");
        body.append("xml=true\n");
        List<Header> headers = getHeader(auth,"");
        HttpResult result = HttpUtils.post(url, body.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml("gbk");
        String html = result.getHtml();
        msg = getMsg(html);
        return msg;
    }
    @Override
    public void checkPageSubmit(LoginAuth auth, String checkPageSubmitValue) throws SyncException, Exception {
        String url = auth.getDomain() + "/ams/dwr/exec/thevalidatePageSubmitProxy.checkPageSubmit.dwr";
        StringBuffer body = new StringBuffer("");
        String c0_id = getrandom();
        body.append("callCount=1\n");
        body.append("c0-scriptName=thevalidatePageSubmitProxy\n");
        body.append("c0-methodName=checkPageSubmit\n");
        body.append("c0-id=").append(c0_id).append("\n");
        body.append("c0-param0=boolean:false\n");
        body.append("c0-param1=string:").append(checkPageSubmitValue).append("\n");
        body.append("xml=true\n");
        List<Header> headers = getHeader(auth,"");
        HttpResult result = HttpUtils.post(url, body.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml("gbk");
        String html = result.getHtml();
        logger.info("thevalidatePageSubmitProxy : " + html);
    }

    @Override
    public String getAreaCodeByAccountKey(LoginAuth auth, String accountKey) throws SyncException, Exception {
        String areaCode = "";
        String url = auth.getDomain() + "/ams/dwr/exec/theIBankAccessBOProxy.getRegAreaCode.dwr";
        StringBuffer body = new StringBuffer("");
//        String c0_id = String.valueOf(Math.random()).replaceAll("0\\.", "");
        String c0_id = getrandom();
        body.append("callCount=1\n");
        body.append("c0-scriptName=theIBankAccessBOProxy\n");
        body.append("c0-methodName=getRegAreaCode\n");
        body.append("c0-id=").append(c0_id).append("\n");
        body.append("c0-param0=string:").append(accountKey).append("\n");
        body.append("xml=true\n");
        List<Header> headers = getHeader(auth,"");
        HttpResult result = HttpUtils.post(url, body.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml("gbk");
        String html = result.getHtml();
        Matcher matcher = Pattern.compile("var\\s+?s0=\"(.+?)\"").matcher(html);
        if (matcher.find()) {
            areaCode = matcher.group(1);
        }
        return areaCode;
    }

    @Override
    public String getRegAreaCodeForRemoteOpen(LoginAuth auth, String jibenRegAreaCode) throws SyncException, Exception {
        String areaCode = "";
        String url = auth.getDomain() + "/ams/dwr/exec/theIBankAccessBOProxy.getRegAreaCodeForRemoteOpen.dwr";
        StringBuffer body = new StringBuffer("");
//        String c0_id = String.valueOf(Math.random()).replaceAll("0\\.", "");
        String c0_id = getrandom();
        body.append("callCount=1\n");
        body.append("c0-scriptName=theIBankAccessBOProxy\n");
        body.append("c0-methodName=getRegAreaCodeForRemoteOpen\n");
        body.append("c0-id=").append(c0_id).append("\n");
        body.append("c0-param0=string:").append(jibenRegAreaCode).append("\n");
        body.append("xml=true\n");
        List<Header> headers = getHeader(auth,"");
        HttpResult result = HttpUtils.post(url, body.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml("gbk");
        String html = result.getHtml();
        Matcher matcher = Pattern.compile("var\\s+?s0=\"(.+?)\"").matcher(html);
        if (matcher.find()) {
            areaCode = matcher.group(1);
        }
        return areaCode;
    }

    /**
     * 查询检测 进入列表
     *
     * @param auth
     * @param accountModel
     * @return 1成功，2当前系统正在运行后台批处理
     */
    private void forShowResult(LoginAuth auth, AmsAccountInfo accountModel) throws SyncException, Exception {
        HttpResult result = null;
        result = HttpUtils.get(auth.getDomain() + "/ams/query001.do?method=forFrameForward", auth.getCookie(), null);
        Thread.sleep(500);
        result = HttpUtils.get(auth.getDomain() + "/ams/query001.do?method=forShowQueryConditions", auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            log.info("人行HTML打印：" + html);
            //回调9月15号人行更新后函数
            amsSearchService.callbackMethod(auth,html);
        }
        String url = auth.getDomain() + AmsConfig.ZG_URL_BANK_QUERY_POST;
        String urlparms = getShowResultParams(accountModel);
        logger.info("查询参数：{}",urlparms);
//        result = HttpUtils.post(url, urlparms, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        List<Header> headers = getHeaderForShowResult(auth,auth.getDomain() + "/ams/query001.do?method=forShowQueryConditions");
        log.info("header打印： " + headers);
        result = HttpUtils.post(url, urlparms, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
//        String html = result.getHtml();
        html = result.getHtml();
        if (result.getStatuCode() == 200) {
            if(org.apache.commons.lang.StringUtils.isBlank(html) || html.equals("")){
                throw new SyncException("查询异常，请重新输入");
            }
            if (html.indexOf("当前系统正在运行后台批处理，请稍等再进行业务") > 0) {
                throw new SyncException("人行账管系统服务已关闭,请工作时间使用");
            } else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
                throw new SyncException("用户登录信息失效");
            } else if (html.indexOf("不存在") > -1) {
                accountModel.setAccountStatus(AccountStatus.notExist);
                return;
            }
            // 找到存在数据的table
            Document doc = Jsoup.parse(html);
            Element table = doc.select("#defaulttabletagname .classicLook").first();
            Elements trs = table.getElementsByTag("tr");
            int num = 1;// 没有的话则取第1条
            for (int i = 1; i < trs.size(); i++) {
                Element element = trs.get(i);
                Elements tds = element.getElementsByTag("td");
                // 查询列表中销户日期为 - 或 空 的
                if ("-".equals(StringUtils.trim(tds.get(8).html()).replace("&nbsp;", ""))) {
                    num = i;
                    break;
                }
            }
            Element tr = trs.get(num);// 获取根据条件(账号)查询的结果行
            Elements tds = tr.getElementsByTag("td");
            accountModel.setDepositorName(StringUtils.trim(tds.get(1).html()).replace("&nbsp;", ""));
            accountModel.setBankCode(StringUtils.trim(tds.get(2).html()).replace("&nbsp;", ""));
            accountModel.setBankName(StringUtils.trim(tds.get(3).html()).replace("&nbsp;", ""));
            accountModel.setAcctType(AccountType.str2enum(StringUtils.trim(tds.get(4).html()).replace("&nbsp;", "")));
            if (StringUtils.isEmpty(accountModel.getAcctNo())) {
                accountModel.setAcctNo(StringUtils.trim(tds.get(5).html()).replace("&nbsp;", ""));
            }
            accountModel.setAcctName(StringUtils.trim(tds.get(6).html()).replace("&nbsp;", ""));
            accountModel.setAcctCreateDate(StringUtils.trim(tds.get(7).html()).replace("&nbsp;", ""));
            accountModel.setCancelDate(StringUtils.trim(tds.get(8).html()).replace("&nbsp;", ""));
            String td9 = tds.get(9).html();
            if (td9.indexOf("onShowDetail('") > -1) {
                String idepno = td9.substring(td9.indexOf("onShowDetail('") + 14);
                idepno = idepno.substring(0, idepno.indexOf("'"));
                forShowDetailResult(auth, accountModel, idepno);
            }
        } else {
            logger.info("账号" + accountModel.getAcctNo() + "查询结果时出现错误,错误页面为" + html);
            throw new SyncException("用户登录信息失效");
        }
    }

    /**
     * 进入详细页面
     *
     * @param auth
     * @param accountModel
     * @param idepno
     * @throws SyncException
     * @throws Exception
     */
    private void forShowDetailResult(LoginAuth auth, AmsAccountInfo accountModel, String idepno) throws SyncException, Exception {
        HttpResult result = null;
        StringBuffer urlPars = new StringBuffer("");
//        urlPars.append("&formvo.idepno=").append(idepno);
//        urlPars.append("&formvo.querykind=Query00102");
        urlPars.append("formvo.querykind=Query00102");
        if (StringUtils.isNotEmpty(accountModel.getAcctNo())) {
            urlPars.append("&formvo.saccno=").append(accountModel.getAcctNo());// 账号
            urlPars.append("&formvo.sacclicno=");// 开户许可证
        } else if (StringUtils.isNotEmpty(accountModel.getAccountKey())) {
            urlPars.append("&formvo.sacclicno=").append(accountModel.getAccountKey());// 开户许可证
            urlPars.append("&formvo.saccno=");// 账号
        }
        urlPars.append("&formvo.slicno=&formvo.saccbankcode=&formvo.isall=1");
        urlPars.append("&formvo.isall2=1&formvo.tempreportid=Query00102&txcode=TX1010101");
        String url = auth.getDomain() + AmsConfig.ZG_URL_BANK_QUERY_DETAIL;
        url = url + "&formvo.idepno=" + idepno;
        result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), HeadsCache.getRhzgCommon(auth.getDomain()));
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
//        logger.info("详情页面：" + html);
        if (result.getStatuCode() == 200) {
            Document doc1 = Jsoup.parse(html);
            String sdepid = doc1.getElementsByTag("input").get(1).val();
            Element div1 = doc1.getElementById("defaultprinttagname");
            Elements EdtTbRCells = div1.getElementsByClass("EdtTbRCell");
            Elements EdtTbLCells = div1.getElementsByClass("EdtTbLCell");
            accountModel.setDepositorType(StringUtils.trim(EdtTbLCells.get(1).html()).replace("&nbsp;", ""));
            accountModel.setRegAreaCode(StringUtils.trim(EdtTbLCells.get(2).html()).replace("&nbsp;", ""));
            accountModel.setOrgCode(StringUtils.trim(EdtTbLCells.get(3).html()).replace("&nbsp;", ""));
            accountModel.setFileType(StringUtils.trim(EdtTbRCells.get(4).html()).replace("&nbsp;", ""));
            accountModel.setFileNo(StringUtils.trim(EdtTbLCells.get(4).html()).replace("&nbsp;", ""));
            accountModel.setFileType2(StringUtils.trim(EdtTbRCells.get(5).html()).replace("&nbsp;", ""));
            accountModel.setFileNo2(StringUtils.trim(EdtTbLCells.get(5).html()).replace("&nbsp;", ""));
            //修改少数名字中有·但取数为&middot;的问题
            String legalName = StringEscapeUtils.unescapeHtml(StringUtils.trim(EdtTbLCells.get(6).html()).replace("&nbsp;", ""));
            accountModel.setLegalName(legalName);

            accountModel.setLegalIdcardType(StringUtils.trim(EdtTbLCells.get(7).html()).replace("&nbsp;", ""));
            accountModel.setLegalIdcardNo(StringUtils.trim(EdtTbLCells.get(8).html()).replace("&nbsp;", ""));
            // 行业归属
            Element div2 = doc1.getElementById("defaulttabletagname");
            String industryCode = "";
            Elements trs = div2.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).getElementsByTag("td");
                if (tds.size() == 2) {
                    industryCode += StringUtils.trim(tds.get(1).html()).replace("&nbsp;", "") + ",";
                }
            }
            if (StringUtils.isNotEmpty(industryCode)) {
                industryCode = industryCode.substring(0, industryCode.length() - 1);
            }
            accountModel.setIndustryCode(industryCode);
            accountModel.setStateTaxRegNo(StringUtils.trim(EdtTbLCells.get(9).html()).replace("&nbsp;", ""));
            accountModel.setTaxRegNo(StringUtils.trim(EdtTbLCells.get(10).html()).replace("&nbsp;", ""));
            accountModel.setNoTaxProve(StringUtils.trim(EdtTbLCells.get(11).html()).replace("&nbsp;", ""));
            accountModel.setRegCurrencyType(StringUtils.trim(EdtTbLCells.get(12).html()).replace("&nbsp;", ""));
            accountModel.setRegisteredCapital(StringUtils.trim(EdtTbLCells.get(13).html()).replace("&nbsp;", ""));
            accountModel.setBusinessScope(StringUtils.trim(EdtTbLCells.get(15).html()).replace("&nbsp;", ""));
            accountModel.setRegAddress(StringUtils.trim(EdtTbLCells.get(16).html()).replace("&nbsp;", ""));
            accountModel.setZipCode(StringUtils.trim(EdtTbLCells.get(17).html()).replace("&nbsp;", ""));
            accountModel.setTelephone(StringUtils.trim(EdtTbLCells.get(18).html()).replace("&nbsp;", ""));
            // 19 空
            accountModel.setParCorpName(StringUtils.trim(EdtTbLCells.get(20).html()).replace("&nbsp;", ""));
            accountModel.setParAccountKey(StringUtils.trim(EdtTbLCells.get(21).html()).replace("&nbsp;", ""));
            accountModel.setParOrgCode(StringUtils.trim(EdtTbLCells.get(22).html()).replace("&nbsp;", ""));
            accountModel.setParLegalName(StringUtils.trim(EdtTbLCells.get(23).html()).replace("&nbsp;", ""));
            accountModel.setParLegalIdcardType(StringUtils.trim(EdtTbLCells.get(24).html()).replace("&nbsp;", ""));
            accountModel.setParLegalIdcardNo(StringUtils.trim(EdtTbLCells.get(25).html()).replace("&nbsp;", ""));
            showList(auth, accountModel, idepno, sdepid);
        } else {
            logger.info("账户" + accountModel.getAcctNo() + "查询详细页面异常,查询页面为" + html);
            throw new SyncException("用户登录信息失效");
        }
    }

    /**
     * 进入高级查询页面
     *
     * @param auth
     * @param accountModel
     * @param idepno
     * @param sdepid
     * @return
     */
    private void showList(LoginAuth auth, AmsAccountInfo accountModel, String idepno, String sdepid) throws SyncException, Exception {
        String url = (auth.getDomain() + AmsConfig.ZG_URL_FORSHOWRESULTREPORT).replace("{0}", idepno).replace("{1}", sdepid);
        HttpResult result = null;
        String tempUrl = url;
        String currencyType = "";
        String currency = "";
        String capitalProperty = "";
        for (int page = 1; page < 30; page++) { // 有分页，需进行检索
            if (page > 1) {
                url = url + "&ispage=true&pagingPage=" + page;
            }
            result = HttpUtils.get(url, auth.getCookie(), HeadsCache.getRhzgCommon(auth.getDomain()));
            url = tempUrl;
            result.makeHtml(HttpConfig.HTTP_ENCODING);
            String html = result.getHtml();
//            logger.info("高级查询页面：" + html);
            Document doc = Jsoup.parse(html);
            Element div = doc.getElementById("defaulttabletagname");
            Elements trs = div.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++) {
                Element tr = trs.get(i);
                String acctNo = tr.getElementsByTag("td").get(4).html().replace("&nbsp;", "");
                String status = tr.getElementsByTag("td").get(10).html().replace("&nbsp;", "");
                String acctType = tr.getElementsByTag("td").get(3).html().replace("&nbsp;", "");
                if(html.contains("币种类型")){
                    currencyType = tr.getElementsByTag("td").get(11).html().replace("&nbsp;", "");
                    currency = tr.getElementsByTag("td").get(12).html().replace("&nbsp;", "");
                    capitalProperty = tr.getElementsByTag("td").get(13).html().replace("&nbsp;", "");
                    if(StringUtils.isNotBlank(currencyType)){
                        accountModel.setCurrencyType(currencyType);
                    }
                    if(StringUtils.isNotBlank(currency)){
                        accountModel.setCurrency(currency);
                    }
                    if(StringUtils.isNotBlank(capitalProperty)){
                        accountModel.setCapitalProperty(capitalProperty);
                    }
                }
                //加入账户性质判断
                if (acctNo.equals(accountModel.getAcctNo()) && acctType.equals(accountModel.getAcctType().getFullName())) {
                    accountModel.setAccountStatus(AccountStatus.str2enumByAmsAcctStatus(status));
                    accountModel.setAccountKey(tr.getElementsByTag("td").get(6).html().replace("&nbsp;", ""));
                    accountModel.setAccountLicenseNo(tr.getElementsByTag("td").get(6).html().replace("&nbsp;", ""));
                    // 一般户、专用户、非临时户可查看账户信息
                    if ("预算单位专用存款账户".equals(acctType) || "非预算单位专用存款账户".equals(acctType) || "一般存款账户".equals(acctType) || "非临时机构临时存款账户".equals(acctType)) {
                        String a = tr.getElementsByTag("td").get(14).html();
                        accountModel.setAcctType(AccountType.str2enum(acctType));
                        if (a.indexOf("onShowAccDetail('") > 0) {
                            String iaccno = a.substring(a.indexOf("onShowAccDetail('") + 17);
                            iaccno = iaccno.substring(0, iaccno.indexOf("'"));
                            getAccountDetails(auth, iaccno, accountModel);
                        }
                    }
                    if (!"撤销".equals(status)) {
                        return;
                    }
                }
            }
            Elements tables = doc.getElementsByTag("table");
            String pageStr = tables.get(5).getElementsByTag("td").get(0).html();
            pageStr = pageStr.substring(pageStr.lastIndexOf("共") + 1);
            pageStr = pageStr.substring(0, pageStr.indexOf("页"));
            if (NumberUtils.str2Int(pageStr) <= page) {
                break;
            }
        }
    }

    /**
     * 获取基本存款账户开户许证号(临时、专用账户、一般)
     *
     * @param auth
     * @param iaccno
     * @param accountModel
     * @return
     */
    private void getAccountDetails(LoginAuth auth, String iaccno, AmsAccountInfo accountModel) throws SyncException, Exception {
        String url = (auth.getDomain() + AmsConfig.ZG_URL_FORSHOWRESULTDETAILSREPORT).replace("{0}", iaccno);
        HttpResult result = null;
        StringBuffer urlPars = new StringBuffer();
        urlPars.append("&formvo.reportid=Query00106");
        urlPars.append("&pagenum=");
        urlPars.append("&prn=%26nbsp%3B%B4%F2%26nbsp%3B%26nbsp%3B%D3%A1%26nbsp%3B");
        result = HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), HeadsCache.getRhzgCommon(auth.getDomain()));
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        String html = result.getHtml();
        if (result.getStatuCode() == 200) {
            Document doc = Jsoup.parse(html);
            Elements edtTbLCells = doc.getElementsByClass("EdtTbLCell");
            if (accountModel.getAcctType() == AccountType.yusuan || accountModel.getAcctType() == AccountType.feiyusuan) {
                String accountKey = edtTbLCells.get(9).html().replace("&nbsp;", "");
                accountModel.setAccountKey(accountKey);
                accountModel.setAcctCreateDate(edtTbLCells.get(8).html().replace("&nbsp;", ""));
                accountModel.setAcctName(edtTbLCells.get(3).html().replace("&nbsp;", ""));
                accountModel.setAccountFileType(edtTbLCells.get(10).html().replace("&nbsp;", ""));
                accountModel.setAccountFileNo(edtTbLCells.get(11).html().replace("&nbsp;", ""));
                accountModel.setAccountFileType2(edtTbLCells.get(12).html().replace("&nbsp;", ""));
                accountModel.setAccountFileNo2(edtTbLCells.get(13).html().replace("&nbsp;", ""));
                accountModel.setRegAreaCode(edtTbLCells.get(7).html().replace("&nbsp;", ""));
                accountModel.setCapitalProperty(edtTbLCells.get(4).html().replace("&nbsp;", ""));
                // 判断账户性质
                if (html.indexOf("内设部门信息") > 0) {
                    accountModel.setInsideDepartmentName(edtTbLCells.get(14).html().replace("&nbsp;", ""));
                    accountModel.setInsideSaccdepmanName(edtTbLCells.get(17).html().replace("&nbsp;", ""));
                    accountModel.setInsideSaccdepmanKind(edtTbLCells.get(15).html().replace("&nbsp;", ""));
                    accountModel.setInsideSaccdepmanNo(edtTbLCells.get(16).html().replace("&nbsp;", ""));
                    accountModel.setInsideAddress(edtTbLCells.get(18).html().replace("&nbsp;", ""));
                    accountModel.setInsideZipCode(edtTbLCells.get(20).html().replace("&nbsp;", ""));
                    accountModel.setInsideTelphone(edtTbLCells.get(19).html().replace("&nbsp;", ""));
                    accountModel.setAccountNameFrom("存款人名称加内设部门");
                } else if (html.indexOf("资金管理人姓名") > 0) {
                    accountModel.setMoneyManager(edtTbLCells.get(15).html().replace("&nbsp;", ""));
                    accountModel.setMoneyManagerCtype(edtTbLCells.get(16).html().replace("&nbsp;", ""));
                    accountModel.setMoneyManagerCno(edtTbLCells.get(17).html().replace("&nbsp;", ""));
                    if (StringUtils.isBlank(accountModel.getMoneyManager())) {
                        accountModel.setAccountNameFrom("与存款人名称一致");
                    } else {
                        accountModel.setAccountNameFrom("存款人名称加资金性质");
                        if(StringUtils.isNotBlank(accountModel.getDepositorName()) && StringUtils.isNotBlank(accountModel.getAcctName())){
                            String acctName = accountModel.getAcctName();
                            String depositorName = accountModel.getDepositorName();
                            logger.info(accountModel.getAcctType() + ":acctName:" + acctName);
                            logger.info(accountModel.getAcctType() + ":depositorName:" + depositorName);
                            //两个字段不eq的情况下
                            if(!StringUtils.equals(depositorName,acctName) && acctName.contains(depositorName)){
                                if(acctName.length() > depositorName.length()){
                                    //前缀
                                    String saccprefix=acctName.substring(0, acctName.indexOf(depositorName));
                                    //后缀
                                    String saccpostfix=acctName.substring((saccprefix + depositorName).length(), acctName.length());
                                    if(StringUtils.isNotBlank(saccprefix)){
                                        logger.info("前缀：" + saccprefix);
                                        accountModel.setSaccprefix(saccprefix);
                                    }
                                    if(StringUtils.isNotBlank(saccpostfix)){
                                        logger.info("后缀：" + saccpostfix);
                                        accountModel.setSaccpostfix(saccpostfix);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (accountModel.getAcctType() == AccountType.yiban) {
                accountModel.setAccountFileType(edtTbLCells.get(4).html().replace("&nbsp;", ""));
                accountModel.setAccountFileNo(edtTbLCells.get(5).html().replace("&nbsp;", ""));
            } else if (accountModel.getAcctType() == AccountType.feilinshi) {
                accountModel.setAccountFileType(edtTbLCells.get(4).html().replace("&nbsp;", ""));
                accountModel.setAccountFileNo(edtTbLCells.get(5).html().replace("&nbsp;", ""));
                accountModel.setEffectiveDate(edtTbLCells.get(8).html().replace("&nbsp;", ""));
                accountModel.setCreateAccountReason(edtTbLCells.get(9).html().replace("&nbsp;", ""));
            }
        } else {
            logger.info("账户" + accountModel.getAcctNo() + "高级信息页面异常,查询页面为" + html);
            throw new SyncException("用户登录信息失效");
        }
    }

    /**
     * 获取人行账户查询时参数
     *
     * @param accountModel
     * @return
     */
    private String getShowResultParams(AmsAccountInfo accountModel) {
        StringBuffer urlPars = new StringBuffer("");
        urlPars.append("suserkind=2&suserlevel=2&formvo.query00104=false");
        urlPars.append("&formvo.querykind=&formvo.ssearchtyperet=&formvo.tempreportid=Query00102");
//        urlPars.append("&reportid=Query00102&formvo.saccname=&formvo.querymode2=1");
        urlPars.append("&reportid=Query00102&formvo.sdepname=&formvo.querymode1=1");
        urlPars.append("&formvo.saccfilecode1=&formvo.saccfilecode2=&formvo.sdepnotaxfile=");
        urlPars.append("&formvo.sdeptaxcode=&formvo.sdeporgcode=&formvo.sdepmanagername=");
        urlPars.append("&formvo.sdepmancreCode=&formvo.sentname=&formvo.sdeptName=");
        urlPars.append("&formvo.isall=1&formvo.saccname=&formvo.querymode2=1");
        if (StringUtils.isNotEmpty(accountModel.getAcctNo())) {
            urlPars.append("&formvo.saccno=").append(accountModel.getAcctNo());// 账号
            urlPars.append("&formvo.sacclicno=");// 开户许可证
        } else if (StringUtils.isNotEmpty(accountModel.getAccountKey())) {
            urlPars.append("&formvo.sacclicno=").append(accountModel.getAccountKey());// 开户许可证
            urlPars.append("&formvo.saccno=");// 账号
        }
//        urlPars.append("&formvo.slicno=&formvo.saccbankcode=&formvo.isall2=1&formvo.isall=1");
//        urlPars.append("&formvo.sdepname=&formvo.querymode1=1&formvo.saccfilecode1=");
//        urlPars.append("&formvo.saccfilecode2=&formvo.sdepnotaxfile=&formvo.sdeptaxcode=&formvo.sdeporgcode=");
//        urlPars.append("&formvo.sdepmanagername=&formvo.sdepmancreCode=&formvo.sentname=&formvo.sdeptName=");
        urlPars.append("&formvo.slicno=&formvo.saccbankcode=&formvo.isall2=1");
        urlPars.append("&formvo.ssearchtype=0&formvo.isall3=0");
        urlPars.append("&add=%26nbsp%3B%CC%ED%26nbsp%3B%26nbsp%3B%BC%D3%26nbsp%3B&del=%26nbsp%3B%C9%BE%26nbsp%3B%26nbsp%3B%B3%FD%26nbsp%3B");
        urlPars.append("&add=%26nbsp%3B%CC%ED%26nbsp%3B%26nbsp%3B%BC%D3%26nbsp%3B&del=%26nbsp%3B%C9%BE%26nbsp%3B%26nbsp%3B%B3%FD%26nbsp%3B");
        urlPars.append("&formvo.sbanktypecode=&formvo.querytype3=1&formvo.sbrobankname=");
        urlPars.append("&add=%26nbsp%3B%CC%ED%26nbsp%3B%26nbsp%3B%BC%D3%26nbsp%3B&del=%26nbsp%3B%C9%BE%26nbsp%3B%26nbsp%3B%B3%FD%26nbsp%3B");
        urlPars.append("&formvo.sbrobankcode=");
        urlPars.append("&add=%26nbsp%3B%CC%ED%26nbsp%3B%26nbsp%3B%BC%D3%26nbsp%3B&del=%26nbsp%3B%C9%BE%26nbsp%3B%26nbsp%3B%B3%FD%26nbsp%3B");
        urlPars.append("&formvo.inputnum1=&formvo.inputnum2=&formvo.isall4=1&formvo.sdepregarea=");
        urlPars.append("&formvo.sdepregareaname=&formvo.saccarea=&formvo.saccareaname=&formvo.stradecode=");
        urlPars.append("&formvo.sdomaincode=0&formvo.sdepkind=&formvo.staxcode=0&formvo.saccfiletype1=0&formvo.isall5=1");
        urlPars.append("&formvo.scurtype1=1&sub=%D0%C2%D4%F6%C5%FA%C1%BF%B2%E9%D1%AF%26nbsp%3B&formvo.scurtype=1&cruArray2=1&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        return urlPars.toString();
    }

    private static String[] getMsg(String html) {
        String[] result = new String[2];
        Matcher matcher = Pattern.compile("var\\s+?s1=[\"'](.+?)[\"']").matcher(html);
        if (matcher.find()) {
            String tmpString = matcher.group(1);
            result[0] = EncodeUtils.ascii2native(tmpString);
        }
        Matcher matcher2 = Pattern.compile("var\\s+?s2=[\"'](.+?)[\"']").matcher(html);
        if (matcher2.find()) {
            result[1] = matcher2.group(1);
        }
        return result;
    }

    @Override
    public ResultVO consultRemoteServer(LoginAuth auth, AmsYibanSyncCondition condition) throws Exception, SyncException {
        ResultVO returnResultVo = new ResultVO();
        returnResultVo.setSuccess(false);
        String searchNo = "";
        validConsultModel(condition);// 校验
        try {
            log.info("进入getConsultNo方法");
            searchNo = getConsultNo(auth, condition);// 获取征询编号
        } catch (SyncException e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        Thread.sleep(2000L);
        try {
            log.info("进入checkCanOpenAccountByConsult方法");
            checkCanOpenAccountByConsult(auth, searchNo, condition);// 校验是否能开户或变更
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        Thread.sleep(2000L);
        try {
            log.info("进入consultRequstResult方法");
            consultRequstResult(auth, condition, searchNo);// 从远程服务器调用账户信息
            returnResultVo.setSuccess(true);
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        return returnResultVo;
    }

    @Override
    public ResultVO consultRemoteServer(LoginAuth auth, AmsFeiyusuanSyncCondition condition) throws Exception, SyncException {
        ResultVO returnResultVo = new ResultVO();
        returnResultVo.setSuccess(false);
        String searchNo = "";
        validConsultModel(condition);// 校验
        try {
            searchNo = getConsultNo(auth, condition);// 获取征询编号
        } catch (SyncException e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        Thread.sleep(2000L);
        try {
            checkCanOpenAccountByConsult(auth, searchNo, condition);// 校验是否能开户或变更
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        Thread.sleep(2000L);
        try {
            consultRequstResult(auth, condition, searchNo);// 从远程服务器调用账户信息
            returnResultVo.setSuccess(true);
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            return returnResultVo;
        }
        return returnResultVo;
    }

    @Override
    public ResultVO consultRemoteServer(LoginAuth auth, AllAcct allAcct) throws Exception, SyncException {
        Long stopDate = pbcSyncConfigService.getStopDate();
        logger.info("基本户取消核准征询暂停时间：{}",stopDate);
        ResultVO returnResultVo = new ResultVO();
        returnResultVo.setSuccess(false);
        String searchNo = "";
        validConsultModel(allAcct);// 校验
        try {
            searchNo = getConsultNo(auth, allAcct);// 获取征询编号
        } catch (SyncException e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准获取征询编号异常", e);
            return returnResultVo;
        }
        if(stopDate==null || stopDate==0){
            Thread.sleep(2000L);
        }else{
            Thread.sleep(stopDate);
        }
        try {
            checkCanOpenAccountByConsult(auth, searchNo, allAcct);// 校验是否能开户或变更
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准校验开户变更异常", e);
            return returnResultVo;
        }
        if(stopDate==null || stopDate==0){
            Thread.sleep(2000L);
        }else{
            Thread.sleep(stopDate);
        }
        try {
            consultRequstResult(auth, allAcct, searchNo);// 从远程服务器调用账户信息
            returnResultVo.setSuccess(true);
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准调用远程账户信息异常", e);
            return returnResultVo;
        }
        return returnResultVo;
    }

    @Override
    public ResultVO consultRemoteServer(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception, SyncException {
        Long stopDate = pbcSyncConfigService.getStopDate();
        logger.info("非临时户取消核准征询暂停时间：{}",stopDate);
        ResultVO returnResultVo = new ResultVO();
        returnResultVo.setSuccess(false);
        String searchNo = "";
        validConsultModel(condition);// 校验

        //使用通用参数掉用前2步骤
        AllAcct allAcct = new AllAcct();
        BeanUtils.copyProperties(condition, allAcct);

        try {
            searchNo = getConsultNo(auth, allAcct);// 获取征询编号
        } catch (SyncException e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准获取征询编号异常", e);
            return returnResultVo;
        }
        if(stopDate==null || stopDate==0){
            Thread.sleep(2000L);
        }else{
            Thread.sleep(stopDate);
        }
        try {
            checkCanOpenAccountByConsult(auth, searchNo, allAcct);// 校验是否能开户或变更
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准校验开户变更异常", e);
            return returnResultVo;
        }
        if(stopDate==null || stopDate==0){
            Thread.sleep(2000L);
        }else{
            Thread.sleep(stopDate);
        }
        try {
            consultRequstResult(auth, condition, searchNo);// 从远程服务器调用账户信息
            returnResultVo.setSuccess(true);
        } catch (Exception e) {
            returnResultVo.setMsg(e.getMessage());
            logger.error("取消核准调用远程账户信息异常", e);
            return returnResultVo;
        }
        return returnResultVo;



        //body.append("c0-param0=boolean:false");
    }

    /**
     * 征询时账户信息校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validConsultModel(AmsYibanSyncCondition condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("征询对象为空");
        }
        if (condition.getAcctType() == null) {
            throw new SyncException("征询对象账户性质不能为空");
        }
        if (condition.getOperateType() == null) {
            throw new SyncException("征询对象业务类型不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("征询时账号不能为空");
        }
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            if (StringUtils.isBlank(condition.getAccountKey())) {
                throw new SyncException("征询时开户许可证不能为空");
            }
            if (StringUtils.isEmpty(condition.getAcctCreateDate())) {
                condition.setAcctCreateDate(DateUtils.getNowDateShort());
            }
            if (StringUtils.isBlank(condition.getRegAreaCode())) {
                throw new SyncException("征询时基本户注册地地区代码不能为空");
            }
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("征询时银行所在地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("征询时银行机构号不能为空");
        }
    }

    /**
     * 征询时账户信息校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validConsultModel(AmsFeiyusuanSyncCondition condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("征询对象为空");
        }
        if (condition.getAcctType() == null) {
            throw new SyncException("征询对象账户性质不能为空");
        }
        if (condition.getOperateType() == null) {
            throw new SyncException("征询对象业务类型不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("征询时账号不能为空");
        }
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            if (StringUtils.isBlank(condition.getAccountKey())) {
                throw new SyncException("征询时开户许可证不能为空");
            }
            if (StringUtils.isEmpty(condition.getAcctCreateDate())) {
                condition.setAcctCreateDate(DateUtils.getNowDateShort());
            }
            if (StringUtils.isBlank(condition.getRegAreaCode())) {
                throw new SyncException("征询时基本户注册地地区代码不能为空");
            }
            if (StringUtils.isBlank(condition.getAccountNameFrom())) {
                throw new SyncException("征询时账户构成方式不能为空");
            }
            if (StringUtils.isBlank(condition.getCapitalProperty())) {
                throw new SyncException("征询时资金性质不能为空");
            }
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("征询时银行所在地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("征询时银行机构号不能为空");
        }

    }

    /**
     * 征询获取账户信息
     *
     * @param auth
     * @param condition
     * @param searchNo
     * @throws Exception
     */
    private void consultRequstResult(LoginAuth auth, AmsYibanSyncCondition condition, String searchNo) throws Exception {
        log.info("进入consultRequstResult方法");
        StringBuffer urlPars = new StringBuffer();
        String url = "";
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {// 开户
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinue";
            urlPars.append("sacctype=2&operateType=&errorMsg=&authorizationMsg=");
            urlPars.append("&susertype=2&authorizationFlag=0&alertFlag=0");
            urlPars.append("&msgSingleId=").append(searchNo);
            urlPars.append("&sdepregareahidden=").append(condition.getRegAreaCode());
            urlPars.append("&regAreaCode=").append(condition.getBankAreaCode());
            urlPars.append("&accEntAccountInfo.saccbaselicno=").append(condition.getAccountKey());
            urlPars.append("&accEntAccountInfo.saccbasearea=").append(condition.getRegAreaCode());
            urlPars.append("&accEntAccountInfo.saccbankcode=").append(condition.getBankCode());
            urlPars.append("&saccbanknamehidden=").append(URLEncoder.encode(condition.getBankName(), "gbk"));
            urlPars.append("&accEntAccountInfo.saccno=").append(condition.getAcctNo());
            urlPars.append("&accEntAccountInfo.daccbegindate=").append(condition.getAcctCreateDate());
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {// 变更
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinueForChange";
            urlPars.append("authorizationFlag=0&alertFlag=0&errorMsg=&authorizationMsg=");
            urlPars.append("&msgSingleId=").append(searchNo);
            urlPars.append("&operateType=");
            urlPars.append("&saccno=").append(condition.getAcctNo());
            urlPars.append("&saccbankcode=").append(condition.getBankCode());
            urlPars.append("&saccbanknamehidden=");
            urlPars.append(URLEncoder.encode(condition.getBankName(), "gbk"));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        }
        List<Header> headers = yidiHeader(auth,auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_GENERALVALIDATE);
        HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        Thread.sleep(1000L);
        HttpResult httpResult = null;
        String html = "";
        // 反显录入证明文件信息页面
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {// 开户
            headers = yidiHeaderForDetail(auth,auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_GENERALVALIDATE);
            url = auth.getDomain() + "/ams/recordcommon.do?method=forInputBasicImportantInfoForConsult";
            httpResult = HttpUtils.get(url, auth.getCookie(), headers);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
            log.info("开户征询HTML：" + html);
            if (html.indexOf("录入证明文件信息") > -1) {
                amsSearchService.callbackMethod(auth,html);
                Thread.sleep(1000L);
            } else {
                throw new SyncException("开户征询成功，但进行下一步异常,请联系系统管理员");
            }
        } else {
            headers = yidiHeaderForDetail(auth,auth.getDomain() + AmsConfig.ZG_URL_ACCOUNT_GENERALVALIDATE);
            url = auth.getDomain() + "/ams/changeCommon.do?method=forInputBasicImportantInfoForConsult";
            httpResult = HttpUtils.get(url, auth.getCookie(), headers);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
            log.info("变更征询HTML：" + html);
            if (html.indexOf("变更信息录入") > -1) {
                log.info("changeCommon征询成功，HTML：" + html);
                amsSearchService.callbackMethod(auth,html);
                Thread.sleep(500L);
            } else {
                throw new SyncException("变更征询成功，但进行下一步异常,请联系系统管理员");
            }
        }
    }

    /**
     * 征询获取账户信息
     *
     * @param auth
     * @param condition
     * @param searchNo
     * @throws Exception
     */
    private void consultRequstResult(LoginAuth auth, AmsFeiyusuanSyncCondition condition, String searchNo) throws Exception {
        List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
        StringBuffer urlPars = new StringBuffer();
        String url = "";
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {// 开户
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinue";
            urlPars.append("&sacctype=2");// 本地户、异地户判断,""为本地户；2为异地户
            urlPars.append("&fromNotChecked=0&susertype=2&errorMsg=&authorizationMsg=");
            urlPars.append("&authorizationFlag=0").append("&alertFlag=0");
            urlPars.append("&msgSingleId=").append(searchNo);
            urlPars.append("&operateType=").append("&sdepregareahidden=").append(condition.getRegAreaCode());
            urlPars.append("&regAreaCode=").append(condition.getBankAreaCode());// 登录人行所在地注册地区代码
            urlPars.append("&accEntAccountInfo.saccbaselicno=").append(condition.getAccountKey());// 开户许可证核准号
            urlPars.append("&accEntAccountInfo.saccbasearea=").append(condition.getRegAreaCode());// 注册地区代码
            urlPars.append("&accEntAccountInfo.saccbankcode=").append(condition.getBankCode());// 开户行代码
            urlPars.append("&saccbanknamehidden=").append(URLEncoder.encode(condition.getBankName(), "gbk"));// 开户行名称
            urlPars.append("&accEntAccountInfo.saccno=").append(condition.getAcctNo());
            urlPars.append("&accEntAccountInfo.imode=").append(condition.getAccountNameFrom());// 账户构成方式
            urlPars.append("&accEntAccountInfo.saccfundkind=").append(condition.getCapitalProperty());// 资金性质
            urlPars.append("&accEntAccountInfo.daccbegindate=").append(condition.getAcctCreateDate());// 开户时间
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {// 变更
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinueForChangeNonbudgetSpec";
            urlPars.append("authorizationFlag=0&alertFlag=0&errorMsg=&authorizationMsg=&operateType=");
            urlPars.append("&msgSingleId=").append(searchNo);
            urlPars.append("&saccno=").append(condition.getAcctNo());
            urlPars.append("&FD_OpenBankCode=102301000010");
            urlPars.append("&FD_OpenBankName=%D6%D0%B9%FA%B9%A4%C9%CC%D2%F8%D0%D0%BD%AD%CB%D5%CA%A1%B7%D6%D0%D0");
            urlPars.append("&saccbankcode=").append(condition.getBankCode());
            urlPars.append("&saccbanknamehidden=");
            urlPars.append(URLEncoder.encode(condition.getBankName(), "gbk"));
            urlPars.append("&sub=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B");
        }
        HttpUtils.post(url, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        Thread.sleep(500L);
        HttpResult httpResult = null;
        String html = "";
        // 反显录入证明文件信息页面
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {// 开户
            url = auth.getDomain() + "/ams/recordnonbudgetspec.do?method=forInputBasicImportantInfoForConsult";
            httpResult = HttpUtils.get(url, auth.getCookie(), headers);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
            if (html.indexOf("录入证明文件信息") > -1) {
                Thread.sleep(500L);
            } else {
                throw new SyncException("开户征询成功，但进行下一步异常,请联系系统管理员");
            }
        } else {
            url = auth.getDomain() + "/ams/forInputBasicImportantInfoForConsultNonbudgetSpec.do";
            httpResult = HttpUtils.get(url, auth.getCookie(), headers);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
            if (html.indexOf("变更信息录入") > -1) {
                Thread.sleep(500L);
                getFysAcctountData(html, condition);
            } else {
                throw new SyncException("变更征询成功，但进行下一步异常,请联系系统管理员");
            }
        }
    }

    /**
     * 获取账户变更前的账户信息
     *
     * @param html
     * @return
     */
    private void getFysAcctountData(String html, AmsFeiyusuanSyncCondition amsFeiyusuanSyncCondition) {
        Document doc = Jsoup.parse(html);
        Element commColumn = doc.getElementById("myTable");
        Elements commColumns = commColumn.getElementsByClass("EdtTbLCell");

        // 非预算证明文件1类型
        if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountFileType())) {
            amsFeiyusuanSyncCondition.setAccountFileType(commColumns.get(0).getElementsByAttribute("selected").val());
        }
        // 非预算证明文件1编号
        if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountFileNo())) {
            amsFeiyusuanSyncCondition.setAccountFileNo(commColumns.get(1).getElementsByClass("StdEditBox").val());
        }
        // 资金性质
        if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getCapitalProperty())) {
            amsFeiyusuanSyncCondition.setCapitalProperty(commColumns.get(2).getElementsByAttribute("selected").val());
        }
        // 备注
        if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getRemark())) {
            amsFeiyusuanSyncCondition.setRemark(commColumns.get(9).getElementsByClass("StdEditBox").val());
        }
        // 账户名称构成方式
        if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getAccountNameFrom())) {
            String accountNameType = commColumns.get(8).getElementsByAttribute("selected").val();
            amsFeiyusuanSyncCondition.setAccountNameFrom(accountNameType);
        }
        String accountNameType = amsFeiyusuanSyncCondition.getAccountNameFrom();
        // 与存款人名称一致
        if (accountNameType.equals("0")) {
            Element elementsyz = doc.getElementById("mytableFundManager");
            Elements eleValues = elementsyz.getElementsByClass("EdtTbLCell");
            // 资金管理人姓名
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManager())) {
                amsFeiyusuanSyncCondition.setMoneyManager(eleValues.get(0).getElementsByClass("StdEditBox").val());
            }
            // 资金管理人身份证件种类
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCtype())) {
                amsFeiyusuanSyncCondition.setMoneyManagerCtype(eleValues.get(1).getElementsByAttribute("selected").val());
            }
            // 资金管理人身份证件编号
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCno())) {
                amsFeiyusuanSyncCondition.setMoneyManagerCno(eleValues.get(2).getElementsByClass("StdEditBox").val());
            }
        } else if (accountNameType.equals("1")) {// 存款人名称加内设部门
            Element elementsns = doc.getElementById("mytableInterDep");
            Elements eleValues = elementsns.getElementsByClass("EdtTbLCell");
            // 内设部门名称
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideDepartmentName())) {
                amsFeiyusuanSyncCondition.setInsideDepartmentName(eleValues.get(0).getElementsByClass("StdEditBox").val());
            }
            // 内设部门负责人名称
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanName())) {
                amsFeiyusuanSyncCondition.setInsideSaccdepmanName(eleValues.get(1).getElementsByClass("StdEditBox").val());
            }
            // 内设部门非洲人身份证件类型
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanKind())) {
                amsFeiyusuanSyncCondition.setInsideSaccdepmanName(eleValues.get(2).getElementsByAttribute("selected").val());
            }
            // 内设部门负责人身份证件编号
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideSaccdepmanNo())) {
                amsFeiyusuanSyncCondition.setInsideSaccdepmanNo(eleValues.get(3).getElementsByClass("StdEditBox").val());
            }
            // 内设部门电话
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideTelphone())) {
                amsFeiyusuanSyncCondition.setInsideTelphone(eleValues.get(4).getElementsByClass("StdEditBox").val());
            }
            // 内设部门邮编
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideZipCode())) {
                amsFeiyusuanSyncCondition.setInsideZipCode(eleValues.get(5).getElementsByClass("StdEditBox").val());
            }
            // 内设部门地址
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getInsideAddress())) {
                amsFeiyusuanSyncCondition.setInsideAddress(eleValues.get(6).getElementsByClass("StdEditBox").val());
            }
        } else if (accountNameType.equals("2")) { // 存款人名称加资金性质
            Element elementzj = doc.getElementById("mytableFundandAcc");
            Elements eleValues = elementzj.getElementsByClass("EdtTbLCell");
            // 后缀值
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getSaccpostfix())) {
                amsFeiyusuanSyncCondition.setSaccpostfix(eleValues.get(1).getElementsByClass("StdEditBox").val());
            }
            // 资金管理人姓名
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManager())) {
                amsFeiyusuanSyncCondition.setMoneyManager(eleValues.get(5).getElementsByClass("StdEditBox").val());
            }
            // 资金管理人身份证件种类
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCtype())) {
                amsFeiyusuanSyncCondition.setMoneyManagerCtype(eleValues.get(6).getElementsByAttribute("selected").val());
            }
            // 资金管理人身份证件编号
            if (StringUtils.isBlank(amsFeiyusuanSyncCondition.getMoneyManagerCno())) {
                amsFeiyusuanSyncCondition.setMoneyManagerCno(eleValues.get(7).getElementsByClass("StdEditBox").val());
            }
        }
    }

    /**
     * 发起征询校验账户是否允许操作
     *
     * @param auth
     * @param consultId
     * @param condition
     * @throws SyncException
     * @throws Exception
     */
    private void checkCanOpenAccountByConsult(LoginAuth auth, String consultId, AmsYibanSyncCondition condition) throws SyncException, Exception {
        //List <Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
//        List<Header> headers = getHeader(auth);
        List<Header> headers = getHeader(auth,"");
        String url = auth.getDomain() + "/ams/dwr/exec/theIRequestService.refreshMsg.dwr";
        String c0_id = getrandom();
        StringBuffer bodyBuffer = new StringBuffer();
        bodyBuffer.append("callCount=1\n");
        bodyBuffer.append("c0-scriptName=theIRequestService\n");
        bodyBuffer.append("c0-methodName=refreshMsg\n");
        bodyBuffer.append("c0-id=").append(c0_id).append("\n");
        bodyBuffer.append("c0-param0=string:").append(consultId).append("\n");
        bodyBuffer.append("xml=true\n");
        HttpResult httpResult = HttpUtils.post(url, bodyBuffer.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        httpResult.makeHtml("gbk");
        String html = httpResult.getHtml();
//        logger.info("征询返回html：{}", html);
        logger.info("checkCanOpenAccountByConsult征询返回html：{}",html);
        Matcher matcher = Pattern.compile("var\\ss1=\"(.*)\";s0").matcher(html);
        if (matcher.find()) {
            String zxResutStr = matcher.group(1);
            if (StringUtils.isBlank(zxResutStr)) {
                throw new SyncException("异地征询结果为空!");
            } else {
                zxResutStr = EncodeUtils.ascii2native(zxResutStr);
                if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
                    if (zxResutStr.equals("不予开户")) {
                        matcher = Pattern.compile("var\\ss2=\"(.*)\";").matcher(html);
                        if (matcher.find()) {
                            zxResutStr = matcher.group(1);
                            zxResutStr = EncodeUtils.ascii2native(zxResutStr);
                            throw new SyncException(zxResutStr);
                        }
                    }
                } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
                    if (!zxResutStr.contains("同意变更")) {
                        throw new SyncException(zxResutStr);
                    }
                }
            }
        } else {
            logger.info("账号" + condition.getAcctNo() + "异地征询查询是否允许操作异常，请重试!");
            throw new SyncException("异地征询查询是否允许操作异常，请重试!");
        }
    }

    /**
     * 获取征询号码
     *
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    private String getConsultNo(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException, Exception {
        String searchNo = "";
        String url = getFirstConsultUrl(auth, condition);// 获取url
        HttpResult httpResult = HttpUtils.get(url, auth.getCookie(), null);
        httpResult.makeHtml("gbk");
        String html = httpResult.getHtml();
        Matcher matcher = Pattern.compile("var\\s+msgSingleId='(\\d+)'").matcher(html);
        if (matcher.find()) {
            searchNo = matcher.group(1);
        } else {
            logger.info("账号" + condition.getAcctNo() + "征询号查询失败!" + html);
            throw new SyncException("征询号查询失败!");
        }
        return searchNo;
    }

    /**
     * 获取征询编号接口的url
     *
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     */
    private String getFirstConsultUrl(LoginAuth auth, AmsYibanSyncCondition condition) throws SyncException {
        String url = "";
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialog";
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
            if (condition.getAcctType() == SyncAcctType.yiban) {
                url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialogForChange";
            } else {
                url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialogForChangeNonbudgetSpec";
            }
        } else {
            logger.info("账号" + condition.getAcctNo() + "询时无法识别新开户或变更，请联系系统管理员!");
            throw new SyncException("询时无法识别新开户或变更，请联系系统管理员");
        }
        return url;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAnnaulAcctNo(LoginAuth auth, String acctNo) throws SyncException, Exception {
        AmsAccountInfo acctInfo = new AmsAccountInfo();
        AmsAnnualInfo amsAnnualInfo = new AmsAnnualInfo();
        amsAnnualInfo.setAcctNo(acctNo);
        amsAnnualService.gotoAnnuanlPage(auth);
        AmsAnnualResultStatus annualResultStatus = amsAnnualService.validAcctNoAnnual(auth, acctNo, amsAnnualInfo);
        if (annualResultStatus != AmsAnnualResultStatus.Success) {
            BeanUtils.copyProperties(amsAnnualInfo, acctInfo);
        } else {
            throw new SyncException("账户信息查询失败:" + annualResultStatus.getFullName());
        }
        return acctInfo;
    }

    /**
     * 获取征询号码-通用
     *
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    private String getConsultNo(LoginAuth auth, AllAcct condition) throws SyncException, Exception {
        String searchNo = "";
        String url = getFirstConsultUrl(auth, condition);// 获取url
        //获取header
        List<Header> headers = getHeader(auth,"");
        logger.info("征询访问的url{}", url);
//        HttpResult httpResult = HttpUtils.get(url, auth.getCookie(), null);
        HttpResult httpResult = HttpUtils.get(url, auth.getCookie(), headers);
        httpResult.makeHtml("gbk");
        String html = httpResult.getHtml();
        logger.info("获取征询号的html：{}",html);
        Matcher matcher = Pattern.compile("var\\s+msgSingleId='(\\d+)'").matcher(html);
        if (matcher.find()) {
            searchNo = matcher.group(1);
        } else {
            logger.info("账号" + condition.getAcctNo() + "征询号查询失败!" + html);
            throw new SyncException("征询号查询失败!");
        }
        return searchNo;
    }

    /**
     * 获取征询编号接口的url-通用
     *
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     */
    private String getFirstConsultUrl(LoginAuth auth, AllAcct condition) throws SyncException {
        String url = "";
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialog";
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
            if (condition.getAcctType() == SyncAcctType.yiban) {
                url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialogForChange";
            } else if (condition.getAcctType() == SyncAcctType.feiyusuan) {
                url = auth.getDomain() + "/ams/dataExchange.do?method=forShowDialogForChangeNonbudgetSpec";
            } else if (condition.getAcctType() == SyncAcctType.jiben) {
                //url = auth.getDomain() + "";
            } else if (condition.getAcctType() == SyncAcctType.feilinshi) {
                //url = auth.getDomain() + "";
            }
        } else {
            logger.info("账号" + condition.getAcctNo() + "询时无法识别新开户或变更，请联系系统管理员!");
            throw new SyncException("询时无法识别新开户或变更，请联系系统管理员");
        }
        return url;
    }

    /**
     * 发起征询校验账户是否允许操作 - 通用
     *
     * @param auth
     * @param consultId
     * @param condition
     * @throws SyncException
     * @throws Exception
     */
    private void checkCanOpenAccountByConsult(LoginAuth auth, String consultId, AllAcct condition) throws SyncException, Exception {
//        List <Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
        List<Header> headers = getHeader(auth,"");
        String url = auth.getDomain() + "/ams/dwr/exec/theIRequestService.refreshMsg.dwr";
        String c0_id = getrandom();
        StringBuffer bodyBuffer = new StringBuffer();
        bodyBuffer.append("callCount=1\n");
        bodyBuffer.append("c0-scriptName=theIRequestService\n");
        bodyBuffer.append("c0-methodName=refreshMsg\n");
        bodyBuffer.append("c0-id=").append(c0_id).append("\n");
        bodyBuffer.append("c0-param0=string:").append(consultId).append("\n");
        bodyBuffer.append("xml=true\n");
        HttpResult httpResult = HttpUtils.post(url, bodyBuffer.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        httpResult.makeHtml("gbk");
        String html = httpResult.getHtml();
//        logger.info("异地征询校验是否开户：{}", html);
        logger.info("校验是否开户checkCanOpenAccountByConsult：{}",html);
        Matcher matcher = Pattern.compile("var\\ss1=\"(.*)\";s0").matcher(html);
        if (matcher.find()) {
            String zxResutStr = matcher.group(1);
            if (StringUtils.isBlank(zxResutStr)) {
                throw new SyncException("异地征询结果为空!");
            } else {
                zxResutStr = EncodeUtils.ascii2native(zxResutStr);
                if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
                    if (zxResutStr.equals("不予开户")) {
                        matcher = Pattern.compile("var\\ss2=\"(.*)\";").matcher(html);
                        if (matcher.find()) {
                            zxResutStr = matcher.group(1);
                            zxResutStr = EncodeUtils.ascii2native(zxResutStr);
                            throw new SyncException(zxResutStr);
                        }
                    }
                } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
                    if (!zxResutStr.contains("同意变更")) {
                        throw new SyncException(zxResutStr);
                    }
                }
            }
            logger.info("账号" + condition.getAcctNo() + "异地征询结果{}", zxResutStr);
        } else {
            logger.info("账号" + condition.getAcctNo() + "异地征询查询是否允许操作异常，请重试!");
            throw new SyncException("异地征询查询是否允许操作异常，请重试!");
        }
    }

    /**
     * 征询获取账户信息 - 基本户
     *
     * @param auth
     * @param condition
     * @param searchNo
     * @throws Exception
     */
    private void consultRequstResult(LoginAuth auth, AllAcct condition, String searchNo) throws Exception {
        String urlParsStr = "";
        String url = "";
        // 开户
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinue";
            urlParsStr = amsJibenOpenBeiAnService.getFirstBodyUrlParmsByFirstStep(condition);
            //加入征询数编号
            urlParsStr = urlParsStr.replace("&operateType=01", "&operateType=").replace("&msgSingleId=", "&msgSingleId=" + searchNo);
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
            // 变更
            //url = auth.getDomain() + "/ams/dataExchange.do?method=forContinueForChange";
        }
//        HttpUtils.post(url, urlParsStr, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        //获取header
        List<Header> headers = yidiHeader(auth,"");
        HttpUtils.post(url, urlParsStr, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        Thread.sleep(500);
        HttpResult httpResult = null;
        String html = "";
        // 反显录入证明文件信息页面
        // 开户
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + AmsConfig.ZG_URL_JIBEN_APPRLOCALBASICPILOT + "?method=forInputBasicImportantInfoForConsult";
//            httpResult = HttpUtils.get(url, auth.getCookie(), null);
            httpResult = HttpUtils.get(url, auth.getCookie(), headers);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
            logger.info("校验结果consultRequstResult：{}",html);
            if (html.indexOf("信息录入") > -1) {
                logger.info("征询成功，进入信息录入页面");
                Thread.sleep(1000);
            } else {
//                logger.info("基本户唯一性校验征询页面：{}", html);
                throw new SyncException("基本户开户征询成功，但进行下一步异常,请联系系统管理员");
            }
        }
        //变更
        else {
//            url = auth.getDomain() + "/ams/changeCommon.do?method=forInputBasicImportantInfoForConsult";
//            httpResult = HttpUtils.get(url, auth.getCookie(), null);
//            httpResult.makeHtml("gbk");
//            html = httpResult.getHtml();
//            if (html.indexOf("变更信息录入") > -1) {
//                Thread.sleep(500);
//            } else {
//                throw new SyncException("变更征询成功，但进行下一步异常,请联系系统管理员");
//            }
        }
    }

    /**
     * 征询获取账户信息 - 非临时
     *
     * @param auth
     * @param condition
     * @param searchNo
     * @throws Exception
     */
    private void consultRequstResult(LoginAuth auth, AmsFeilinshiSyncCondition condition, String searchNo) throws Exception {
        String urlParsStr = "";
        String url = "";
        // 开户
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + "/ams/dataExchange.do?method=forContinue";
            urlParsStr = amsFeilinshiOpenBeiAnService.getFirstBodyUrlParmsByFirstStep(condition);
            //加入征询数编号
            urlParsStr = urlParsStr.replace("&msgSingleId=", "&msgSingleId=" + searchNo);
        } else if (condition.getOperateType() == SyncOperateType.ACCT_CHANGE) {
            // 变更
            //url = auth.getDomain() + "/ams/dataExchange.do?method=forContinueForChange";
        }
//        HttpUtils.post(url, urlParsStr, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        List<Header> headers = yidiHeader(auth,"");
        HttpUtils.post(url, urlParsStr, HttpConfig.HTTP_ENCODING, auth.getCookie(), headers);
        Thread.sleep(500);
        HttpResult httpResult = null;
        String html = "";
        // 开户
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url = auth.getDomain() + AmsConfig.ZG_URL_feilinshi_apprnontempinsttempPilot + "?method=forInputBasicImportantInfoForConsult";
            httpResult = HttpUtils.get(url, auth.getCookie(), null);
            httpResult.makeHtml("gbk");
            html = httpResult.getHtml();
//            logger.info("临时户开户结果{}", html);
            logger.info("临时户开户结果consultRequstResult{}",html);
            if (html.indexOf("临时存款账户开户原因") > -1) {
                Thread.sleep(500);
            } else {
                throw new SyncException("非临时开户征询成功，但进行下一步异常,请联系系统管理员");
            }
        }
        //变更
        else {
//            url = auth.getDomain() + "/ams/changeCommon.do?method=forInputBasicImportantInfoForConsult";
//            httpResult = HttpUtils.get(url, auth.getCookie(), null);
//            httpResult.makeHtml("gbk");
//            html = httpResult.getHtml();
//            if (html.indexOf("变更信息录入") > -1) {
//                Thread.sleep(500);
//            } else {
//                throw new SyncException("变更征询成功，但进行下一步异常,请联系系统管理员");
//            }
        }
    }

    /**
     * 基本户开户征询校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validConsultModel(AllAcct condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("征询对象为空");
        }
        if (condition.getAcctType() == null) {
            throw new SyncException("征询对象账户性质不能为空");
        }
        if (condition.getOperateType() == null) {
            throw new SyncException("征询对象业务类型不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("征询时账号不能为空");
        }
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            if (StringUtils.isBlank(condition.getRegAreaCode())) {
                throw new SyncException("征询时注册地区代码不能为空");
            }
            if (StringUtils.isEmpty(condition.getAcctCreateDate())) {
                throw new SyncException("征询时开户日期不能为空");
            }
            if (StringUtils.isBlank(condition.getDepositorType())) {
                throw new SyncException("征询时存款人类别不能为空");
            }
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("征询时银行所在地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("征询时银行机构号不能为空");
        }
    }

    /**
     * 非临时开户征询校验
     *
     * @param condition
     * @throws SyncException
     */
    private void validConsultModel(AmsFeilinshiSyncCondition condition) throws SyncException {
        if (condition == null) {
            throw new SyncException("征询对象为空");
        }
        if (condition.getAcctType() == null) {
            throw new SyncException("征询对象账户性质不能为空");
        }
        if (condition.getOperateType() == null) {
            throw new SyncException("征询对象业务类型不能为空");
        }
        if (StringUtils.isBlank(condition.getAcctNo())) {
            throw new SyncException("征询时账号不能为空");
        }
        if (condition.getOperateType() == SyncOperateType.ACCT_OPEN) {
            if (StringUtils.isBlank(condition.getRegAreaCode())) {
                throw new SyncException("征询时注册地区代码不能为空");
            }
            if (StringUtils.isEmpty(condition.getAcctCreateDate())) {
                throw new SyncException("征询时开户日期不能为空");
            }
            if (StringUtils.isBlank(condition.getAccountKey())) {
                throw new SyncException("征询时基本存款编号不能为空");
            }
        }
        if (StringUtils.isBlank(condition.getBankAreaCode())) {
            throw new SyncException("征询时银行所在地区代码不能为空");
        }
        if (StringUtils.isBlank(condition.getBankCode())) {
            throw new SyncException("征询时银行机构号不能为空");
        }
    }

    private static String getrandom() {
        //四位随机数加时间戳
        return RandomStringUtils.randomNumeric(4) + "_" + System.currentTimeMillis();
    }



    public String getCheckPageSubmitValue(String html){
        String submitValue = "";
        Matcher matcher = Pattern.compile("thevalidatePageSubmitProxy.checkPageSubmit\\(\'(.*?)\'\\)").matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        if(CollectionUtils.isNotEmpty(list)){
            if(list.size() > 5){
                submitValue = list.get(4);
            }else{
                submitValue = list.get(0);
            }
        }
        return submitValue;
    }

    @Override
    public void callbackMethod(LoginAuth auth,String html)  throws SyncException, Exception {
        try {
            //获取checksubit值
            String checkPageSubmitValue = getCheckPageSubmitValue(html);
            //回调checksubit dwr
            if(org.apache.commons.lang.StringUtils.isNotBlank(checkPageSubmitValue)){
                logger.info("checkPageSubmitValue:" + checkPageSubmitValue);
                checkPageSubmit(auth,checkPageSubmitValue);
            }
        } catch (Exception e) {
            logger.error("回调人行更新后参数异常", e);
            throw new SyncException("回调人行更新后参数异常");
        }
    }

    public List<Header> getHeader(LoginAuth auth,String urlReferer){
        List<Header> headers = new ArrayList<>();
        Header headerAccept = new BasicHeader("Accept","*/*");
        Header encoding = new BasicHeader("Accept-Encoding","gzip, deflate");
        Header language = new BasicHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        Header agent = new BasicHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; InfoPath.3)");
        Header contentType = new BasicHeader("Content-Type","text/plain");
        Header host = new BasicHeader("Host",auth.getDomain().replace("http://",""));
        Header connection = new BasicHeader("Connection","Keep-Alive");
        Header pragma = new BasicHeader("Pragma","no-cache");
        if(StringUtils.isNotBlank(urlReferer)){
            Header referer = new BasicHeader("Referer",urlReferer);
            headers.add(referer);
        }

        headers.add(headerAccept);
        headers.add(encoding);
        headers.add(language);
        headers.add(agent);
        headers.add(contentType);
        headers.add(host);
        headers.add(connection);
        headers.add(pragma);
        return headers;
    }
    public List<Header> yidiHeader(LoginAuth auth,String urlReferer){
        List<Header> headers = new ArrayList<>();
        Header headerAccept = new BasicHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*");
        Header encoding = new BasicHeader("Accept-Encoding","gzip, deflate");
        Header language = new BasicHeader("Accept-Language","zh-CN");
        Header agent = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        Header contentType = new BasicHeader("Content-Type","application/x-www-form-urlencoded");
        Header host = new BasicHeader("Host",auth.getDomain().replace("http://",""));
        if(StringUtils.isNotBlank(urlReferer)){
            Header referer = new BasicHeader("Referer",urlReferer);
            headers.add(referer);
        }
        headers.add(headerAccept);
        headers.add(encoding);
        headers.add(language);
        headers.add(agent);
        headers.add(contentType);
        headers.add(host);
        return headers;
    }

    public List<Header> yidiHeaderForDetail(LoginAuth auth,String urlReferer){
        List<Header> headers = new ArrayList<>();
        Header headerAccept = new BasicHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*");
        Header encoding = new BasicHeader("Accept-Encoding","gzip, deflate");
        Header language = new BasicHeader("Accept-Language","zh-CN");
        Header agent = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        Header host = new BasicHeader("Host",auth.getDomain().replace("http://",""));
        if(StringUtils.isNotBlank(urlReferer)){
            Header referer = new BasicHeader("Referer",urlReferer);
            headers.add(referer);
        }
        headers.add(headerAccept);
        headers.add(encoding);
        headers.add(language);
        headers.add(agent);
        headers.add(host);
        return headers;
    }

    @Override
    public List<Header> getHeaderForShowResult(LoginAuth auth,String url){
        List<Header> headers = new ArrayList<>();
        Header headerAccept = new BasicHeader("Accept","text/html, application/xhtml+xml, */*");
        Header encoding = new BasicHeader("Accept-Encoding","gzip, deflate");
        Header language = new BasicHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        Header agent = new BasicHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; InfoPath.3)");
        Header contentType = new BasicHeader("Content-Type","application/x-www-form-urlencoded");
        Header host = new BasicHeader("Host",auth.getDomain().replace("http://",""));
        Header connection = new BasicHeader("Connection","Keep-Alive");
        Header pragma = new BasicHeader("Pragma","no-cache");
        if(StringUtils.isNotBlank(url)){
            Header referer = new BasicHeader("Referer",url);
            headers.add(referer);
        }
        headers.add(headerAccept);
        headers.add(encoding);
        headers.add(language);
        headers.add(agent);
        headers.add(contentType);
        headers.add(host);
        headers.add(connection);
        headers.add(pragma);
        return headers;
    }

}
