package com.ideatech.ams.annual.enums;

import lombok.Getter;

/**
 * 人行提交性
 *
 * @author van
 * @date 11:33 2018/8/8
 */
@Getter
public enum PbcSubmitStatusEnum {


	SUCCESS("提交成功"), FAIL("提交失败"), NO_NEED_SUBMIT("无需提交"), WAIT_SUBMIT("待提交");

	private String name;

	private PbcSubmitStatusEnum(String name) {
		this.name = name;
	}
}
