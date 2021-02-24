package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Cancelled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CancelledDao extends JpaRepository<Cancelled,Long>, JpaSpecificationExecutor<Cancelled> {
    void deleteByKeyName(String name);

    List<Cancelled> findAllByKeyName(String name);
}
