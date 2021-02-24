package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

@Data
public class CollectTaskDto {
	private Long id;
	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 要处理的数据总数
	 */
	private int count;
	/**
	 * 已处理的数据总数
	 */
	private int processed;
	/**
	 * 已成功的数据总数
	 */
	private int successed;

	/**
	 * 失败的数据总数
	 */
	private int failed;

	private String exceptionReason;

	private DataSourceEnum collectTaskType;

	private Long annualTaskId;

	private CollectTaskState collectStatus;

	private CompanyIfType isCompleted;


}
