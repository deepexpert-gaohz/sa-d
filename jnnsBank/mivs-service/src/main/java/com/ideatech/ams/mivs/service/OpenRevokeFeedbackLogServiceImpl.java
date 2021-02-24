package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.OpenRevokeFeedbackLogDao;
import com.ideatech.ams.mivs.dto.OpenRevokeFeedbackLogDto;
import com.ideatech.ams.mivs.entity.OpenRevokeFeedbackLog;
import com.ideatech.ams.mivs.spec.OpenRevokeFeedbackLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019-08-08.
 */

@Service
public class OpenRevokeFeedbackLogServiceImpl extends BaseServiceImpl<OpenRevokeFeedbackLogDao, OpenRevokeFeedbackLog, OpenRevokeFeedbackLogDto> implements OpenRevokeFeedbackLogService {

    @Override
    public TableResultResponse<OpenRevokeFeedbackLogDto> query(OpenRevokeFeedbackLogDto openRevokeFeedbackLogDto, Pageable pageable) {
        openRevokeFeedbackLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        OpenRevokeFeedbackLogSpec openRevokeFeedbackLogSpec = new OpenRevokeFeedbackLogSpec(openRevokeFeedbackLogDto);
        Page<OpenRevokeFeedbackLog> openRevokeFeedbackLogPage = getBaseDao().findAll(openRevokeFeedbackLogSpec,pageable);
        return new TableResultResponse((int) openRevokeFeedbackLogPage.getTotalElements(), ConverterService.convertToList(openRevokeFeedbackLogPage.getContent(),OpenRevokeFeedbackLogDto.class));
    }
}
