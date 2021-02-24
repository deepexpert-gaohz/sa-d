package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dto.ApplyCustomerPublicMidDto;

public interface OpenAccountService {
    ApplyCustomerPublicMidDto getCustomerInfoBySourceId(Long id);
}
