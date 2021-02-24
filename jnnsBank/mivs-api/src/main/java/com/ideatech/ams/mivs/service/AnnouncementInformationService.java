package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/25.
 *
 * 5.10	公告信息报文
 */
public interface AnnouncementInformationService extends BaseService<AnnouncementInformationDto> {

    TableResultResponse<AnnouncementInformationDto> query(AnnouncementInformationDto institutionAbnormalNoticeDto, Pageable pageable);
}
