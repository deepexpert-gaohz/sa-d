package com.ideatech.ams.system.pbc.dao;

import com.ideatech.ams.system.pbc.entity.PbcIPAddressPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PbcIPAddressDao extends JpaRepository<PbcIPAddressPo, Long>, JpaSpecificationExecutor<PbcIPAddressPo> {

    PbcIPAddressPo findByIp(String ip);
}
