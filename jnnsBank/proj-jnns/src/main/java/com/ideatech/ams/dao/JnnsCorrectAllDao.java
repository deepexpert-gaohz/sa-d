package com.ideatech.ams.dao;

import com.ideatech.ams.domain.JnnsCorrectAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 业务冲正dao
 */
public interface JnnsCorrectAllDao extends JpaRepository<JnnsCorrectAll, Long>, JpaSpecificationExecutor<JnnsCorrectAll> {

}
