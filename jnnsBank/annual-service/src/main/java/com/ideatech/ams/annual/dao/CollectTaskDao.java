package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectTaskDao  extends JpaRepository<CollectTask, Long>, JpaSpecificationExecutor<CollectTask> {
    /*
     * 找出最新的采集任务，并且该采集任务的状态不是已完成的
     */
    @Query("select collectTask from CollectTask collectTask where isCompleted <> 'Yes' AND collectTaskType = ?1 AND annualTaskId = ?2 order by createdDate desc ")
    List<CollectTask> findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum collectTaskType, Long taskId);

    @Query("select collectTask from CollectTask collectTask where collectTaskType = ?1 AND annualTaskId = ?2 order by createdDate desc ")
    List<CollectTask> findLastTaskByTypeAndAnnualTaskIdAnd(DataSourceEnum collectTaskType, Long taskId);

    List<CollectTask> findByCollectTaskType(DataSourceEnum collectTaskType);

    CollectTask findById(Long id);

	/**
	 * 根据任务删除采集数据
	 * @param taskId
	 */
    @Modifying
    @Query("delete from CollectTask where annualTaskId = ?1")
	void deleteByAnnualTaskId(Long taskId);

	void deleteByAnnualTaskIdAndCollectTaskType(Long taskId,DataSourceEnum collectTaskType);

}
