package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.model.dao.ModelDao;
import com.ideatech.ams.risk.model.dao.ModelFieldDao;
import com.ideatech.ams.risk.model.entity.ModelField;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelFieldInitializer extends AbstractDataInitializer {
    @Autowired
    ModelDao modelDao;

    @Autowired
    ModelFieldDao modelFieldDao;

    @Override
    protected void doInit() throws Exception {
//        createModelField("yd_acct_no", "账户号", "RISK_1001", 0, 0, 1);
//        createModelField("yd_acct_name", "账户名称", "RISK_1001", 0, 0, 2);
//        createModelField("yd_trade_time", "交易时间", "RISK_1001", 0, 0, 3);
//        createModelField("yd_amount", "交易金额", "RISK_1001", 0, 0, 4);
//
//
//        createModelField("yd_acct_no", "账户号", "RISK_1002", 0, 0, 1);
//        createModelField("yd_acct_name", "账户名称", "RISK_1002", 0, 0, 2);
//        createModelField("yd_amount", "交易金额", "RISK_1002", 0, 0, 3);
//        createModelField("yd_trade_date", "交易日期", "RISK_1002", 0, 0, 4);
//        createModelField("yd_start_date", "印鉴卡启用日期", "RISK_1002", 0, 0, 5);
//        createModelField("yd_serial_Id", "交易流水号", "RISK_1002", 0, 0, 6);


        createModelField("yd_customer_id", "客户号", "RISK_2001", 0, 0, 1);
        createModelField("yd_acct_no", "账户号", "RISK_2001", 0, 0, 2);
        createModelField("yd_depositor_name", "客户名称", "RISK_2001", 0, 0, 3);
        createModelField("yd_acct_name", "账户名称", "RISK_2001", 0, 0, 4);

        createModelField("yd_customer_id", "客户号", "RISK_2002", 0, 0, 1);
        createModelField("yd_depositor_name", "客户名称", "RISK_2002", 0, 0, 2);
        createModelField("yd_reg_address", "注册地址", "RISK_2002", 0, 0, 3);


        createModelField("yd_work_address", "经营地地址", "RISK_2003", 0, 0, 2);
        createModelField("yd_depositor_name", "客户名称", "RISK_2003", 0, 0, 0);
        createModelField("yd_acct_create_date", "开户日期", "RISK_2003", 0, 0, 2);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2003", 0, 0, 1);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 2);
        createModelField("yd_bank_name", "开户机构", "RISK_2003", 0, 0, 3);
        createModelField("yd_legal_name", "法人姓名", "RISK_2003", 0, 0, 5);
        createModelField("yd_bank_name", "开户机构", "RISK_2003", 0, 0, 6);
        createModelField("yd_finance_idcard_no", "财务主管证件号", "RISK_2003", 0, 0, 8);

        createModelField("yd_legal_name", "法人姓名", "RISK_2005", 0, 0, 0);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2005", 0, 0, 4);
        createModelField("yd_bank_name", "开户机构", "RISK_2005", 0, 0, 1);
        createModelField("yd_acct_name", "账户名称", "RISK_2005", 0, 0, 3);
        createModelField("yd_acct_no", "账户号", "RISK_2005", 0, 0, 2);

        createModelField("yd_legal_name", "法人姓名", "RISK_2006", 0, 0, 0);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2006", 0, 0, 1);


        createModelField("yd_legal_telephone", "法人电话", "RISK_2007", 0, 0, 8);
        createModelField("yd_legal_name", "法人姓名", "RISK_2007", 0, 0, 5);
        createModelField("yd_finance_telephone", "财务负责人电话", "RISK_2007", 0, 0, 10);
        createModelField("yd_finance_name", "财务负责人姓名", "RISK_2007", 0, 0, 11);
        createModelField("yd_finance_idcard_no", "财务负责人证件号", "RISK_2007", 0, 0, 12);
        createModelField("yd_depositor_name", "客户名称", "RISK_2007", 0, 0, 0);
        createModelField("yd_customer_id", "客户号", "RISK_2007", 0, 0, 1);
        createModelField("yd_bank_name", "开户机构", "RISK_2007", 0, 0, 2);
        createModelField("yd_acct_create_date", "开户日期", "RISK_2007", 0, 0, 3);
        createModelField("yd_operator_name", "经办人姓名", "RISK_2007", 0, 0, 7);
        createModelField("yd_operator_telephone", "经办人电话", "RISK_2007", 0, 0, 9);
        createModelField("yd_operator_idcard_no", "经办人证件号", "RISK_2007", 0, 0, 12);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2007", 0, 0, 14);

        createModelField("yd_reg_address", "注册地址", "RISK_2008", 0, 0, 9);
        createModelField("yd_depositor_name", "客户名称", "RISK_2008", 0, 0, 0);
        createModelField("yd_customer_id", "客户号", "RISK_2008", 0, 0, 1);
        createModelField("yd_operator_idcard_no", "经办人证件号", "RISK_2008", 0, 0, 2);
        createModelField("yd_legal_name", "法人姓名", "RISK_2008", 0, 0, 3);
        createModelField("yd_legal_telephone", "法人电话", "RISK_2008", 0, 0, 4);
        createModelField("yd_finance_idcard_no", "财务主管证件号", "RISK_2008", 0, 0, 5);
        createModelField("yd_finance_name", "财务主管名称", "RISK_2008", 0, 0, 6);
        createModelField("yd_operator_name", "经办人姓名", "RISK_2008", 0, 0, 8);
        createModelField("yd_finance_idcard_no", "财务负责人证件号", "RISK_2008", 0, 0, 5);
        createModelField("yd_finance_telephone", "财务负责人电话", "RISK_2008", 0, 0, 7);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 0);
        createModelField("yd_operator_telephone", "经办人电话", "RISK_2008", 0, 0, 9);
        createModelField("yd_bank_name", "开户机构", "RISK_2008", 0, 0, 7);
        createModelField("yd_finance_name", "财务负责人姓名", "RISK_2008", 0, 0, 6);

        //新增账户性质
//        createModelField("yd_acct_type", "账户性质", "RISK_1001", 0, 0, 14);
//        createModelField("yd_acct_type", "账户性质", "RISK_1002", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2001", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2002", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2003", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2005", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2006", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2007", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2008", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_3001", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_3002", 0, 0, 14);

        createModelField("yd_risk_desc", "风险原因", "RISK_1001", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_1002", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2001", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2002", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2003", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2005", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2006", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2007", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_2008", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_3001", 0, 0, 15);
        createModelField("yd_risk_desc", "风险原因", "RISK_3002", 0, 0, 15);

        createModelField("yd_account_no", "账户号", "RISK_4001", 0, 0, 1);
        createModelField("yd_risk_desc", "风险原因", "RISK_4001", 0, 0, 2);

        createModelField("yd_account_no", "账户号", "RISK_4002", 0, 0, 1);
        createModelField("yd_acct_name", "账户名称", "RISK_4002", 0, 0, 2);
        createModelField("yd_risk_desc", "风险原因", "RISK_4002", 0, 0, 3);

        createModelField("yd_depositor_name", "客户名称", "RISK_3001", 0, 0, 0);
        createModelField("yd_legal_name", "法人姓名", "RISK_3001", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_3001", 0, 0, 2);

        createModelField("yd_depositor_name", "客户名称", "RISK_3002", 0, 0, 0);
        createModelField("yd_legal_name", "法人姓名", "RISK_3002", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_3002", 0, 0, 2);

    }

    public void createModelField(String fieldsEn, String fieldsZh, String modelId, Integer showFlag, Integer exportFlag, Integer orderFlag) {
        ModelField modelField = new ModelField();
        modelField.setFieldsEn(fieldsEn);
        modelField.setFieldsZh(fieldsZh);
        modelField.setModelId(modelId);
        modelField.setShowFlag(showFlag);
        modelField.setExportFlag(exportFlag);
        modelField.setOrderFlag(orderFlag);
        modelFieldDao.save(modelField);
    }

    @Override
    protected boolean isNeedInit() {
        return modelFieldDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
