package com.ideatech.ams.pbc.dto;


import com.ideatech.ams.pbc.enums.SyncAcctType;
import lombok.Data;

import java.util.List;

/**
 * 报备类账户销户 同步字段
 * 
 * @author zoulang
 *
 */
@Data
public class AmsRevokeSyncCondition {

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
}
