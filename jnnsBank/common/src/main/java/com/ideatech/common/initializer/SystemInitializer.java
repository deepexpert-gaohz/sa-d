/**
 *
 */
package com.ideatech.common.initializer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 系统初始化器
 *
 * @author zhailiang
 *
 */
@Component
public class SystemInitializer implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * 系统中所有的{@link DataInitializer}接口实现
	 */
	@Autowired(required = false)
	private List<DataInitializer> dataInitializers;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 循环调用系统中所有的{@link DataInitializer}
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (CollectionUtils.isNotEmpty(dataInitializers)) {

			Collections.sort(dataInitializers, new Comparator<DataInitializer>() {
				@Override
				public int compare(DataInitializer initor1, DataInitializer initor2) {
					return initor1.getIndex().compareTo(initor2.getIndex());
				}
			});

			for (DataInitializer dataInitializer : dataInitializers) {
				try {
					dataInitializer.init();
				} catch (Exception e) {
					logger.info("系统数据初始化失败(" + dataInitializer.getClass().getSimpleName() + ")", e);
				}
			}
		}
	}

}
