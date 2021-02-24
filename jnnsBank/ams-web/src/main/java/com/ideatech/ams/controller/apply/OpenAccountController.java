package com.ideatech.ams.controller.apply;

import com.ideatech.ams.apply.dto.ApplyCustomerPublicMidDto;
import com.ideatech.ams.apply.service.OpenAccountService;
import com.ideatech.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openAccount")
public class OpenAccountController {

    @Autowired
    private OpenAccountService openAccountService;

    @RequestMapping(value = "/customerInfo/preOpenAccountId/{id}", method = RequestMethod.GET)
    public ObjectRestResponse<ApplyCustomerPublicMidDto> getCustomerInfoDetailsByPreOpenAccountId(@PathVariable Long id) {
        ApplyCustomerPublicMidDto customerPublicMidDto =  openAccountService.getCustomerInfoBySourceId(id);
        return new ObjectRestResponse<ApplyCustomerPublicMidDto>().result(customerPublicMidDto).rel(true);
    }

}
