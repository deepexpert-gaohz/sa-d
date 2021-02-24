package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.Illegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IllegalDao extends JpaRepository<Illegal, Long>, JpaSpecificationExecutor<Illegal> {

    List<Illegal> findBySaicinfoId(Long saicInfoId);
}
