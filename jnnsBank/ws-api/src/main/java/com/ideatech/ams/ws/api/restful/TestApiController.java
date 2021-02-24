package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.ws.api.service.SaicApiService;
import com.ideatech.ams.ws.api.service.TeleCarrierVerifyService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by houxianghua on 2018/11/13.
 * service接口测试
 */
@RestController
@RequestMapping(value = "/api/test")
@Slf4j
public class TestApiController {

    @Autowired
    private SaicApiService saicApiService;

    @Autowired
    private TeleCarrierVerifyService teleCarrierVerifyService;

    @RequestMapping(value = "/queryIsKyc")
    public ResultDto queryIsKyc(String name) {
        return saicApiService.queryIsKyc(name);
    }

    @GetMapping("/checkSaicInfo")
    public ResultDto checkSaicInfo(String name, String key) {
        return saicApiService.checkSaicInfo(name,key);
    }

    @GetMapping("/checkSaicInfos")
    public ResultDto checkSaicInfos(String name, String key, String organCode) {
        return saicApiService.checkSaicInfo(name, key, organCode);
    }

    @GetMapping("/verify")
    public ResultDto verify(String name, String idNo, String mobile) {
        return teleCarrierVerifyService.verify(name,idNo,mobile);
    }
}
