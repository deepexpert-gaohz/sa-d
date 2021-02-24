package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.rule.dao.RuleFieldDao;
import com.ideatech.ams.risk.rule.entity.RuleField;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleFieldInitializer extends AbstractDataInitializer {

    @Autowired
    RuleFieldDao ruleFieldDao;

    @Override
    protected void doInit() throws Exception {
        //开户类模型规则初始化
        createruleConfigure(1L, "RISK_2001", "开户人频繁开户、销户或变更账户信息", "rn", "开户或销户次数");
        createruleConfigure(2L, "RISK_2001", "开户人频繁开户、销户或变更账户信息", "ts", "频繁天数范围");
        //createruleConfigure(14L,"RISK_2009","所有开户人频繁开户、销户或变更账户信息","rn","开户或销户次数");
        //createruleConfigure(15L,"RISK_2009","所有开户人频繁开户、销户或变更账户信息","ts","频繁天数范围");
        createruleConfigure(3L, "RISK_2002", "单位同一注册地址被多次使用", "rn", "注册地址次数");
        //createruleConfigure(4L,"RISK_2003","双异地（注册地、经营地）单位开立银行账户","rn","异地开立");
        createruleConfigure(11L, "RISK_2005", "同一法定代表人或负责人开立多个账户", "rn", "账户个数");
        createruleConfigure(12L, "RISK_2006", "同一经办人员代理多个主体开立账户", "rn", "账户个数");


        //新加模型规则
        createruleConfigure(56L, "RISK_3001", "法定代表人或单位负责人年龄小于20岁大于65岁", "rn1", "最小年龄");
        createruleConfigure(57L, "RISK_3001", "法定代表人或单位负责人年龄小于20岁大于65岁", "rn2", "最大年龄");

        //交易类模型规则初始化
//        createruleConfigure(16L, "RISK_1001", "新开户15日发生100万以上交易", "ts", "天数");
//        createruleConfigure(17L, "RISK_1001", "新开户15日发生100万以上交易", "jyje", "交易金额");
//        createruleConfigure(18L, "RISK_1002", "印鉴卡变更15日内发生50万交易", "ts", "天数");
//        createruleConfigure(19L, "RISK_1002", "印鉴卡变更15日内发生50万交易", "jyje", "交易金额");
//        createruleConfigure(20L, "RISK_2019", "新开立账户短期内有大额资金汇入汇出", "ts", "天数");
//        createruleConfigure(21L, "RISK_2019", "新开立账户短期内有大额资金汇入汇出", "jyje", "交易金额");
//        createruleConfigure(22L, "RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "ts", "天数");
//        createruleConfigure(23L, "RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn1", "转出次数");
//        createruleConfigure(24L, "RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn2", "转入次数");
//        createruleConfigure(25L, "RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn3", "不同地区");
//        createruleConfigure(26L, "RISK_2012", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "rn4", "单次交易");
//        createruleConfigure(27L, "RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "ts", "短期设定天数");
//        createruleConfigure(28L, "RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn1", "转入次数");
//        createruleConfigure(29L, "RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn2", "转出次数");
//        createruleConfigure(30L, "RISK_2011", "短期内资金分散转入、集中转出，尤其是资金来源多个地区", "rn3", "不同地区");
//        createruleConfigure(31L, "RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "jyje", "大额交易限额");
//        createruleConfigure(32L, "RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "rn", "次数");
//        createruleConfigure(33L, "RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts", "短期设定天数");
//        createruleConfigure(34L, "RISK_2020", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts1", "停止使用天数");
//        createruleConfigure(35L, "RISK_2016", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "rn", "交易发生次数");
//        createruleConfigure(36L, "RISK_2016", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "ts", "短期设定天数");
//        createruleConfigure(37L, "RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "jyje", "大额交易限额");
//        createruleConfigure(38L, "RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "ts", "间隔天数");
//        createruleConfigure(39L, "RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "cs", "次数");
//        createruleConfigure(40L, "RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "cs1", "次数");
//        createruleConfigure(41L, "RISK_2023", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "sjyje", "绝对金额");
//        createruleConfigure(42L, "RISK_2024", "长期未发生业务又突然发生大额资金收付的账户", "jyje", "大额交易限额");
//        createruleConfigure(43L, "RISK_2024", "长期未发生业务又突然发生大额资金收付的账户", "ts", "间隔天数");
//
//        createruleConfigure(44L, "RISK_2010", "单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的", "jyje", "大额交易限额");
//        createruleConfigure(45L, "RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "rn", "交易次数");
//        createruleConfigure(46L, "RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "rn1", "借贷交易次数");
//        createruleConfigure(47L, "RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "ts", "交易期限");
//        createruleConfigure(48L, "RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "rn", "交易次数");
//        createruleConfigure(49L, "RISK_2014", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "rn1", "借贷交易次数");
//
//        createruleConfigure(50L, "RISK_2017", "新开单位结算账户，半年内未发生交易", "ts", "半年内");
//
//        createruleConfigure(51L, "RISK_2022", "企业注册时间与开立基本账户间隔超过半年以上", "ts", "半年内");
//        createruleConfigure(52L, "RISK_2013", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "ts", "天数");
//
//        createruleConfigure(53L, "RISK_2021", "存在拆分交易，故意规避交易限额", "jyje", "大额交易限额");
//        createruleConfigure(54L, "RISK_2021", "存在拆分交易，故意规避交易限额", "rn", "次数");
//        createruleConfigure(55L, "RISK_2021", "存在拆分交易，故意规避交易限额", "ts", "交易设定天数");


    }

    public void createruleConfigure(Long id, String modelId, String modelName, String field, String fieldName) {
        RuleField ruleField = new RuleField();
        ruleField.setId(id);
        ruleField.setModelId(modelId);
        ruleField.setModelName(modelName);
        ruleField.setField(field);
        ruleField.setFieldName(fieldName);
        ruleField.setCorporateBank("root");
        ruleFieldDao.save(ruleField);
    }

    @Override
    protected boolean isNeedInit() {
        return ruleFieldDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
