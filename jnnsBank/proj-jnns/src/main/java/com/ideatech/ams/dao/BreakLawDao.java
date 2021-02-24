package com.ideatech.ams.dao;

import com.ideatech.ams.dto.SaicQuery.BreakLawInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BreakLawDao extends JpaRepository<BreakLawInfo, Long>, JpaSpecificationExecutor<BreakLawInfo> {


    List<BreakLawInfo> findByName(String name);
}