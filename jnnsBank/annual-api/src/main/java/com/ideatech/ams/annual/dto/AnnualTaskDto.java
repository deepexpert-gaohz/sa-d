package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.TaskStatusEnum;
import com.ideatech.ams.annual.enums.TaskTypeEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 年检任务
 * @author van
 * @date 2018/8/7 20:41
 */
@Data
public class AnnualTaskDto extends BaseMaintainableDto {

	private Long id;

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
	private TaskTypeEnum type;

	/**
	 * 总数
	 */
	private Long sum;

	private Long processedNum;

	private Long passedNum;

	/**
	 * 任务状态
	 */
	private TaskStatusEnum status;

}