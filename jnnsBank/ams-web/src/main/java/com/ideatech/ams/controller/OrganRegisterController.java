package com.ideatech.ams.controller;

import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organRegister")
@Slf4j
public class OrganRegisterController {
    @Autowired
    private OrganRegisterService organRegisterService;

    @GetMapping("/getOrganRegisterFlag")
    public Boolean getOrganRegisterFlag() {
        return organRegisterService.getOrganRegisterIsNull(SecurityUtils.getCurrentOrgFullId());
    }
    @GetMapping("/getOrganRegisterFlagByBankCode")
    public Boolean getOrganRegisterFlagByBankCode(String bankCode) {

        return organRegisterService.getOrganRegisterFlagByBankCode(bankCode);
    }

}
