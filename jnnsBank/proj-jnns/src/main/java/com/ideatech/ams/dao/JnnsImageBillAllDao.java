package com.ideatech.ams.dao;



import com.ideatech.ams.domain.JnnsImageBillAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JnnsImageBillAllDao extends JpaRepository<JnnsImageBillAll, Long>, JpaSpecificationExecutor<JnnsImageBillAll> {

    JnnsImageBillAll findByJnBillId(String jnBillId);
}
