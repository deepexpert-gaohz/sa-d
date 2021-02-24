package com.ideatech.ams.system.blacklist.dao;

import com.ideatech.ams.system.blacklist.entity.BlackListEntryPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BlackListEntryDao extends JpaRepository<BlackListEntryPo, Long>, JpaSpecificationExecutor<BlackListEntryPo> {

    List<BlackListEntryPo> findByEntName(String entName);

}
