package com.ideatech.ams.risk.model.dao;

import com.ideatech.ams.risk.model.entity.RiskConfigerField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RiskConfigerFieldDao extends JpaRepository<RiskConfigerField,Long>, JpaSpecificationExecutor<RiskConfigerField> {

    List<RiskConfigerField> findAll();

    RiskConfigerField findByField(String field);
}


