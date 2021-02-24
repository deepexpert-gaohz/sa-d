package com.ideatech.ams.annual.enums;

/**
 * 任务类型
 * @author van
 * @date 2018/8/7 20:19
 */
public enum TaskStatusEnum {
	INIT("未开始"), PREPARED("准备完成"), PROCESSING("进行中"), FINISH("完成"), AGAIN_PROCESSING("重新年检进行中"), AGAIN_FINISH("重新年检完成");

	private String name;

	TaskStatusEnum(String name) {
		this.name = name;
	}

}
