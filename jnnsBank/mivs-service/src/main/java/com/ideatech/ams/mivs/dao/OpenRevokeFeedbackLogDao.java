package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.OpenRevokeFeedbackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-08-08.
 */

@Repository
public interface OpenRevokeFeedbackLogDao extends JpaRepository<OpenRevokeFeedbackLog,Long>, JpaSpecificationExecutor<OpenRevokeFeedbackLog> {

}
