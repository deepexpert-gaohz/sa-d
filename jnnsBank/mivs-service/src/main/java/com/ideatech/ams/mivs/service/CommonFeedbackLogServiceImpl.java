package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.CommonFeedbackLogDao;
import com.ideatech.ams.mivs.dto.CommonFeedbackLogDto;
import com.ideatech.ams.mivs.entity.CommonFeedbackLog;
import com.ideatech.ams.mivs.spec.CommonFeedbackLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019-08-06.
 */

@Service
public class CommonFeedbackLogServiceImpl extends BaseServiceImpl<CommonFeedbackLogDao, CommonFeedbackLog, CommonFeedbackLogDto> implements CommonFeedbackLogService{
    @Override
    public TableResultResponse query(CommonFeedbackLogDto commonFeedbackLogDto, Pageable pageable) {
        commonFeedbackLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        CommonFeedbackLogSpec commonFeedbackLogSpec = new CommonFeedbackLogSpec(commonFeedbackLogDto);
        Page<CommonFeedbackLog> commonFeedbackLogDtoPage = getBaseDao().findAll(commonFeedbackLogSpec,pageable);
        return new TableResultResponse((int) commonFeedbackLogDtoPage.getTotalElements(), ConverterService.convertToList(commonFeedbackLogDtoPage.getContent(),CommonFeedbackLogDto.class));
    }
}
