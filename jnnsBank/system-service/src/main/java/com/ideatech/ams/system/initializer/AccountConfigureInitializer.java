package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.configuration.dao.AccountConfigureDao;
import com.ideatech.ams.system.configuration.entity.AccountConfigurePo;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountConfigureInitializer extends AbstractDataInitializer {

    @Autowired
    private AccountConfigureDao accountConfigureDao;


    @Override
    protected void doInit() throws Exception {
        //基本户开户  //01 ： 企业法人  02 ： 非企业法人  13 ： 有字号个体工商户  14： 无字号个体工商户
        createAccountConfigure("jiben","01","ACCT_OPEN");
        createAccountConfigure("jiben","02","ACCT_OPEN");
        createAccountConfigure("jiben","13","ACCT_OPEN");
        createAccountConfigure("jiben","14","ACCT_OPEN");
        //基本户变更
        createAccountConfigure("jiben","01","ACCT_CHANGE");
        createAccountConfigure("jiben","02","ACCT_CHANGE");
        createAccountConfigure("jiben","13","ACCT_CHANGE");
        createAccountConfigure("jiben","14","ACCT_CHANGE");
        //基本户销户
        createAccountConfigure("jiben","01","ACCT_REVOKE");
        createAccountConfigure("jiben","02","ACCT_REVOKE");
        createAccountConfigure("jiben","13","ACCT_REVOKE");
        createAccountConfigure("jiben","14","ACCT_REVOKE");

        //非临时开户 非临时变更 非临时销户 非临时展期  只有账户类型跟业务状态
        createAccountConfigure("feilinshi","","ACCT_OPEN");
        createAccountConfigure("feilinshi","","ACCT_CHANGE");
        createAccountConfigure("feilinshi","","ACCT_REVOKE");
        createAccountConfigure("feilinshi","","ACCT_EXTENSION");
    }

    public void createAccountConfigure(String acctType,String depositorType,String operateType){
        AccountConfigurePo accountConfigurePo = new AccountConfigurePo();
        accountConfigurePo.setAcctType(acctType);
        accountConfigurePo.setDepositorType(depositorType);
        accountConfigurePo.setOperateType(operateType);
        accountConfigureDao.save(accountConfigurePo);
    }

    @Override
    protected boolean isNeedInit() {
        return accountConfigureDao.count() < 1;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
