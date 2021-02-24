package com.ideatech.ams.annual.enums;

import lombok.Getter;

/**
 * 强制年检标识
 *
 * @author van
 * @date 11:35 2018/8/8
 */
@Getter
public enum ForceStatusEnum {
	INIT("未强制年检"), SUCCESS("强制年检成功"), AGAIN_SUCCESS("重新年检成功");

	private String name;

	ForceStatusEnum(String name) {
		this.name = name;
	}
}
