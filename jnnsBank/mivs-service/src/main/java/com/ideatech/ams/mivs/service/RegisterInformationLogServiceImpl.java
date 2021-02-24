package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.RegisterInformationLogDao;
import com.ideatech.ams.mivs.dto.RegisterInformationLogDto;
import com.ideatech.ams.mivs.entity.RegisterInformationLog;
import com.ideatech.ams.mivs.spec.RegisterInformationLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Service
public class RegisterInformationLogServiceImpl extends BaseServiceImpl<RegisterInformationLogDao,RegisterInformationLog, RegisterInformationLogDto> implements RegisterInformationLogService {
    @Override
    public TableResultResponse<RegisterInformationLogDto> query(RegisterInformationLogDto registerInformationLogDto, Pageable pageable) {
        registerInformationLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        RegisterInformationLogSpec registerInformationLogSpec = new RegisterInformationLogSpec(registerInformationLogDto);
        Page<RegisterInformationLog> registerInformationLogPage = getBaseDao().findAll(registerInformationLogSpec,pageable);
        return new TableResultResponse((int) registerInformationLogPage.getTotalElements(), ConverterService.convertToList(registerInformationLogPage.getContent(),RegisterInformationLogDto.class));
    }
}
