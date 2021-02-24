package com.ideatech.ams.pbc.dto;


import com.ideatech.ams.pbc.enums.SyncAcctType;
import lombok.Data;

import java.util.List;

/**
 * 取消核准账户销户 同步字段
 *(基本户、非临时)
 */
@Data
public class AmsRevokeBeiAnSyncCondition {

	/**
	 * 账户性质
	 */
	private SyncAcctType acctType;

	/**
	 * 银行机构代码
	 */
	private String bankCode;

	private String bankName;

	/**
	 * 账号
	 */
	private String acctNo;

	/**
	 * 销户原因
	 */
	private String cancenReason;
	/**
	 * 开户许可证号
	 * 基本户以J开头，非临时以L开头
	 */
	private String accountKey;
	//账户数据
	private List<AmsPrintInfo> aLLAccountData;
}
