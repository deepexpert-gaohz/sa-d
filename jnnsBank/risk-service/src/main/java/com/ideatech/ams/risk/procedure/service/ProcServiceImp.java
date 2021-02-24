package com.ideatech.ams.risk.procedure.service;


import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.account.entity.AccountAllData;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.service.ModelService;
import com.ideatech.ams.risk.riskdata.dao.RiskApiDao;
import com.ideatech.ams.risk.riskdata.dao.RiskCheckInfoDao;
import com.ideatech.ams.risk.riskdata.entity.RiskCheckInfo;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;
import com.ideatech.ams.risk.rule.dao.RuleConfigureDao;
import com.ideatech.ams.risk.rule.entity.RuleConfigure;
import com.ideatech.ams.risk.util.comparisonUtil;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProcServiceImp implements ProcService {

    private static final Logger log = LoggerFactory.getLogger(ProcServiceImp.class);

    @Autowired
    private EntityManager em;


    @Autowired
    private ModelService modelService;

    @Autowired
    private RiskApiDao riskApiDao;


    @Autowired
    private OrganizationDao organizationDao;


    @Autowired
    private RuleConfigureDao ruleConfigureDao;

    @Autowired
    private RiskCheckInfoDao riskDzDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    //获取规则的conAndVal    原getSql函数
    public String getSql(String modelid, String fieldid) {
        String result = "";
        //String sql = MysqlSQLConstant.getSqlProc;
        String sql = OracleSQLConstant.getSqlProc;
        if (modelid != null && fieldid != null) {
            sql += "and yd_model_id='" + modelid + "' and yd_field='" + fieldid + "'";
        }
        Query nativeQuery = em.createNativeQuery(sql);
        Object conAndVal = nativeQuery.getResultList();
        if (conAndVal != null) {
            result = (conAndVal == null) ? "" : conAndVal.toString();
            result = result.replace("[", "");
            result = result.replace("]", "");
        }
        return result;
    }

    /**
     * @author liuz
     * @date 2019-9-19
     * 配置yd_risk_record_info表中插入风险数据
     */
    public RiskRecordInfo insetRecordInfo(String riskId, String iDate) {
        RiskRecordInfo riskRecordInfo = new RiskRecordInfo();
        riskRecordInfo.setRiskId(riskId);
        riskRecordInfo.setDeleted(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            riskRecordInfo.setRiskDate(sdf.parse(iDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        riskRecordInfo.setStatus("0");
        return riskRecordInfo;
    }

    /**
     * @author liuz
     * @date 2019-9-19
     * 同一单位客户在n天内发生x次以上的开户、销户或变更信息操作！
     */
    @Transactional
    public List<RiskRecordInfo> risk2001Application(String iDate, Boolean isHighRiskApi) {
            log.info("批量执行RISK_2001开始");
        String riskId = "RISK_2001";
        //天数
        String ts = this.getSql(riskId, "ts");
        //次数
        String rn = this.getSql(riskId, "rn");
        ModelDto model2001 = modelService.findByModelId(riskId);
        List<RiskRecordInfo> list = new ArrayList<>();
        if (model2001 != null && model2001.getStatus().equals("1")) {
            String sql = "select distinct table1.yd_customer_no, table2.cnt, table1.yd_acct_no,table1.yd_acct_name, so.yd_code\n" +
                    "  from (select t.yd_customer_no, t1.yd_acct_no,t1.yd_acct_name, t1.yd_organ_full_id \n" +
                    "          from yd_account_bills_all t\n" +
                    "          left join yd_accounts_all t1\n" +
                    "            on t1.yd_ref_bill_id = t.yd_id\n" +
                    "          left outer join yd_account_public ac\n" +
                    "            on t1.yd_id = ac.yd_account_id\n" +
                    "         where t.yd_bill_date BETWEEN to_char(sysdate - " + ts + ", 'yyyy-MM-dd') and\n" +
                    "               to_char(sysdate, 'yyyy-MM-dd')\n" +
                    "           and t.yd_acct_no not in\n" +
                    "               (select rw.yd_account_id\n" +
                    "                  from yd_risk_white_list rw\n" +
                    "                 where rw.yd_account_id = t.yd_acct_no)\n" +
                    "           and t.yd_bill_type in\n" +
                    "               ('ACT_OPEN', 'ACCT_CHANGE', 'ACCT_REVOKE', 'ACCT_INIT')) table1\n" +
                    "  left join (select t.yd_customer_no, count(1) as cnt\n" +
                    "               from yd_account_bills_all t\n" +
                    "               left join yd_accounts_all t1\n" +
                    "                 on t1.yd_ref_bill_id = t.yd_id\n" +
                    "               left outer join yd_account_public ac\n" +
                    "                 on t1.yd_id = ac.yd_account_id\n" +
                    "              where t.yd_bill_date BETWEEN\n" +
                    "                    to_char(sysdate - " + ts + ", 'yyyy-MM-dd') and\n" +
                    "                    to_char(sysdate, 'yyyy-MM-dd')\n" +
                    "                and t.yd_acct_no not in\n" +
                    "                    (select rw.yd_account_id\n" +
                    "                       from yd_risk_white_list rw\n" +
                    "                      where rw.yd_account_id = t.yd_acct_no)\n" +
                    "                and t.yd_bill_type in\n" +
                    "                    ('ACT_OPEN', 'ACCT_CHANGE', 'ACCT_REVOKE', 'ACCT_INIT')\n" +
                    "              group by t.yd_customer_no) table2\n" +
                    "    on table1.yd_customer_no = table2.yd_customer_no\n" +
                    "  left join yd_sys_organization so on so.yd_full_id = table1.yd_organ_full_id \n"+
                    " where 1 = 1 and table1.yd_acct_no is not null\n" +
                    "   and cnt" + rn;
            log.info(sql);
            Query nativeQuery = em.createNativeQuery(sql);
            List<Object[]> resultList = nativeQuery.getResultList();

            if (resultList != null && resultList.size() != 0) {
                for (Object[] o : resultList) {
                    //插入
                    RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                    riskRecordInfo.setAccountNo(((o[2] == null) ? "" : o[2].toString()));
                    riskRecordInfo.setRiskId(riskId);
                    riskRecordInfo.setRiskCnt(((o[1] == null) ? "" : o[1].toString()));
                    riskRecordInfo.setRiskDesc("同一企业客户在" + ts + "天内发生" + rn + "次以上的开户、销户或变更信息操作！");
                    riskRecordInfo.setRiskPoint(((o[0] == null) ? "" : o[0].toString()));
                    riskRecordInfo.setCustomerId((o[0] == null) ? "" : o[0].toString());
                    riskRecordInfo.setRiskPointDesc("客户号:" + ((o[0] == null) ? "" : o[0].toString()));
                    riskRecordInfo.setAccountName(((o[3] == null) ? "" : o[3].toString()));
                    //20201107
                    riskRecordInfo.setOrganCode(((o[4] == null) ? "" : o[4].toString()));
                    list.add(riskRecordInfo);
                }
                if (!isHighRiskApi) {
                    riskApiDao.save(list);
                }
            }
        }
        log.info("批量执行RISK_2001结束");
        return list;
    }

    /**
     * @author liuz
     * @date 2019-9-19
     * 多个单位客户预留同一注册地址风险模型!
     */
    @Transactional
    public List<RiskRecordInfo> risk2002Application(String iDate, Boolean isHighRiskApi) {
        log.info("批量执行RISK_2002开始");
        String riskId = "RISK_2002";
        String rn = this.getSql(riskId, "rn");
        ModelDto model2002 = modelService.findByModelId(riskId);
        List<RiskRecordInfo> list = new ArrayList<>();
        if (model2002 != null && model2002.getStatus().equals("1")) {
            String sql = "SELECT distinct yd_customer_no, cnt, yd_reg_address,yd_acct_no,yd_acct_name,yd_organ_code\n" +
                    "  FROM (SELECT a.yd_customer_no as yd_customer_no,\n" +
                    "               a.yd_reg_address,\n" +
                    "               count(distinct a.yd_customer_no) over(partition by yd_reg_address) as cnt,a.yd_acct_no,a.yd_acct_name,a.yd_organ_code\n" +
                    "          FROM yd_account_all_data a)\n" +
                    " WHERE 1 = 1 and yd_reg_address is not null\n" +
                    " and cnt \n" + rn;
            Query nativeQuery = em.createNativeQuery(sql);
            log.info(sql);
            List<Object[]> resultList = nativeQuery.getResultList();
            if (resultList != null && resultList.size() != 0) {
                for (Object[] o : resultList) {
                    RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                    riskRecordInfo.setRiskCnt(((o[1] == null) ? "" : o[1].toString()));//cnt
                    riskRecordInfo.setRiskPoint(((o[2] == null) ? "" : o[2].toString()));//address
                    riskRecordInfo.setRiskDesc("多" + rn + "个单位客户预留同一注册地址" + riskRecordInfo.getRiskPoint());
                    riskRecordInfo.setCustomerId((o[0] == null) ? "" : o[0].toString());
                    riskRecordInfo.setRiskPointDesc("客户号:" + ((o[2] == null) ? "" : o[2].toString()));
                    riskRecordInfo.setAccountNo((o[3] == null) ? "" : o[3].toString());
                    riskRecordInfo.setAccountName((o[4] == null) ? "" : o[4].toString());

                    //20201107 潘修改机构号
                    riskRecordInfo.setOrganCode((o[5] == null) ? "" : o[5].toString());
                    list.add(riskRecordInfo);

                }
                try {
                    riskApiDao.save(list);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        log.info("批量执行RISK_2002结束");
        return list;
    }


    /**
     * @author liuz
     * @date 2019-9-23
     * 同一法人或负责人已经开立了多n个账户！RISK_2005_ACCOUNT_APPLICATION
     */
    @Transactional
    public List<RiskRecordInfo> risk2005Application(String iDate, Boolean isHighRiskApi) {
        log.info("批量执行RISK_2005开始");
        String riskId = "RISK_2005";
        String rn = this.getSql(riskId, "rn");
        ModelDto model2005 = modelService.findByModelId(riskId);
        List<RiskRecordInfo> list = new ArrayList<>();
        if (model2005 != null && model2005.getStatus().equals("1")) {

            String sql = "select distinct yd_acct_no, cnt, yd_legal_idcard_no,yd_acct_name,yd_organ_code\n" +
                    "  from (select a.yd_legal_idcard_no,\n" +
                    "       a.yd_acct_no,\n" +
                    "       count(a.yd_acct_no) over(partition by a.yd_legal_idcard_no) cnt,a.yd_acct_name,a.yd_organ_code\n" +
                    "  from yd_account_all_data a\n" +
                    " where 1 = 1\n" +
                    "   and a.yd_account_status = 'A'\n" +
                    "   and a.yd_legal_idcard_no is not null)\n" +
                    " where 1 = 1\n" +
                    "   and cnt " + rn;
            Query nativeQuery = em.createNativeQuery(sql);
            log.info("RISK_2005" + sql);
            List<Object[]> resultList = nativeQuery.getResultList();
            if (resultList != null && resultList.size() != 0) {
                for (Object[] o : resultList) {
                    RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                    riskRecordInfo.setRiskCnt(((o[1] == null) ? "" : o[1].toString()));
                    riskRecordInfo.setRiskPoint(((o[2] == null) ? "" : o[2].toString()));
                    riskRecordInfo.setRiskDesc("同一法人或负责人" + riskRecordInfo.getRiskPoint() + "已经开立了多(" + rn + ")个账户！");
                    riskRecordInfo.setAccountNo((o[0] == null) ? "" : o[0].toString());
                    riskRecordInfo.setRiskPointDesc("法人证件号:" + ((o[2] == null) ? "" : o[2].toString()));
                    riskRecordInfo.setAccountName(((o[3] == null) ? "" : o[3].toString()));

                    //20201107
                    riskRecordInfo.setOrganCode(((o[4] == null) ? "" : o[4].toString()));
                    list.add(riskRecordInfo);
                }
                if (!isHighRiskApi) {
                    riskApiDao.save(list);
                }
            }
        }
        log.info("批量执行RISK_2005结束");
        return list;
    }


    /**
     * @param iDate
     * @Author liuz
     * @Date 2019-9-23
     * 同一经办人?已经开立了多n个账户！
     */
    @Transactional
    public List<RiskRecordInfo> risk2006Application(String iDate, Boolean isHighRiskApi) {
        log.info("批量执行RISK_2006开始");
        String riskId = "RISK_2006";
        String rn = this.getSql(riskId, "rn");
        ModelDto model2006 = modelService.findByModelId(riskId);
        List<RiskRecordInfo> list = new ArrayList<>();
        if (model2006 != null && model2006.getStatus().equals("1")) {
          /*  String sql = "select distinct yd_acct_no,yd_customer_no,cnt ,yd_operator_idcard_no,yd_acct_name\n" +
                    "             from (select\n" +
                    "    c.yd_acct_no, b.yd_customer_no,d.yd_operator_idcard_no,c.yd_acct_name\n" +
                    "    ,count(distinct c.yd_acct_no)  over(partition by d.yd_operator_idcard_no) cnt\n" +
                    "    from yd_customer_public a\n" +
                    "    left outer join yd_customers_all b on a.yd_customer_id=b.yd_id\n" +
                    "    left outer join yd_accounts_all c on b.yd_customer_no=c.yd_customer_no\n" +
                    "    left outer join yd_account_public d on c.yd_id=d.yd_account_id\n" +
                    "    where 1=1\n  " +
                    "    and  d.yd_operator_idcard_no is not null\n" +
                    "    and c.yd_acct_no not in(\n" +
                    "    select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=c.yd_acct_no) )\n" +*/


                    String sql = "      select distinct yd_acct_no, yd_customer_no,cnt, yd_operator_idcard_no, yd_acct_name,yd_code \n"+
                    " from (select c.yd_acct_no,a.yd_customer_no,d.yd_operator_idcard_no,c.yd_acct_name,so.yd_code,\n"+
                    "  count(distinct c.yd_acct_no) over(partition by d.yd_operator_idcard_no) cnt \n"+
                    "  from yd_accounts_all c left join yd_account_public d on c.yd_id = d.yd_account_id \n"+
                    "  left join yd_customers_all a on a.yd_customer_no = c.yd_customer_no \n"+
                    "  left join yd_sys_organization so on so.yd_full_id = c.yd_organ_full_id  where 1 = 1 \n"+
                    "  and d.yd_operator_idcard_no is not null and c.yd_acct_no not in \n"+
                    "  (select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id = c.yd_acct_no)) \n"+
                    "    where 1=1 and cnt" + rn;
            Query nativeQuery = em.createNativeQuery(sql);
            log.info(sql);
            int firstResult = nativeQuery.getMaxResults();
            System.out.println(firstResult);
            List<Object[]> resultList = nativeQuery.getResultList();
            if (resultList != null && resultList.size() != 0) {
                for (Object[] o : resultList) {
                    RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                    riskRecordInfo.setAccountNo(((o[0] == null) ? "" : o[0].toString()));
                    riskRecordInfo.setRiskPoint(((o[3] == null) ? "" : o[3].toString()));//yd_legal_idcard_no
                    riskRecordInfo.setRiskCnt(((o[2] == null) ? "" : o[2].toString()));
                    riskRecordInfo.setRiskDesc("同一经办人(" + ((o[3] == null) ? "" : o[3].toString()) + ")已经开立了多(" + rn + ")个账户！");
                    riskRecordInfo.setCustomerId((o[1] == null) ? "" : o[1].toString());
                    riskRecordInfo.setRiskPointDesc("经办人:" + ((o[3] == null) ? "" : o[3].toString()));
                    riskRecordInfo.setAccountName((o[4] == null) ? "" : o[4].toString());
                    riskRecordInfo.setOrganCode((o[5] == null) ? "" : o[5].toString());
                    list.add(riskRecordInfo);
                }
                if (!isHighRiskApi) {
                    riskApiDao.save(list);
                }
            }
        }
        log.info("批量执行RISK_2006结束");
        return list;
    }


    /**
     * @author liuz
     * @date 2019-9-20
     * 同一主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码  RISK_2008_ACCOUNT_APPLICATION
     */
    @Transactional
    public List<RiskRecordInfo> risk2008Appliaction(String iDate, Boolean isHighRiskApi) {
        log.info("批量执行RISK_2008开始");
        String riskId = "RISK_2008";
        ModelDto model2008 = modelService.findByModelId(riskId);
        List<RiskRecordInfo> list = new ArrayList<>();
        if (model2008 != null && model2008.getStatus().equals("1")) {
            String sql = " select distinct a.yd_finance_telephone,\n" +
                    "                      a.yd_legal_telephone,\n" +
                    "                      ac.yd_operator_telephone,\n" +
                    "                      dc.yd_customer_no,\n" +
                    "                      dc.yd_acct_no,dc.yd_acct_name, aa.yd_organ_code\n" +
                    "        from yd_customer_public a\n" +
                    "        left outer join yd_customers_all c on a.yd_customer_id = c.yd_id\n" +
                    "        left outer join yd_accounts_all dc on c.yd_customer_no = dc.yd_customer_no\n" +
                    "        left outer join yd_account_public ac on dc.yd_id = ac.yd_account_id\n" +
                    " left join yd_account_all_data aa on aa.yd_acct_no = dc.yd_acct_no \n"+
                    "       where a.yd_legal_telephone is not null\n";
            Query nativeQuery = em.createNativeQuery(sql);
            log.info(sql);
            List<Object[]> resultList = nativeQuery.getResultList();
            int processNum = 0;
            if (resultList != null && resultList.size() != 0) {
                for (Object[] o : resultList) {
                    processNum++;
                    String financeTel = (o[0] == null) ? "" : o[0].toString();
                    String legalTel = (o[1] == null) ? "" : o[1].toString();
                    String operatorTel = (o[2] == null) ? "" : o[2].toString();
                    if (legalTel.equalsIgnoreCase(financeTel) || legalTel.equalsIgnoreCase(operatorTel)) {
                        RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                        riskRecordInfo.setRiskPoint(((o[1] == null) ? "" : o[1].toString()));
                        riskRecordInfo.setCustomerId((o[3] == null) ? "" : o[3].toString());
                        riskRecordInfo.setRiskDesc("同一主体(" + riskRecordInfo.getCustomerId() + ")中法定代表人、财务负责人、财务经办人预留了同一个联系号码" + ((o[1] == null) ? "" : o[1].toString()));
                        riskRecordInfo.setRiskPointDesc("联系方式:" + ((o[1] == null) ? "" : o[1].toString()));
                        riskRecordInfo.setAccountNo((o[4] == null) ? "" : o[4].toString());
                        riskRecordInfo.setAccountName((o[5] == null) ? "" : o[5].toString());

                        //20201107
                        riskRecordInfo.setOrganCode((o[6] == null) ? "" : o[6].toString());
                        list.add(riskRecordInfo);
                        if (!isHighRiskApi) {
                            riskApiDao.save(riskRecordInfo);
                        }
                    } else {
                        if (StringUtils.isNotBlank(financeTel)) {
                            if (financeTel.equalsIgnoreCase(operatorTel)) {
                                RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                                riskRecordInfo.setRiskPoint(((o[1] == null) ? "" : o[1].toString()));
                                riskRecordInfo.setCustomerId((o[3] == null) ? "" : o[3].toString());
                                riskRecordInfo.setRiskDesc("同一主体(" + riskRecordInfo.getCustomerId() + ")中法定代表人、财务负责人、财务经办人预留了同一个联系号码" + ((o[1] == null) ? "" : o[1].toString()));
                                riskRecordInfo.setAccountNo((o[4] == null) ? "" : o[4].toString());
                                riskRecordInfo.setAccountName((o[5] == null) ? "" : o[5].toString());

                                //20201107
                                riskRecordInfo.setOrganCode((o[6] == null) ? "" : o[6].toString());
                                list.add(riskRecordInfo);
                                if (!isHighRiskApi) {
                                    riskApiDao.save(riskRecordInfo);
                                }
                            }
                        }
                    }
                    if (processNum % 100 == 0) {
                        log.info("批量执行模型【RISK_2008】，总数一共：" + resultList.size() + ",已跑批" + processNum + "条数据");
                    }
                }
            }
        }
        log.info("批量执行RISK_2008结束");
        return list;
    }

    /**
     * 新模型总的触发方法。
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void risk3000Appliaction(String iDate, Boolean isHighRiskApi) {
        log.info("批量执行模型RISK_3001开始");
        TransactionDefinition definition = new DefaultTransactionDefinition(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        String sql = "select t.yd_acct_no,t.yd_acct_name,t.yd_customer_no,t.yd_legal_idcard_no,t.yd_organ_code from YD_ACCOUNT_ALL_DATA t  where yd_account_status = 'A'";
        Query nativeQuery = em.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        String rn1 = "";
        String rn2 = "";
        try {
            RuleConfigure ruleConfigure1 = ruleConfigureDao.findByRuleId("56");
            if (ruleConfigure1 != null) {
                rn1 = ruleConfigure1.getValue();
            }
            RuleConfigure ruleConfigure2 = ruleConfigureDao.findByRuleId("57");
            if (ruleConfigure2 != null) {
                rn2 = ruleConfigure2.getValue();
            }
        } catch (Exception e) {
            log.info("模型法定代表人或单位负责人年龄小于20或大于65岁，获取规则报错：" + e.getMessage());
        }
        int processNum = 0;
        AccountAllData a;
        try {
            for (Object[] o : resultList) {
                processNum++;
                a = new AccountAllData();
                a.setAcctNo(o[0] == null ? "" : o[0].toString());
                a.setAcctName(o[1] == null ? "" : o[1].toString());
                a.setCustomerNo(o[2] == null ? "" : o[2].toString());
                a.setLegalIdcardNo(o[3] == null ? "" : o[3].toString());
                //20201107
                a.setOrganCode(o[4] == null ? "" : o[4].toString());

                risk3001Appliaction(iDate, isHighRiskApi, a, rn1, rn2);
                if (processNum % 100 == 0) {
                    log.info("批量执行模型【RISK_3001】，总数一共：" + resultList.size() + ",已跑批" + processNum + "条数据");
                    transactionManager.commit(transaction);
                    transaction = transactionManager.getTransaction(definition);
                }
            }
            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.error("批量执行RISK_3001时异常", e);
            transactionManager.rollback(transaction);
        }
        try {
            transaction = transactionManager.getTransaction(definition);
            for (Object[] o : resultList) {
                processNum++;
                a = new AccountAllData();
                a.setAcctNo(o[0] == null ? "" : o[0].toString());
                a.setAcctName(o[1] == null ? "" : o[1].toString());
                a.setCustomerNo(o[2] == null ? "" : o[2].toString());
                a.setLegalIdcardNo(o[3] == null ? "" : o[3].toString());
                //20201107
                a.setOrganCode(o[4] == null ? "" : o[4].toString());
                risk3002Appliaction(iDate, isHighRiskApi, a);
                if (processNum % 100 == 0) {
                    log.info("批量执行模型【RISK_3002】，总数一共：" + resultList.size() + ",已跑批" + processNum + "条数据");
                    transactionManager.commit(transaction);
                    transaction = transactionManager.getTransaction(definition);
                }
            }
            transactionManager.commit(transaction);
            log.info("批量执行模型RISK_3001结束");
        } catch (Exception e) {
            log.error("批量执行RISK_3002时异常", e);
            transactionManager.rollback(transaction);
        }
    }


    /**
     * 法定代表人或单位负责人年龄小于20或大于65岁
     *
     * @param iDate
     * @param isHighRiskApi
     * @return
     */
    @Transactional
    public void risk3001Appliaction(String iDate, Boolean isHighRiskApi, AccountAllData accountAllData, String rn1, String rn2) {
        String riskId = "RISK_3001";
        try {
            if (StringUtils.isNotBlank(accountAllData.getLegalIdcardNo()) && accountAllData.getLegalIdcardNo().length() == 18 && StringUtils.isNotBlank(rn1)
                    && StringUtils.isNotBlank(rn2) && getIdcardNo(accountAllData.getLegalIdcardNo(), rn1, rn2)) {
                RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                riskRecordInfo.setAccountNo(accountAllData.getAcctNo());
                riskRecordInfo.setRiskPoint(accountAllData.getLegalIdcardNo());
                riskRecordInfo.setRiskCnt("1");
                riskRecordInfo.setRiskDesc("法定代表人或单位负责人年龄小于" + rn1 + "岁大于" + rn2 + "岁");
                riskRecordInfo.setCustomerId(accountAllData.getCustomerNo());
                riskRecordInfo.setRiskPointDesc("法人证件号:" + accountAllData.getLegalIdcardNo());
                riskRecordInfo.setAccountName(accountAllData.getAcctName());

                //20201107 潘修改设置机构号
                riskRecordInfo.setOrganCode(accountAllData.getOrganCode());
                if (!isHighRiskApi) {
                    riskApiDao.save(riskRecordInfo);
                }
            }
        } catch (Exception e) {
            log.info("模型法定代表人或单位负责人年龄小于20或大于60岁报错：" + e.getMessage());
        }
    }


    /**
     * 截取身份证号，判断年龄
     */
    public boolean getIdcardNo(String IdcardNo, String rn1, String rn2) {
        try {
            String IdcardNoDate = IdcardNo.substring(6, 10);
            Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
            Matcher isNum = pattern.matcher(IdcardNoDate);
            if (isNum.matches()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                String year = df.format(new Date());
                int age = Integer.parseInt(year) - Integer.parseInt(IdcardNoDate);
                if (age < Integer.parseInt(rn1) || age > Integer.parseInt(rn2)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log.info("根据身份证号判断年龄报错：" + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void saveDzDate(RiskCheckInfo riskCheckInfo) {
        riskDzDao.save(riskCheckInfo);
    }

    /**
     * 企业法人或单位负责人非常州本地
     *
     * @param iDate
     * @param isHighRiskApi
     * @return
     */
    @Transactional
    public void risk3002Appliaction(String iDate, Boolean isHighRiskApi, AccountAllData accountAllData) {
        String riskId = "RISK_3002";
        if (StringUtils.isNotBlank(accountAllData.getLegalIdcardNo()) && accountAllData.getLegalIdcardNo().length() == 18 || accountAllData.getLegalIdcardNo().length() == 15) {
            //判断法人身份证是否为常州地区
            if (!comparisonUtil.comparisonLegalIdcardNo(accountAllData.getLegalIdcardNo())) {
                RiskRecordInfo riskRecordInfo = this.insetRecordInfo(riskId, iDate);
                riskRecordInfo.setAccountNo(accountAllData.getAcctNo());
                riskRecordInfo.setRiskPoint(accountAllData.getLegalIdcardNo());
                riskRecordInfo.setRiskCnt("1");
                riskRecordInfo.setRiskDesc("法定代表人或单位负责人非常州本地");
                riskRecordInfo.setCustomerId(accountAllData.getCustomerNo());
                riskRecordInfo.setRiskPointDesc("注册地址:" + accountAllData.getRegAddress() + "; 法人证件号:" + accountAllData.getLegalIdcardNo());
                riskRecordInfo.setAccountName(accountAllData.getAcctName());

                //202021107 潘修改设置机构号
                riskRecordInfo.setOrganCode(accountAllData.getOrganCode());
                if (!isHighRiskApi) {
                    riskApiDao.save(riskRecordInfo);
                }
            }
        }
    }

    public String findBankCodeByCode(String code) {
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(code);
        boolean isValid = m.matches();
        if (isValid == false) {
            OrganizationPo byCode = organizationDao.findByCode(code);
            List<OrganizationPo> byFullIdLike = organizationDao.findByFullIdLike("%" + byCode.getParentId());
            if (byFullIdLike != null) {
                code = byFullIdLike.get(0).getCode();
            }
            Pattern p1 = Pattern.compile("^[A-Za-z]+$");
            Matcher m1 = p1.matcher(code);
            boolean isValidTo = m1.matches();
            if (isValidTo == false) {
                code = this.findBankCodeByCode(code);
            } else {
                return code;
            }
        }
        return code;
    }
}




