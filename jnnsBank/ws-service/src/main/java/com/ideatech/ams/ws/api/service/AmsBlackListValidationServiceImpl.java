package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.system.blacklist.dto.BlackListEntryDto;
import com.ideatech.ams.system.blacklist.enums.BlackListResultEnum;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description AMS的黑名单校验
 * @Author wanghongjie
 * @Date 2018/9/6
 **/
@Service
@Transactional
@Slf4j
public class AmsBlackListValidationServiceImpl implements AmsBlackListValidationService{
    @Autowired
    private BlackListService blackListService;

    @Override
    public ResultDto checkAmsBlackListValidate(String entName) {
        BlackListResultEnum blackListResult = blackListService.findByNameMixWhite(entName);
        return ResultDtoFactory.toAckData(blackListResult);
    }
}
