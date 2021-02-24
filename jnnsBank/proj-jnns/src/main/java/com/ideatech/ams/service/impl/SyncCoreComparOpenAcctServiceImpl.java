package com.ideatech.ams.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountsAllSearchInfo;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.dao.JnnsAccountsAllDao;
import com.ideatech.ams.dao.SyncCompareDao;
import com.ideatech.ams.dto.SaicQuery.BreakLaw;
import com.ideatech.ams.dto.SaicQuery.Deabbeat;
import com.ideatech.ams.dto.SaicQuery.Owing;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.esb.RequestBody;
import com.ideatech.ams.kyc.service.holiday.HolidayService;
import com.ideatech.ams.readData.AlteritemMointor;
import com.ideatech.ams.readData.service.JnnsAlteritemService;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.riskdata.service.RiskDataService;
import com.ideatech.ams.service.EsbService;
import com.ideatech.ams.service.JnnsSaicTestService;
import com.ideatech.ams.service.SyncCoreComparOpenAcctService;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.notice.service.NoticeService;
import com.ideatech.ams.utils.DateUtils;
import com.ideatech.ams.utils.HttpIntefaceUtils;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;
import java.util.List;

/**
 * @author ：SUNLELE
 * @date ：Created in 2019/1/21 23:43
 * @description：定时任务同步核心开户数据比对
 * @modified By：
 * @version: 1.0
 */
@Component
@EnableScheduling
@EnableTransactionManagement
public class SyncCoreComparOpenAcctServiceImpl implements SyncCoreComparOpenAcctService {

    static final Logger log = LoggerFactory.getLogger(SyncCoreComparOpenAcctServiceImpl.class);


    @Autowired
    private SyncCompareDao syncCompareDao;

    @Autowired
    private EsbService esbService;

    @Autowired
    private JnnsAccountsAllDao jnnsAccountsAllDao;
    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;
    @Autowired
    private AccountsAllService accountsAllService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private HolidayService holidayService;
    @Autowired
    private CustomerPublicService customerPublicService;
    @Autowired
    private AccountPublicService accountPublicService;
    @Autowired
    private AccountPublicLogService accountPublicLogService;
    @Autowired
    RiskDataService riskDataService;
    @Autowired
    JnnsSaicTestService jnnsSaicTestService;
    @Autowired
    JnnsAlteritemService jnnsAlteritemService;
    @Autowired
    private NoticeService noticeService;
    @Value("${jnns.esb.url}")
    protected String esbUrl;

    //核心分页总数
    private int zongshu;

    //请求数据次数
    private int cishu = 0;

    @Override
    public void syncCoreCompare() {

        syncCompareDao.deleteByAcctOpenDate(DateUtils.getNowDateShort());

        log.info("获取核心开户数据开始.......");
        getOrganCode("01");
        log.info("获取核心开户数据结束.......");

        log.info("获取核心销户数据开始！！！！");
        getOrganCode("02");
        log.info("获取核心销户数据结束！！！！");
    }

    /**
     * 查询全部机构
     */
    @Override
    public void getOrganCode(String kaixhubz) {
        getCoreOpenAcctList(null, kaixhubz);
    }
    @Override
    public void delCoreData() {
        int delCount=0;
        List<String> accountsAlls = jnnsAccountsAllDao.selectCoreData();
        log.info("第一步查询的accountsAlls数量为："+accountsAlls.size());
        for (int a = 0; a < accountsAlls.size(); a++) {
            List<AccountsAll> allByAcctNameAndString003 = jnnsAccountsAllDao.findAllByAcctNameAndString003(accountsAlls.get(a), "1");
            for (int i = 0; i < allByAcctNameAndString003.size(); i++) {
                for(AccountsAll accountsAll:allByAcctNameAndString003){
                    if (allByAcctNameAndString003.get(i).getAcctNo().length() < accountsAll.getAcctNo().length() &&allByAcctNameAndString003.get(i).getAcctCreateDate().equals(accountsAll.getAcctCreateDate())) {
                        log.info("定时任务删除存量数据中重复账号==删除长号acctNo==="+accountsAll.getAcctNo());
                        jnnsAccountsAllDao.deleteByAcctNo(accountsAll.getAcctNo());
                        delCount++;
                    } else if (allByAcctNameAndString003.get(i).getAcctNo().length() > accountsAll.getAcctNo().length() &&allByAcctNameAndString003.get(i).getAcctCreateDate().equals(accountsAll.getAcctCreateDate())) {
                        log.info("定时任务删除存量数据中重复账号==删除长号acctNo2==="+allByAcctNameAndString003.get(i).getAcctNo());
                        jnnsAccountsAllDao.deleteByAcctNo(allByAcctNameAndString003.get(i).getAcctNo());
                        delCount++;
                    }
                }
            }
        }
        log.info("定时任务删除存量重复数据（删除长号）删除总数为："+delCount/2);
    }

