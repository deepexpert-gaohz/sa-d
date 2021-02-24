package com.ideatech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @author van
 * @date 10:13 2018/8/9
 */
@Configuration
public class ThreadConfig {

	@Value("${import.file.pbc-cover-core.thread-size:10}")
	private int threadSize;

	@Bean
	public ThreadPoolTaskExecutor annualExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(200);
		taskExecutor.setCorePoolSize(10);
		return taskExecutor;
	}

	@Bean
	public ThreadPoolTaskExecutor compareExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(200);
		taskExecutor.setCorePoolSize(10);
		return taskExecutor;
	}

	@Bean
	public ThreadPoolTaskExecutor pbcCoverCoreExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(200);
		taskExecutor.setCorePoolSize(threadSize);
		return taskExecutor;
	}
}
