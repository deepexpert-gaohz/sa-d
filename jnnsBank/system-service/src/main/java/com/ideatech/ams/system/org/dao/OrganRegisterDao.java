package com.ideatech.ams.system.org.dao;

import com.ideatech.ams.system.org.entity.OrganRegisterPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganRegisterDao extends JpaRepository<OrganRegisterPo, Long>, JpaSpecificationExecutor<OrganRegisterPo> {

    List<OrganRegisterPo> findByPbcCode(String pbcCode);

    OrganRegisterPo findByOrganId(Long organId);

    OrganRegisterPo findByIdAndFullIdStartingWith(Long id,String fullId);

    OrganRegisterPo findByFullId(String organFullId);

    void deleteByOrganId(Long organId);
}
