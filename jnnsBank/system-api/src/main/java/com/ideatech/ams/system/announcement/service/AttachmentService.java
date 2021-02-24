package com.ideatech.ams.system.announcement.service;

import com.ideatech.ams.system.announcement.dto.AttachmentDto;
import com.ideatech.common.service.BaseService;

/**
 * @author jzh
 * @date 2019/2/27.
 */
public interface AttachmentService extends BaseService<AttachmentDto> {

    Long saveAttachment(AttachmentDto attachmentDto);

    AttachmentDto getAttachmentByAnnouncementId(Long id);
}
