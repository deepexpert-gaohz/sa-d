package com.ideatech.common.config;

import com.ideatech.common.entity.id.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author fantao
 * @date 2019/11/13 5:36 下午
 */
@Configuration
@Slf4j
public class IdWorkerConfig {

    /**
     * 统一的ID生成器
     * 分布式环境下需要指定不同的workId
     *
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        Long workId = new Long(new Random().nextInt(30));
        IdWorker idWorker = new IdWorker(workId);
        log.info("生成的workId为{}", workId);
        return idWorker;
    }

}
