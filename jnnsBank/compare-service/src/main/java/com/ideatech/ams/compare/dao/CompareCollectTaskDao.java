package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareCollectTask;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 比对管理--采集任务DAO层
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Repository
public interface CompareCollectTaskDao  extends JpaRepository<CompareCollectTask,Long>, JpaSpecificationExecutor<CompareCollectTask> {
    CompareCollectTask findById(Long id);
    CompareCollectTask findByCompareTaskIdAndCollectTaskType(Long compareTaskId, DataSourceEnum collectTaskType);
    List<CompareCollectTask> findByCompareTaskIdAndCollectStatus(Long compareTaskId, CollectTaskState collectStatus);
    CompareCollectTask findByCompareTaskIdAndDataSourceId(Long compareTaskId,Long dataSourceId);
    List<CompareCollectTask> findByCompareTaskId(Long compareTaskId);
}
