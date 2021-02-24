package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.DirectorSupervisorSeniorManagerInformationDao;
import com.ideatech.ams.mivs.dto.rrd.DirectorSupervisorSeniorManagerInformationDto;
import com.ideatech.ams.mivs.entity.DirectorSupervisorSeniorManagerInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class DirectorSupervisorSeniorManagerInformationServiceImpl extends BaseServiceImpl<DirectorSupervisorSeniorManagerInformationDao, DirectorSupervisorSeniorManagerInformation, DirectorSupervisorSeniorManagerInformationDto> implements DirectorSupervisorSeniorManagerInformationService{
    @Override
    public List<DirectorSupervisorSeniorManagerInformationDto> findAllByRegisterInformationLogId(Long registerInformationLogId) {
        return ConverterService.convertToList(getBaseDao().findAllByRegisterInformationLogId(registerInformationLogId),DirectorSupervisorSeniorManagerInformationDto.class);
    }
}
