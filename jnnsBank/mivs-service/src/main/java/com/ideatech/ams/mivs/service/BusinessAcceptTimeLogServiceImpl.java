package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.BusinessAcceptTimeLogDao;
import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeLog;
import com.ideatech.ams.mivs.spec.BusinessAcceptTimeLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Service
public class BusinessAcceptTimeLogServiceImpl extends BaseServiceImpl<BusinessAcceptTimeLogDao, BusinessAcceptTimeLog, BusinessAcceptTimeLogDto> implements BusinessAcceptTimeLogService{
    @Override
    public TableResultResponse<BusinessAcceptTimeLogDto> query(BusinessAcceptTimeLogDto businessAcceptTimeLogDto, Pageable pageable) {
        businessAcceptTimeLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        BusinessAcceptTimeLogSpec businessAcceptTimeLogSpec = new BusinessAcceptTimeLogSpec(businessAcceptTimeLogDto);
        Page<BusinessAcceptTimeLog> businessAcceptTimeLogPage = getBaseDao().findAll(businessAcceptTimeLogSpec,pageable);
        return new TableResultResponse((int) businessAcceptTimeLogPage.getTotalElements(), ConverterService.convertToList(businessAcceptTimeLogPage.getContent(),BusinessAcceptTimeLogDto.class));
    }
}
