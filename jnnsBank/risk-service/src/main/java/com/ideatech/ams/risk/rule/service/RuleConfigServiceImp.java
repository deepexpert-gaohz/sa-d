package com.ideatech.ams.risk.rule.service;

import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.rule.dao.RuleConfigureDao;
import com.ideatech.ams.risk.rule.dao.RuleFieldDao;
import com.ideatech.ams.risk.rule.dto.RuleConfigureDto;
import com.ideatech.ams.risk.rule.dto.RuleFieldDto;
import com.ideatech.ams.risk.rule.dto.RuleSearchDto;
import com.ideatech.ams.risk.rule.entity.RuleConfigure;
import com.ideatech.ams.risk.rule.entity.RuleField;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleConfigServiceImp implements RuleConfigService {
    @Autowired
    EntityManager em;
    @Autowired
    RuleConfigureDao ruleConfigureDao;
    @Autowired
    RuleFieldDao ruleFieldDao;

    @Autowired
    OrganizationDao organizationDao;
    /**
     * 总行机构码
     */
    @Value("${ams.szsm.ods.orgId:9998}")
    private String _orgId;
    /**
     * 总行机构名称
     */
    @Value("${ams.szsm.ods.orgName:总行}")
    private String _orgName;

    @Override
    public RuleSearchDto findRuleFieldByModelId(RuleSearchDto ruleSearchDto) {
        Pageable pageable = new PageRequest(Math.max(ruleSearchDto.getOffset() - 1, 0), ruleSearchDto.getLimit());
        //        String sql = " select  f.yd_field_name, f.yd_field,t.yd_condition,t.yd_value,t.yd_id  " +
//                " from YD_RISK_RULE_CONFIGURATION t ,yd_risk_rule_field f where f.yd_id = t.yd_rule_id and f.YD_DELETED=0 and t.YD_DELETED = 0 " ;
        String sql = OracleSQLConstant.findRuleFieldByModelIdSql;
        //String sql = MysqlSQLConstant.findRuleFieldByModelIdSql;
       /* String sql = " select  f.yd_field_name, f.yd_field,t.yd_condition,t.yd_value,t.yd_id \n" +
                " from YD_RISK_RULE_CONFIGURATION t,yd_risk_rule_field f where t.yd_rule_id not in (select o.yd_id from yd_risk_rule_field o where o.YD_DELETED='0' ) and f.YD_DELETED=0 and t.YD_DELETED = 0  ";
        */
        if (StringUtils.isNotBlank(ruleSearchDto.getModelId())) {
            sql += " and f.yd_model_id = ?1 ";
        }
        //sql +="AND t.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode()+"'";
        Query query = em.createNativeQuery(sql);

        if (StringUtils.isNotBlank(ruleSearchDto.getModelId())) {
            query.setParameter(1, ruleSearchDto.getModelId() + "");
        }
        List<Object[]> navList = query.getResultList();

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> resultList = query.getResultList();

        Long count = 0l;
        if (navList != null) {
            count = (long) navList.size();
        }
        List<RuleConfigureDto> list = new ArrayList<>();
        for (Object[] o : resultList) {
            RuleConfigureDto r = new RuleConfigureDto();
            r.setFieldName((o[0] == null) ? "" : o[0].toString());
            r.setField((o[1] == null) ? "" : o[1].toString());
            r.setCondition((o[2] == null) ? "" : o[2].toString());
            r.setValue((o[3] == null) ? "" : o[3].toString());
            r.setId(Long.parseLong(o[4].toString()));
            list.add(r);
        }
        ruleSearchDto.setList(list);
        ruleSearchDto.setTotalPages((int) Math.ceil(count.intValue() / ruleSearchDto.getLimit()));
        ruleSearchDto.setTotalRecord(count);
        return ruleSearchDto;
    }

    @Override
    public List<RuleFieldDto> findByModelId(String modelId) {
        List<RuleField> allByModelId = ruleFieldDao.findAllByModelId(modelId);
        //查找在配置表中不存在配置的字段
        //        String sql ="select f.YD_ID,f.YD_FIELD_NAME from yd_risk_rule_field f where f.yd_id\n" +
//                "not in (select t.yd_rule_id from YD_RISK_RULE_CONFIGURATION t where yd_deleted='0') and f.yd_deleted='0' and f.yd_model_id = '"+modelId+"'";
        String sql = OracleSQLConstant.findByModelIdSql + modelId + "'";
        //String sql = MysqlSQLConstant.findByModelIdSql + modelId + "'";
        Query query = em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        List<RuleFieldDto> resultList = new ArrayList<>();
        if (list.size() != 0) {
            for (Object[] o : list) {
                RuleFieldDto r = new RuleFieldDto();
                r.setId(Long.parseLong(o[0].toString()));
                r.setFieldName((o[1] == null) ? "" : o[1].toString());
                resultList.add(r);
            }
        }
        return resultList;
    }

    @Override
    @Transactional
    public void saveRuleConfiger(RuleConfigureDto ruleConfigureDto) {
        RuleConfigure ruleConfigure = new RuleConfigure();
        if (null != ruleConfigureDto.getId()) {
            ruleConfigure = ruleConfigureDao.findOne(ruleConfigureDto.getId());
            if (ruleConfigure == null) {
                ruleConfigure = new RuleConfigure();
            }
        }
        RuleConfigure convert = ConverterService.convert(ruleConfigureDto, ruleConfigure);
        //convert.setCorporateBank(RiskUtil.getOrganizationCode());
        ruleConfigureDao.saveAndFlush(convert);
    }

    @Override
    public RuleConfigureDto findConfigerById(Long id) {
        RuleConfigureDto ruleConfigureDto = new RuleConfigureDto();
        RuleConfigure ruleConfigure = new RuleConfigure();
        if (id != null) {
            ruleConfigure = ruleConfigureDao.findOne(id);
            if (ruleConfigure == null) {
                ruleConfigure = new RuleConfigure();
            }
        }
        String condition = "";
        if (ruleConfigure.getCondition() != null && !ruleConfigure.getCondition().equalsIgnoreCase("")) {
            switch (ruleConfigure.getCondition()) {
                case ">":
                    condition = "bigThan";
                    break;
                case "<":
                    condition = "lessThan";
                    break;
                case "=":
                    condition = "equal";
                    break;
                case ">=":
                    condition = "bigEqualThan";
                    break;
                case "<=":
                    condition = "lessEqualThan";
                    break;
                case "!=":
                    condition = "notEqual";
                    break;
                case "in":
                    condition = "in";
                    break;
                case "not in":
                    condition = "notIn";
                    break;
                case "like":
                    condition = "like";
                    break;
                case "between":
                    condition = "between";
                    break;
            }
        }
        ruleConfigure.setCondition(condition);

        ConverterService.convert(ruleConfigure, ruleConfigureDto);
        return ruleConfigureDto;
    }

    @Override
    public void deleteConfiger(Long id) {
        ruleConfigureDao.delete(id);
    }

    @Override
    public RuleFieldDto findFieldById(Long id) {
        RuleFieldDto ruleFieldDto = new RuleFieldDto();
        RuleField one = ruleFieldDao.findOne(id);
        ConverterService.convert(one, ruleFieldDto);
        return ruleFieldDto;
    }

    /**
     * 根据模型查询相对规则
     *
     * @param modelId
     * @return
     */
    @Override
    public List<RuleConfigureDto> getModelRuleByModelId(String modelId, String corporateBank) {
        String sql = OracleSQLConstant.getModelRuleByModelIdSql;
        //String sql = MysqlSQLConstant.getModelRuleByModelIdSql;
        //sql = sql + " and rf.yd_corporate_bank = '"+corporateBank+"'";
        if (modelId != null && !(modelId).equalsIgnoreCase("")) {
            sql += "and rf.yd_model_id='" + modelId + "'";
        }
        Query query = em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        List<RuleConfigureDto> resultList = new ArrayList<>();
        if (list.size() != 0) {
            for (Object[] o : list) {
                RuleConfigureDto r = new RuleConfigureDto();
                r.setModelId((o[0] == null) ? "" : o[0].toString());
                r.setFieldName((o[1] == null) ? "" : o[1].toString());
                r.setConAndVal((o[2] == null) ? "" : o[2].toString());
                r.setCondition((o[3] == null) ? "" : o[3].toString());
                r.setValue((o[4] == null) ? "" : o[4].toString());
                r.setField((o[5] == null) ? "" : o[5].toString());
                resultList.add(r);
            }
        }
        return resultList;
    }

    @Override
    public Boolean initRule() {
        try {
            ruleFieldDao.deleteAll();
            ruleConfigureDao.deleteAll();
            ruleFieldInit(_orgId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void initRuleCon(String code) throws Exception {
        //createruleConfigure("4",">=",">=3","3");
        createruleConfigure("3", ">=", ">=3", "3", code);
        createruleConfigure("19", ">=", ">=500000", "500000", code);
        createruleConfigure("18", "", "15", "15", code);
        createruleConfigure("17", ">=", ">=1000000", "1000000", code);
        createruleConfigure("16", "", "15", "15", code);
        createruleConfigure("14", ">=", ">=3", "3", code);
        createruleConfigure("13", ">=", ">=3", "3", code);
        createruleConfigure("12", ">=", ">=3", "3", code);
        createruleConfigure("11", ">=", ">=3", "3", code);
        createruleConfigure("1", ">=", ">=3", "3", code);
        createruleConfigure("2", "", "7", "7", code);
        createruleConfigure("15", ">=", ">=3", "3", code);

        //交易类规则内容
        createruleConfigure("20", "", "10", "10", code);
        createruleConfigure("21", ">=", ">=100000", "100000", code);
        createruleConfigure("22", "", "10", "10", code);
        createruleConfigure("23", ">", ">2", "2", code);
        createruleConfigure("24", "between", "between 1 and 3 ", "1,3", code);
        createruleConfigure("25", "=", "=1", "1", code);
        createruleConfigure("26", "=", "=1", "1", code);
        createruleConfigure("27", "", "10", "10", code);
        createruleConfigure("28", ">", ">2", "2", code);
        createruleConfigure("29", "between", "between 1 and 3 ", "1,3", code);
        createruleConfigure("30", "=", "=1", "1", code);
        createruleConfigure("31", ">", ">500000", "500000", code);
        createruleConfigure("32", ">", ">3", "3", code);
        createruleConfigure("33", "", "10", "10", code);
        createruleConfigure("34", ">", ">20", "20", code);
        createruleConfigure("35", ">", ">3", "3", code);
        createruleConfigure("36", "", "10", "10", code);
        createruleConfigure("37", ">", ">100000", "100000", code);
        createruleConfigure("38", "", "7", "7", code);
        createruleConfigure("39", ">", ">3", "3", code);
        createruleConfigure("40", ">", ">3", "3", code);
        createruleConfigure("41", "<", "<1000", "1000", code);
        createruleConfigure("42", ">", ">100000", "100000", code);
        createruleConfigure("43", "", "180", "180", code);
        createruleConfigure("44", ">", ">100000", "100000", code);
        createruleConfigure("45", ">", ">1", "1", code);
        createruleConfigure("46", ">", ">0", "0", code);
        createruleConfigure("47", "", "10", "10", code);
        createruleConfigure("48", ">", ">2", "2", code);
        createruleConfigure("49", ">", ">2", "2", code);
        createruleConfigure("50", ">=", ">=180", "180", code);
        createruleConfigure("51", ">=", ">=180", "180", code);


    }

    public void createruleConfigure(String ruleId, String condition, String conAndVal, String val, String code) {
        RuleConfigure ruleConfigure = new RuleConfigure();
        ruleConfigure.setRuleId(ruleId);
        ruleConfigure.setCondition(condition);
        ruleConfigure.setConAndVal(conAndVal);
        ruleConfigure.setValue(val);
        ruleConfigure.setCorporateBank(code);
        ruleConfigureDao.save(ruleConfigure);
    }


    protected void ruleFieldInit(String code) throws Exception {
        //开户类模型规则初始化
        RuleField ruleField = createruleField("RISK_2001", "开户人频繁开户、销户或变更账户信息", "rn", "开户或销户次数", code);
        createruleConfigure(ruleField.getId().toString(), ">=", ">=3", "3", code);

        RuleField ruleField1 = createruleField("RISK_2001", "开户人频繁开户、销户或变更账户信息", "ts", "频繁天数范围", code);
        createruleConfigure(ruleField1.getId().toString(), "", "7", "7", code);

        RuleField ruleField2 = createruleField("RISK_2002", "单位同一注册地址被多次使用", "rn", "注册地址次数", code);
        createruleConfigure(ruleField2.getId().toString(), ">=", ">=3", "3", code);

        RuleField ruleField3 = createruleField("RISK_2005", "同一法定代表人或负责人开立多个账户", "rn", "账户个数", code);
        createruleConfigure(ruleField3.getId().toString(), ">=", ">=3", "3", code);

        RuleField ruleField4 = createruleField("RISK_2006", "同一经办人员代理多个主体开立账户", "rn", "账户个数", code);
        createruleConfigure(ruleField4.getId().toString(), ">=", ">=3", "3", code);

        RuleField ruleField2007 = createruleField("RISK_2007", "多个主体的法定代表人、财务负责人、经办人员预留同一个联系方式等的账户筛选。", "", "", code);
        createruleConfigure(ruleField2007.getId().toString(), "", "", "", code);

        //交易类模型规则初始化
        RuleField ruleField5 = createruleField("RISK_1001", "新开户15日发生100万以上交易", "ts", "天数", code);
        createruleConfigure(ruleField5.getId().toString(), "", "15", "15", code);

        RuleField ruleField6 = createruleField("RISK_1001", "新开户15日发生100万以上交易", "jyje", "交易金额", code);
        createruleConfigure(ruleField6.getId().toString(), ">=", ">=1000000", "1000000", code);

        RuleField ruleField7 = createruleField("RISK_1002", "印鉴卡变更15日内发生50万交易", "ts", "天数", code);
        createruleConfigure(ruleField7.getId().toString(), "", "15", "15", code);

        RuleField ruleField8 = createruleField("RISK_1002", "印鉴卡变更15日内发生50万交易", "jyje", "交易金额", code);
        createruleConfigure(ruleField8.getId().toString(), ">=", ">=500000", "500000", code);

        RuleField ruleField9 = createruleField("RISK_2019", "新开立账户短期内有大额资金汇入汇出", "ts", "天数", code);
        createruleConfigure(ruleField9.getId().toString(), "", "10", "10", code);

        RuleField ruleField10 = createruleField("RISK_2019", "新开立账户短期内有大额资金汇入汇出", "jyje", "交易金额", code);
        createruleConfigure(ruleField10.getId().toString(), ">=", ">=100000", "100000", code);

        RuleField ruleField11 = createruleField("RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "ts", "天数", code);
        createruleConfigure(ruleField11.getId().toString(), "", "10", "10", code);

        RuleField ruleField12 = createruleField("RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn1", "转出次数", code);
        createruleConfigure(ruleField12.getId().toString(), ">", ">2", "2", code);

        RuleField ruleField13 = createruleField("RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn2", "转入次数", code);
        createruleConfigure(ruleField13.getId().toString(), "between", "between 1 and 3 ", "1,3", code);

        RuleField ruleField14 = createruleField("RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn3", "不同地区", code);
        createruleConfigure(ruleField14.getId().toString(), "=", "=1", "1", code);

        RuleField ruleField15 = createruleField("RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn4", "单次交易", code);
        createruleConfigure(ruleField15.getId().toString(), "=", "=1", "1", code);

        RuleField ruleField16 = createruleField("RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "ts", "短期设定天数", code);
        createruleConfigure(ruleField16.getId().toString(), "", "10", "10", code);

        RuleField ruleField17 = createruleField("RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn1", "转入次数", code);
        createruleConfigure(ruleField17.getId().toString(), ">", ">2", "2", code);

        RuleField ruleField18 = createruleField("RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn2", "转出次数", code);
        createruleConfigure(ruleField18.getId().toString(), "between", "between 1 and 3 ", "1,3", code);

        RuleField ruleField19 = createruleField("RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn3", "不同地区", code);
        createruleConfigure(ruleField19.getId().toString(), "=", "=1", "1", code);

        RuleField ruleField20 = createruleField("RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "jyje", "大额交易限额", code);
        createruleConfigure(ruleField20.getId().toString(), ">", ">500000", "500000", code);

        RuleField ruleField21 = createruleField("RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "rn", "次数", code);
        createruleConfigure(ruleField21.getId().toString(), ">", ">3", "3", code);

        RuleField ruleField22 = createruleField("RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts", "短期设定天数", code);
        createruleConfigure(ruleField22.getId().toString(), "", "10", "10", code);

        RuleField ruleField23 = createruleField("RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts1", "停止使用天数", code);
        createruleConfigure(ruleField23.getId().toString(), ">", ">20", "20", code);

        RuleField ruleField24 = createruleField("RISK_2016", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "rn", "交易发生次数", code);
        createruleConfigure(ruleField24.getId().toString(), ">", ">3", "3", code);

        RuleField ruleField25 = createruleField("RISK_2016", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts", "短期设定天数", code);
        createruleConfigure(ruleField25.getId().toString(), "", "10", "10", code);

        RuleField ruleField26 = createruleField("RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "jyje", "大额交易限额", code);
        createruleConfigure(ruleField26.getId().toString(), ">", ">100000", "100000", code);

        RuleField ruleField27 = createruleField("RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "ts", "间隔天数", code);
        createruleConfigure(ruleField27.getId().toString(), "", "7", "7", code);

        RuleField ruleField28 = createruleField("RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "cs", "次数", code);
        createruleConfigure(ruleField28.getId().toString(), ">", ">3", "3", code);

        RuleField ruleField29 = createruleField("RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "cs1", "次数", code);
        createruleConfigure(ruleField29.getId().toString(), ">", ">3", "3", code);

        RuleField ruleField30 = createruleField("RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "sjyje", "绝对金额", code);
        createruleConfigure(ruleField30.getId().toString(), "<", "<1000", "1000", code);

        RuleField ruleField31 = createruleField("RISK_2024", "长期未发生业务又突然发生大额资金收付的账户", "jyje", "大额交易限额", code);
        createruleConfigure(ruleField31.getId().toString(), ">", ">100000", "100000", code);

        RuleField ruleField32 = createruleField("RISK_2024", "长期未发生业务又突然发生大额资金收付的账户", "ts", "间隔天数", code);
        createruleConfigure(ruleField32.getId().toString(), "", "180", "180", code);

        RuleField ruleField33 = createruleField("RISK_2010", "单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的", "jyje", "大额交易限额", code);
        createruleConfigure(ruleField33.getId().toString(), ">", ">100000", "100000", code);

        RuleField ruleField34 = createruleField("RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "rn", "交易次数", code);
        createruleConfigure(ruleField34.getId().toString(), ">", ">1", "1", code);

        RuleField ruleField35 = createruleField("RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "rn1", "借贷交易次数", code);
        createruleConfigure(ruleField35.getId().toString(), ">", ">0", "0", code);

        RuleField ruleField41 = createruleField("RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "ts", "天数", code);
        createruleConfigure(ruleField41.getId().toString(), "", "7", "7", code);


        RuleField ruleField36 = createruleField("RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "ts", "交易期限", code);
        createruleConfigure(ruleField36.getId().toString(), "", "10", "10", code);

        RuleField ruleField37 = createruleField("RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "rn", "交易次数", code);
        createruleConfigure(ruleField37.getId().toString(), ">", ">2", "2", code);

        RuleField ruleField38 = createruleField("RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "rn1", "借贷交易次数", code);
        createruleConfigure(ruleField38.getId().toString(), ">", ">2", "2", code);

        RuleField ruleField39 = createruleField("RISK_2017", "新开单位结算账户，半年内未发生交易", "ts", "半年内", code);
        createruleConfigure(ruleField39.getId().toString(), "", "180", "180", code);

        RuleField ruleField40 = createruleField("RISK_2022", "企业注册时间与开立基本账户间隔超过半年以上", "ts", "半年内", code);
        createruleConfigure(ruleField40.getId().toString(), ">=", ">=180", "180", code);


        RuleField ruleField50 = createruleField("RISK_2021", "存在拆分交易，故意规避交易限额", "jyje", "大额交易限额", code);
        createruleConfigure(ruleField50.getId().toString(), ">", ">10000", "10000", code);
        RuleField ruleField51 = createruleField("RISK_2021", "存在拆分交易，故意规避交易限额", "rn", "次数", code);
        createruleConfigure(ruleField51.getId().toString(), ">", ">5", "5", code);
        RuleField ruleField52 = createruleField("RISK_2021", "存在拆分交易，故意规避交易限额", "ts", "交易设定天数", code);
        createruleConfigure(ruleField52.getId().toString(), "", "10", "10", code);

        RuleField ruleField2008 = createruleField("RISK_2008", "同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式", "", "", code);
        createruleConfigure(ruleField2008.getId().toString(), "", "", "", code);
        RuleField ruleField2015 = createruleField("RISK_2015", "营业执照证件、法人身份证等证件到期，账户还在发生交易行为", "", "", code);
        createruleConfigure(ruleField2015.getId().toString(), "", "", "", code);
        RuleField ruleField2009 = createruleField("RISK_2009", "对公客户企业已经被注销或者吊销，但仍存在账户交易行为", "", "", code);
        createruleConfigure(ruleField2009.getId().toString(), "", "", "", code);

    }

    public RuleField createruleField(String modelId, String modelName, String field, String fieldName, String code) {
        RuleField ruleField = new RuleField();
        ruleField.setModelId(modelId);
        ruleField.setModelName(modelName);
        ruleField.setField(field);
        ruleField.setFieldName(fieldName);
        ruleField.setCorporateBank(code);
        RuleField save = ruleFieldDao.save(ruleField);
        return save;
    }
}
