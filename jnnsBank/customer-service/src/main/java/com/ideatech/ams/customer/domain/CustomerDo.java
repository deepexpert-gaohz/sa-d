package com.ideatech.ams.customer.domain;

import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.entity.CustomersAll;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import lombok.Data;

@Data
public class CustomerDo {

    private CustomersAll customersAll;
    private CustomerPublic customerPublic;
    private OrganizationPo organizationPo;

    public CustomerDo(CustomersAll customersAll, CustomerPublic customerPublic, OrganizationPo organizationPo) {
        this.customersAll = customersAll;
        this.customerPublic = customerPublic;
        this.organizationPo = organizationPo;
    }
}
