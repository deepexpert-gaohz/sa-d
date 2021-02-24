package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.BusinessAcceptTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Repository
public interface BusinessAcceptTimeLogDao extends JpaRepository<BusinessAcceptTimeLog,Long>, JpaSpecificationExecutor<BusinessAcceptTimeLog> {
}
