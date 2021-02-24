package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.SaicMonitorPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaicMonitorDao extends JpaRepository<SaicMonitorPo, Long>, JpaSpecificationExecutor<SaicMonitorPo> {

}
