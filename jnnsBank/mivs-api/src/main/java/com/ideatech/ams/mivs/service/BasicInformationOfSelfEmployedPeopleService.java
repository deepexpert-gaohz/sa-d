package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.rrd.BasicInformationOfSelfEmployedPeopleDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface BasicInformationOfSelfEmployedPeopleService extends BaseService<BasicInformationOfSelfEmployedPeopleDto> {

    BasicInformationOfSelfEmployedPeopleDto findByRegisterInformationLogId(Long registerInformationLogId);
}
