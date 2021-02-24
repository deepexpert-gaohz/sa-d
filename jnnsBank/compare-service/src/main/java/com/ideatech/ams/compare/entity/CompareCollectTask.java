package com.ideatech.ams.compare.entity;

import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 比对管理--采集任务表
 */
@Entity
@Data
public class CompareCollectTask extends BaseMaintainablePo {

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
	@Column(length = 2000)
	private String exceptionReason;

	/**
	 * 数据来源
	 */
	@Enumerated(EnumType.STRING)
	private DataSourceEnum collectTaskType;
	/**
	 * 数据源的ID
	 */
	private Long dataSourceId;

	/**
	 * 任务ID
	 */
	private Long compareTaskId;
	/*
	 * 采集状态 有三种状态， -1 未开始 0 正在采集 1 采集成功 2 采集暂停 3 失败 4 关闭等待服务开始
	 */
	@Enumerated(EnumType.STRING)
	private CollectTaskState collectStatus;

	/**
	 * 是否完成
	 */
	@Enumerated(EnumType.STRING)
	private CompanyIfType isCompleted = CompanyIfType.No;
	
}
