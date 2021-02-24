package com.ideatech.ams.pbc.dto;


import com.ideatech.ams.pbc.enums.SyncAcctType;
import lombok.Data;

/**
 * 账户久悬 同步字段
 * 
 * @author zoulang
 *
 */
@Data
public class AmsSuspendSyncCondition {

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



}
