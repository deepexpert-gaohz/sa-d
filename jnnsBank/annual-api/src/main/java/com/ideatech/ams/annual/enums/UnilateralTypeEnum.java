package com.ideatech.ams.annual.enums;

import lombok.Getter;

/**
 *单边类型
 *
 * @author van
 * @date 10:21 2018/8/8
 */
@Getter
public enum UnilateralTypeEnum {
	PBC("人行单边"), CORE("核心单边"), NONE("正常");

	private String name;

	UnilateralTypeEnum(String name) {
		this.name = name;
	}
}
