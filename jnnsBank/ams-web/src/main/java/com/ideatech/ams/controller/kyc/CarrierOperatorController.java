package com.ideatech.ams.controller.kyc;

import com.ideatech.ams.kyc.dto.CarrierOperatorDto;
import com.ideatech.ams.kyc.service.CarrierOperatorService;
import com.ideatech.ams.ws.api.service.TeleCarrierVerifyService;
import com.ideatech.common.dto.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/carrier")
public class CarrierOperatorController {

    @Autowired
    private CarrierOperatorService carrierOperatorService;

    @Autowired
    private TeleCarrierVerifyService teleCarrierVerifyService;

    @RequestMapping(value = "/getCarrierOperatorResult", method = RequestMethod.GET)
    public CarrierOperatorDto getCarrierOperatorResult(CarrierOperatorDto carrierOperatorDto) {
        CarrierOperatorDto dto = carrierOperatorService.getCarrierOperatorResult(carrierOperatorDto);
        if(!StringUtils.equals("success", dto.getStatus())) {
            String reason = dto.getReason();
            if (StringUtils.isBlank(reason)) {
                reason = dto.getResult();
                dto.setReason(reason);
            }
        }

        return dto;
    }

}
