package com.ideatech.ams.risk.highRisk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.highRisk.dao.*;
import com.ideatech.ams.risk.highRisk.entity.*;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.util.HttpRequest;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yangwz
 * @Description
 * @date 2019-10-16 17:00
 */
@Slf4j
@Service
public class ExternalDataServiceImpl implements ExternalDataService {

    @Value("${saic.api.key}")
    private String apiKey;

    @Autowired
    EntityManager entityManager;
    @Autowired
    HighRiskApiDao highRiskApiDao;
    @Autowired
    HttpRequest httpRequest;
    @Autowired
    HighRiskService highRiskService;
    @Autowired
    AnomalyaDao anomalyaDao;
    @Autowired
    CancelledDao cancelledDao;
    @Autowired
    CreditDao creditDao;
    @Autowired
    EnforcedDao enforcedDao;
    @Autowired
    FyNoticeDao fyNoticeDao;
    @Autowired
    JudgmentDao judgmentDao;
    @Autowired
    LllegalDao lllegalDao;
    @Autowired
    PledgeDao pledgeDao;
    @Autowired
    EquityDao equityDao;
    @Autowired
    CustomerPublicDao customerPublicDao;
    @Autowired
    HighRiskDataDao highRiskDataDao;

    public void saveExternalData() {
        //根据黑名单表获得对应的账号
        String highSql = MysqlSQLConstant.getHighRiskAccNo;
        Query query = entityManager.createNativeQuery(highSql);
        List<Object[]> highSqlList = query.getResultList();
        //获得配合规则
        List<HighRiskRule> ruleList = highRiskService.findAllHighRiskRule();
        String externalData = ruleList.get(0).getExternalData();
        //获得外部接口
        String[] externalDataApi;
        if (StringUtils.isNotBlank(externalData)) {
            externalDataApi = externalData.split(",");
            for (int i = 0; i < externalDataApi.length; i++) {
                if (externalDataApi[i].equals("api_001")) {
                    dealData(highSqlList, "api_001", Judgment.class);
                } else if (externalDataApi[i].equals("api_002")) {
                    dealData(highSqlList, "api_002", Enforced.class);
                } else if (externalDataApi[i].equals("api_003")) {
                    dealData(highSqlList, "api_003", Credit.class);
                } else if (externalDataApi[i].equals("api_004")) {
                    dealData(highSqlList, "api_004", FyNotice.class);
                } else if (externalDataApi[i].equals("api_005")) {
                    dealData(highSqlList, "api_005", Equity.class);
                } else if (externalDataApi[i].equals("api_006")) {
                    dealData(highSqlList, "api_006", Pledge.class);
                } else if (externalDataApi[i].equals("api_007")) {

                } else if (externalDataApi[i].equals("api_008")) {
                    dealData(highSqlList, "api_008", Anomalya.class);
                } else if (externalDataApi[i].equals("api_009")) {
                    dealData(highSqlList, "api_009", Lllegal.class);
                } else if (externalDataApi[i].equals("api_010")) {
                    dealData(highSqlList, "api_010", Cancelled.class);
                }
            }
        }
    }


    public void dealData(List<Object[]> highSqlList, String apiNo, Class<?> t) {

        //获得今日高风险数据
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String riskDate = formatter.format(new Date());
        //获得对应的外部接口信息
        HighRiskApi highRiskApi = highRiskApiDao.getHighRiskApiByApiNoAndCorporateFullId(apiNo, SecurityUtils.getCurrentOrgFullId());

        List<HighRiskData> highRiskList = new ArrayList<HighRiskData>();
        for (Object[] obj : highSqlList) {
            String depositorAccNo = obj[7] == null ? "" : obj[7].toString();
            String deName = obj[4] == null ? "" : obj[4].toString();
            if (StringUtils.isNotBlank(deName)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(highRiskApi.getKeyWord(), deName);
                String jsonString = httpRequest.getIdpRequest(highRiskApi.getApiUrl(), params, 20000);
                JSONObject json = JSON.parseObject(jsonString);
                String items = json.getString("data");
                if (StringUtils.isNotBlank(items)) {
                    List<?> itemsList = JSON.parseArray(items, t);
                    if (itemsList.size() > 0) {
                        findSaveExternal(itemsList, apiNo, deName);
                        HighRiskData highRiskData = new HighRiskData ();
                        highRiskData.setCustomerNo(obj[0] == null ? "" : obj[0].toString());
                        highRiskData.setLegalName(obj[1] == null ? "" : obj[1].toString());
                        highRiskData.setLegalIdcardNo(obj[2] == null ? "" : obj[2].toString());
                        highRiskData.setLegalIdcardType(obj[3] == null ? "" : obj[3].toString());
                        highRiskData.setDepositorName(obj[4] == null ? "" : obj[4].toString());
                        highRiskData.setDepositorcardNo(obj[5] == null ? "" : obj[5].toString());
                        highRiskData.setDepositorcardType(obj[6] == null ? "" : obj[6].toString());
                        highRiskData.setAccountNo(obj[7] == null ? "" : obj[7].toString());
                        highRiskData.setRiskDesc(highRiskApi.getApiName());
                        highRiskData.setRiskId(highRiskApi.getApiNo());
                        highRiskData.setRiskDate(riskDate);
                        highRiskData.setCorporateBank( RiskUtil.getOrganizationCode());
                        highRiskData.setStatus("0");
                        highRiskList.add(highRiskData);
                    }
                }
            }
        }
        highRiskDataDao.save(highRiskList);
    }

