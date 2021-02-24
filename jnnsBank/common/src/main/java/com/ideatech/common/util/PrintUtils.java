package com.ideatech.common.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintUtils {
	public static void printObjectColumn(Object object) {
		Map<String, Object> map = BeanValueUtils.getFieldValueMap(object);
		log.info("==========" + object.getClass().getSimpleName() + "对象字段值开始===========");
		for (String key : map.keySet()) {
			if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key).toString())) {
				log.info(key + ":" + map.get(key));
			}
		}
		log.info("==========" + object.getClass().getSimpleName() + "对象字段值结束===========");
	}
}
