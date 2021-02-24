package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.BATNServiceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019-09-20.
 */

@Repository
public interface BATNServiceInformationDao extends JpaRepository<BATNServiceInformation,Long>, JpaSpecificationExecutor<BATNServiceInformation> {

    List<BATNServiceInformation> findAllByNoticeId(Long noticeId);
}
