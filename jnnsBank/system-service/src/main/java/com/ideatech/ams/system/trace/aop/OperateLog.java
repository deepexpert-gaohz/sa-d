package com.ideatech.ams.system.trace.aop;

import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.trace.service.DefaultOperateLogServiceImpl;
import com.ideatech.ams.system.trace.service.OperateLogService;

import java.lang.annotation.*;

/**
 * @author jzh
 * @date 2019-10-30.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 用户操作痕迹的服务类
     * @return
     */
    Class<? extends OperateLogService> operateLogService() default DefaultOperateLogServiceImpl.class;

    /**
     * 操作模块
     * @return
     */
    OperateModule operateModule();

    /**
     * 操作类型
     * @return
     */
    OperateType operateType();

    /**
     * 操作内容
     * @return
     */
    String operateContent() default "";

    /**
     * 生成操作内容时是否用operateContent覆盖OperateType，及生成是否带有ID的信息
     * 当operateContent默认空时，该设置无效。
     * @return
     */
    boolean cover() default false;
}
