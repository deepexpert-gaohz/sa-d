package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskHandleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface RiskHandleInfoDao extends JpaRepository<RiskHandleInfo,Long>, JpaSpecificationExecutor<RiskHandleInfo> {

    int countAllByAccountNoAndRiskId(String accountNo, String riskId);

    List<RiskHandleInfo> findIdByAccountNoAndStatus(String status, String accountNo);

}
