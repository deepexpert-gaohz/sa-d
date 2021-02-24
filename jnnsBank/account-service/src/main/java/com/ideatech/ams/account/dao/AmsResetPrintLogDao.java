package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AmsResetPrintLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author vantoo
 * @date 15:53 2018/5/28
 */
@Repository
public interface AmsResetPrintLogDao extends JpaRepository<AmsResetPrintLog, Long>, JpaSpecificationExecutor<AmsResetPrintLog> {

}
