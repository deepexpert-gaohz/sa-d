package com.ideatech.ams.dao;

import com.ideatech.ams.dto.SaicQuery.OwingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OwingDao extends JpaRepository<OwingInfo, Long>, JpaSpecificationExecutor<OwingInfo> {



    List<OwingInfo> findByName(String name);
}
