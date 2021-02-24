package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskMiddelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RiskMiddelInfoDao extends JpaRepository<RiskMiddelInfo,Long>, JpaSpecificationExecutor<RiskMiddelInfo> {

    @Transactional
    void deleteByRiskId(String riskId);
}
