package com.ideatech.ams.controller;

import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.dto.UserTraceSearchDTO;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.trace.service.UserTraceService;
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

/**
 * @author jzh
 * @date 2019-10-31.
 */

@RestController
@RequestMapping(value = "/trace")
@Slf4j
public class UserTraceController {

    @Autowired
    private UserTraceService userTraceService;

//    @OperateLog(operateModule = OperateModule.ROLE,operateType = OperateType.SELECT)
    @RequestMapping(value = "/query")
    public TableResultResponse query(UserTraceSearchDTO userTraceSearchDTO, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return userTraceService.query(userTraceSearchDTO,pageable);
    }

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.SELECT)
//    @RequestMapping(value = "/query2")
    public TableResultResponse query2(UserTraceSearchDTO userTraceSearchDTO, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return userTraceService.query(userTraceSearchDTO,pageable);
    }

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.SELECT)
//    @RequestMapping(value = "/query3")
    public ResultDto query3(){
        int a = 1/0;
        return ResultDtoFactory.toAck();
    }
}
