package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.KycSearchHistory;
import com.ideatech.ams.kyc.entity.SaicSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface KycSearchHistoryDao extends JpaRepository<KycSearchHistory, Long>, JpaSpecificationExecutor<KycSearchHistory> {
    List<KycSearchHistory> findByEntnameOrderByQuerydateDesc(String entname);
}
