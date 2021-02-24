package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.EzhMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EzhMessageDao extends JpaRepository<EzhMessage, Long>, JpaSpecificationExecutor<EzhMessage> {


}
