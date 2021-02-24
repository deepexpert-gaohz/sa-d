package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.LicenseNullifyDao;
import com.ideatech.ams.mivs.dto.rrd.LicenseNullifyDto;
import com.ideatech.ams.mivs.entity.LicenseNullify;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class LicenseNullifyServiceImpl extends BaseServiceImpl<LicenseNullifyDao, LicenseNullify, LicenseNullifyDto> implements LicenseNullifyService {
    @Override
    public List<LicenseNullifyDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),LicenseNullifyDto.class);
    }
}
