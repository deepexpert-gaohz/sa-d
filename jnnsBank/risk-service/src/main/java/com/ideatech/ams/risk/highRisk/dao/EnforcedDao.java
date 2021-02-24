package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Enforced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EnforcedDao extends JpaRepository<Enforced,Long>, JpaSpecificationExecutor<Enforced> {
    void deleteByKeyName(String companyName);

    List<Enforced> findAllByKeyName(String companyName);
}
