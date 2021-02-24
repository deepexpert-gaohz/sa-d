package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  采集人行机构集合
 * @author wanghongjie
 * @version 2018-07-13 11:28
 */
@Entity
@Table(name = "PBC_COLLECT_ORGAN")
@Data
public class PbcCollectOrgan extends BaseMaintainablePo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 265675977576420464L;

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
	@Column(length = 2000)
	private String parErrorMsg;
	/**
	 * 组织结构ID
	 */
	private Long organizationId;
}
