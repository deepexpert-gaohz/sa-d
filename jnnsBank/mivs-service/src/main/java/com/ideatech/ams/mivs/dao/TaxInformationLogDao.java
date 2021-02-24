package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.TaxInformationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/7/29.
 */

@Repository
public interface TaxInformationLogDao extends JpaRepository<TaxInformationLog,Long>, JpaSpecificationExecutor<TaxInformationLog> {
}
