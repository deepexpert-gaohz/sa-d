package com.ideatech.ams.system.trace.dao;

import com.ideatech.ams.system.trace.entity.UserTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-10-30.
 */

@Repository
public interface UserTraceDao extends JpaRepository<UserTrace, Long>, JpaSpecificationExecutor<UserTrace> {
}
