package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @Description: 采集人行机构集合
 * @author wanghongjie
 * @version 2018-07-13 11:28
 */
@Data
public class PbcCollectOrganDto extends BaseMaintainableDto {

	private static final long serialVersionUID = 3472673927505610900L;

	private Long id;

	/**
	 * 采集时间yyyyMMdd hh:mm:ss
	 */
	private String parDate;

	/**
	 * 采集批次 yyyyMMdd
	 */
	private String collectBatch;

	/**
	 * 机构名称
	 */
	private String bankName;

	/**
	 * 采集状态
	 */
	@Enumerated(EnumType.STRING)
	private CollectState collectState;
	/**
	 * 采集失败原因
	 */
	private String parErrorMsg;
	/**
	 * 组织结构ID
	 */
	private Long organizationId;
}
