package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.dto.BatchSuspendDto;
import com.ideatech.ams.account.entity.BatchSuspendPo;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BatchSuspendDao extends JpaRepository<BatchSuspendPo, Long>, JpaSpecificationExecutor<BatchSuspendPo> {

    /**
     * 根据批次号以及处理状态查看数据
     *
     * @param batchNo
     * @param processed
     * @return
     */
    long countByBatchNoAndProcessed(String batchNo, CompanyIfType processed);

    /**
     * 查找最新流水号
     *
     * @return
     */
    BatchSuspendPo findFirstByOrderByBatchNoDesc();

    /**
     * 根据流水编号返回信息
     *
     * @param batchNo
     * @return
     */
    List<BatchSuspendPo> findByBatchNo(String batchNo);

    /**
     * 根据处理状态以及批次号返回数据
     *
     * @param batchNo
     * @param processed
     * @return
     */
    List<BatchSuspendPo> findByBatchNoAndProcessed(String batchNo, CompanyIfType processed);

    /**
     * 根据批次号结束任务
     *
     * @param batchNo
     */
    @Modifying
    @Query("update BatchSuspendPo set processed = 'Yes' where batchNo = ?1")
    void updateProcessedByBatchNo(String batchNo);

    List<BatchSuspendPo> findByOrganFullIdLike(String organFullId);
}
