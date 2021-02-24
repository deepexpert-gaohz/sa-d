package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.TelecomValidationPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TelecomValidationDao extends JpaRepository<TelecomValidationPo, Long>, JpaSpecificationExecutor<TelecomValidationPo> {
    List<TelecomValidationPo> findByBatchNo(String batchNo);

    List<TelecomValidationPo> findByNameAndIdNoAndMobileAndBatchNo(String name,String idNo,String  mobile,String batchNo);
}
