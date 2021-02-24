package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Pledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PledgeDao extends JpaRepository<Pledge,Long>, JpaSpecificationExecutor<Pledge> {
    void deleteByKeyName(String companyName);

    List<Pledge> findAllByKeyName(String companyName);
}
