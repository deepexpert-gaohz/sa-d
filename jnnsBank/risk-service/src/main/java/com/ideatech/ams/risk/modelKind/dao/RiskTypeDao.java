package com.ideatech.ams.risk.modelKind.dao;

import com.ideatech.ams.risk.modelKind.entity.RiskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
public interface RiskTypeDao extends JpaRepository<RiskType,Long>, JpaSpecificationExecutor<RiskType> {
    RiskType findByTypeName(String typeName);

    @Query("select distinct r.typeName from RiskType r")
    List<String> findDistinctByTypeName();
}
