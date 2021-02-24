package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CoreStockHolderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CoreStockHolderDao extends JpaRepository<CoreStockHolderPo, Long>, JpaSpecificationExecutor<CoreStockHolderPo> {
    List<CoreStockHolderPo> findByBatchNo(String batchNo);
}
