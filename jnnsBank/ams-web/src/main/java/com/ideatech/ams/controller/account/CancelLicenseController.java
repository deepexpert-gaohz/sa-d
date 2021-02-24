package com.ideatech.ams.controller.account;

import com.ideatech.ams.system.configuration.service.AccountConfigureService;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fantao
 * @date 2019-05-05 16:54
 */
@Slf4j
@RestController
@RequestMapping("/cancelLicense")
public class CancelLicenseController {

    @Autowired
    private AccountConfigureService accountConfigureService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @GetMapping("/check")
    public ResultDto isCancelLicense(String acctType, String depositorType, String operateType,Long organId) {
        OrganRegisterDto organRegisterDto;
        if (organId == null) {
            organRegisterDto = organRegisterService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
        } else {
            organRegisterDto = organRegisterService.findByOrganId(organId);
        }
        boolean isCancelLicense = false;

        if (organRegisterDto != null) {
            isCancelLicense = accountConfigureService.query(acctType, depositorType, operateType) != null;
        }
        return ResultDtoFactory.toAckData(isCancelLicense);

    }

}
