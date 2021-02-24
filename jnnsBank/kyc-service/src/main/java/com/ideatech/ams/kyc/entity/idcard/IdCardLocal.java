package com.ideatech.ams.kyc.entity.idcard;

import com.ideatech.ams.kyc.enums.IdCardType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 本地身份证信息
 * @author yang
 *
 */
@Data
@Entity
@Table(name = "IdCardLocal")
public class IdCardLocal extends BaseMaintainablePo implements Serializable{
	/**
	   * 序列化ID,缓存需要
	   */
	  private static final long serialVersionUID = 5454155825314635342L;
	  public static String baseTableName = "YD_IdCardLocal";
	/**
	 * 身份证号码
	 */
	@Column(length = 18)
	private String idCardNo;
	/**
	 * 姓名
	 */
	private String idCardName;
	/**
	 *性别 0-男  1-女
	 */
	private String idCardSex;
	/**
	 * 民族
	 */
	private String idCardNation;
	/**
	 * 出生日期
	 */
	private String idCardBirthday;
	/**
	 * 身份证上地址
	 */
	private String idCardAddress;
	/**
	 * 身份证签发机关
	 */
	private String idCardOrgan;
	/**
	 * 有效期
	 */
	private String idCardValidity;
	/**
	 * 核查人员类型
	 */
	private String idCardedType;
	/**
	 * 本地身份证头像base64字符串
	 * CLOB   oracle
	 * ntext   SQL server
	 * text    MySQL
	 */
	@Column(name="idCardLocalImageByte", nullable=true)
	private String idCardLocalImageByte;
	/**
	 * 人行身份证头像base64字符串
	 * CLOB   oracle
	 * ntext   SQL server
	 * text    MySQL
	 */
	@Column(name="idCardPbcImageByte", nullable=true)
	private String idCardPbcImageByte;
	/**
	 * 身份证的代次（1/2代）
	 */
	@Enumerated(EnumType.STRING)
	private IdCardType idCardType;
	/**
	 * 完整机构ID
	 */
	private String organFullId;
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
