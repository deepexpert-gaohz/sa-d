package com.ideatech.ams.risk.highRisk.service;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.highRisk.dao.HighRiskApiDao;
import com.ideatech.ams.risk.highRisk.dao.HighRiskDao;
import com.ideatech.ams.risk.highRisk.dao.HighRiskDataDao;
import com.ideatech.ams.risk.highRisk.dao.HighRiskRuleDao;
import com.ideatech.ams.risk.highRisk.dto.HighRiskApiDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskDataDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskListDto;
import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import com.ideatech.ams.risk.highRisk.entity.HighRiskApi;
import com.ideatech.ams.risk.highRisk.entity.HighRiskData;
import com.ideatech.ams.risk.highRisk.entity.HighRiskRule;
import com.ideatech.ams.risk.highRisk.poi.HighRiskPoi;
import com.ideatech.ams.risk.model.dao.ModelDao;
import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.ams.risk.model.poi.ExportExcel;
import com.ideatech.ams.risk.procedure.service.ProcService;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;
import com.ideatech.ams.risk.riskdata.service.RiskApiServiceImpl;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional
public class HighRiskServiceImpl implements HighRiskService {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private HighRiskDao highRiskDao;
    @Autowired
    HighRiskRuleDao highRiskRuleDao;
    @Autowired
    HighRiskDataDao highRiskDataDao;
    @Autowired
    HighRiskApiDao highRiskApiDao;
    @Autowired
    EntityManager em;
    @Autowired
    ProcService procService;
    @Autowired
    RiskApiServiceImpl riskApiService;
    @Autowired
    ModelDao modelDao;
    @Autowired
    AccountsAllDao accountsAllDao;

    @Override
    public IExcelExport generateAnnualCompanyReport() {
        IExcelExport excelExport = new HighRiskPoi();
        List<HighRisk> highRiskList = new ArrayList<HighRisk>();
        HighRisk highRisk = new HighRisk();
        highRiskList.add(highRisk);
        excelExport.setPoiList(highRiskList);
        return excelExport;
    }


    @Override
    public void saveHighRisk(List<HighRisk> list) {
        highRiskDao.save(list);

    }

    /**
     * @param list
     */
    @Override
    @Transactional
    public void updateHighRisk(List<HighRisk> list) {
        String sql = MysqlSQLConstant.getHighRiskByCustomerNo;
        List<String> custList = new ArrayList<>();
        for (HighRisk highRisk : list) {
            highRisk.setCorporateBank(RiskUtil.getOrganizationCode());
            if (StringUtils.isNotBlank(highRisk.getCustomerNo())) {
                sql += " and yd_customer_no =  ?1 ";
            }
            Query nativeQuery = entityManager.createNativeQuery(sql);
            if (StringUtils.isNotBlank(highRisk.getCustomerNo())) {
                nativeQuery.setParameter(1, highRisk.getCustomerNo());
            }
            List<Object[]> resultList = nativeQuery.getResultList();
            if (resultList.size() != 0) {
                custList.add(highRisk.getCustomerNo());
            }
        }
        highRiskDao.deleteByCustomerNoIn(custList);
        highRiskDao.save(list);
    }


    @Override
    public List<HighRisk> findAllHighRisk() {

        return highRiskDao.findHighRiskByCorporateBank(RiskUtil.getOrganizationCode());

    }

    @Override
    public List<HighRiskRule> findAllHighRiskRule() {

        return highRiskRuleDao.findByCorporateBank(RiskUtil.getOrganizationCode());

    }

    @Override
    public List<HighRiskData> findAllHighRiskData() {

        return highRiskDataDao.findAll();

    }

    @Override
    public HighRiskDataDto queryHighRiskData(HighRiskDataDto highRiskDataDto) {
        Pageable pageable = new PageRequest(Math.max(highRiskDataDto.getOffset() - 1, 0), highRiskDataDto.getLimit());
//        String sql = MysqlSQLConstant.getHighRisk;
        String sql = OracleSQLConstant.getHighRisk1;
//        String countSql = MysqlSQLConstant.getHighRiskCountSql;
        String countSql = OracleSQLConstant.getHighRiskCountSql;
        sql += " and t.yd_corporate_bank='" + RiskUtil.getOrganizationCode() + "'";
        //oracle时
        if (StringUtils.isNotBlank(highRiskDataDto.getStatus())) {
            if (highRiskDataDto.getStatus().equals("0")) {
                sql += " and a.yd_account_management = '0' ";
                countSql += "and a.yd_account_management = '0'";
            } else {
                sql += " and a.yd_account_management != '0' ";
                countSql += " and a.yd_account_management != '0'";
            }
        }
        sql += OracleSQLConstant.getHighRisk2;
        countSql += " and t.yd_corporate_bank='" + RiskUtil.getOrganizationCode() + "'";
        return getHighRiskData(highRiskDataDto, pageable, sql, countSql);

    }


    @Override
    public HighRiskListDto queryHighRiskList(HighRiskListDto highRiskListDto) {
        Pageable pageable = new PageRequest(Math.max(highRiskListDto.getOffset() - 1, 0), highRiskListDto.getLimit());
//        String sql = MysqlSQLConstant.getHighRiskList;
        String sql = OracleSQLConstant.getHighRiskList;
        String countSql = MysqlSQLConstant.getHighRiskListCountSql;
        sql += " and t.yd_corporate_bank='" + RiskUtil.getOrganizationCode() + "' and a.yd_account_status = 'normal' ";
        countSql += " and t.yd_corporate_bank='" + RiskUtil.getOrganizationCode() + "' and a.yd_account_status = 'normal' ";
        return getHighRiskList(highRiskListDto, pageable, sql, countSql);

    }

