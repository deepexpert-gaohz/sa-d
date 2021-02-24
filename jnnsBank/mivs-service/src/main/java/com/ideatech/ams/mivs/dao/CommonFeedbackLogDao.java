package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.CommonFeedbackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-08-06.
 */

@Repository
public interface CommonFeedbackLogDao extends JpaRepository<CommonFeedbackLog,Long>, JpaSpecificationExecutor<CommonFeedbackLog> {
}
