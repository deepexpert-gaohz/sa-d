package com.ideatech.ams.system.announcement.service;

import com.ideatech.ams.system.announcement.dao.AttachmentDao;
import com.ideatech.ams.system.announcement.dto.AttachmentDto;
import com.ideatech.ams.system.announcement.entity.Attachment;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author jzh
 * @date 2019/2/27.
 */

@Transactional
@Slf4j
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<AttachmentDao, Attachment, AttachmentDto> implements AttachmentService {

    @Autowired
    private AttachmentDao attachmentDao;

    @Override
    public Long saveAttachment(AttachmentDto attachmentDto) {
        Attachment attachment = ConverterService.convert(attachmentDto,Attachment.class);
        return attachmentDao.save(attachment).getId();
    }

    @Override
    public AttachmentDto getAttachmentByAnnouncementId(Long id) {
        Attachment attachment = attachmentDao.findTopByAnnouncementIdOrderByIdDesc(id);
        if (attachment==null){
            return null;
        }
        return ConverterService.convert(attachment,AttachmentDto.class);
    }
}
