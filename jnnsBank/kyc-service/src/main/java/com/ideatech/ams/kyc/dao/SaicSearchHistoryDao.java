package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.SaicSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaicSearchHistoryDao extends JpaRepository<SaicSearchHistory, Long>, JpaSpecificationExecutor<SaicSearchHistory> {
}
