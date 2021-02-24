package com.ideatech.ams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Description 系统基本配置
 * @Author wanghongjie
 * @Date 2018/8/3
 **/
@Configuration
public class AmsConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(300);
        taskExecutor.setCorePoolSize(100);
        return taskExecutor;
    }
}
