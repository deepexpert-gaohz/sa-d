package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.rule.dao.RuleConfigureDao;
import com.ideatech.ams.risk.rule.entity.RuleConfigure;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigInitializer extends AbstractDataInitializer {

    @Autowired
    RuleConfigureDao ruleConfigureDao;

    @Override
    protected void doInit() throws Exception {

        //createruleConfigure("4",">=",">=3","3");
        createruleConfigure("3", ">=", ">=3", "3");
        createruleConfigure("19", ">=", ">=500000", "500000");
        createruleConfigure("18", "", "15", "15");
        createruleConfigure("17", ">=", ">=1000000", "1000000");
        createruleConfigure("16", "", "15", "15");
        createruleConfigure("14", ">=", ">=3", "3");
        createruleConfigure("13", ">=", ">=3", "3");
        createruleConfigure("12", ">=", ">=3", "3");
        createruleConfigure("11", ">=", ">=3", "3");
        createruleConfigure("1", ">=", ">=3", "3");
        createruleConfigure("2", "", "7", "7");
        createruleConfigure("15", ">=", ">=3", "3");

        createruleConfigure("56", "<", "<20", "20");
        createruleConfigure("57", ">", ">60", "60");

        //交易类规则内容
//        createruleConfigure("20", "", "10", "10");
//        createruleConfigure("21", ">=", ">=100000", "100000");
//        createruleConfigure("22", "", "10", "10");
//        createruleConfigure("23", ">", ">2", "2");
//        createruleConfigure("24", "between", "between 1 and 3 ", "1,3");
//        createruleConfigure("25", "=", "=1", "1");
//        createruleConfigure("26", "=", "=1", "1");
//        createruleConfigure("27", "", "10", "10");
//        createruleConfigure("28", ">", ">2", "2");
//        createruleConfigure("29", "between", "between 1 and 3 ", "1,3");
//        createruleConfigure("30", "=", "=1", "1");
//        createruleConfigure("31", ">", ">500000", "500000");
//        createruleConfigure("32", ">", ">3", "3");
//        createruleConfigure("33", "", "10", "10");
//        createruleConfigure("34", ">", ">20", "20");
//        createruleConfigure("35", ">", ">3", "3");
//        createruleConfigure("36", "", "10", "10");
//        createruleConfigure("37", ">", ">100000", "100000");
//        createruleConfigure("38", "", "7", "7");
//        createruleConfigure("39", ">", ">3", "3");
//        createruleConfigure("40", ">", ">3", "3");
//        createruleConfigure("41", "<", "<1000", "1000");
//        createruleConfigure("42", ">", ">100000", "100000");
//        createruleConfigure("43", "", "180", "180");
//        createruleConfigure("44", ">", ">100000", "100000");
//        createruleConfigure("45", ">", ">1", "1");
//        createruleConfigure("46", ">", ">0", "0");
//        createruleConfigure("47", "", "10", "10");
//        createruleConfigure("48", ">", ">2", "2");
//        createruleConfigure("49", ">", ">2", "2");
//        createruleConfigure("50", "", "180", "180");
//        createruleConfigure("51", ">=", ">=180", "180");
//        createruleConfigure("52", "", "7", "7");
//
//        createruleConfigure("53", ">", ">10000", "10000");
//        createruleConfigure("54", ">", ">5", "5");
//        createruleConfigure("55", "", "10", "10");

    }

    public void createruleConfigure(String ruleId, String condition, String conAndVal, String val) {
        RuleConfigure ruleConfigure = new RuleConfigure();
        ruleConfigure.setRuleId(ruleId);
        ruleConfigure.setCondition(condition);
        ruleConfigure.setConAndVal(conAndVal);
        ruleConfigure.setValue(val);
        ruleConfigure.setCorporateBank("root");
        ruleConfigureDao.save(ruleConfigure);
    }

    @Override
    protected boolean isNeedInit() {
        return ruleConfigureDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