    @Override
    public HighRiskApiDto queryRiskApi(HighRiskApiDto highRiskApiDto) {
        List<HighRiskApi> list = highRiskApiDao.findAll();
        Pageable pageable = new PageRequest(Math.max(highRiskApiDto.getOffset() - 1, 0), highRiskApiDto.getLimit());
        String sql = MysqlSQLConstant.findRiskApi;
        sql += " And yd_corporate_bank='" + RiskUtil.getOrganizationCode() + "'";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = nativeQuery.getResultList();
        List<HighRiskApi> riskApiList = new ArrayList<>();
        for (Object[] o : resultList) {
            HighRiskApi api = new HighRiskApi();
            api.setId(Long.parseLong(o[0].toString()));
            api.setApiNo((o[1] == null) ? "" : o[1].toString());
            api.setApiName((o[2] == null) ? "" : o[2].toString());
            api.setApiUrl((o[3] == null) ? "" : o[3].toString());
            api.setKeyWord((o[4] == null) ? "" : o[4].toString());
            api.setRetData((o[5] == null) ? "" : o[5].toString());
            riskApiList.add(api);
        }
        highRiskApiDto.setTotalRecord((long) list.size());
        highRiskApiDto.setTotalPages((int) Math.ceil(list.size() / highRiskApiDto.getLimit()));
        highRiskApiDto.setList(riskApiList);

        return highRiskApiDto;
    }

    @Override
    public HighRiskListDto findhighRiskskList(String depositorNo) {


        return null;
    }

