package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.ServiceInformationDao;
import com.ideatech.ams.mivs.dto.bqrd.ServiceInformationDto;
import com.ideatech.ams.mivs.entity.ServiceInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class ServiceInformationServiceImpl extends BaseServiceImpl<ServiceInformationDao, ServiceInformation, ServiceInformationDto> implements ServiceInformationService {
    @Override
    public ServiceInformationDto findByBusinessAcceptTimeLogId(Long businessAcceptTimeLogId) {
        return ConverterService.convert(getBaseDao().findTopByBusinessAcceptTimeLogId(businessAcceptTimeLogId),ServiceInformationDto.class);
    }
}
