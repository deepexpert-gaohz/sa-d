package com.ideatech.ams.annual.enums;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author van
 * @date 14:06 2018/8/8
 */
@Getter
public enum AbnormalStatusEnum {

	//@formatter:off
	INIT("未采集"),
	NOT_FOUND("未找到"), CANCEL("注销撤销"), REVOKE("吊销"),
	ABNORMAL_OPERATION("经营异常"), NO_ANNUAL_REPORT("无年报"),
	BUSINESS_LICENSE_EXPIRED("营业执照到期"),
	BLACK("黑名单"),ILLEGAL("严重违法");
	//@formatter:on

	private String name;

	public static final String ABNORMAL_SEPARATOR = ",";

	AbnormalStatusEnum(String name) {
		this.name = name;
	}




}
