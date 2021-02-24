package com.ideatech.ams.system.trace.aop;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.service.OperateLogService;
import com.ideatech.common.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

/**
 * 用户操作痕迹切面类
 * @author jzh
 * @date 2019-10-30.
 */

//@Component 改成配置启动
@Slf4j
@Aspect
public class UserTraceAspect {


    /**
     * 返回后切面
     * @param joinPoint
     * @param operateLog
     * @param object
     * @throws Throwable
     */
    @AfterReturning(value = "@annotation(operateLog)",returning = "object")
    public void doAterReturning(JoinPoint joinPoint, OperateLog operateLog ,Object object)throws Throwable{
        try{
            OperateLogService operateLogService = ApplicationContextUtil.getBean(operateLog.operateLogService());
            operateLogService.process(joinPoint,getUserTraceDTO(operateLog),object);
        }catch (Exception e){
            log.warn("保存用户操作痕迹时发生异常:",e);
        }
    }

    /**
     * 抛出异常后切面
     * @param joinPoint
     * @param operateLog
     * @param e
     * @throws Throwable
     */
    @AfterThrowing(value = "@annotation(operateLog)", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, OperateLog operateLog, Throwable e) throws Throwable{
        try{
            OperateLogService operateLogService = ApplicationContextUtil.getBean(operateLog.operateLogService());
            operateLogService.process(joinPoint,getUserTraceDTO(operateLog),e);
        }catch (Exception e2){
            log.warn("保存用户操作痕迹时发生异常:",e2);
        }
    }

    /**
     * 根据OperateLog注解配置返回UserTraceDTO
     * @param operateLog
     * @return
     */
    private UserTraceDTO getUserTraceDTO(OperateLog operateLog){
        UserTraceDTO userTraceDTO = new UserTraceDTO();
        userTraceDTO.setOperateModule(operateLog.operateModule());
        userTraceDTO.setOperateType(operateLog.operateType());
        userTraceDTO.setOperateContent(operateLog.operateContent());
        userTraceDTO.setCover(operateLog.cover());
        return userTraceDTO;
    }
}
