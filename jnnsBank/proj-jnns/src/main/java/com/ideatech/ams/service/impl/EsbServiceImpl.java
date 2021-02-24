package com.ideatech.ams.service.impl;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.dao.AccountsBillsAllDao;
import com.ideatech.ams.dao.JhhDao;
import com.ideatech.ams.dao.SyncCompareDao;
import com.ideatech.ams.domain.Jhh;
import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.esb.*;
import com.ideatech.ams.dto.esb.response.*;
import com.ideatech.ams.service.EsbService;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.ams.unifyLogin.CharsetEnum;
import com.ideatech.ams.unifyLogin.socket.ClientThreadExecutor;
import com.ideatech.ams.unifyLogin.socket.MessageUtil;
import com.ideatech.ams.utils.DateUtils;

import com.ideatech.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

;

/**
 * esb服务类
 *
 * @auther zoulang
 * @create 2018-11-30 2:37 PM
 **/
@Service
@EnableTransactionManagement(order = 8)
public class EsbServiceImpl implements EsbService {
    private static final Logger log = LoggerFactory.getLogger(EsbServiceImpl.class);

    @Value("${jnns.esb.url}")
    protected String esbUrl;
    @Autowired
    private SyncCompareService syncCompareService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SyncCompareDao syncCompareDao;

    @Autowired
    private AccountsBillsAllDao accountsBillsAllDao;

    @Autowired
    private JhhDao jhhDao;

    @Value("${socket.ip}")
    private String ip;

    @Value("${socket.port}")
    private String port;

    @Value("${socket.appid}")
    private String appid;


    @Autowired
    private UserDao userDao;
    public Integer getResponsezs(String i) {
        ResponseBody responseBody = JSON.parseObject(i, ResponseBody.class);
        RTransaction rTransaction = JSON.parseObject(responseBody.getTransaction(), RTransaction.class);
        RBody rBody = JSON.parseObject(rTransaction.getBody(), RBody.class);
        Response response1 = JSON.parseObject(rBody.getResponse(), Response.class);
        RBizHeader rBizHeader = JSON.parseObject(response1.getBizHeader(), RBizHeader.class);
        String zonghu = rBizHeader.getZongbshu();
        if (StringUtils.isEmpty(zonghu)) {
            return 0;
        }
        return Integer.parseInt(zonghu);
    }

    public boolean getSaveSyncCompareInfo(List<SyncCompareInfo> syncCompareInfoList) {
        //取出核心数据集合账户数据
        for (SyncCompareInfo syncCompareInfo : syncCompareInfoList) {
            if (StringUtils.isNotEmpty(syncCompareInfo.getAcctNo()) && StringUtils.isNotEmpty(syncCompareInfo.getKaixhubz())
                    && StringUtils.isNotEmpty(syncCompareInfo.getAcctOpenDate())) {
                SyncCompare syncCompare = syncCompareDao.findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(syncCompareInfo.getAcctNo(), syncCompareInfo.getKaixhubz(), syncCompareInfo.getAcctOpenDate());
                if (syncCompare != null) {
                    syncCompareDao.delete(syncCompare.getId());
                    syncCompareService.create(syncCompareInfo);
                } else {
                    syncCompareService.create(syncCompareInfo);
                }
            } else {
                //存储数据到同步表
                syncCompareService.create(syncCompareInfo);
            }
        }
        return true;
    }

