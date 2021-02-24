package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import org.aspectj.lang.JoinPoint;

/**
 * @author jzh
 * @date 2019-10-30.
 */
public interface OperateLogService {

    void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO) throws Exception;

    void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Object object)throws Exception;

    void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Throwable e)throws Exception;
}
