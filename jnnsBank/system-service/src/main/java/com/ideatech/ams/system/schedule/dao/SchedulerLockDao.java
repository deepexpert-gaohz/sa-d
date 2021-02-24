package com.ideatech.ams.system.schedule.dao;

import com.ideatech.ams.system.schedule.entity.SchedulerLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019-08-21.
 */

@Repository
public interface SchedulerLockDao extends JpaRepository<SchedulerLock, Long>, JpaSpecificationExecutor<SchedulerLock> {

    SchedulerLock findFirstByName(String name);
}
