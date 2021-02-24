package com.ideatech.ams.system.announcement.service;

import com.ideatech.ams.system.announcement.dto.AnnouncementDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author jzh
 * @date 2019/2/25.
 */
public interface AnnouncementService extends BaseService<AnnouncementDto> {

    TableResultResponse page(Pageable pageable,String organfullId);

    AnnouncementDto getTop(String organfullId);

    TableResultResponse page(Pageable pageable,String title,String organfullId);

    /**
     * 软删除
     * @param id
     */
    void delete(Long id);

    String upload(InputStream is, String fileName) ;

    Long saveAnnouncement(AnnouncementDto announcementDto);

    void download(HttpServletResponse response, Long id);

}
