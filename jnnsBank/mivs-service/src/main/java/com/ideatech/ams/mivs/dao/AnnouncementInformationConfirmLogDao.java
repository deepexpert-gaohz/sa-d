package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.AnnouncementInformationConfirmLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-09-02.
 */

@Repository
public interface AnnouncementInformationConfirmLogDao extends JpaRepository<AnnouncementInformationConfirmLog,Long>, JpaSpecificationExecutor<AnnouncementInformationConfirmLog> {
}
