package com.ideatech.ams.annual.dto.poi;

import com.ideatech.ams.annual.enums.*;
import lombok.Data;

/**
 *
 * 工商导出excel
 * @author van
 * @date 15:34 2018/8/8
 */
@Data
public class AnnualResultPoi {

	/**
	 * 账号
	 */
	private String acctNo;

	/**
	 * 展示需比对字段
	 */
	private String coreName;
	private String coreOrgCode;
	private String coreOrganCode;
	private String coreLegalName;
	private String coreRegNo;
    private String coreBusinessScope;
    private String coreRegAddress;
	private String coreRegisteredCapital;
	private String coreAccountStatus;
	private String coreLegalIdcardType;
	private String coreLegalIdcardNo;
	private String coreRegCurrencyType;
	private String coreStateTaxRegNo;
	private String coreTaxRegNo;

	private String saicName;
	private String saicOrgCode;
    private String saicOrganCode;
	private String saicLegalName;
	private String saicRegNo;
    private String saicBusinessScope;
    private String saicRegAddress;
	private String saicRegisteredCapital;
	private String saicAccountStatus;
	private String saicLegalIdcardType;
	private String saicLegalIdcardNo;
	private String saicRegCurrencyType;
	private String saicStateTaxRegNo;
	private String saicTaxRegNo;

	private String pbcName;
	private String pbcOrgCode;
    private String pbcOrganCode;
	private String pbcLegalName;
	private String pbcRegNo;
	private String pbcRegAddress;
    private String pbcRegisteredCapital;
    private String pbcBusinessScope;
	private String pbcAccountStatus;
	private String pbcLegalIdcardType;
	private String pbcLegalIdcardNo;
	private String pbcRegCurrencyType;
	private String pbcStateTaxRegNo;
	private String pbcTaxRegNo;

//	/**
//	 * 人行机构号
//	 */
//	private String organPbcCode;

	/**
	 * 账户性质
	 */
	private String acctType;

	/**
	 * 行内机构号
	 */
	private String organCode;

	/**
	 * 单边类型
	 */
	private String unilateral;

	/**
	 * 异常状态
	 * 多值用逗号分隔
	 */
	private String abnormal;

	/**
	 * 是否黑名单
	 */
	private String black;

	/**
	 * 工商状态
	 */
	private String saicStatus;

	/**
	 * 是否匹配
	 */
	private String match;

	/**
	 * 年检结果
	 */
	private String result;

	/**
	 * 提交人
	 */
	private String pbcSubmitter;
//	/**
//	 * 人行提交状态
//	 */
//	private String pbcSubmitStatus;
//
//	/**
//	 * 强制年检状态
//	 */
//	private String forceStatus;

}
