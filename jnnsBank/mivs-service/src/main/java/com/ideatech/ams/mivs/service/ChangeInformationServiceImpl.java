package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.ChangeInformationDao;
import com.ideatech.ams.mivs.dto.rrd.ChangeInformationDto;
import com.ideatech.ams.mivs.entity.ChangeInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class ChangeInformationServiceImpl extends BaseServiceImpl<ChangeInformationDao, ChangeInformation, ChangeInformationDto> implements ChangeInformationService {
    @Override
    public List<ChangeInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),ChangeInformationDto.class);
    }
}
