package com.ideatech.ams.account.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 核心对公账户表
 */
@Entity
@Table(name = "CORE_PUBLIC_ACCOUNT",
		indexes = {@Index(name = "core_public_an_idx",columnList = "acctNo")})
@Data
public class CorePublicAccount extends CorePublicAccountBase implements Serializable {

	/**
	 * 序列化ID,缓存需要
	 *
	 */
	private static final long serialVersionUID = 5454155825314635342L;

	/**
	 * 错误信息
	 */
	@Column(length = 100)
	private String errorReason;

	private String currencyType;
	private String currency0;
	private String currency1;
	private String nationality;
}
