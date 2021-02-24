package com.ideatech.ams.system.annotation.enums;

import lombok.Getter;

/**
 * 年检数据处理状态
 *
 * @author van
 * @date 11:33 2018/8/8
 */
@Getter
public enum SendStatusEnum {


	SUCCESS("处理成功"), FAIL("处理失败"), PROCESSING("正在处理"), WAIT_PROCESS("待处理");

	private String name;

	private SendStatusEnum(String name) {
		this.name = name;
	}
}
