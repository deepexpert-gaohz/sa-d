package com.ideatech.ams.config;

import com.ideatech.ams.system.trace.aop.UserTraceAspect;
import com.ideatech.ams.system.trace.aop.UserTraceAspectCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 切面配置类
 * @author jzh
 * @date 2019-10-30.
 */

@Configuration
public class AspectConfig {

    @Bean
    @Conditional(UserTraceAspectCondition.class)
    public UserTraceAspect uerTraceAspect(){
        return new UserTraceAspect();
    }
}
