package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CreditDao extends JpaRepository<Credit,Long>, JpaSpecificationExecutor<Credit> {
    void deleteByKeyName(String companyName);

    List<Credit> findAllByKeyName(String companyName);
}
