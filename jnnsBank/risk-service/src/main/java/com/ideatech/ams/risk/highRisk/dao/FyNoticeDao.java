package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.FyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FyNoticeDao extends JpaRepository<FyNotice,Long>, JpaSpecificationExecutor<FyNotice> {
    void deleteByKeyName(String companyName);

    List<FyNotice> findAllByKeyName(String companyName);
}
