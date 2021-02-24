package com.ideatech.ams.customer.dao.illegal;

import com.ideatech.ams.customer.entity.illegal.IllegalQueryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IllegalQueryBatchDao extends JpaRepository<IllegalQueryBatch, Long>, JpaSpecificationExecutor<IllegalQueryBatch> {

}
