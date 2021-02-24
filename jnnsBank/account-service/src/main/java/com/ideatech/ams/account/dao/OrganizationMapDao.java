package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.OrganizationMapPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrganizationMapDao extends JpaRepository<OrganizationMapPo, Long>, JpaSpecificationExecutor<OrganizationMapPo> {


    OrganizationMapPo findByOrgancode(String code);
}
