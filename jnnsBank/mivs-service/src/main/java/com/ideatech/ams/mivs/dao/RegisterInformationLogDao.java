package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.RegisterInformationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/7/31.
 */

@Repository
public interface RegisterInformationLogDao extends JpaRepository<RegisterInformationLog,Long>, JpaSpecificationExecutor<RegisterInformationLog> {
}