    public HighRiskListDto getHighRiskList(HighRiskListDto highRiskListDto, Pageable pageable, String sql, String countSql) {
        if (StringUtils.isNotBlank(highRiskListDto.getAccountNo())) {
            sql += " and a.YD_ACCT_NO like ?1";
            countSql += " and a.YD_ACCT_NO like ?1";
        }
        if (StringUtils.isNotBlank(highRiskListDto.getAccountType())) {
            sql += " and a.YD_ACCT_TYPE = ?2";
            countSql += " and a.YD_ACCT_TYPE = ?2";
        }
        if (StringUtils.isNotBlank(highRiskListDto.getBankName())) {
            sql += " and a.yd_bank_name like ?3";
            countSql += " and a.yd_bank_name like ?3";
        }
        if (StringUtils.isNotBlank(highRiskListDto.getStartEndTime())) {
//            sql += " and DATEDIFF( a.yd_acct_create_date, ?4 ) >= 0 and DATEDIFF( a.yd_acct_create_date, ?5 ) <= 0 ";
//            countSql += " and DATEDIFF( a.yd_acct_create_date, ?4 ) >= 0 and DATEDIFF( a.yd_acct_create_date, ?5 ) <= 0 ";

            sql += " and TO_DATE(a.yd_acct_create_date,'yyyy-mm-dd')-TO_DATE(?4,'yyyy-mm-dd') >= 0 and TO_DATE(a.yd_acct_create_date,'yyyy-mm-dd')-TO_DATE(?5,'yyyy-mm-dd') <= 0 ";
            countSql += " and TO_DATE(a.yd_acct_create_date,'yyyy-mm-dd')-TO_DATE(?4,'yyyy-mm-dd') >= 0 and TO_DATE(a.yd_acct_create_date,'yyyy-mm-dd')-TO_DATE(?5,'yyyy-mm-dd') <= 0 ";
        }
        if (StringUtils.isNotBlank(highRiskListDto.getDepositorNo())) {
            sql += " and t.yd_depositorcard_no  = ?6";
            countSql += " and t.yd_depositorcard_no  = ?6";
        }
        if (StringUtils.isNotBlank(highRiskListDto.getStatus())) {
            if (highRiskListDto.getStatus().equals("0")) {
                sql += " and a.yd_account_management = '0' ";
                countSql += " and a.yd_account_management = '0'";
            } else {
                sql += " and a.yd_account_management != '0' ";
                countSql += " and a.yd_account_management != '0'";
            }
        }
        if (StringUtils.isNotBlank(highRiskListDto.getCustomerNo())) {
            sql += " and t.yd_customer_no  = ?7";
            countSql += " and t.yd_customer_no  = ?7";
        }
//        sql += " GROUP BY t.yd_account_no ORDER BY a.yd_acct_create_date desc ) b";
        sql += "AND T .yd_account_no IN (SELECT yd_account_no FROM yd_risk_high_info\n" +
                " GROUP BY yd_account_no)ORDER BY\n" +
                "\tA .yd_acct_create_date DESC";
        countSql += " GROUP BY t.yd_account_no) b";
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(highRiskListDto.getAccountNo())) {
            nativeQuery.setParameter(1, "%" + highRiskListDto.getAccountNo() + "%");
            nativeQueryCount.setParameter(1, "%" + highRiskListDto.getAccountNo() + "%");
        }
        if (StringUtils.isNotBlank(highRiskListDto.getAccountType())) {
            nativeQuery.setParameter(2, highRiskListDto.getAccountType());
            nativeQueryCount.setParameter(2, highRiskListDto.getAccountType());
        }
        if (StringUtils.isNotBlank(highRiskListDto.getBankName())) {
            nativeQuery.setParameter(3, "%" + highRiskListDto.getBankName() + "%");
            nativeQueryCount.setParameter(3, "%" + highRiskListDto.getBankName() + "%");
        }
        if (StringUtils.isNotBlank(highRiskListDto.getStartEndTime())) {
            nativeQuery.setParameter(4, highRiskListDto.getStartEndTime().split("~")[0].trim());
            nativeQuery.setParameter(5, highRiskListDto.getStartEndTime().split("~")[1].trim());
            nativeQueryCount.setParameter(4, highRiskListDto.getStartEndTime().split("~")[0].trim());
            nativeQueryCount.setParameter(5, highRiskListDto.getStartEndTime().split("~")[1].trim());
        }
        if (StringUtils.isNotBlank(highRiskListDto.getDepositorNo())) {
            nativeQuery.setParameter(6, highRiskListDto.getDepositorNo());
            nativeQueryCount.setParameter(6, highRiskListDto.getDepositorNo());
        }
        if (StringUtils.isNotBlank(highRiskListDto.getCustomerNo())) {
            nativeQuery.setParameter(7, highRiskListDto.getCustomerNo());
            nativeQueryCount.setParameter(7, highRiskListDto.getCustomerNo());
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.get(0)));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        List<HighRiskListDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            HighRiskListDto high = new HighRiskListDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            high.setOrdNum(orm);
            high.setId((obj[1] == null) ? 0L : Long.valueOf(String.valueOf(obj[1])));
            high.setCustomerNo((obj[2] == null) ? "" : obj[2].toString());
            high.setDepositorName((obj[3] == null) ? "" : obj[3].toString());
            high.setStatus((obj[4] == null) ? "" : obj[4].toString());
            high.setAccountNo((obj[5] == null) ? "" : obj[5].toString());
            high.setAccountType((obj[6] == null) ? "" : obj[6].toString());
            high.setBankName((obj[7] == null) ? "" : obj[7].toString());
            String createDate = (obj[8] == null) ? "" : obj[8].toString();
            if (createDate != "") {
                try {
                    Date currentDate = formatter.parse(createDate);
                    createDate = formatter1.format(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            high.setAcctCreateDate(createDate);
            high.setDepositorNo((obj[9] == null) ? "" : obj[9].toString());
            high.setRiskDesc((obj[10] == null) ? "" : obj[10].toString());
            list.add(high);
        }
        highRiskListDto.setList(list);
        highRiskListDto.setTotalPages((int) Math.ceil(count.intValue() / highRiskListDto.getLimit()));
        highRiskListDto.setTotalRecord(count);
        return highRiskListDto;

    }

    public HighRiskDataDto getHighRiskData(HighRiskDataDto highRiskDataDto, Pageable pageable, String sql, String countSql) {
        if (StringUtils.isNotBlank(highRiskDataDto.getDepositorName())) {
            //mysql
//            sql += " and t.YD_DEPOSITOR_NAME like ?1";
            //oracle
            sql += " and dc.YD_DEPOSITOR_NAME like ?1";
            countSql += " and t.YD_DEPOSITOR_NAME like ?1";
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getDepositorcardNo())) {
            //mysql
//            sql += " and t.yd_depositorcard_no like ?2";
            //oracle
            sql += " and rc.yd_company_certificate_no like ?2";
            countSql += " and t.yd_depositorcard_no like ?2";
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getLegalName())) {
            //mysql
//             sql += " and t.yd_legal_name like ?3";
            //oracle
            sql += " and dc.yd_legal_name like ?3";
            countSql += " and t.yd_legal_name like ?3";
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getLegalIdcardNo())) {
            //mysql
//            sql += " and t.yd_legal_idcard_no like ?4";
            //oracle
            sql += " and dc.yd_legal_idcard_no like ?4";
            countSql += " and t.yd_legal_idcard_no like ?4";
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getStartEndTime())) {
//            sql += " and DATEDIFF( t.yd_risk_date, ?5 ) >= 0 and DATEDIFF( t.yd_risk_date, ?6 ) <= 0 ";
//            countSql += " and DATEDIFF( t.yd_risk_date, ?5 ) >= 0 and DATEDIFF( t.yd_risk_date, ?6 ) <= 0 ";
            sql += " and TO_DATE(t.yd_risk_date,'yyyy-mm-dd')-TO_DATE(?5,'yyyy-mm-dd') >= 0 and TO_DATE(t.yd_risk_date,'yyyy-mm-dd')-TO_DATE(?6,'yyyy-mm-dd') <= 0 ";
            countSql += " and TO_DATE(t.yd_risk_date,'yyyy-mm-dd')-TO_DATE(?5,'yyyy-mm-dd') >= 0 and TO_DATE(t.yd_risk_date,'yyyy-mm-dd')-TO_DATE(?6,'yyyy-mm-dd') <= 0 ";

        }
        //oracle写在里面
