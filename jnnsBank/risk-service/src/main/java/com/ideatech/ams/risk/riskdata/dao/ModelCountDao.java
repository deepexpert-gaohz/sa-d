package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.model.entity.ModelCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ModelCountDao extends JpaRepository<ModelCount,Long>, JpaSpecificationExecutor<ModelCount> {

    //    List<ModelCount> findAllByCjrqAndOrgIdAndKhId(String cjrq,String orgId,String khId);
    @Transactional
    @Query(value = "delete from yd_risk_model_count where yd_model_id = ?2 and yd_cjrq=?1 and yd_corporate_bank=?3", nativeQuery = true)
    @Modifying
    void deleteByCjrqAndModelId(String cjrq, String modelId, String code);
    @Query(value = "SELECT max(yd_cjrq) as cjrq FROM yd_risk_model_count WHERE yd_corporate_bank = ?1  and rownum=1 ORDER BY yd_cjrq ",nativeQuery = true)
    String findNearDate(String code);
}
