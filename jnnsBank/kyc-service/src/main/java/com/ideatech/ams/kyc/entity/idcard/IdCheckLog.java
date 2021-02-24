package com.ideatech.ams.kyc.entity.idcard;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 身份证信息核查记录
 * @author yang
 *
 */
@Data
@Entity
@Table(name = "IdCheckLog")
public class IdCheckLog extends BaseMaintainablePo implements Serializable{

	/**
	   * 序列化ID,缓存需要
	   */
	  private static final long serialVersionUID = 5454155825314635342L;
	  public static String baseTableName = "YD_IdCheckLog";
	  /**
	   * 本地身份证信息
	   */
	  private Long idCardLocalId;
	  /**
	   * 人行身份证信息
	   */
	  private Long idCardPbcId;
	  /**
	   * 操作码
	   */
	  private String checkCode;
	  /**
	   * 操作信息
	   */
	  private String checkMessage;
	  /**
	   * 核查时间
	   */
	  @Temporal(TemporalType.TIMESTAMP)
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
}
