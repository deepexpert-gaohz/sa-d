package com.ideatech.ams.annual.enums;

/**
 *可提交性
 *
 * @author van
 * @date 11:33 2018/8/8
 */
public enum CanBeSubmitStatusEnum {


	CANBE_SUBMIT("可提交"), NOT_CANBE_SUBMIT("不可提交");

	private String name;

	private CanBeSubmitStatusEnum(String name) {
		this.name = name;
	}
}
