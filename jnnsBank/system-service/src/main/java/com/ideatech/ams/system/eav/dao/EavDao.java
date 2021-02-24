package com.ideatech.ams.system.eav.dao;

import com.ideatech.ams.system.eav.entity.EavPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EavDao extends JpaRepository<EavPo, Long>, JpaSpecificationExecutor<EavPo> {
    List<EavPo> findByEntityIdAndDocId(Long entityId, Long docId);

    void deleteByEntityIdAndDocId(Long entityId, Long docId);
}
