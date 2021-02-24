package com.ideatech.ams.account.listener;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.event.AccountsAllEvent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountsAllListener implements ApplicationListener<AccountsAllEvent> {
    @Autowired
    private AccountsAllService accountsAllService;

    @Override
    public void onApplicationEvent(AccountsAllEvent accountsAllEvent) {
        CustomerAllResponse customerAllResponse = accountsAllEvent.getCustomerAllResponse();
        Long customerLogId = accountsAllEvent.getCustomerLogId();

        List<AccountsAllInfo> list = accountsAllService.findByCustomerLogId(customerLogId);
        if(CollectionUtils.isNotEmpty(list)) {
            customerAllResponse.setString001(list.get(0).getAcctType().name());
        }


    }
}
