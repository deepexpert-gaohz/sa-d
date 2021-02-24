package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.bqrd.ServiceInformationDto;
import com.ideatech.common.service.BaseService;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface ServiceInformationService extends BaseService<ServiceInformationDto> {
    ServiceInformationDto findByBusinessAcceptTimeLogId(Long businessAcceptTimeLogId);
}
