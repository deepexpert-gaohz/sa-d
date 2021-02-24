package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Lllegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LllegalDao extends JpaRepository<Lllegal,Long>, JpaSpecificationExecutor<Lllegal> {
    int deleteByName(String name);
    void deleteByKeyName(String name);

    List<Lllegal> findAllByKeyName(String name);

    List<Lllegal> findByName(String name);

    List<Lllegal> findByOrganFullId(String orgfullId);

    List<Lllegal> findByOrgCode(String orgCode);

}
