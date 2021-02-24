package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RiskApiDao extends JpaRepository<RiskRecordInfo, Long>, JpaSpecificationExecutor<RiskRecordInfo> {


    RiskRecordInfo findOne(Long id);

    @Transactional
    void deleteByRiskId(String riskId);

    @Query(value = "select max(yd_risk_date) as riskDate from yd_risk_trade_info WHERE yd_corporate_bank = ?1 and rownum=1 ORDER BY yd_risk_date  ", nativeQuery = true)
    String findTradeNearDate(String code);
}

