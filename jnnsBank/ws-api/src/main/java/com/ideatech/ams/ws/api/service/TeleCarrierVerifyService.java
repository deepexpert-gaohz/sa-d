package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface TeleCarrierVerifyService {
    ResultDto verify(String name, String idNo, String mobile);

    ResultDto verify(String name, String idNo, String mobile, String username);

}
