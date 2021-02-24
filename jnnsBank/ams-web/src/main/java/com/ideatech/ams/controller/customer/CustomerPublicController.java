package com.ideatech.ams.controller.customer;

import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/customerPublic")
public class CustomerPublicController {

    @Autowired
    private CustomersAllService customersAllService;
    @Autowired
    private OrganizationService organizationService;

    /**
     * 客户列表查询
     * @param info
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableResultResponse<CustomerAllResponse> pre(CustomerAllResponse info, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<CustomerAllResponse> tableResultResponse =  customersAllService.query(info, pageable);

        return tableResultResponse;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveCustomerPublic(CustomerAllResponse info) {
        customersAllService.saveCustomerInfo(info);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public CustomerAllResponse findById(Long customerId) {
        return customersAllService.findById(customerId);
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public CustomersAllInfo findByName(String name) {
        CustomersAllInfo customersAllInfo = customersAllService.findByDepositorName(name);
        if (customersAllInfo != null && customersAllInfo.getOrganFullId() != null) {
            OrganizationDto organ = organizationService.findByOrganFullId(customersAllInfo.getOrganFullId());
            if (organ != null) {
                customersAllInfo.setOrganName(organ.getName());
            }
        }
        return customersAllInfo;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editCustomerPublic(CustomerAllResponse info) {
        customersAllService.editCustomerInfo(info);
    }

}
