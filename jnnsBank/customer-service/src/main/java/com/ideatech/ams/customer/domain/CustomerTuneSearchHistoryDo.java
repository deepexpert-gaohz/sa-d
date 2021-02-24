package com.ideatech.ams.customer.domain;

import com.ideatech.ams.customer.entity.CustomerTuneSearchHistory;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.entity.UserPo;
import lombok.Data;

@Data
public class CustomerTuneSearchHistoryDo {

    private CustomerTuneSearchHistory customerTuneSearchHistory;
    private OrganizationPo organizationPo;
    private UserPo userPo;

    public CustomerTuneSearchHistoryDo(CustomerTuneSearchHistory customerTuneSearchHistory, OrganizationPo organizationPo, UserPo userPo) {
        this.customerTuneSearchHistory = customerTuneSearchHistory;
        this.organizationPo = organizationPo;
        this.userPo = userPo;
    }
}
