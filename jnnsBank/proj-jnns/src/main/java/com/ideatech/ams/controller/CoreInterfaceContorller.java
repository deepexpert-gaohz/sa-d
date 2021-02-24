package com.ideatech.ams.controller;

import com.ideatech.ams.service.SyncCoreComparOpenAcctService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/interface")
@RestController
public class CoreInterfaceContorller {

    private static final Logger log = LoggerFactory.getLogger(CoreInterfaceContorller.class);

    @Autowired
    SyncCoreComparOpenAcctService syncCoreComparOpenAcctService;

    @GetMapping("/getCoreAccountList")
    public String getCoreAccountInfo(String kaixhubz) {

        syncCoreComparOpenAcctService.getOrganCode(kaixhubz);

        return null;
    }
}
