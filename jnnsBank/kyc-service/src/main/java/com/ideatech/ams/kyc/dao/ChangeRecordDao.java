package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.ChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChangeRecordDao extends JpaRepository<ChangeRecord, Long>, JpaSpecificationExecutor<ChangeRecord> {
    List<ChangeRecord> findBySaicinfoId(Long saicInfoId);
}
