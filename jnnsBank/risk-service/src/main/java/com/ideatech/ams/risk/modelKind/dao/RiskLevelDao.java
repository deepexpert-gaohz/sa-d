package com.ideatech.ams.risk.modelKind.dao;


import com.ideatech.ams.risk.modelKind.entity.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
public interface RiskLevelDao extends JpaRepository<RiskLevel,Long>, JpaSpecificationExecutor<RiskLevel> {
    RiskLevel findByLevelName(String levelName);

    @Query("select distinct r.levelName from RiskLevel r")
    List<String> findDistinctLevelName();



}
