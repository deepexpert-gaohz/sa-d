package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.model.dao.ModelDao;
import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelInitializer extends AbstractDataInitializer {
    @Autowired
    ModelDao modelDao;

    @Override
    protected void doInit() throws Exception {
        createModel("基本开户人频繁开户、销户或变更账户信息", "RISK_2001", "1", "基本开户人频繁开户、销户或变更账户信息", "1002", "1002");
        createModel("进行对单位同一注册地址被多次使用的账户筛选。", "RISK_2002", "1", "进行对单位同一注册地址被多次使用的账户筛选。", "1002", "1002");
        createModel("进行对双异地（注册地、经营地）单位开立银行账户的筛选。", "RISK_2003", "1", "进行对双异地（注册地、经营地）单位开立银行账户的筛选。", "1002", "1002");
        //createModel("不同主体账户开户资料存在较密切关联，如不同主体的开户时间、地点、联系地址、手机号相同或相近且交易对手相同","RISK_2004","1","不同主体账户开户资料存在较密切关联，如不同主体的开户时间、地点、联系地址、手机号相同或相近且交易对手相同");
        createModel("进行对同一法定代表人或负责人开立多个账户进行筛选。", "RISK_2005", "1", "进行对同一法定代表人或负责人开立多个账户进行筛选。", "1002", "1002");
        createModel("进行对同一经办人员代理多个主体开立账户进行筛选", "RISK_2006", "1", "进行对同一经办人员代理多个主体开立账户进行筛选", "1002", "1002");
        createModel("多个主体的法定代表人、财务负责人、经办人员预留同一个联系方式等的账户筛选。", "RISK_2007", "1", "多个主体的法定代表人、财务负责人、经办人员预留同一个联系方式等的账户筛选。", "1002", "1002");
        createModel("同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式", "RISK_2008", "1", "同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式", "1002", "1002");

        createModel("法定代表人或单位负责人年龄小于22岁大于60岁", "RISK_3001", "1", "法定代表人或单位负责人年龄小于20岁大于60岁", "1002", "1002");
        createModel("法定代表人或单位负责人非常州本地", "RISK_3002", "1", "法定代表人或单位负责人非常州本地", "1002", "1002");


        //createModel("所有开户人频繁开户、销户或变更账户信息","RISK_2009","1","所有开户人频繁开户、销户或变更账户信息","1002","1002");
//        createModel("新开户15日发生100万以上交易", "RISK_1001", "1", "新开户15日发生100万以上交易", "1001", "1002");
//        createModel("印鉴卡变更15日内发生50万交易", "RISK_1002", "1", "印鉴卡变更15日内发生50万交易", "1001", "1002");
//        createModel("新开立账户短期内有大额资金汇入汇出", "RISK_2019", "1", "新开立账户短期内有大额资金汇入汇出", "1001", "1002");
//        createModel("短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "RISK_2012", "1", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "1001", "1002");
//        createModel("营业执照证件、法人身份证等证件到期，账户还在发生交易行为", "RISK_2015", "1", "营业执照证件、法人身份证等证件到期，账户还在发生交易行为", "1001", "1002");
//        createModel("账户短期内发生频繁或大额交易,后突然停止使用或者销户", "RISK_2020", "1", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "1001", "1002");
//        createModel("短期内资金分散转入、集中转出，尤其是资金来源多个地区", "RISK_2011", "1", "短期内资金分散转入、集中转出，尤其是资金来源多个地", "1001", "1002");
//        createModel("对公客户企业已经被注销或者吊销，但仍存在账户交易行为", "RISK_2009", "1", "对公客户企业已经被注销或者吊销，但仍存在账户交易行为", "1001", "1002");
//        createModel("单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的", "RISK_2010", "1", "单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的", "1001", "1002");
//        createModel("相同收付款人之间短期内频繁发生资金收付", "RISK_2016", "0", "相同收付款人之间短期内频繁发生资金收付", "1001", "1002");
//        createModel("短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "RISK_2023", "1", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "1001", "1002");
//        createModel("长期未发生业务又突然发生大额资金收付的账户", "RISK_2024", "1", "长期未发生业务又突然发生大额资金收付的账户", "1001", "1002");
//
//        createModel("账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "RISK_2013", "1", "账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转", "1001", "1002");
//        createModel("公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "RISK_2014", "1", "公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)", "1001", "1002");
//
        createModel("超过两个对账周期未实现有效对账的账户", "RISK_4001", "0", "超过两个对账周期未实现有效对账的账户", "1003", "1002");
        createModel("对账地址与经营地址不符", "RISK_4002", "0", "对账地址与经营地址不符", "1003", "1002");
//        createModel("存在拆分交易，故意规避交易限额", "RISK_2021", "0", "存在拆分交易，故意规避交易限额", "1001", "1002");
//        createModel("企业注册时间与开立基本账户间隔超过半年以上", "RISK_2022", "0", "企业注册时间与开立基本账户间隔超过半年以上", "1001", "1002");
//        createModel("新开单位结算账户，半年内未发生交易", "RISK_2017", "0", "新开单位结算账户，半年内未发生交易", "1001", "1002");


    }


    public void createModel(String name, String modelId, String status, String mdesc, String typeId, String levelId) {
        Model model = new Model();
        model.setName(name);
        model.setModelId(modelId);
        model.setStatus(status);
        model.setMdesc(mdesc);
        model.setCorporateBank("root");
        model.setTypeId(typeId);
        model.setLevelId(levelId);
        modelDao.save(model);
    }

    @Override
    protected boolean isNeedInit() {
        return modelDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
