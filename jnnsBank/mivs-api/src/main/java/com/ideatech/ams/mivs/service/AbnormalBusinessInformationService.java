package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.rrd.AbnormalBusinessInformationDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface AbnormalBusinessInformationService extends BaseService<AbnormalBusinessInformationDto> {

    List<AbnormalBusinessInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
