package com.ideatech.ams.system.batch.dao;

import com.ideatech.ams.system.batch.entity.BatchPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchDao extends JpaRepository<BatchPo, Long>, JpaSpecificationExecutor<BatchPo> {

    BatchPo findByBatchNo(String batchNo);

}
