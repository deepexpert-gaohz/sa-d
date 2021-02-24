package com.ideatech.ams.risk.tradeRisk.dao;

import com.ideatech.ams.risk.tradeRisk.entity.Risk2021;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface Risk2021Dao extends JpaRepository<Risk2021,Long>, JpaSpecificationExecutor<Risk2021> {
    @Query(value = "delete from yd_risk_2021  where yd_cjrq=?1 and yd_corporate_bank=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByCjrqAndCorporateBank(String cjrq, String corporateBank);
}
