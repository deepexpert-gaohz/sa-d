package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.CarrierOperatorDto;

public interface CarrierOperatorService {
    CarrierOperatorDto getCarrierOperatorResult(CarrierOperatorDto carrierOperatorDto);

    CarrierOperatorDto getCarrierOperatorResult(CarrierOperatorDto carrierOperatorDto, String username);

}
