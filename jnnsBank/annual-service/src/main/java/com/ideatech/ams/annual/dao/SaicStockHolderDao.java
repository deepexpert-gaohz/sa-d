package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.SaicStockHolderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaicStockHolderDao extends JpaRepository<SaicStockHolderPo, Long>, JpaSpecificationExecutor<SaicStockHolderPo> {
}
