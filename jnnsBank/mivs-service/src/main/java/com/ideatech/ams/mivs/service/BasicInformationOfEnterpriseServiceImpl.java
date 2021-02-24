package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.BasicInformationOfEnterpriseDao;
import com.ideatech.ams.mivs.dto.rrd.BasicInformationOfEnterpriseDto;
import com.ideatech.ams.mivs.entity.BasicInformationOfEnterprise;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class BasicInformationOfEnterpriseServiceImpl extends BaseServiceImpl<BasicInformationOfEnterpriseDao, BasicInformationOfEnterprise, BasicInformationOfEnterpriseDto> implements BasicInformationOfEnterpriseService {
    @Override
    public BasicInformationOfEnterpriseDto findByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convert(getBaseDao().findTopByRegisterInformationLogId(registerInformationLogId),BasicInformationOfEnterpriseDto.class);
    }
}
