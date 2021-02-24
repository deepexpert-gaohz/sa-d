package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JudgmentDao extends JpaRepository<Judgment,Long>, JpaSpecificationExecutor<Judgment> {
    void deleteByKeyName(String companyName);

    List<Judgment> findAllByKeyName(String companyName);
}