//        if (StringUtils.isNotBlank(highRiskDataDto.getStatus())) {
//            if (highRiskDataDto.getStatus().equals("0")) {
//                sql += " and a.yd_account_management = '0' ";
//                countSql += "and a.yd_account_management = '0'";
//            } else {
//                sql += " and a.yd_account_management != '0' ";
//                countSql += " and a.yd_account_management != '0'";
//            }
//        }
//        sql += "  ) a";
//        countSql += "  ) a";
        //oracle时注释掉
//        sql += " GROUP BY t.yd_customer_no ORDER BY t.yd_risk_date desc) a";
        countSql += " GROUP BY t.yd_customer_no ORDER BY t.yd_risk_date desc) a";
        if (StringUtils.isNotBlank(highRiskDataDto.getRiskDesc())) {
            //mysql
//            sql += " where a.yd_risk_desc like ?7 ";
            //oracle
            sql += " and t.yd_risk_desc like ?7 ";
            countSql += " where a.yd_risk_desc like ?7 ";
        }
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(highRiskDataDto.getDepositorName())) {
            nativeQuery.setParameter(1, "%" + highRiskDataDto.getDepositorName() + "%");
            nativeQueryCount.setParameter(1, "%" + highRiskDataDto.getDepositorName() + "%");
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getDepositorcardNo())) {
            nativeQuery.setParameter(2, "%" + highRiskDataDto.getDepositorcardNo() + "%");
            nativeQueryCount.setParameter(2, "%" + highRiskDataDto.getDepositorcardNo() + "%");
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getLegalName())) {
            nativeQuery.setParameter(3, "%" + highRiskDataDto.getLegalName() + "%");
            nativeQueryCount.setParameter(3, "%" + highRiskDataDto.getLegalName() + "%");
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getLegalIdcardNo())) {
            nativeQuery.setParameter(4, "%" + highRiskDataDto.getLegalIdcardNo() + "%");
            nativeQueryCount.setParameter(4, "%" + highRiskDataDto.getLegalIdcardNo() + "%");
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getStartEndTime())) {
            nativeQuery.setParameter(5, highRiskDataDto.getStartEndTime().split("~")[0].trim());
            nativeQuery.setParameter(6, highRiskDataDto.getStartEndTime().split("~")[1].trim());
            nativeQueryCount.setParameter(5, highRiskDataDto.getStartEndTime().split("~")[0].trim());
            nativeQueryCount.setParameter(6, highRiskDataDto.getStartEndTime().split("~")[1].trim());
        }
        if (StringUtils.isNotBlank(highRiskDataDto.getRiskDesc())) {
            nativeQuery.setParameter(7, "%" + highRiskDataDto.getRiskDesc() + "%");
            nativeQueryCount.setParameter(7, "%" + highRiskDataDto.getRiskDesc() + "%");
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.get(0)));
        }
        List<HighRiskDataDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            HighRiskDataDto e = new HighRiskDataDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            e.setOrdNum(orm);
            e.setCustomerNo((obj[1] == null) ? "" : obj[1].toString());
            e.setDepositorName((obj[2] == null) ? "" : obj[2].toString());
            e.setLegalName((obj[3] == null) ? "" : obj[3].toString());
            e.setDepositorcardType((obj[4] == null) ? "" : obj[4].toString());
            e.setDepositorcardNo((obj[5] == null) ? "" : obj[5].toString());
            e.setLegalIdcardType((obj[6] == null) ? "" : obj[6].toString());
            e.setLegalIdcardNo((obj[7] == null) ? "" : obj[7].toString());
            String riskdes = (obj[8] == null) ? "" : obj[8].toString();
            riskdes = riskdes.substring(0, riskdes.length() - 1);
            String des[] = riskdes.split("-,");
            //创建一个集合
            List<String> list1 = new ArrayList();
            //遍历数组往集合里存元素
            for (int i = 0; i < des.length; i++) {
                //如果集合里面没有相同的元素才往里存
                if (!list1.contains(des[i])) {
                    list1.add(des[i]);
                }
            }
            Map<String, String> ma = new HashMap<>();
            for (String str : des) {
                Model model = modelDao.findByNameAndCorporateBank(str, RiskUtil.getOrganizationCode());
                if (model != null) {
                    ma.put(model.getModelId(), model.getName());
                }
            }
            String newDes = "";
            int num = 1;
            if (ma.get("RISK_1001") != null) {
                newDes = newDes + num + "." + ma.get("RISK_1001");
                num++;
            }
            if (ma.get("RISK_1002") != null) {
                newDes = newDes + num + "." + ma.get("RISK_1002");
                num++;
            }
            if (ma.get("RISK_2001") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2001");
                num++;
            }
            if (ma.get("RISK_2002") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2002");
                num++;
            }
            if (ma.get("RISK_2005") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2005");
                num++;
            }
            if (ma.get("RISK_2006") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2006");
                num++;
            }
            if (ma.get("RISK_2007") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2007");
                num++;
            }
            if (ma.get("RISK_2008") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2008");
                num++;
            }
            if (ma.get("RISK_2009") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2009");
                num++;
            }
            if (ma.get("RISK_2010") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2010");
                num++;
            }
            if (ma.get("RISK_2011") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2011");
                num++;
            }
            if (ma.get("RISK_2012") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2012");
                num++;
            }
            if (ma.get("RISK_2013") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2013");
                num++;
            }
            if (ma.get("RISK_2014") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2014");
                num++;
            }
            if (ma.get("RISK_2015") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2015");
                num++;
            }
            if (ma.get("RISK_2016") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2016");
                num++;
            }
            if (ma.get("RISK_2017") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2017");
                num++;
            }
            if (ma.get("RISK_2019") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2019");
                num++;
            }
            if (ma.get("RISK_2020") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2020");
                num++;
            }
            if (ma.get("RISK_2021") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2021");
                num++;
            }
            if (ma.get("RISK_2022") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2022");
                num++;
            }
            if (ma.get("RISK_2023") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2023");
                num++;
            }
            if (ma.get("RISK_2024") != null) {
                newDes = newDes + num + "." + ma.get("RISK_2024");
                num++;
            }
            e.setRiskDesc(newDes);
            e.setRiskDate((obj[9] == null) ? "" : obj[9].toString());
