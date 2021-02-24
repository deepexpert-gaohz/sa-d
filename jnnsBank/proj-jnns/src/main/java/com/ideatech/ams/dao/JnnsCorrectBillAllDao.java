package com.ideatech.ams.dao;

import com.ideatech.ams.domain.JnnsCorrectBillAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 业务冲正流水表
 */
public interface JnnsCorrectBillAllDao extends JpaRepository<JnnsCorrectBillAll, Long>, JpaSpecificationExecutor<JnnsCorrectBillAll> {
    JnnsCorrectBillAll findByJnBillId(String jnBillId);
}
