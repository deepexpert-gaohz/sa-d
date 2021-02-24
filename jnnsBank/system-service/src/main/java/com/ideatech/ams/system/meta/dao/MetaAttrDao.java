package com.ideatech.ams.system.meta.dao;

import com.ideatech.ams.system.meta.entity.MetaAttrPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MetaAttrDao extends JpaRepository<MetaAttrPo, Long>, JpaSpecificationExecutor<MetaAttrPo> {
    List<MetaAttrPo> findByDocId(Long docId);

    MetaAttrPo findByDocIdAndName(Long docId, String name);

}
