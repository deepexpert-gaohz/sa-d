/**
 * 
 */
package com.ideatech.ams.annual.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 比对结果统计信息，按机构统计
 * @author zhailiang
 *
 */
@Data
public class AnnualStatisticsDto extends BaseMaintainableDto {

	private Long id;

	private Long organId;

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

	public AnnualStatisticsDto(long count2, long success2) {
		this.count = count2;
		this.success = success2;
	}

}
