package com.ideatech.ams.controller;

import com.ideatech.ams.system.operateLog.dto.OperateLogDto;
import com.ideatech.ams.system.operateLog.service.OperateLogService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operatelog")
@Slf4j
public class OperateLogController {
    @Autowired
    private OperateLogService operateLogService;
    @RequestMapping(value = "/findByRefBillId")
    public TableResultResponse<OperateLogDto> findByRefBillId(Long refBillId, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return operateLogService.query(refBillId,pageable);
    }
    @RequestMapping(value = "/save")
    public ResultDto save(OperateLogDto operateLogDto){
        try {
            OperateLogDto dto = operateLogService.updateAndSave(operateLogDto);
            return ResultDtoFactory.toApiSuccess(dto);
        }catch (Exception e){
            log.error("操作记录保存失败，失败原因：{}",e);
            return ResultDtoFactory.toApiError(ResultCode.DATA_SAVE_FAILURE.code(),e.getMessage(),e);
        }
    }
    @RequestMapping(value = "/listByRefBillId")
    public TableResultResponse<OperateLogDto> findByRefBillId(Long refBillId){
        return operateLogService.query(refBillId);
    }
}
