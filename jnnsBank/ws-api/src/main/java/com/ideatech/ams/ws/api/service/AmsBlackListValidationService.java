package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface AmsBlackListValidationService {
    ResultDto checkAmsBlackListValidate(String entName);
}
