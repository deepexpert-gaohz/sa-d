package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.AnnualStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface AnnualStatisticsDao extends JpaRepository<AnnualStatistics, Long>, JpaSpecificationExecutor<AnnualStatistics> {

    AnnualStatistics findByTaskIdAndOrganId(Long taskId, Long id);

    /**
     * 删除某任务的所有统计
     *
     * @param taskId
     * @return
     */
    @Modifying
    @Transactional
    @Query("delete from AnnualStatistics where taskId = ?1")
    int deleteByTaskId(Long taskId);

}
