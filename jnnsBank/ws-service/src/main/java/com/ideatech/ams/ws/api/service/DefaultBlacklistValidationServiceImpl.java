package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;

/**
 * @author liangding
 * @create 2018-08-22 下午11:36
 **/
public class DefaultBlacklistValidationServiceImpl implements BlacklistValidationService {
    @Override
    public ResultDto check(String name, String bod, String nationality) {
        return ResultDtoFactory.toAck("校验通过");
    }

}
