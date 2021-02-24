package com.ideatech.ams.system.operateLog.dao;

import com.ideatech.ams.system.operateLog.entity.OperateLogPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateLogDao extends JpaRepository<OperateLogPo, Long>, JpaSpecificationExecutor<OperateLogPo> {

    List<OperateLogPo> findByRefBillIdOrderByLastUpdateDate(Long refBillId, Pageable pageable);
    List<OperateLogPo> findByRefBillIdOrderByLastUpdateDate(Long refBillId);
    long countByRefBillId(Long refBillId);
}
