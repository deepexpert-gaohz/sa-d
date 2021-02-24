package com.ideatech.ams.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 核心对公账户表错误表
 */
@Entity
@Table(name = "CORE_PUBLIC_ACCOUNT_ERROR",
		indexes = {@Index(name = "core_public_err_an_idx",columnList = "acctNo")})
@Data
public class CorePublicAccountError extends CorePublicAccountBase implements Serializable {

	/**
	 * 序列化ID,缓存需要
	 *
	 */
	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 错误信息
	 */
	@Lob
//	@Column(length = 2000)
	@Column
	private String errorReason;

	/**
	 * 保存原始token数据
	 */
	@Lob
//	@Column(length = 2000)
	@Column
	private String errorToken;
	private String currencyType;
	private String currency0;
	private String currency1;
	private String nationality;
}
