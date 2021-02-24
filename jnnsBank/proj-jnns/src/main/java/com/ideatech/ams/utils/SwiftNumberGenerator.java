package com.ideatech.ams.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 流水号
 * 
 * @author zoulang
 * @date 2017年10月30日
 *
 */
@Component
public class SwiftNumberGenerator {

	/** 流水号长度 */
	private static final int SWIFT_NUMBER_LENGTH = 10;

	private static final String NO_1_SWIFT_NUMBER = "000001";

	private static SwiftNumberGenerator generator = null;

	private static Date currentDate = new Date();

	private static String currentSwiftNumber = null;

	static {
		generator = new SwiftNumberGenerator();
	}

	private SwiftNumberGenerator() {

	}

	public static SwiftNumberGenerator getInstance() {
		return generator;
	}

	private Date now() {
		return new Date();
	}

	public synchronized String genenrateNextSwiftNumber() {
		Date now = now();
		String number = null;
		// 第一次调用的时候，currentSwiftNumber为null
		if (StringUtils.isEmpty(currentSwiftNumber)) {
			number = DateUtils.getNowDateShort("yyyyMMdd") + NO_1_SWIFT_NUMBER;
		} else {
			if (org.apache.commons.lang.time.DateUtils.isSameDay(now, currentDate)) {
				long num = Long.parseLong(currentSwiftNumber);
				// 如果到达最大值，就从1开始
				if (Long.MAX_VALUE == num) {
					num = 1;
				}
				number = StringUtils.leftPad(String.valueOf(num + 1), SWIFT_NUMBER_LENGTH, "0");
			}
			// 不是同一天的话，从0000000001开始生成
			else {
				currentDate = now;
				number = NO_1_SWIFT_NUMBER;
			}
		}
		currentSwiftNumber = number;
		return number;
	}

	public static void main(String[] args) {
		SwiftNumberGenerator generator = SwiftNumberGenerator.getInstance();
		for (int i = 0; i < 10; i++) {
			System.out.println(generator.genenrateNextSwiftNumber());
		}
	}
}
