package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.modelKind.dao.RiskTypeDao;
import com.ideatech.ams.risk.modelKind.entity.RiskType;
import com.ideatech.ams.risk.procedure.dao.RiskTradeInfoDao;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


@Service
public class RiskDataServiceToExportImpl implements RiskDataServiceToExp {

    @PersistenceContext
    private EntityManager em; //注入EntityManager
    @Autowired
    RiskTradeInfoDao riskTradeInfoDao;
    @Autowired
    RiskTypeDao riskTypeDao;

    public List<List<Object>> mapTOlist(List list) {
        List lis = new ArrayList();
        for (int j = 0; j < list.size(); j++) {
            List li = new ArrayList();
            Object[] object = (Object[]) list.get(j);
            for (int i = 0; i < object.length; i++) {
                li.add(object[i]);
            }
            lis.add(li);
        }
        return lis;
    }


    @Override
    public RiskDetailsSearchDto findRiskListDetails(RiskDetailsSearchDto riskDetailsSearchDto) {
        Pageable pageable = new PageRequest(Math.max(riskDetailsSearchDto.getOffset() - 1, 0), riskDetailsSearchDto.getLimit());
        String sql = this.createQuerySQL(riskDetailsSearchDto);
        Query query = em.createNativeQuery(sql);
        Query queryCount = em.createNativeQuery(sql);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = query.getResultList();
//        List<BigDecimal> resultList1 = queryCount.getResultList();
//        BigDecimal big= resultList1.get(0);
//        Long count = big.longValue();
        List<String[]> resultListCount = queryCount.getResultList();
        Long count;
        if (resultListCount.size() == 0) {
            count = 0L;
        } else {
            count = (long) resultListCount.size();
        }
        riskDetailsSearchDto.setList(resultList);
        riskDetailsSearchDto.setTotalRecord(count);
        riskDetailsSearchDto.setTotalPages((int) Math.ceil(count.intValue() / riskDetailsSearchDto.getLimit()));
        return riskDetailsSearchDto;
    }