    public List<SyncCompareInfo> getResponse(String response, String kaixhubz) {
        List<SyncCompareInfo> syncCompareInfoList = new ArrayList<SyncCompareInfo>();
        ResponseBody responseBody = JSON.parseObject(response, ResponseBody.class);
        if (StringUtils.isNotEmpty(responseBody.getTransaction())) {
            RTransaction rTransaction = JSON.parseObject(responseBody.getTransaction(), RTransaction.class);
            if (StringUtils.isNotEmpty(rTransaction.getBody())) {
                RBody rBody = JSON.parseObject(rTransaction.getBody(), RBody.class);
                if (StringUtils.isNotEmpty(rBody.getResponse())) {
                    Response response1 = JSON.parseObject(rBody.getResponse(), Response.class);
                    if (StringUtils.isNotEmpty(response1.getBizBody())) {
                        RBizBody rBizBody = JSON.parseObject(response1.getBizBody(), RBizBody.class);
                        if (StringUtils.isNotEmpty(rBizBody.getAcoplist_ARRAY())) {
                            Acoplist_ARRAY acoplist_array = JSON.parseObject(rBizBody.getAcoplist_ARRAY(), Acoplist_ARRAY.class);
                            if (StringUtils.isNotEmpty(acoplist_array.getMX())) {
                                List<MX> mxList = JSON.parseArray(acoplist_array.getMX(), MX.class);
                                for (int i = 0; i < mxList.size(); i++) {
                                    SyncCompare syncCompare = null;
                                    if (StringUtils.isEmpty(mxList.get(i).getKehuzhao())) {
                                        log.info("数据重复，跳过！");
                                        continue;
                                    }
                                    if ("01".equals(kaixhubz)) {
                                        log.info("--------开户重复数据筛选开始--------");
                                        syncCompare = syncCompareDao.findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(mxList.get(i).getKehuzhao(), kaixhubz, mxList.get(i).getKaihriqi());
                                        if (syncCompare != null) {
                                            log.info("开户数据查询：" + syncCompare.toString());
                                            log.info("数据重复，跳过！");
                                            continue;
                                        }
                                    }
                                    if ("02".equals(kaixhubz)) {
                                        log.info("--------销户重复数据筛选开始--------");
                                        syncCompare = syncCompareDao.findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(mxList.get(i).getKehuzhao(), kaixhubz, mxList.get(i).getXiohriqi());
                                        if (syncCompare != null) {
                                            log.info("销户数据查询：" + syncCompare.toString());
                                            log.info("数据重复，跳过！");
                                            continue;
                                        }
                                    }
                                    SyncCompareInfo syncCompareInfo = new SyncCompareInfo();
                                    syncCompareInfo.setAcctNo(mxList.get(i).getKehuzhao());
                                    syncCompareInfo.setDepositorName(mxList.get(i).getZhhuzwmc());
                                    syncCompareInfo.setZhufldm1(mxList.get(i).getZhufldm1());

                                    switch (mxList.get(i).getZhufldm1()) {
                                        case "0001":
                                            syncCompareInfo.setAcctType("jiben");
                                            break;
                                        case "0002":
                                            syncCompareInfo.setAcctType("yiban");
                                            break;
                                        case "0003":
                                            syncCompareInfo.setAcctType("linshi");
                                            break;
                                        case "0004":
                                            syncCompareInfo.setAcctType("zhuanhu");
                                            break;
                                        default:
                                            syncCompareInfo.setAcctType("unknow");
                                    }
                                    if ("01".equals(kaixhubz)) {
                                        AccountBillsAll accountBillsAll = accountsBillsAllDao.findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(mxList.get(i).getKehuzhao(), BillType.ACCT_OPEN);
                                        if (accountBillsAll != null) {
                                            if (StringUtils.isNotEmpty(accountBillsAll.getPbcSyncStatus().toString())) {
                                                syncCompareInfo.setPbcStarts(accountBillsAll.getPbcSyncStatus().toString());
                                            } else {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                            }
                                            if (StringUtils.isNotEmpty(accountBillsAll.getEccsSyncStatus().toString())) {
                                                if ("jiben".equals(syncCompareInfo.getAcctType())) {
                                                    syncCompareInfo.setEccsStarts(accountBillsAll.getEccsSyncStatus().toString());
                                                } else {
                                                    syncCompareInfo.setEccsStarts("buTongBu");
                                                }
                                            } else {
                                                syncCompareInfo.setEccsStarts("weiShangBao");
                                            }
                                        } else {
                                            if ("jiben".equals(syncCompareInfo.getAcctType())) {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                                syncCompareInfo.setEccsStarts("weiShangBao");
                                            } else {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                                syncCompareInfo.setEccsStarts("buTongBu");
                                            }
                                        }

                                        syncCompareInfo.setKaixhubz("01");
                                        syncCompareInfo.setBusinessDate(mxList.get(i).getKaihriqi());
                                        syncCompareInfo.setOrganCode(mxList.get(i).getKaihjigo());
                                        OrganizationDto organizationDto = organizationService.findByCode(mxList.get(i).getKaihjigo());
                                        if (organizationDto != null) {
                                            syncCompareInfo.setOrganFullId(organizationDto.getFullId());
                                        } else {
                                            syncCompareInfo.setOrganFullId("1");
                                        }
                                    }
                                    if ("02".equals(kaixhubz)) {
                                        AccountBillsAll accountBillsAll = null;
                                        //核心传过来无交换号，导致销户无法正确查询流水表状态。导致一直为未上报状态，江南农商2019年7月18日
                                        log.info("同步开变销时，账户状态为销户时，根据账号" + mxList.get(i).getKehuzhao() + "开始从产品流水表查询信息");
                                        accountBillsAll = accountsBillsAllDao.findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(mxList.get(i).getKehuzhao(), BillType.ACCT_REVOKE);
                                        if (accountBillsAll == null || StringUtils.isEmpty(accountBillsAll.getAcctNo())) {
                                            log.info("同步开变销时，账户状态为销户时，根据账号" + mxList.get(i).getKehuzhao() + "从产品流水表查询信息为空");
                                            Jhh jhh = jhhDao.findTopByAcctNo(mxList.get(i).getKehuzhao());
                                            if (jhh != null && StringUtils.isNotBlank(jhh.getAcctNo())) {
                                                accountBillsAll = accountsBillsAllDao.findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(jhh.getJhhAcctNo(), BillType.ACCT_REVOKE);
                                            } else {
                                                log.info("同步开变销时，账户状态为销户时，根据账号" + mxList.get(i).getKehuzhao() + "从交换号表查询信息为空");
                                            }
                                        }
                                        if (accountBillsAll != null) {
                                            if (StringUtils.isNotEmpty(accountBillsAll.getPbcSyncStatus().toString())) {
                                                syncCompareInfo.setPbcStarts(accountBillsAll.getPbcSyncStatus().toString());
                                            } else {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                            }
                                            if (StringUtils.isNotEmpty(accountBillsAll.getEccsSyncStatus().toString())) {
                                                if ("jiben".equals(syncCompareInfo.getAcctType())) {
                                                    syncCompareInfo.setEccsStarts(accountBillsAll.getEccsSyncStatus().toString());
                                                } else {
                                                    syncCompareInfo.setEccsStarts("buTongBu");
                                                }
                                            } else {
                                                syncCompareInfo.setEccsStarts("weiShangBao");
                                            }
                                        } else {
                                            if ("jiben".equals(syncCompareInfo.getAcctType())) {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                                syncCompareInfo.setEccsStarts("weiShangBao");
                                            } else {
                                                syncCompareInfo.setPbcStarts("weiShangBao");
                                                syncCompareInfo.setEccsStarts("buTongBu");
                                            }
                                        }
                                        syncCompareInfo.setKaixhubz("02");
                                        syncCompareInfo.setBusinessDate(mxList.get(i).getXiohriqi());
                                        syncCompareInfo.setOrganCode(mxList.get(i).getXiohjigo());
                                        OrganizationDto organizationDto = organizationService.findByCode(mxList.get(i).getXiohjigo());
                                        if (organizationDto != null) {
                                            syncCompareInfo.setOrganFullId(organizationDto.getFullId());
                                        } else {
                                            syncCompareInfo.setOrganFullId("1");
                                        }
                                    }
                                    syncCompareInfo.setAcctOpenDate(DateUtils.getNowDateShort());
                                    syncCompareInfoList.add(syncCompareInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("从核心取回来的数据：" + syncCompareInfoList.toString());
        return syncCompareInfoList;
    }

    public RequestBody getRequestBody(String kaixhubz, int cishu) {
        RequestBody requestBody = new RequestBody();
        requestBody.setTransaction(getTransaction(kaixhubz, cishu));
        return requestBody;
    }

    public Transaction getTransaction(String kaixhubz, int cishu) {
        Transaction Transaction = new Transaction();
        Transaction.setBody(getBody(kaixhubz, cishu));
        Transaction.setHeader(getHeader());
        return Transaction;
    }

    public Header getHeader() {
        Header Header = new Header();
        Header.setSysHeader(getSysHead());
        return Header;
    }

    public Body getBody(String kaixhubz, int cishu) {
        Body Body = new Body();
        Body.setRequest(getRequest(kaixhubz, cishu));
        return Body;
    }

    public Request getRequest(String kaixhubz, int cishu) {
        Request request = new Request();
        request.setBizBody(getBizBody(kaixhubz, cishu));
        request.setBizHeader(getBizHeader());
        return request;
    }

    public BizHeader getBizHeader() {
        BizHeader bizHead = new BizHeader();
        bizHead.setFqxtbios("339");//发起系统标示
        bizHead.setWaibclma("dp2637");//发起方交易码
        bizHead.setJiaoyima("dp2637");//交易码
        //为空设置默认查询机构
        bizHead.setJiaoyijg("01001");//交易机构
        bizHead.setQudaoflm("339");//渠道类型
        bizHead.setJiaoyigy("JNUSR");//交易柜员
        bizHead.setQudaohao("339");//发起方渠道
        return bizHead;
    }

    public BizBody getBizBody(String kaixhubz, int cishu) {
        BizBody bizBody = new BizBody();
        int s = 5;
        Integer es = (cishu * s) - 5;
        bizBody.setQishibis(es.toString());
        bizBody.setChxunbis("5");
        if ("01".equals(kaixhubz)) {
            bizBody.setKaixhubz("1");
        }
        if ("02".equals(kaixhubz)) {
            bizBody.setKaixhubz("2");
        }
        return bizBody;
    }

    public SysHeader getSysHead() {
        String proNumber = "0339";
        String index = "0001";
        String serviceCd = "P00001011560";
        String operation = "QueryTodPubAccInfo";
        String dealYard = "dp2637";
        String requestProject = "339";
        String serviceProject = "111";
        SysHeader sysHeader = new SysHeader();
        sysHeader.setMsgId(proNumber + getNowDateShort("yyyyMMdd") + getNowDateShort("HHmmssSSS") + index);
        log.info("MsgId" + sysHeader.getMsgId());
        sysHeader.setMsgDate(getNowDateShort("yyyy-MM-dd"));
        sysHeader.setMsgTime(getNowDateShort("HH:mm:ss:SSS"));
        sysHeader.setServiceCd(serviceCd);
        sysHeader.setOperation(operation);
        sysHeader.setClientCd(requestProject);
        sysHeader.setServerCd(serviceProject);
        return sysHeader;
    }

    public Map<String, Map<String, String>> getUsernameDianLiang() {
        log.info("进入点亮功能！");
        List<UserPo> list = userDao.findAll();
        log.info("查询的用户list：" + list.toString());
        int urlPort = Integer.parseInt(port);
        Map<String, Map<String, String>> map = new HashMap<>();
        for (UserPo userPo : list) {
            log.info("用户名称：" + userPo.getUsername());
            String msg = "prcscd=adduser|ip=|userid=" + userPo.getUsername() + "|appid=" + appid + "|certtp=0|";
            Map<String, String> returnMap = MessageUtil.converToMap(ClientThreadExecutor.send(ip, urlPort, MessageUtil.addHead(msg), CharsetEnum.GBK, 4));//发送报文，并获得响应报文
            log.info("点亮功能：" + returnMap.toString());
            map.put(userPo.getUsername(), returnMap);
        }
        return map;
    }


    @Override
    public String getResponseCustom(String httpIntefaceUtils) {
        ResponseBody responseBody = JSON.parseObject(httpIntefaceUtils, ResponseBody.class);
        Transaction transaction = JSON.parseObject(responseBody.getTransaction(), Transaction.class);
        Body body = transaction.getBody();
       // log.info("Body---------" + body.toString());
        Response response = body.getResponse();
        //log.info("response---------" + response.toString());
        String bizBody = response.getBizBody();
        //log.info("bizBody---------" + bizBody);
        return bizBody;
    }
}



