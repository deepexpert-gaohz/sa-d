package com.ideatech.ams.risk.rule.dao;

import com.ideatech.ams.risk.rule.entity.RuleField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RuleFieldDao extends JpaRepository<RuleField,Long>, JpaSpecificationExecutor<RuleField> {

    List<RuleField> findAllByModelId(String modelId);

    List<RuleField> findByCorporateBank(String code);
    @Transactional
    @Query(value = "delete from yd_risk_rule_field where yd_corporate_bank=?1", nativeQuery = true)
    @Modifying
    void deleteByCorporateBank(String code);
}
