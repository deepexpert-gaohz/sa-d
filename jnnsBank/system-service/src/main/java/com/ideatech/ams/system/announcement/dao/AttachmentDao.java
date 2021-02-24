package com.ideatech.ams.system.announcement.dao;

import com.ideatech.ams.system.announcement.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jzh
 * @date 2019/2/27.
 */
public interface AttachmentDao extends JpaRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment> {

    Attachment findTopByAnnouncementIdOrderByIdDesc(Long announcementId);
}
