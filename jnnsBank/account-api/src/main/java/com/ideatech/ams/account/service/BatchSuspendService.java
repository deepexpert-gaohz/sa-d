package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.BatchSuspendDto;
import com.ideatech.ams.account.dto.BatchSuspendSearchDto;
import com.ideatech.ams.account.enums.bill.BatchSuspendSourceEnum;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface BatchSuspendService extends BaseService<BatchSuspendDto> {

    /**
     * 根据批次号查询是否有未处理的数据
     *
     * @param batchNo
     * @return
     */
    Boolean isProcessing(String batchNo);

    /**
     * 查询任务是否都已处理
     *
     * @return
     */
    BatchSuspendDto isProcessing();

    /**
     * 根据批次号统计处理进度
     *
     * @param batchNo
     * @return
     */
    ObjectRestResponse getProcessByBatchNo(String batchNo);

    /**
     * 开始处理批量任务
     *
     * @param batchNo
     */
    void doProcess(String batchNo);

    /**
     * 结束批次
     *
     * @param batchNo
     */
    void finishByBatchNo(String batchNo);

    /**
     * 批量久悬文件处理
     *
     * @throws Exception
     */
    void process() throws Exception;

    /**
     * 处理TXT文件
     *
     * @param file
     */
    List<BatchSuspendDto> readTxtFile(File file);

    /**
     * 数据化数据并保存
     *
     * @param batchNo
     * @param source
     * @param batchSuspendDtos
     */
    void initSave(String batchNo, BatchSuspendSourceEnum source, List<BatchSuspendDto> batchSuspendDtos);

    /**
     * 分页查询
     * @param searchDto
     * @param pageable
     * @return
     */
    TableResultResponse query(BatchSuspendSearchDto searchDto, Pageable pageable);

    /**
     * 重新上报人行
     *
     * @param ids
     * @param billIds
     */
    void submitSync(Long[] ids, Long[] billIds);

    /**
     * 根据机构fullId查找久悬数据
     * @param organFullId
     * @return
     */
    List<BatchSuspendDto> findByOrganFullIdLike(String organFullId);

}
