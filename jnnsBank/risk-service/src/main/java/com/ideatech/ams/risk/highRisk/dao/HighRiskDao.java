package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HighRiskDao extends JpaRepository<HighRisk,Long>, JpaSpecificationExecutor<HighRisk> {

        void deleteByCustomerNoIn(List<String> customerNoList);

        List<HighRisk> findHighRiskByCorporateBank(String code);

        void deleteByCorporateBank(String code);
}
