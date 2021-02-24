package com.ideatech.ams.account.dao.core;

import com.ideatech.ams.account.entity.core.CoreFileBatchPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreFileBatchDao extends JpaRepository<CoreFileBatchPo, Long>, JpaSpecificationExecutor<CoreFileBatchPo> {
}
