package com.ideatech.ams.annual.entity;


import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  采集人行的账户集合
 * @author wanghongjie
 * @version 2018-07-13 11:28
 */
@Entity
@Table(name = "PBC_COLLECT_ACCOUNT")
@Data
public class PbcCollectAccount extends BaseMaintainablePo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -87443033799067760L;

	/**
	 * 采集时间 yyyyMMdd hh:mm:ss
	 */
	private String parDate;

	/**
	 * 账号
	 */
	private String acctNo;
	/**
	 * 采集状态
	 */
	@Enumerated(EnumType.STRING)
	private CollectState collectState;

	/**
	 * 机构信用代码证系统采集状态
	 */
	@Enumerated(EnumType.STRING)
	private CollectState eccsCollectState;
	
	/**
	 * 信用代码证采集时间
	 */
	private String eccsParDate;

	/**
	 * 信用代码证采集失败完整
	 */
	private String eccsParErrorMsg;

	/**
	 * 采集失败原因
	 */
	@Column(length = 2000)
	private String parErrorMsg;

	/**
	 * 账户状态
	 */
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;

	/**
	 * 开户日期
	 */
	private String acctCreateDate;

	/**
	 * 企业名称
	 */
	private String acctName;

	/**
	 * 存款人名称
	 */
	private String depositorName;
	
	/**
	 * 采集人行机构集合ID
	 */
	private Long collectOrganId;

	/**
	 * 年检task的ID
	 */
	private Long annualTaskId;

	/**
	 * 收集task的ID
	 */
	private Long collectTaskId;
	
}
