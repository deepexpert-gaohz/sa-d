package com.ideatech.ams.system.meta.dao;

import com.ideatech.ams.system.meta.entity.MetaDocPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author liangding
 * @create 2018-06-08 下午3:08
 **/
public interface MetaDocDao extends JpaRepository<MetaDocPo, Long>, JpaSpecificationExecutor<MetaDocPo> {
    MetaDocPo findByCode(String docCode);
}
