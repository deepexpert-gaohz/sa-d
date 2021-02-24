package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.CompanyShareholdersAndFundingInformationDao;
import com.ideatech.ams.mivs.dto.rrd.CompanyShareholdersAndFundingInformationDto;
import com.ideatech.ams.mivs.entity.CompanyShareholdersAndFundingInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class CompanyShareholdersAndFundingInformationServiceImpl extends BaseServiceImpl<CompanyShareholdersAndFundingInformationDao, CompanyShareholdersAndFundingInformation, CompanyShareholdersAndFundingInformationDto> implements CompanyShareholdersAndFundingInformationService {
    @Override
    public List<CompanyShareholdersAndFundingInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),CompanyShareholdersAndFundingInformationDto.class);
    }
}
