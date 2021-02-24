package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.rrd.IllegalAndDiscreditInformationDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface IllegalAndDiscreditInformationService extends BaseService<IllegalAndDiscreditInformationDto> {

    List<IllegalAndDiscreditInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
