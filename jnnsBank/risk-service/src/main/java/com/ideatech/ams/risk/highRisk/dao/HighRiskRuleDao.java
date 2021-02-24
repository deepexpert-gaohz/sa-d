package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.HighRiskRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HighRiskRuleDao extends JpaRepository<HighRiskRule,Long>, JpaSpecificationExecutor<HighRiskRule> {
    List<HighRiskRule> findHighRisksByCorporateBank(String code);
    List<HighRiskRule> findByCorporateBank(String code);
    HighRiskRule findHighRiskByCorporateBank(String code);
}
