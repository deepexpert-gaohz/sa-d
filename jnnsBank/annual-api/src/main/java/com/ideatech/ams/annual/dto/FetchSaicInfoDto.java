package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 *
 *
 * @author van
 * @date 20:21 2018/8/8
 */
@Data
public class FetchSaicInfoDto extends BaseMaintainableDto {

	private Long id;

	private String acctNo;

	private Long annualTaskId;

	private Long collectTaskId;

	private String customerName;

	private String customerNameOther;

	private String regNo;

	private String failReason;

	/**
	 * 经营状态
	 */
	private String state;

	/**
	 * 工商注册号
	 */
	private String unitycreditcode;

	/**
	 * 营业期限终止日期
	 */
	private String enddate;

	/*
	 * 每条记录添加采集状态字段
	 */
	private CollectState collectState;

	/**
	 * idp返回json
	 */
	private String idpJsonStr;

}
