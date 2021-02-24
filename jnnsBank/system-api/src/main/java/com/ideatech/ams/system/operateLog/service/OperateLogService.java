package com.ideatech.ams.system.operateLog.service;

import com.ideatech.ams.system.operateLog.dto.OperateLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface OperateLogService extends BaseService<OperateLogDto> {
    /**
     * 查询流水的操作记录 分页
     * @param refBillId
     * @param pageable
     * @return
     */
    TableResultResponse<OperateLogDto> query(Long refBillId, Pageable pageable);

    TableResultResponse<OperateLogDto> query(Long refBillId);

    /**
     * 记录日志
     * @param logDto
     * @return
     */
    OperateLogDto updateAndSave(OperateLogDto logDto);
}
