package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.rrd.CompanyShareholdersAndFundingInformationDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */
public interface CompanyShareholdersAndFundingInformationService extends BaseService<CompanyShareholdersAndFundingInformationDto> {

    List<CompanyShareholdersAndFundingInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId);
}
