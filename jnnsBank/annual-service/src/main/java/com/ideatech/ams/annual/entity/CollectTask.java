package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.entity.BasePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;

/**
 * 采集任务表
 */
@Entity
@Data
public class CollectTask extends BasePo {

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
	private Integer count;
	/**
	 * 已处理的数据总数
	 */
	private Integer processed;
	/**
	 * 已成功的数据总数
	 */
	private Integer successed;

	/**
	 * 失败的数据总数
	 */
	private Integer failed;

	/**
	 * 异常原因
	 */
	@Column(name = "exception_reason")
	@Lob
	private String exceptionReason;

	/**
	 * 数据来源
	 */
	@Enumerated(EnumType.STRING)
	private DataSourceEnum collectTaskType;

	/**
	 * 任务ID
	 */
	private Long annualTaskId;
	/*
	 * 采集状态 有三种状态， Init 初始化  collecting 采集中  done 采集结束 pause 采集暂停
	 */
	@Enumerated(EnumType.STRING)
	private CollectTaskState collectStatus;

	/**
	 * 是否完成
	 */
	@Enumerated(EnumType.STRING)
	private CompanyIfType isCompleted = CompanyIfType.No;
	
}
