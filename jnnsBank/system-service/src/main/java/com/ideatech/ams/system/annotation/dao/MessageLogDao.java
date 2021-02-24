package com.ideatech.ams.system.annotation.dao;

import com.ideatech.ams.system.annotation.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageLogDao extends JpaRepository<MessageLog, Long>, JpaSpecificationExecutor<MessageLog> {
}
