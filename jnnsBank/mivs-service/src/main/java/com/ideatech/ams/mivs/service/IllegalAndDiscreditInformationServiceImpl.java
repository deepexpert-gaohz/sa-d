package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.IllegalAndDiscreditInformationDao;
import com.ideatech.ams.mivs.dto.rrd.IllegalAndDiscreditInformationDto;
import com.ideatech.ams.mivs.entity.IllegalAndDiscreditInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class IllegalAndDiscreditInformationServiceImpl extends BaseServiceImpl<IllegalAndDiscreditInformationDao, IllegalAndDiscreditInformation, IllegalAndDiscreditInformationDto> implements IllegalAndDiscreditInformationService {
    @Override
    public List<IllegalAndDiscreditInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),IllegalAndDiscreditInformationDto.class);
    }
}
