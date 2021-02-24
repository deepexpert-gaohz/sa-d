package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.AnnouncementInformationConfirmLogDao;
import com.ideatech.ams.mivs.dto.AnnouncementInformationConfirmLogDto;
import com.ideatech.ams.mivs.entity.AnnouncementInformationConfirmLog;
import com.ideatech.ams.mivs.spec.AnnouncementInformationConfirmLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019-09-02.
 */

@Slf4j
@Service
public class AnnouncementInformationConfirmLogServiceImpl
        extends BaseServiceImpl<AnnouncementInformationConfirmLogDao, AnnouncementInformationConfirmLog, AnnouncementInformationConfirmLogDto>
        implements AnnouncementInformationConfirmLogService{


    @Override
    public TableResultResponse query(AnnouncementInformationConfirmLogDto confirmLogDto, Pageable pageable) {
        confirmLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        AnnouncementInformationConfirmLogSpec confirmLogSpec = new AnnouncementInformationConfirmLogSpec(confirmLogDto);
        Page<AnnouncementInformationConfirmLog> confirmLogPage = getBaseDao().findAll(confirmLogSpec,pageable);
        return new TableResultResponse((int) confirmLogPage.getTotalElements(), ConverterService.convertToList(confirmLogPage.getContent(),AnnouncementInformationConfirmLogDto.class));
    }
}
