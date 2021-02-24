package com.ideatech.ams.kyc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.kyc.entity.Supervise;

public interface SuperviseDao extends JpaRepository<Supervise, Long>, JpaSpecificationExecutor<Supervise> {
	Supervise findById(Long id);
    List<Supervise> findBySaicinfoId(Long saicinfoId);
}
