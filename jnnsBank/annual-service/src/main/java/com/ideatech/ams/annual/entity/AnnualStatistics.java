/**
 * 
 */
package com.ideatech.ams.annual.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 比对结果统计信息，按机构统计
 * @author zhailiang
 *
 */
@Entity
@Data
public class AnnualStatistics extends BaseMaintainablePo {

	/**
	 * 机构ID
	 */
	private Long organId;

	/**
	 * 任务ID
	 */
	private Long taskId;

	/**
	 * 总数
	 */
	private long count;
	/**
	 * 成功数
	 */
	private long success;

	/**
	 * 根据task和uuid锁定compareTask外检的数据。
	 */
//	private String uuid;

}