    @Override
    public void insertYuJingData(Pageable pageable ){
        try{
            noticeList("tempAcct");
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            noticeList("legalDueNotice");
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            noticeList("fileDueNotice");
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            queryDzData();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            queryRiskStaticData();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            listByDeabbrat(pageable);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            listByBreak(pageable);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        try{
            listByOwing(pageable);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        }
        public ResultDto noticeList(String noticeType) throws Exception {
            CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto =new CompanyPreOpenAccountEntDto();
            CustomerPublicInfo customerPublicInfo= new CustomerPublicInfo();
            AccountsAllSearchInfo accountsAllInfo=new AccountsAllSearchInfo();
            PagingDto pagingDto =new  PagingDto();
            String operatorIdcardDue=null;
            Boolean isOperatorIdcardDue=null;
            String organFullId = SecurityUtils.getCurrentOrgFullId();
            switch (noticeType) {
                case "tempAcct": {
                    log.info("临时户到期预警保存开始");
                    if(isOperatorIdcardDue!=null){
                        accountsAllInfo.setIsEffectiveDateOver(isOperatorIdcardDue);
                    }
                    if(operatorIdcardDue!=null){
                        accountsAllInfo.setEffectiveDate(operatorIdcardDue);
                    }
                    Long tempAcctNoticeDay = configService.findOneByKey("tempAcctNoticeDay");
                    Long tempAcctOverNoticeDay = configService.findOneByKey("tempAcctOverNoticeDay");
                    if(tempAcctNoticeDay == null) {
                        tempAcctNoticeDay = 3L;
                    }
                    if(tempAcctOverNoticeDay == null) {
                        tempAcctOverNoticeDay = 3L;
                    }
                    if (tempAcctNoticeDay != null) {
                        Date nowDate = DateUtil.beginOfDate(new Date());
                        Date afterDate = DateUtil.addDays(nowDate, (int) (tempAcctNoticeDay + 0));
                        Date beforeDate = DateUtil.subDays(nowDate, (int) (tempAcctOverNoticeDay + 0));
                        accountsAllInfo.setOrganFullId(organFullId);
                        return ResultDtoFactory.toAckData(
                                accountsAllService.listTempAcctBefore1(accountsAllInfo, com.ideatech.common.util.DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                        com.ideatech.common.util.DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), pagingDto));
                    }
                    break;
                }
                case "legalDueNotice": {
                    log.info("法人证件到期预警保存开始");
                    Long legalDueNoticeDay = configService.findOneByKey("legalDueNoticeDay");
                    Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
                    if(legalDueNoticeDay == null) {
                        legalDueNoticeDay = 3L;
                    }
                    if(legalOverNoticeDay == null) {
                        legalOverNoticeDay = 3L;
                    }
                    if (legalDueNoticeDay != null) {
                        Date nowDate = DateUtil.beginOfDate(new Date());
                        Date afterDate = DateUtil.addDays(nowDate, (int) (legalDueNoticeDay + 0));
                        Date beforeDate = DateUtil.subDays(nowDate, (int) (legalOverNoticeDay + 0));
                        return ResultDtoFactory.toAckData(
                                customerPublicService.listCustDueBefore1("legalDueNotice", customerPublicInfo, com.ideatech.common.util.DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                        com.ideatech.common.util.DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                    }
                    break;
                }
                case "fileDueNotice": {
                    log.info("证明文件到期预警保存开始");
                    Long fileDueNoticeDay = configService.findOneByKey("fileDueNoticeDay");
                    Long fileOverNoticeDay = configService.findOneByKey("fileOverNoticeDay");
                    if(fileDueNoticeDay == null) {
                        fileDueNoticeDay = 3L;
                    }
                    if(fileOverNoticeDay == null) {
                        fileOverNoticeDay = 3L;
                    }
                    if (fileDueNoticeDay != null) {
                        Date nowDate = DateUtil.beginOfDate(new Date());
                        Date afterDate = DateUtil.addDays(nowDate, (int) (fileDueNoticeDay + 0));
                        Date beforeDate = DateUtil.subDays(nowDate, (int) (fileOverNoticeDay + 0));
                        return ResultDtoFactory.toAckData(
                                customerPublicService.listCustDueBefore1("fileDueNotice", customerPublicInfo, com.ideatech.common.util.DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                        com.ideatech.common.util.DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                    }
                    break;
                }
                case "operatorDueNotice": {
                    Long operatorDueNoticeDay = configService.findOneByKey("operatorDueNoticeDay");
                    Long operatorOverNoticeDay = configService.findOneByKey("operatorOverNoticeDay");
                    if(operatorDueNoticeDay == null) {
                        operatorDueNoticeDay = 3L;
                    }
                    if(operatorOverNoticeDay == null) {
                        operatorOverNoticeDay = 3L;
                    }
                    if (operatorDueNoticeDay != null) {
                        Date nowDate = DateUtil.beginOfDate(new Date());
                        Date afterDate = DateUtil.addDays(nowDate, (int) (operatorDueNoticeDay + 0));
                        Date beforeDate = DateUtil.subDays(nowDate, (int) (operatorOverNoticeDay + 0));
                        return ResultDtoFactory.toAckData(
                                accountPublicLogService.listOperatorIdcardDueBefore(isOperatorIdcardDue, operatorIdcardDue, com.ideatech.common.util.DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                        com.ideatech.common.util.DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                    }
                    break;
                }
                case "resvrUnprocess": {
                    Long resvrProcessDay = configService.findOneByKey("resvrProcessDay");
                    if(resvrProcessDay == null) {
                        resvrProcessDay = 3L;
                    }
                    if (resvrProcessDay != null) {
                        try {
                            Date dateBefore = holidayService.addWorkday(com.ideatech.common.util.DateUtils.getNowDateShort(), (int) -resvrProcessDay);
                            companyPreOpenAccountEntDto.setStatus(ApplyEnum.UnComplete.getValue());//未处理
                            companyPreOpenAccountEntDto.setCreatedDate(dateBefore);//过期
                            return ResultDtoFactory.toAckData(
                                    companyPreOpenAccountEntService.listUnprocessedCountBefore(companyPreOpenAccountEntDto,dateBefore,
                                            organFullId,
                                            pagingDto));
                        } catch (Exception e) {
                            log.error("统计预约未及时接洽数量时出错", e);
                        }
                    }
                    break;
                }
              default:
                    break;
            }
            return ResultDtoFactory.toNack("数据不存在");
        }
        //对账
    public ResultDto queryDzData( ) {
        log.info("对账开始-----");
        ModelSearchExtendDto modelSearchExtendDto = new ModelSearchExtendDto();
        return ResultDtoFactory.toAckData(riskDataService.queryRiskDzData1(modelSearchExtendDto));

    }
    //开户监测
    public ResultDto queryRiskStaticData( ) {
        log.info("开户监测开始-----");
        ModelSearchExtendDto modelSearchExtendDto = new ModelSearchExtendDto();
        return ResultDtoFactory.toAckData(riskDataService.queryRiskStaticData1(modelSearchExtendDto));
    }
    //合规预警
    public TableResultResponse<Deabbeat> listByDeabbrat(Pageable pageable ) {
        log.info("合规预警--欠税开始");
        Deabbeat dto=new Deabbeat();
        TableResultResponse<Deabbeat> tableResultResponse = jnnsSaicTestService.queryDeabbeat1(dto, pageable);
        return tableResultResponse;
    }
    //变更项监控表
    public TableResultResponse<AlteritemMointor> queryList(Pageable pageable){
        log.info("变更项监控表开始");
        AlteritemMointor dto =new AlteritemMointor();
        TableResultResponse<AlteritemMointor> tableResultResponse = jnnsAlteritemService.query1(dto, pageable);
        return tableResultResponse;
    }
    //合规预警
    public TableResultResponse<BreakLaw> listByBreak( Pageable pageable) {
        log.info("合规预警--违法开始");
        BreakLaw dto =new BreakLaw();
        TableResultResponse<BreakLaw> tableResultResponse = jnnsSaicTestService.queryBreakLaw1(dto, pageable);
        return tableResultResponse;
    }
    //合规预警
    public TableResultResponse<Owing> listByOwing(Pageable pageable) {
        log.info("合规预警--失信开始");
        Owing dto =new Owing();
        TableResultResponse<Owing> tableResultResponse = jnnsSaicTestService.queryOwing1(dto, pageable);
        return tableResultResponse;
    }
    /**
     * 全量查询核心开户数据
     *
     * @param organCode
     */
    public void getCoreOpenAcctList(String organCode, String kaixhubz) {
        //请求次数加1
        cishu++;
        //根据机构进行查询核心
        RequestBody requestBody = esbService.getRequestBody(kaixhubz, cishu);
        //接收机构代码不为空
        if (StringUtils.isNotEmpty(organCode)) {
            //使用该机构代码进行查询
            requestBody.getTransaction().getBody().getRequest().getBizHeader().setJiaoyijg(organCode);
            requestBody.getTransaction().getBody().getRequest().getBizBody().setJiaoyijg(organCode);
        }
        //转化请求报文
        String jsonStr = JSONObject.toJSONString(requestBody);
        jsonStr = jsonStr.replaceAll("\"body\"", "\"Body\"").replaceAll("\"header\"", "\"Header\"");
        //log.info("————————————————请求核心的报文————————————————："+jsonStr);
        //请求esb
        String response = HttpIntefaceUtils.send(esbUrl, jsonStr);
        //解析返回报文
        List<SyncCompareInfo> responseList = esbService.getResponse(response, kaixhubz);
        esbService.getSaveSyncCompareInfo(responseList);
        zongshu = esbService.getResponsezs(response);
        if (zongshu != 0 && zongshu > 5) {
            if (cishu <= (zongshu / 5)) {
                getCoreOpenAcctList(organCode, kaixhubz);
            }
        }
        cishu = 0;
    }

}