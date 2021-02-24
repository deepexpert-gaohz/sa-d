package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.BusinessAcceptTimeLogDao;
import com.ideatech.ams.mivs.dao.BusinessAcceptTimeNoticeDao;
import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.ams.mivs.dto.bnd.BusinessAcceptTimeNoticeDto;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeLog;
import com.ideatech.ams.mivs.entity.BusinessAcceptTimeNotice;
import com.ideatech.ams.mivs.spec.BusinessAcceptTimeLogSpec;
import com.ideatech.ams.mivs.spec.BusinessAcceptTimeNoticeSpec;
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
public class BusinessAcceptTimeNoticeServiceImpl extends BaseServiceImpl<BusinessAcceptTimeNoticeDao, BusinessAcceptTimeNotice, BusinessAcceptTimeNoticeDto> implements BusinessAcceptTimeNoticeService{
    @Override
    public TableResultResponse<BusinessAcceptTimeNoticeDto> query(BusinessAcceptTimeNoticeDto businessAcceptTimeNoticeDto, Pageable pageable) {
        BusinessAcceptTimeNoticeSpec businessAcceptTimeNoticeSpec = new BusinessAcceptTimeNoticeSpec(businessAcceptTimeNoticeDto);
        Page<BusinessAcceptTimeNotice> businessAcceptTimeNoticePage = getBaseDao().findAll(businessAcceptTimeNoticeSpec,pageable);
        return new TableResultResponse((int) businessAcceptTimeNoticePage.getTotalElements(), ConverterService.convertToList(businessAcceptTimeNoticePage.getContent(),BusinessAcceptTimeNoticeDto.class));
    }

}
