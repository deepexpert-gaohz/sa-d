package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Equity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EquityDao extends JpaRepository<Equity,Long>, JpaSpecificationExecutor<Equity> {
    void deleteByKeyName(String companyName);

    List<Equity> findAllByKeyName(String companyName);
}
