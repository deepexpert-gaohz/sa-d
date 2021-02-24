package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.rrd.LicenseNullifyDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface LicenseNullifyService extends BaseService<LicenseNullifyDto> {

    List<LicenseNullifyDto> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
