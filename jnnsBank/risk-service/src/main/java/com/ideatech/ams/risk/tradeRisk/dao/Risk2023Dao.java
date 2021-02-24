package com.ideatech.ams.risk.tradeRisk.dao;

import com.ideatech.ams.risk.tradeRisk.entity.Risk2023;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author yangwz
 * @Description
 * @date 2019-10-31 15:07
 */
public interface Risk2023Dao extends JpaRepository<Risk2023,Long>, JpaSpecificationExecutor<Risk2023> {

    @Query(value = "delete from yd_risk_2023  where yd_cjrq=?1 and yd_corporate_bank=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByCjrqAndCorporateBank(String cjrq, String corporateBank);

}
