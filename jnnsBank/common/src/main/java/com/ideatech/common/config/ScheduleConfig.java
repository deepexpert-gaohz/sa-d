/**
 * 
 */
package com.ideatech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务模块配置，加载此配置后，系统可以通过向平台注册{@link ScheduledService}接口的实现来添加定时任务<br>
 * <br>
 * 
 * 所有的定时任务都会被包装成Spring Batch的Job，当定时任务被执行时，Spring Batch会负责将定时任务的 执行情况记录到数据库。<br>
 * <br>
 * 
 * @author jojo 2014-8-13 上午9:16:09
 *
 */
@Configuration
public class ScheduleConfig {

	/**
	 * 用来控制并发线程的数量
	 */
	@Value("${ams.schedule.executor.pool.size:300}")
	private int poolsize;

	/**
	 * <pre>
	 * 任务调度器配置
	 * 
	 * <pre>
	 * @return
	 * @author jojo 2014-8-13 上午9:56:06
	 */
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(poolsize);
		scheduler.afterPropertiesSet();
		return scheduler;
	}

}
