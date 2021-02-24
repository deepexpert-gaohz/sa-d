package com.ideatech.ams.kyc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.kyc.entity.Director;

public interface DirectorDao extends JpaRepository<Director, Long>, JpaSpecificationExecutor<Director> {
	Director findById(Long id);
    List<Director> findBySaicinfoId(Long saicinfoId);
}
