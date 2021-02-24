package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.AnnouncementInformationConfirmLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jzh
 * @date 2019-09-02.
 */

public interface AnnouncementInformationConfirmLogService extends BaseService<AnnouncementInformationConfirmLogDto> {

    TableResultResponse query(AnnouncementInformationConfirmLogDto confirmLogDto, Pageable pageable);
}
