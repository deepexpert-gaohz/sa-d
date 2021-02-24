package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.model.dao.RiskConfigerFieldDao;
import com.ideatech.ams.risk.model.entity.RiskConfigerField;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiskConfigFieldInitializer extends AbstractDataInitializer {

    @Autowired
    RiskConfigerFieldDao riskConfigerFieldDao;

    @Override
    protected void doInit() throws Exception {
        createConfigureField(1L,"yd_acct_no","账户号");
        createConfigureField(2L,"yd_acct_name","账户名称");
        createConfigureField(3L,"yd_customer_id","客户号");
        createConfigureField(4L,"yd_depositor_name","客户名称");
        createConfigureField(5L,"yd_acct_create_date","开户日期");
        createConfigureField(6L,"yd_reg_address","注册地址");
        createConfigureField(7L,"yd_legal_idcard_no","法人证件号");
        createConfigureField(8L,"yd_legal_name","法人姓名");
        createConfigureField(9L,"yd_operator_idcard_no","经办人证件号");
        createConfigureField(10L,"yd_operator_name","经办人姓名");
        createConfigureField(11L,"yd_work_address","经营地地址");
        createConfigureField(12L,"yd_bank_name","开户机构");
        createConfigureField(13L,"yd_finance_idcard_no","财务负责人证件号");
        createConfigureField(14L,"yd_finance_name","财务负责人姓名");
        createConfigureField(15L,"yd_bill_type","操作类型");
        createConfigureField(16L,"yd_trade_date","交易日期");
        createConfigureField(17L,"yd_trade_time","交易时间");
        createConfigureField(18L,"yd_serial_Id","交易流水号");
        createConfigureField(19L,"yd_amount","交易金额");
        createConfigureField(20L,"yd_start_date","印鉴卡启用日期");
        createConfigureField(21L,"yd_serial_date","流水日期");
        createConfigureField(22L,"yd_finance_telephone","财务负责人电话");
        createConfigureField(23L,"yd_legal_telephone","法人电话");
        createConfigureField(24L,"yd_operator_telephone","经办人电话");
        createConfigureField(25L,"yd_acct_type","账户性质");
    }

    public void createConfigureField(Long id,String field,String fieldName){
        RiskConfigerField riskConfigerField = new RiskConfigerField();
        riskConfigerField.setId(id);
        riskConfigerField.setField(field);
        riskConfigerField.setFieldName(fieldName);
        riskConfigerFieldDao.save(riskConfigerField);
    }

    @Override
    protected boolean isNeedInit() {
        riskConfigerFieldDao.deleteAll();
        return riskConfigerFieldDao.count() < 1;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
