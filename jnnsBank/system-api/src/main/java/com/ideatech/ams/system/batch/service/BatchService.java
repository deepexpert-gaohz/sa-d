package com.ideatech.ams.system.batch.service;

import com.ideatech.ams.system.batch.dto.BatchDto;
import com.ideatech.ams.system.batch.dto.BatchSearchDto;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author vantoo
 * @date 2018/10/17 3:26 PM
 */
public interface BatchService extends BaseService<BatchDto> {

    /**
     * 创建批次
     *
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @return
     */

    String createBatch(String fileName, Long fileSize, BatchTypeEnum batchType);

    /**
     * 创建批次
     *
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param txCount 数据量
     * @return
     */

    String createBatch(String fileName, Long fileSize, Long txCount, BatchTypeEnum batchType);

    /**
     * 结束批次
     * @param batchNo
     */
    void finishBatch(String batchNo);

    /**
     * 查询接口
     *
     * @param batchSearchDto
     * @return
     */
    TableResultResponse query(BatchSearchDto batchSearchDto, Pageable pageable);

    /**
     * 更新业务量
     * @param txCount
     * @param batchNo
     */
    void updateBatchCount(String batchNo, Long txCount);

}
