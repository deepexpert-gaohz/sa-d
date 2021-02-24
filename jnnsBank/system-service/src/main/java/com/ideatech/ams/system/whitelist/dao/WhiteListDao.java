package com.ideatech.ams.system.whitelist.dao;

import com.ideatech.ams.system.blacklist.entity.BlackListEntryPo;
import com.ideatech.ams.system.whitelist.entity.WhiteListPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WhiteListDao extends JpaRepository<WhiteListPo, Long>, JpaSpecificationExecutor<WhiteListPo> {

    WhiteListPo findByEntNameAndStatus(String entName,String status);

//    List<WhiteListPo> findByEntName(String entName);

    WhiteListPo findByEntNameAndOrgId(String entName,Long orgId);

    WhiteListPo findByEntNameAndOrganCode(String entName,String organCode);

}
