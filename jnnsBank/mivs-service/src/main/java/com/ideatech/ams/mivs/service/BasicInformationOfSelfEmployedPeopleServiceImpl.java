package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.BasicInformationOfSelfEmployedPeopleDao;
import com.ideatech.ams.mivs.dto.rrd.BasicInformationOfSelfEmployedPeopleDto;
import com.ideatech.ams.mivs.entity.BasicInformationOfSelfEmployedPeople;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class BasicInformationOfSelfEmployedPeopleServiceImpl extends BaseServiceImpl<BasicInformationOfSelfEmployedPeopleDao, BasicInformationOfSelfEmployedPeople, BasicInformationOfSelfEmployedPeopleDto> implements BasicInformationOfSelfEmployedPeopleService{
    @Override
    public BasicInformationOfSelfEmployedPeopleDto findByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convert(getBaseDao().findTopByRegisterInformationLogId(registerInformationLogId),BasicInformationOfSelfEmployedPeopleDto.class);
    }
}
