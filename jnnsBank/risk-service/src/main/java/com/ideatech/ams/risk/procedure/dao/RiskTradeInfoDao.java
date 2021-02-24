package com.ideatech.ams.risk.procedure.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskTradeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface RiskTradeInfoDao extends JpaRepository<RiskTradeInfo,Long>, JpaSpecificationExecutor<RiskTradeInfo> {
    RiskTradeInfo findByRiskIdAndRiskPointAndCorporateBank(String riskId, String riskPoint, String corporateBank);

    @Transactional
    void deleteByRiskIdAndCorporateBankAndRiskPoint(String riskId, String corporateBank, String riskPoint);

    List<RiskTradeInfo> findByCorporateBankAndRiskId(String corporateBank, String riskId);

    @Transactional
    @Modifying
    @Query(value = "delete from yd_risk_trade_info where yd_risk_date =?1 and yd_corporate_bank= ?2",nativeQuery = true)
    void deleteByRiskDateAndCorporateBank(Date date, String corporateBank);

}