    public void findSaveExternal(List<?> list, String apiNo, String deName) {
        if (apiNo.equals("api_001")) {
            List<Judgment> itemsList = (List<Judgment>) list;
            List<Judgment> judgmentList = judgmentDao.findAllByKeyName(deName);
            if (judgmentList.size() > 0) {
                judgmentDao.delete(judgmentList);
            }
            for (Judgment data : itemsList) {
                data.setKeyName(deName);

            }
            judgmentDao.save(itemsList);
        } else if (apiNo.equals("api_002")) {
            List<Enforced> itemsList = (List<Enforced>) list;
            List<Enforced> enforcedList = enforcedDao.findAllByKeyName(deName);
            if (enforcedList.size() > 0) {
                enforcedDao.delete(enforcedList);
            }
            for (Enforced data : itemsList) {
                data.setKeyName(deName);
            }
            enforcedDao.save(itemsList);
        } else if (apiNo.equals("api_003")) {
            List<Credit> itemsList = (List<Credit>) list;
            List<Credit> creditList = creditDao.findAllByKeyName(deName);
            if (creditList.size() > 0) {
                creditDao.delete(creditList);
            }
            for (Credit data : itemsList) {
                data.setKeyName(deName);
            }
            creditDao.save(itemsList);
        } else if (apiNo.equals("api_004")) {
            List<FyNotice> itemsList = (List<FyNotice>) list;
            List<FyNotice> fyNoticeList = fyNoticeDao.findAllByKeyName(deName);
            if (fyNoticeList.size() > 0) {
                fyNoticeDao.delete(fyNoticeList);
            }
            for (FyNotice data : itemsList) {
                data.setKeyName(deName);
            }
            fyNoticeDao.save(itemsList);

        } else if (apiNo.equals("api_005")) {
            List<Equity> itemsList = (List<Equity>) list;
            List<Equity> equityList = equityDao.findAllByKeyName(deName);
            if (equityList.size() > 0) {
                equityDao.delete(equityList);
            }
            for (Equity data : itemsList) {
                data.setKeyName(deName);
            }
            equityDao.save(itemsList);
        } else if (apiNo.equals("api_006")) {
            List<Pledge> itemsList = (List<Pledge>) list;
            List<Pledge> pledgeList = pledgeDao.findAllByKeyName(deName);
            if (pledgeList.size() > 0) {
                pledgeDao.delete(pledgeList);
            }
            for (Pledge data : itemsList) {
                data.setKeyName(deName);
            }
            pledgeDao.save(itemsList);
        } else if (apiNo.equals("api_007")) {

        } else if (apiNo.equals("api_008")) {
            List<Anomalya> itemsList = (List<Anomalya>) list;
            List<Anomalya> anomalyaList = anomalyaDao.findAllByKeyName(deName);
            if (anomalyaList.size() > 0) {
                anomalyaDao.delete(anomalyaList);
            }
            for (Anomalya data : itemsList) {
                data.setKeyName(deName);
            }
            anomalyaDao.save(itemsList);
        } else if (apiNo.equals("api_009")) {
            List<Lllegal> itemsList = (List<Lllegal>) list;
            List<Lllegal> lllegalList = lllegalDao.findAllByKeyName(deName);
            if (lllegalList.size() > 0) {
                lllegalDao.delete(lllegalList);
            }
            for (Lllegal data : itemsList) {
                data.setKeyName(deName);
            }
            lllegalDao.save(itemsList);
        } else if (apiNo.equals("api_010")) {
            List<Cancelled> itemsList = (List<Cancelled>) list;
            List<Cancelled> cancelledList = cancelledDao.findAllByKeyName(deName);
            if (cancelledList.size() > 0) {
                cancelledDao.delete(cancelledList);
            }
            for (Cancelled data : itemsList) {
                data.setKeyName(deName);
            }
            cancelledDao.save(itemsList);
        }
    }


}