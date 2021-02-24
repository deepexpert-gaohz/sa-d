package com.ideatech.ams.customer.dao.illegal;

import com.ideatech.ams.customer.entity.illegal.IllegalQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IllegalQueryDao extends JpaRepository<IllegalQuery, Long>, JpaSpecificationExecutor<IllegalQuery> {

    List<IllegalQuery> findByIllegalQueryBatchId(Long batchId);

    List<IllegalQuery> findByIllegalQueryBatchIdAndAndIllegalStatusIsNull(Long batchId);

    long countByIllegalQueryBatchId(Long batchId);
}
