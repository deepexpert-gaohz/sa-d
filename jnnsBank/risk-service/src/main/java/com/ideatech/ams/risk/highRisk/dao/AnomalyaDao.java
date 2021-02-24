package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Anomalya;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnomalyaDao extends JpaRepository<Anomalya,Long>, JpaSpecificationExecutor<Anomalya> {
    int deleteByName(String name);

    void deleteByKeyName(String name);

    List<Anomalya> findAllByKeyName(String name);

    List<Anomalya> findByName(String name);

    List<Anomalya> findByOrganFullId(String orgFullId);

    List<Anomalya> findByOrgCode(String orgCode);
}
