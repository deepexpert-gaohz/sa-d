package com.ideatech.ams.system.configuration.service;

import com.ideatech.ams.system.configuration.dao.AccountConfigureDao;
import com.ideatech.ams.system.configuration.dto.AccountConfigureDto;
import com.ideatech.ams.system.configuration.entity.AccountConfigurePo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountConfigureServiceImpl extends BaseServiceImpl<AccountConfigureDao, AccountConfigurePo,AccountConfigureDto> implements AccountConfigureService{

    @Autowired
    private AccountConfigureDao accountConfigureDao;

    @Override
    public void del(Long id) {
        accountConfigureDao.delete(id);
    }

    @Override
    public AccountConfigureDto query(String acctType, String depositorType, String operateType) {
        AccountConfigureDto accountConfigureDto = null;
        //有存款人类型的查询条件
        if("jiben".equals(acctType) || "linshi".equals(acctType) || "teshu".equals(acctType)){
            accountConfigureDto = ConverterService.convert(accountConfigureDao.findByAcctTypeAndDepositorTypeAndOperateType(acctType,depositorType,operateType),AccountConfigureDto.class);
        }else{

        }
        return accountConfigureDto;
    }
}
