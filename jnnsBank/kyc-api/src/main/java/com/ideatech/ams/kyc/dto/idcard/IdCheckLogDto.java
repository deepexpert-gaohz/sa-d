package com.ideatech.ams.kyc.dto.idcard;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

@Data
public class IdCheckLogDto extends BaseMaintainableDto {

	private Long id;

	/**
	   * 本地身份证信息id
	   */
	  private Long idCardLocalId;
	  /**
	   * 人行身份证信息id
	   */
	  private Long idCardPbcId;
	  /**
	   * 人行查询结果码
	   */
	  private String checkCode;
	  /**
	   * 操作信息
	   */
	  private String checkMessage;
	  /**
	   * 核查时间
	   */
	  
	  private Date checkDate;
	  /**
	   * 核查IP
	   */
	  private String checkIP;
	  /**
	   * 核查人员
	   */
	  private Long checkUserId;
	  /**
	   * 完整机构id
	   */
	  private String organFullId;
	  /**
	   * 核查结果标识
	   */
	  private String checkStatus;
	  /**
	   * 核查结果
	   */
	  private String checkResult;
	  /**
		 * 备用字段1
		 */
	  private String string01;
		/**
		 * 备用字段2
		 */
		private String string02;
		/**
		 * 备用字段3
		 */
		private String string03;
		/**
		 * 备用字段4
		 */
		private String string04;
		/**
		 * 备用字段5
		 */
		private String string05;
		
		//20180117新增属性--方便传送到前台
		private String idCardNo;
		private String idCardName;
		private String idCardOrgan;
		private String idCardLocalImageByte;
		private String idCardPbcImageByte;
}
