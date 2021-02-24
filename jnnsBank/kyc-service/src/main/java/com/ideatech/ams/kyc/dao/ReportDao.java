package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReportDao extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
    List<Report> findBySaicinfoId(Long saicInfoId);
    List<Report> findBySaicinfoIdOrderByReleasedateAsc(Long saicInfoId);
}
