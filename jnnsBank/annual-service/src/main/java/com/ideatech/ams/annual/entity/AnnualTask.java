package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.TaskStatusEnum;
import com.ideatech.ams.annual.enums.TaskTypeEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *年检任务表
 *
 * @author van
 * @date 20:15 2018/8/7
 */
@Entity
@Data
public class AnnualTask extends BaseMaintainablePo {

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 使用的比对规则
	 */
	private Long compareRuleId;

	/**
	 * 年份
	 */
	private Integer year;

	/**
	 * 任务类型
	 */
	@Enumerated(EnumType.STRING)
	private TaskTypeEnum type;

	/**
	 * 总数
	 */
	private Long sum;

	/**
	 * 失败数
	 */
	private Long processedNum;

	/**
	 * 通过总数
	 */
	private Long passedNum;

	/**
	 * 任务状态
	 */
	@Enumerated(EnumType.STRING)
	private TaskStatusEnum status;

}
