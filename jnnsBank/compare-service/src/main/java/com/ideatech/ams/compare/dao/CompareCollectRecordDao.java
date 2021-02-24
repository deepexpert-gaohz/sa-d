package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareCollectRecord;
import com.ideatech.ams.compare.enums.CollectState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 比对管理--采集记录DAO层
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Repository
public interface CompareCollectRecordDao extends JpaRepository<CompareCollectRecord,Long>, JpaSpecificationExecutor<CompareCollectRecord> {
    List<CompareCollectRecord> findByCollectTaskIdAndCompareTaskId(Long collectTaskId, Long compareTaskId);
    List<CompareCollectRecord> findByCollectTaskIdAndCompareTaskIdAndCollectState(Long collectTaskId, Long compareTaskId, CollectState collectState);
    Page<CompareCollectRecord> findByCompareTaskId(Long taskId, Pageable pageable);
    CompareCollectRecord findByCompareTaskIdAndAcctNoAndDataSourceType(Long taskId,String acctNo,String dataSourceType);
    CompareCollectRecord findByCollectTaskIdAndCompareTaskIdAndAcctNo(Long collectTaskId, Long compareTaskId,String acctNo);
    List<CompareCollectRecord> findByCompareTaskIdAndDataSourceType(Long taskId,String dataSourceType);
}
