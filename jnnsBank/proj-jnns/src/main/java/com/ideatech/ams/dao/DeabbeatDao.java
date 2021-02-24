package com.ideatech.ams.dao;

import com.ideatech.ams.dto.SaicQuery.DeabbeatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DeabbeatDao extends JpaRepository<DeabbeatInfo, Long>, JpaSpecificationExecutor<DeabbeatInfo> {

    List<DeabbeatInfo> findByName(String name);


}
