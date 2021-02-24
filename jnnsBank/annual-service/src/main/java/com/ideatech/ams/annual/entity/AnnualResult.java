package com.ideatech.ams.annual.entity;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.SaicStatusEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * 年检结果表
 * @author van
 * @date 2018/8/8 10:14
 */
@Entity
@Data
@Table(indexes = {@Index(name = "annual_result_an_idx", columnList = "acctNo"),
		@Index(name = "annual_result_dn_idx", columnList = "depositorName"),
		@Index(name = "annual_result_ti_idx", columnList = "taskId"),
		@Index(name = "annual_result_ofi_idx", columnList = "organFullId")})
public class AnnualResult extends BaseMaintainablePo {

	/**
	 * 账号
	 */
	private String acctNo;

	/**
	 * 存款人名称
	 */
	private String depositorName;

	/**
	 * 人行机构号
	 */
	private String organPbcCode;

	/**
	 * 机构fullId
	 */
	private String organFullId;

	/**
	 * 任务id
	 */
	private Long taskId;

	/**
	 * 核心比对数据JSON格式
	 */
	@Column(length = 2000)
    @Lob
	private String coreData;

	/**
	 * 人行比对数据JSON格式
	 */
	@Column(length = 2000)
    @Lob
	private String pbcData;

	/**
	 * 工商比对数据JSON格式
	 */
	@Column(length = 64000)
	@Lob
	private String saicData;

	/**
	 * 单边类型
	 */
	@Enumerated(EnumType.STRING)
	private UnilateralTypeEnum unilateral;

	/**
	 * 是否匹配
	 */
	private Boolean match;

	/**
	 * 异常状态
	 * 多值用逗号分隔
	 */
	private String abnormal;

	/**
	 * 是否黑名单
	 */
	private Boolean black;

	/**
	 * 工商状态
	 */
	private SaicStatusEnum saicStatus;

	/**
	 * 年检结果
	 */
	@Enumerated(EnumType.STRING)
	private ResultStatusEnum result = ResultStatusEnum.INIT;

	/**
	 * 人行提交状态
	 */
	@Enumerated(EnumType.STRING)
	private PbcSubmitStatusEnum pbcSubmitStatus;

	/**
	 * 提交人
	 */
	private String pbcSubmitter;

	/**
	 * 提交日期
	 */
	private String pbcSubmitDate;

	/**
	 * 人行提交错误原因
	 */
	private String pbcSubmitErrorMsg;

	/**
	 * 强制年检状态
	 */
	@Enumerated(EnumType.STRING)
	private ForceStatusEnum forceStatus = ForceStatusEnum.INIT;

	/**
	 * 删除状态
	 */
	private Boolean deleted = Boolean.FALSE;

	/**
	 * 字段比对状态
	 */
	@Column(length = 2000)
	@Lob
	private String compareResult;

	/**
	 * 核心机构号
	 */
	private String organCode;

	/**
	 * 数据处理状态
	 */
	@Enumerated(EnumType.STRING)
	private DataProcessStatusEnum dataProcessStatus = DataProcessStatusEnum.WAIT_PROCESS;

	/**
	 * 数据处理人
	 */
	private String dataProcessPerson;

	/**
	 * 数据处理日期
	 */
	private String dataProcessDate;

	/**
	 * 账户性质
	 */
	@Enumerated(EnumType.STRING)
	private CompanyAcctType acctType;

}
