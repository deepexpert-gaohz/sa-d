package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareField;
import com.ideatech.ams.compare.entity.CompareStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompareStatisticsDao extends JpaRepository<CompareStatistics,Long>, JpaSpecificationExecutor<CompareStatistics> {

    CompareStatistics findByCompareTaskIdAndOrganId(Long taskId, Long id);

    void deleteByCompareTaskId(Long taskId);
}