    @Override
    public String createQuerySQL(RiskDetailsSearchDto riskDetailsSearchDto) {
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        String sql = "";
        if (riskDetailsSearchDto.getModelId().equals("RISK_4001")) {
            String commonSql = OracleSQLConstant.RISK4000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_4001') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_4002")) {
            String commonSql = OracleSQLConstant.RISK4000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  a.yd_acct_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_4002') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3001")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3001') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3002")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3002') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3003")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3003') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3004")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3004') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3005")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3005') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2001")) {
            String commonSql = OracleSQLConstant.RISK2001SQL;
            //String commonSql = MysqlSQLConstant.RISK2001SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2001') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2002")) {
            String commonSql = OracleSQLConstant.RISK2002SQL;
            //String commonSql = MysqlSQLConstant.RISK2002SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            String riskPointVal = null;
            try {
                riskPointVal = URLDecoder.decode(riskDetailsSearchDto.getRiskPoint(), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskPointVal + "' and t.yd_risk_id='RISK_2002') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2003")) {
            String commonSql = OracleSQLConstant.RISK2003SQL;
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2003') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2004")) {
            //暂时不做
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2005")) {
            String commonSql = OracleSQLConstant.RISK2005SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2005') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2006")) {
            String commonSql = OracleSQLConstant.RISK2006SQL;
            //String commonSql = MysqlSQLConstant.RISK2006SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2006') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2007")) {
            String commonSql = OracleSQLConstant.RISK2007SQL;
            //String commonSql = MysqlSQLConstant.RISK2007SQL;
//            commonSql +=" and t1.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t1.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_2007') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2008")) {
            String commonSql = OracleSQLConstant.RISK2008SQL;
            //String commonSql = MysqlSQLConstant.RISK2008SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2008') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1001")) {
            String commonSql = OracleSQLConstant.RISK1001And1002SQL;
            //String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
//            commonSql +=" and t1.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t2.yd_flag='1' and  t2.yd_serial_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_1001') T";
            //countSql=" Select count(1) FROM ("+sql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1002")) {
            String commonSql = OracleSQLConstant.RISK1001And1002SQL;
            //String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
//            commonSql +=" and t1.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t2.yd_flag='2' and  t2.yd_serial_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_1002') T";
            // countSql=" Select count(1) FROM ("+sql+")";
        } else {
            String tableName = "YD_" + riskDetailsSearchDto.getModelId().toUpperCase();
            String riskDate = riskDetailsSearchDto.getRiskDate();
            if (riskDate != null) {
                riskDate = riskDate.replace("-", "");
            }
            String commonSql = "select " + riskDetailsSearchDto.getOfield() + "  from " + tableName + " T   where 1=1 and t.yd_risk_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_cjrq = '" + riskDate + "'";
//            commonSql +=" and T.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and T.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            sql = commonSql;
        }
        return sql;
    }

    //导出
    public String createQueryRiskSQL(RiskDetailsSearchDto riskDetailsSearchDto, String code) {
        String currentOrgFullId = riskDetailsSearchDto.getFullId();
        String sql = "";
        if (riskDetailsSearchDto.getModelId().equals("RISK_2001")) {
            //  String commonSql = MysqlSQLConstant.RISK2001SQL;
            String commonSql = OracleSQLConstant.RISK2001SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select distinct  " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t.yd_risk_id='RISK_2001') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2002")) {
            //String commonSql = MysqlSQLConstant.RISK2002SQL;
            String commonSql = OracleSQLConstant.RISK2002SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t.yd_risk_id='RISK_2002') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2003")) {
            String commonSql = OracleSQLConstant.RISK2003SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2004")) {

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2005")) {

            //String commonSql = MysqlSQLConstant.RISK2005SQL;
            String commonSql = OracleSQLConstant.RISK2005SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t.yd_risk_id='RISK_2005') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2006")) {

            // String commonSql = MysqlSQLConstant.RISK2006SQL;
            String commonSql = OracleSQLConstant.RISK2006SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t.yd_risk_id='RISK_2006') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2007")) {

            //String commonSql = MysqlSQLConstant.RISK2007SQL;
            String commonSql = OracleSQLConstant.RISK2007SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t1.yd_risk_id='RISK_2007') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2008")) {

            //String commonSql = MysqlSQLConstant.RISK2008SQL;
            String commonSql = OracleSQLConstant.RISK2008SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd')<= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t.yd_risk_id='RISK_2008') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1001")) {

            String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t2.yd_flag='1' and  t1.yd_risk_id='RISK_1001') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1002")) {

            String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
            }

            sql = "select  distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and t2.yd_flag='2' and t1.yd_risk_id='RISK_1002') T";

        } else {
            String tableName = "YD_" + riskDetailsSearchDto.getModelId().toUpperCase();
            String commonSql = "select " + riskDetailsSearchDto.getOfield() + " from " + tableName + " T  where 1=1  ";
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMaxDate())) {
                String maxDate = riskDetailsSearchDto.getMaxDate().replace("-", "");
                commonSql = commonSql + "and T.yd_cjrq <= '" + maxDate.trim() + "'";
            }
            if (StringUtils.isNotBlank(riskDetailsSearchDto.getMinDate())) {
                String minDate = riskDetailsSearchDto.getMinDate().replace("-", "");
                commonSql = commonSql + "and T.yd_cjrq >= '" + minDate.trim() + "'";
            }

            sql = commonSql;
        }
        return sql;
    }

    public String createQueryCountSQL(RiskDetailsSearchDto riskDetailsSearchDto) {
        String countSql = "";
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        if (riskDetailsSearchDto.getModelId().equals("RISK_4001")) {
            String commonSql = OracleSQLConstant.RISK4000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  a.yd_acct_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_4001') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_4002")) {
            String commonSql = OracleSQLConstant.RISK4000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  a.yd_acct_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_4002') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3001")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  dc.yd_legal_idcard_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3001') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3002")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  dc.yd_legal_idcard_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3002') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3003")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3003') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3004")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3004') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_3005")) {
            String commonSql = OracleSQLConstant.RISK3000SQL;
            //String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = "select count(1) FROM (" + commonSql + " and  dc.yd_depositor_name='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_3005') T";
            // countSql=" Select count(1) FROM ("+commonSql+")";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2001")) {
            String commonSql = OracleSQLConstant.RISK2001SQL;
            //String commonSql = MysqlSQLConstant.RISK2001SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  t.yd_customer_id='" + riskDetailsSearchDto.getRiskPoint () + "' and t.yd_risk_id='RISK_2001') T";
            countSql = " Select count(1) FROM (" + commonSql + " and  t.yd_customer_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2001') T";

        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2002")) {
            String commonSql = OracleSQLConstant.RISK2002SQL;
            //String commonSql = MysqlSQLConstant.RISK2002SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";

            String riskPointVal = null;
            try {
                riskPointVal = URLDecoder.decode(riskDetailsSearchDto.getRiskPoint(), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  dc.yd_reg_address='" + riskPointVal + "' and t.yd_risk_id='RISK_2002') T";
            countSql = " Select count(1) from (" + commonSql + " and  dc.yd_reg_address='" + riskPointVal + "' and t.yd_risk_id='RISK_2002') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2003")) {
            String commonSql = OracleSQLConstant.RISK2003SQL;
            countSql = "select distinct " + riskDetailsSearchDto.getOfield() + "\n from (" + commonSql + " and  t.yd_customer_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2003') T";


        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2004")) {
            //暂时不做
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2005")) {
            String commonSql = OracleSQLConstant.RISK2005SQL;
            // String commonSql = MysqlSQLConstant.RISK2005SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  b.yd_legal_idcard_no='" + riskDetailsSearchDto.getRiskPoint () + "' and t.yd_risk_id='RISK_2005') T";
            countSql = " Select count(1) from (" + commonSql + " and  b.yd_legal_idcard_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2005') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2006")) {
            String commonSql = OracleSQLConstant.RISK2006SQL;
            //String commonSql = MysqlSQLConstant.RISK2006SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  ac.yd_operator_idcard_no='" + riskDetailsSearchDto.getRiskPoint () + "' and t.yd_risk_id='RISK_2006') T";
            countSql = " Select count(1) from (" + commonSql + " and  ac.yd_operator_idcard_no='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2006') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2007")) {
            String commonSql = OracleSQLConstant.RISK2007SQL;
            // String commonSql = MysqlSQLConstant.RISK2007SQL;
//            commonSql +=" and t1.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            // sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  t1.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint () + "' and t1.yd_risk_id='RISK_2007') T";
            countSql = " Select count(1) from  (" + commonSql + " and  t1.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_2007') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_2008")) {
            String commonSql = OracleSQLConstant.RISK2008SQL;
            //String commonSql = MysqlSQLConstant.RISK2008SQL;
//            commonSql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint () + "' and t.yd_risk_id='RISK_2008') T";
            countSql = " Select count(1) from (" + commonSql + " and  t.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint() + "' and t.yd_risk_id='RISK_2008') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1001")) {
            String commonSql = OracleSQLConstant.RISK1001And1002SQL;
            //String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
//            commonSql +=" and t1.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and t2.yd_flag='1' and  t1.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint () + "' and t1.yd_risk_id='RISK_1001') T";
            countSql = " Select count(1) from  (" + commonSql + " and t2.yd_flag='1' and  t2.yd_serial_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_1001') T";
        } else if (riskDetailsSearchDto.getModelId().equals("RISK_1002")) {
            String commonSql = OracleSQLConstant.RISK1001And1002SQL;
            //String commonSql = MysqlSQLConstant.RISK1001And1002SQL;
            //commonSql += " and t1.yd_corporate_bank='" + RiskUtil.getOrganizationCode () + "'";
//            commonSql +=" and t1.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            //sql = "select " + riskDetailsSearchDto.getOfield () + "\n from (" + commonSql + " and t2.yd_flag='2' and  t1.yd_risk_point='" + riskDetailsSearchDto.getRiskPoint () + "' and t1.yd_risk_id='RISK_1002') T";
            countSql = " Select count(1) from (" + commonSql + " and t2.yd_flag='2' and  t2.yd_serial_id='" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_risk_id='RISK_1002') T";
        } else {
            String tableName = "YD_" + riskDetailsSearchDto.getModelId().toUpperCase();
            String riskDate = riskDetailsSearchDto.getRiskDate();
            if (riskDate != null) {
                riskDate = riskDate.replace("-", "");
            }
            String commonSql = "select count(1) from " + tableName + " T  where 1=1 and T.yd_risk_id='" + riskDetailsSearchDto.getRiskPoint() + "' and T.yd_cjrq = '" + riskDate + "'";
//            commonSql +=" and T.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
//            commonSql +=" and T.yd_corporate_full_id like '%"+ currentOrgFullId+"%'";
            countSql = commonSql;
        }
        return countSql;
    }


    public RiskDetailsSearchDto findRiskListExpDetails(RiskDetailsSearchDto riskDetailsSearchDto) {
        String sql = this.createQuerySQL(riskDetailsSearchDto);
        Query query = em.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        riskDetailsSearchDto.setList(resultList);
        return riskDetailsSearchDto;
    }

    public RiskDetailsSearchDto findModelByRiskid(RiskDetailsSearchDto riskDetailsSearchDto, String code) {
        String sql = this.createQueryRiskSQL(riskDetailsSearchDto, code);
        Query query = em.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        if (resultList != null && resultList.size() != 0) {
            List<Object[]> list = new ArrayList<>();
            for (int i = 0; i < resultList.size(); i++) {
                Object[] o = resultList.get(i);
                Object[] obj = new Object[o.length];
                for (int j = 0; j < o.length; j++) {
                    String str = (o[j] == null) ? "" : o[j].toString();
                    obj[j] = str;
                }
                list.add(obj);
            }
            riskDetailsSearchDto.setList(list);
        }
        return riskDetailsSearchDto;
    }

    public List<Object[]> findRiskTradeInfo(String modelId, String code) {
        String sql = MysqlSQLConstant.queryTradeRiskDataExSql;
        sql += " AND t3.yd_corporate_bank = '" + code + "' and t3.yd_risk_id = '" + modelId + "') t";
        Query nativeQuery = em.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        return resultList;
    }

    @Override
    public String findModelType(String modelName, String code) {

        RiskType risk = riskTypeDao.findByTypeName(modelName);
        String id = Long.toString(risk.getId());
        return id;
    }
}