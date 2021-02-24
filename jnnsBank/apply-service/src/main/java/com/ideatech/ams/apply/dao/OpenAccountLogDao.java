package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.OpenAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OpenAccountLogDao extends JpaRepository<OpenAccountLog, Long>, JpaSpecificationExecutor<OpenAccountLog> {

}
