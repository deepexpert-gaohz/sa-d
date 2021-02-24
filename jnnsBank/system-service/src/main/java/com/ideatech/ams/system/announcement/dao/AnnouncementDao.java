package com.ideatech.ams.system.announcement.dao;

import com.ideatech.ams.system.announcement.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

/**
 * @author jzh
 * @date 2019/2/25.
 */
public interface AnnouncementDao extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {

    Announcement findTopByOrganfullIdOrderByIdDesc(String organfullId);

    Page<Announcement> findAllByTitleLikeAndOrganfullIdInOrderByIdDesc(String title ,Collection<String> organfullId,Pageable pageable);

    Page<Announcement> findAllByOrganfullIdInOrderByIdDesc(Collection<String> organfullId, Pageable pageable);
}
