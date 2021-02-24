package com.ideatech.ams.pbc.dto;

import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import lombok.Data;

/**
 * 人行账管系统征询查询条件
 * 
 * @author zoulang
 *
 */
@Data
public class AmsConsultCondition extends AmsYibanSyncCondition {

	/**
	 * 账户性质
	 */
	private SyncAcctType acctType;

	/**
	 * 业务操作类型
	 */
	private SyncOperateType operateType;
	
	/**
	 * 账户构成方式--预算户
	 */
	private String accountNameFrom;

	/**
	 * 资金性质--预算户
	 */
	private String capitalProperty;

}
