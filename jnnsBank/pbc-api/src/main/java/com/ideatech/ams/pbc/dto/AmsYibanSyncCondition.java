package com.ideatech.ams.pbc.dto;


import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import lombok.Data;

/**
 * 一般户同步字段(条件)包括开户、变更
 * 
 * @author zoulang
 *
 */
@Data
public class AmsYibanSyncCondition {

	/**
	 * 账户性质
	 */
	private SyncAcctType acctType;

	/**
	 * 业务操作类型
	 */
	private SyncOperateType operateType;

	/**
	 * 基本户开户许可证
	 */
	private String accountKey;

	/**
	 * 许可证对应的注册地区代码
	 */
	private String accountKeyRegAreaCode;
	/**
	 * 基本户注册地地区代码
	 */
	private String regAreaCode;
	/**
	 * 银行机构代码
	 */
	private String bankCode;

	private String bankName;
	/**
	 * 机构所在地地区代码
	 */
	private String bankAreaCode;
	/**
	 * 账号
	 */
	private String acctNo;
	/**
	 * 账户开户日期
	 */
	private String acctCreateDate;

	/**
	 * 账户证明文件种类
	 */
	private String accountFileType;

	/**
	 * 账户证明文件编号
	 */
	private String accountFileNo;
	/**
	 * 备注
	 */
	private String remark;


	private String openAccountSiteType;

	private String currencyType;
	private String currency0;
	private String currency1;
}
