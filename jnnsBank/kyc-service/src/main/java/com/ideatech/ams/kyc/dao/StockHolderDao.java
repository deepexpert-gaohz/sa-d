package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.StockHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StockHolderDao extends JpaRepository<StockHolder, Long>, JpaSpecificationExecutor<StockHolder> {

    List<StockHolder> findBySaicinfoId(Long saicinfoId);

    StockHolder findById(Long id);

}
