package com.ideatech.ams.annual.dto;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.SaicStatusEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

/**
 *
 *
 * @author van
 * @date 15:34 2018/8/8
 */
@Data
public class AnnualResultDto extends BaseMaintainableDto {

	private Long id;

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

	private String organFullId;

	/**
	 * 任务id
	 */
	private Long taskId;

	/**
	 * 需要比对数据JSON格式
	 */
	private String coreData;

	private String pbcData;

	private String saicData;

	/**
	 * 单边类型
	 */
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
	private ResultStatusEnum result;

	/**
	 * 人行提交状态
	 */
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
	 * 提交错误原因
	 */
	private String pbcSubmitErrorMsg;

	/**
	 * 强制年检状态
	 */
	private ForceStatusEnum forceStatus;

	/**
	 * 强制年检状态
	 */
	private List<ForceStatusEnum> forceStatusList;

	private Boolean deleted;

	/**
	 * 人行提交状态集合
	 */
	private List<PbcSubmitStatusEnum> pbcSubmitStatuses;
	/**
	 * 人行提交状态集合包含NULL
	 */
	private List<PbcSubmitStatusEnum> pbcSubmitStatusesAndNull;

	private List<SaicStatusEnum> saicStatuses;

	private List<ResultStatusEnum> results;

	private String compareResult;

	/**
	 * 核心机构号
	 */
	private String organCode;

	/**
	 * 数据处理状态
	 */
	private DataProcessStatusEnum dataProcessStatus;

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
