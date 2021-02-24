package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.AnnouncementInformationDao;
import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.ams.mivs.entity.AnnouncementInformation;
import com.ideatech.ams.mivs.spec.AnnouncementInformationSpec;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/25.
 */

@Service
public class AnnouncementInformationServiceImpl extends BaseServiceImpl<AnnouncementInformationDao, AnnouncementInformation, AnnouncementInformationDto> implements AnnouncementInformationService{
    @Override
    public TableResultResponse<AnnouncementInformationDto> query(AnnouncementInformationDto institutionAbnormalNoticeDto, Pageable pageable) {
        AnnouncementInformationSpec announcementInformationSpec = new AnnouncementInformationSpec(institutionAbnormalNoticeDto);
        Page<AnnouncementInformation> page= getBaseDao().findAll(announcementInformationSpec,pageable);
        return new TableResultResponse((int)page.getTotalElements(),page.getContent());
    }
}
