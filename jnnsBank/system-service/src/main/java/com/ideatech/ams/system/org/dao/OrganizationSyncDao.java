package com.ideatech.ams.system.org.dao;

import com.ideatech.ams.system.org.entity.OrganizationSyncPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationSyncDao extends JpaRepository<OrganizationSyncPo, Long>, JpaSpecificationExecutor<OrganizationSyncPo> {
    List<OrganizationSyncPo> findBySyncFinishStatusOrderByCreatedDateAsc(Boolean syncStatus);
}
