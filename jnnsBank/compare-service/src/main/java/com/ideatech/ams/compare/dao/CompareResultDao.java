package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("compareResultDao")
public interface CompareResultDao extends JpaRepository<CompareResult, Long>, JpaSpecificationExecutor<CompareResult> {

    /**
     * 统计任务的条数
     * @param compareTaskId
     * @return
     */
    long countByCompareTaskId(Long compareTaskId);

    CompareResult findByCompareTaskIdAndDepositorName(Long compareTaskId, String depositorName);

}