//            e.setStatus((obj[10] == null) ? "" : obj[10].toString());
            e.setId((obj[11] == null) ? 0L : Long.valueOf(String.valueOf(obj[11])));
            String str = (obj[12] == null) ? "" : obj[12].toString();
            String sta[] = str.split(",");
            for (int i = 0; i < sta.length; i++) {
                if (sta[i].equals("0")) {
                    e.setStatus(sta[i]);
                }
            }
            if (e.getStatus() == null) {
                for (int i = 0; i < sta.length; i++) {
                    if (sta[i].equals("2") || sta[i].equals("3")) {
                        e.setStatus(sta[i]);
                        break;
                    } else {
                        e.setStatus(sta[i]);
                    }
                }
            }
            Boolean lean = true;
            if (StringUtils.isNotBlank(highRiskDataDto.getStatus())) {
                if (!highRiskDataDto.getStatus().equals("0")) {
                    List<HighRiskData> chooList = highRiskDataDao.findHighRiskDataByCustomerNoAndCorporateBank(e.getCustomerNo(), RiskUtil.getOrganizationCode());
                    if (chooList.size() > 1) {
                        for (HighRiskData data : chooList) {
                            if (data.getStatus().equals("0")) {
                                lean = false;
                            }
                        }
                    }
                }
            }
            if (lean) {
                list.add(e);
            }
        }
        highRiskDataDto.setList(list);
        highRiskDataDto.setTotalPages((int) Math.ceil(count.intValue() / highRiskDataDto.getLimit()));
        highRiskDataDto.setTotalRecord(count);
        return highRiskDataDto;

    }

    @Override
    public void saveCon(HighRiskRule highRiskRule, String str) {
        HighRiskRule ruleConf = highRiskRuleDao.findHighRiskByCorporateBank(RiskUtil.getOrganizationCode());
        if (ruleConf == null) {
            highRiskRule.setRunState("0");
            highRiskRule.setRuleId("");
            if (highRiskRule.getExternalData() == null) {
                highRiskRule.setExternalData("");
            }
            if (highRiskRule.getRuleModel() == null) {
                highRiskRule.setRuleModel("");
            }
            highRiskRule.setCorporateBank(RiskUtil.getOrganizationCode());
            highRiskRuleDao.save(highRiskRule);

        } else {
            if (str.equals("modelRule")) {
                ruleConf.setRuleModel(highRiskRule.getRuleModel());
            }
            if (str.equals("runState")) {

                ruleConf.setRunState(highRiskRule.getRunState());
            }
            if (str.equals("extData")) {

                ruleConf.setExternalData(highRiskRule.getExternalData());
            }
            highRiskRuleDao.save(ruleConf);
        }


    }

    @Override
    @Transactional
    public void resetData() {
        highRiskDao.deleteByCorporateBank(RiskUtil.getOrganizationCode());
        HighRiskRule ruleConf = highRiskRuleDao.findHighRiskByCorporateBank(RiskUtil.getOrganizationCode());
        if (ruleConf != null) {
            ruleConf.setRuleId("");
            ruleConf.setRuleModel("");
            ruleConf.setExternalData("");
            ruleConf.setRunState("0");
        }
    }

    @Override
    public void disdisHighRisk(String id, String accountNo, String handleType, String choo, String customerNo) {
        if (StringUtils.isNotBlank(choo)) {
            if (choo.equals("0")) {
                HighRiskData highRiskData = highRiskDataDao.findOne(Long.valueOf(id));
                highRiskData.setStatus(handleType);
                highRiskDataDao.save(highRiskData);
            } else {
                List<HighRiskData> list = highRiskDataDao.findHighRiskDataByDepositorcardNoAndCorporateBank(customerNo, RiskUtil.getOrganizationCode());
                for (HighRiskData data : list) {
                    HighRiskData highRiskData = highRiskDataDao.findOne(Long.valueOf(data.getId()));
                    highRiskData.setStatus(handleType);
                    highRiskDataDao.save(highRiskData);
                }
            }
        }

    }

    @Override
    public List<AccountPublicInfo> findAcctType() {
        String sql = "SELECT\n" +
                "\tyd_acct_type\n" +
                "FROM\n" +
                "\tyd_accounts_all a\n" +
                "GROUP BY yd_acct_type";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<AccountPublicInfo> resultList = nativeQuery.getResultList();

        return resultList;
    }

    @Override
    public List<HighRiskApi> findDataApi() {
        return highRiskApiDao.findByCorporateBank(RiskUtil.getOrganizationCode());
    }

    @Override
    public void exportRiskList(HighRiskListDto highRiskListDto, String rootPath, String StrDt) throws Exception {

        List<String> headerList = new ArrayList<String>();
        headerList.add("账号");
        headerList.add("企业名称");
        headerList.add("账号类型");
        headerList.add("开户行名称");
        headerList.add("开户时期");
        List<HighRiskListDto> list = highRiskListDto.getList();
        List<Object[]> list1 = new ArrayList<Object[]>();
        for (HighRiskListDto data : list) {
            Object obj[] = new Object[headerList.size()];
            obj[0] = data.getAccountNo();
            obj[1] = data.getDepositorName();
            obj[2] = data.getAccountType();
            obj[3] = data.getBankName();
            obj[4] = data.getAcctCreateDate();
            list1.add(obj);
        }
        String name = "高风险数据详情" + StrDt;
        ExportExcel exportExcel = new ExportExcel("高风险数据详情", headerList);
        exportExcel.getExcel(list1, rootPath, name);// 将数据生成Excel

    }

    @Override
    public boolean addRiskApi(HighRiskApi highRiskApi) {
        HighRiskApi riskApi = highRiskApiDao.getHighRiskApiByApiNoAndCorporateBank(highRiskApi.getApiNo(), RiskUtil.getOrganizationCode());

        if (riskApi == null) {
            highRiskApi.setCorporateBank(RiskUtil.getOrganizationCode());
            highRiskApiDao.save(highRiskApi);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResultDto saveRiskApi(HighRiskApi highRiskApi) {
        ResultDto resultDto = new ResultDto();
        HighRiskApi riskApi = highRiskApiDao.findOne(highRiskApi.getId());
        List<HighRiskApi> list = highRiskApiDao.findByCorporateBank(RiskUtil.getOrganizationCode());
        for (HighRiskApi api : list) {
            if (api.getApiNo().equals(highRiskApi.getApiNo())) {
                if (riskApi.getApiNo().equals(highRiskApi.getApiNo())) {
                    riskApi.setApiNo(highRiskApi.getApiNo());
                    riskApi.setApiName(highRiskApi.getApiName());
                    riskApi.setApiUrl(highRiskApi.getApiUrl());
                    riskApi.setKeyWord(highRiskApi.getKeyWord());
                    riskApi.setRetData(highRiskApi.getRetData());
                    highRiskApiDao.save(riskApi);
                    resultDto.setCode("ACK");
                    resultDto.setMessage("修改成功!");
                    return resultDto;
                } else {
                    resultDto.setCode("NACK");
                    resultDto.setMessage("已存在相同接口编号!");
                    return resultDto;
                }
            }
        }
        riskApi.setApiNo(highRiskApi.getApiNo());
        riskApi.setApiName(highRiskApi.getApiName());
        riskApi.setApiUrl(highRiskApi.getApiUrl());
        riskApi.setKeyWord(highRiskApi.getKeyWord());
        riskApi.setRetData(highRiskApi.getRetData());
        highRiskApiDao.save(riskApi);
        resultDto.setCode("ACK");
        resultDto.setMessage("修改成功!");
        return resultDto;
    }

    @Override
    public HighRiskApi findApiById(Long id) {
        return highRiskApiDao.findOne(id);
    }

    @Override
    public void delRiskApiById(Long id) {
        highRiskApiDao.delete(id);
    }


    @Override
    public void syncHighRiskData(String riskId) {
        //获得匹配的模型
        String riskStr = "'";
        String riskIdList[] = riskId.split(",");
        for (int i = 0; i < riskIdList.length; i++) {
            riskStr += riskIdList[i];
            if (i < riskIdList.length - 1) {
                riskStr = riskStr + "','";
            } else {
                riskStr = riskStr + "'";
            }
        }
        //日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(new Date());
        String riskDate = formatter1.format(new Date());
        //保存准备和行内数据匹配的风险数据(除模型以外)
        List<RiskRecordInfo> list = new ArrayList<RiskRecordInfo>();
        //保存准备和模型匹配的风险数据
        List<RiskRecordInfo> modelList = new ArrayList<RiskRecordInfo>();
        //获得当前机构code
        String code = RiskUtil.getOrganizationCode();
        //实时跑批开户高风险数据
        List<RiskRecordInfo> risk2001 = procService.risk2001Application(currentDate, true);
        List<RiskRecordInfo> risk2002 = procService.risk2002Application(currentDate, true);
        List<RiskRecordInfo> risk2005 = procService.risk2005Application(currentDate, true);
        List<RiskRecordInfo> risk2006 = procService.risk2006Application(currentDate, true);
        List<RiskRecordInfo> risk2007 = riskApiService.generateRisk2007Data(currentDate, true);
        List<RiskRecordInfo> risk2008 = procService.risk2008Appliaction(currentDate, true);
        list.addAll(exList(risk2001));
        list.addAll(risk2002);
        list.addAll(risk2005);
        list.addAll(risk2006);
        list.addAll(exList(risk2007));
        list.addAll(risk2008);
        //分别保存跑批数据
        for (int i = 0; i < riskIdList.length; i++) {
            if (riskIdList[i].equals("RISK_2001")) {
                modelList.addAll(exList(risk2001));
            } else if (riskIdList[i].equals("RISK_2002")) {
                modelList.addAll(risk2002);
            } else if (riskIdList[i].equals("RISK_2005")) {
                modelList.addAll(risk2005);
            } else if (riskIdList[i].equals("RISK_2006")) {
                modelList.addAll(risk2006);
            } else if (riskIdList[i].equals("RISK_2007")) {
                modelList.addAll(exList(risk2007));
            } else if (riskIdList[i].equals("RISK_2008")) {
                modelList.addAll(risk2008);
            }
        }
        list.removeAll(modelList);
        //获得行内数据,根据机构code
        String highSql = MysqlSQLConstant.getHighRiskAccNo;
        highSql += " and c.yd_corporate_bank = '" + code + "'";
        Query query = entityManager.createNativeQuery(highSql);
        List<Object[]> highSqlList = query.getResultList();
        //把数据客户号放在map便于查找
        Map<String, Object[]> ma = new HashMap<String, Object[]>();
        for (Object[] obj : highSqlList) {
            if (obj[0] != null) {
                ma.put(obj[0].toString(), obj);
            }
        }
        //存放新增的高风险数据
        List<HighRiskData> allHighRisk = new ArrayList<HighRiskData>();
        //模型数据
        for (RiskRecordInfo riskRecordInfo : modelList) {
            String modelSql = MysqlSQLConstant.getModelInfo;
            modelSql += " AND c.YD_CUSTOMER_NO = '" + riskRecordInfo.getCustomerId() + "' AND a.yd_acct_no ='" + riskRecordInfo.getAccountNo() + "'";
            Query modelQu = entityManager.createNativeQuery(modelSql);
            List<Object[]> modelSqlList = modelQu.getResultList();
            if (modelSqlList.size() != 0) {
                HighRiskData highRiskData = new HighRiskData();
                highRiskData.setCustomerNo(modelSqlList.get(0)[0] == null ? "" : modelSqlList.get(0)[0].toString());
                highRiskData.setLegalName(modelSqlList.get(0)[1] == null ? "" : modelSqlList.get(0)[1].toString());
                highRiskData.setLegalIdcardNo(modelSqlList.get(0)[2] == null ? "" : modelSqlList.get(0)[2].toString());
                highRiskData.setLegalIdcardType(modelSqlList.get(0)[3] == null ? "" : modelSqlList.get(0)[3].toString());
                highRiskData.setDepositorName(modelSqlList.get(0)[4] == null ? "" : modelSqlList.get(0)[4].toString());
                highRiskData.setDepositorcardNo(modelSqlList.get(0)[5] == null ? "" : modelSqlList.get(0)[5].toString());
                highRiskData.setDepositorcardType(modelSqlList.get(0)[6] == null ? "" : modelSqlList.get(0)[6].toString());
                highRiskData.setRiskDate(riskDate);
                highRiskData.setRiskDesc(riskRecordInfo.getRiskDesc());
                highRiskData.setRiskId(riskRecordInfo.getRiskId());
                highRiskData.setAccountNo(riskRecordInfo.getAccountNo());
                highRiskData.setCorporateBank(RiskUtil.getOrganizationCode());
                String status = modelSqlList.get(0)[7] == null ? "" : modelSqlList.get(0)[7].toString();
                if(status.equals("")){
                    highRiskData.setStatus("0");
                }else{
                    highRiskData.setStatus(status);
                }
                allHighRisk.add(highRiskData);
            }
        }
        //和行内数据匹配,根据客户号
        if (ma != null) {
            for (RiskRecordInfo riskRecordInfo : list) {
                String cusId = riskRecordInfo.getCustomerId();
                RiskApiServiceImpl.getAllHighDateToList(riskDate, ma, allHighRisk, riskRecordInfo, cusId);
            }
        }
        //获得所有的模型编号
        List<Model> models = modelDao.findByCorporateBank(RiskUtil.getOrganizationCode());
        List<String> modelIds = new ArrayList<>();
        //把模型id放在liat中
        for (Model model : models) {
            modelIds.add(model.getModelId());
        }
        List<String> selMoselIds = Arrays.asList(riskIdList);
        //获得没有选择的模型id
        for (String modelId : selMoselIds) {
            if (modelIds.contains(modelId)) {
                modelIds.remove(modelId);
            }
        }
        //匹配交易数据
        //获得交易高风险数据(模型除外)
        List<HighRiskData> highRiskList = new ArrayList<>();
        for (int i = 0; i < modelIds.size(); i++) {
//            String sql = MysqlSQLConstant.getHighTradeInfo;
            String sql = OracleSQLConstant.getHighTradeInfo;
            sql += " and c.yd_corporate_bank = '" + code + "'";
            //mysql
//            sql += "  and t.yd_risk_id = '" + modelIds.get(i) + "' AND t.yd_risk_date = DATE_SUB('" + riskDate + "',INTERVAL 1 DAY)";
            //oracle
            sql += "  and t.yd_risk_id = '" + modelIds.get(i) + "' AND t.yd_risk_date = TO_Date('2019-12-10', 'yyyy-mm-dd')-1";
            Query nativeQuery = entityManager.createNativeQuery(sql);
            List<Object[]> tradeList = nativeQuery.getResultList();
            allHighRisk.addAll(exTradeData(tradeList, highRiskList, riskDate));
            highRiskList.clear();
        }
        //获得交易高风险数据
        for (int i = 0; i < riskIdList.length; i++) {
//            String tradeSql = MysqlSQLConstant.getHighTradeModelInfo;
            String tradeSql = OracleSQLConstant.getHighTradeModelInfo;
//            tradeSql += " and t.yd_corporate_bank = '" + code + "' and t.yd_risk_id = '" + riskIdList[i] + "' \n" +
//                    "AND t.yd_risk_date = DATE_SUB('" + riskDate + "',INTERVAL 1 DAY)";
            tradeSql += " and t.yd_corporate_bank = '" + code + "' and t.yd_risk_id = '" + riskIdList[i] + "' \n" +
                    "AND t.yd_risk_date = TO_Date('2019-12-10', 'yyyy-mm-dd')-1";
            Query tradeQuery = entityManager.createNativeQuery(tradeSql);
            List<Object[]> tradeList = tradeQuery.getResultList();
            allHighRisk.addAll(exTradeData(tradeList, highRiskList, riskDate));
            highRiskList.clear();
        }
        highRiskDataDao.deleteAllByCorporateBank(RiskUtil.getOrganizationCode());
        highRiskDataDao.save(allHighRisk);

    }

    public List<HighRiskData> exTradeData(List<Object[]> tradeList, List<HighRiskData> highRiskList, String riskDate) {
        for (Object[] obj : tradeList) {
            HighRiskData highRiskData = new HighRiskData();
            highRiskData.setCustomerNo(obj[0] == null ? "" : obj[0].toString());
            highRiskData.setLegalName(obj[1] == null ? "" : obj[1].toString());
            highRiskData.setLegalIdcardNo(obj[2] == null ? "" : obj[2].toString());
            highRiskData.setLegalIdcardType(obj[3] == null ? "" : obj[3].toString());
            highRiskData.setDepositorName(obj[4] == null ? "" : obj[4].toString());
            highRiskData.setDepositorcardNo(obj[5] == null ? "" : obj[5].toString());
            highRiskData.setDepositorcardType(obj[6] == null ? "" : obj[6].toString());
            highRiskData.setRiskDate(riskDate);
            highRiskData.setRiskDesc(obj[8] == null ? "" : obj[8].toString());
            highRiskData.setRiskId(obj[9] == null ? "" : obj[9].toString());
            highRiskData.setAccountNo(obj[10] == null ? "" : obj[10].toString());
            highRiskData.setCorporateBank(RiskUtil.getOrganizationCode());
            highRiskData.setStatus("0");
            highRiskList.add(highRiskData);
        }
        return exHighRiskList(highRiskList);
    }

    /**
     * @return
     * @Description 处理account字段空数据
     * @author yangwz
     * @date 2019/11/29 20:21
     * @params risk:处理的数据
     */
    public List<RiskRecordInfo> exList(List<RiskRecordInfo> risk) {
        Map<String, RiskRecordInfo> riskMap = new HashMap<String, RiskRecordInfo>();
        for (RiskRecordInfo riskRecordInfo : risk) {
            if (riskRecordInfo != null) {
                if (riskRecordInfo.getCustomerId() != null && riskMap.get(riskRecordInfo.getCustomerId()) == null) {
                    riskMap.put(riskRecordInfo.getCustomerId(), riskRecordInfo);
                }
            }
        }
        risk.clear();
        for (Map.Entry<String, RiskRecordInfo> a : riskMap.entrySet()) {
            RiskRecordInfo riskRecordInfo = a.getValue();
            List<AccountsAll> accList = accountsAllDao.findByCustomerNoAndAccountStatus(riskRecordInfo.getCustomerId(), AccountStatus.normal);
            for (AccountsAll account : accList) {
                RiskRecordInfoDto riskRecordInfoDto = ConverterService.convert(riskRecordInfo, RiskRecordInfoDto.class);
                riskRecordInfoDto.setAccountNo(account.getAcctNo());
                risk.add(ConverterService.convert(riskRecordInfoDto, RiskRecordInfo.class));
            }
        }
        return risk;
    }

    /**
     * @return
     * @Description 处理高风险交易数据账号
     * @author yangwz
     * @date 2019/12/2 12:38
     * @params risk:处理的数据
     */
    public List<HighRiskData> exHighRiskList(List<HighRiskData> risk) {
        Map<String, HighRiskData> riskMap = new HashMap<String, HighRiskData>();
        for (HighRiskData highRiskData : risk) {
            if (highRiskData.getCustomerNo() != null && riskMap.get(highRiskData.getCustomerNo()) == null) {
                riskMap.put(highRiskData.getCustomerNo(), highRiskData);
            }
        }
        risk.clear();
        for (Map.Entry<String, HighRiskData> a : riskMap.entrySet()) {
            HighRiskData highRiskData = a.getValue();
            List<AccountsAll> accList = accountsAllDao.findByCustomerNoAndAccountStatus(highRiskData.getCustomerNo(), AccountStatus.normal);
            for (AccountsAll account : accList) {
                HighRiskDataDto highRiskDataDto = ConverterService.convert(highRiskData, HighRiskDataDto.class);
                highRiskDataDto.setAccountNo(account.getAcctNo());
                risk.add(ConverterService.convert(highRiskDataDto, HighRiskData.class));
            }
        }
        return risk;
    }
    @Override
    public HighRiskData findById(Long id) {
        return highRiskDataDao.findById(id);
    }
}
