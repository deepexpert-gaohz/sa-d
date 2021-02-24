package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.AbnormalBusinessInformationDao;
import com.ideatech.ams.mivs.dto.rrd.AbnormalBusinessInformationDto;
import com.ideatech.ams.mivs.entity.AbnormalBusinessInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class AbnormalBusinessInformationServiceImpl extends BaseServiceImpl<AbnormalBusinessInformationDao, AbnormalBusinessInformation, AbnormalBusinessInformationDto> implements AbnormalBusinessInformationService{


    @Override
    public List<AbnormalBusinessInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),AbnormalBusinessInformationDto.class);
    }
}
