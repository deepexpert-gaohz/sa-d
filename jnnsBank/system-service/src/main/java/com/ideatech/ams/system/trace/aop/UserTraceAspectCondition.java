package com.ideatech.ams.system.trace.aop;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 条件注解，根据配置文件决定是否初始化类
 * @author jzh
 * @date 2019-10-30.
 */
public class UserTraceAspectCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Boolean.valueOf(conditionContext.getEnvironment().getProperty("ams.userTrace.use"));
    }
}