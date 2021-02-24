package com.ideatech.ams.annual.dto;

import lombok.Data;
/**
 * 
 * @ClassName: PbcXlsFileInfo
 * @Description: TODO(人行下载的Excel对象)
 * @author zoulang
 * @date 2016年1月7日 下午2:51:17
 *
 */
@Data
public class PbcAccountExcelInfo {

	public static String acctNoStr = "acctNo";
	public static String bankCodeStr = "bankCode";
	public static String bankNameStr = "bankName";
	public static String depositorNameStr = "depositorName";
	public static String regAreaCodeStr = "regAreaCode";
	public static String acctNameStr = "acctName";
	public static String acctTypeStr = "acctType";
	public static String accountKeyStr = "accountKey";
	public static String acctOpenDateStr = "acctOpenDate";
	public static String acctCancelDateStr = "acctCancelDate";
	public static String acctStatusStr = "acctStatus";
	public static String currencyTypeStr = "currencyType";
	public static String currencyStr = "currency";
	public static String capitalPropertyStr = "capitalProperty";

	/**
	 * 账号
	 */
	private String acctNo;
	/**
	 * 银行机构代码
	 */
	private String bankCode;
	/**
	 * 银行机构名称
	 */
	private String bankName;
	/**
	 * 存款人名称
	 */
	private String depositorName;
	/**
	 * 注册地地区代码
	 */
	private String regAreaCode;
	/**
	 * 账户名称
	 */
	private String acctName;
	/**
	 * 账户性质
	 */
	private String acctType;
	/**
	 * 开户许可证
	 */
	private String accountKey;
	/**
	 * 账户开户时间
	 */
	private String acctOpenDate;
	/**
	 * 账户销户时间
	 */
	private String acctCancelDate;
	/**
	 * 账户状态
	 */
	private String acctStatus;

	/**
	 * @return the acctNo
	 */
	private String currencyType;

	private String currency;

	/**
	 * 资金性质
	 */
	private String capitalProperty;
//	 * @param acctNo
//	 *            the acctNo to set
//	 */
//	public void setAcctNo(String acctNo) {
//		this.acctNo = acctNo;
//	}

	/**
	 * @return the bankCode
	 */
//	public String getBankCode() {
//		return bankCode;
//	}

	/**
	 * @param bankCode
	 *            the bankCode to set
	 */
//	public void setBankCode(String bankCode) {
//		this.bankCode = bankCode;
//	}

	/**
	 * @return the bankName
	 */
//	public String getBankName() {
//		return bankName;
//	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
//	public void setBankName(String bankName) {
//		this.bankName = bankName;
//	}

	/**
	 * @return the depositorName
	 */
//	public String getDepositorName() {
//		return depositorName;
//	}

	/**
	 * @param depositorName
	 *            the depositorName to set
	 */
//	public void setDepositorName(String depositorName) {
//		this.depositorName = depositorName;
//	}

	/**
	 * @return the regAreaCode
	 */
//	public String getRegAreaCode() {
//		return regAreaCode;
//	}

	/**
	 * @param regAreaCode
	 *            the regAreaCode to set
	 */
//	public void setRegAreaCode(String regAreaCode) {
//		this.regAreaCode = regAreaCode;
//	}

	/**
	 * @return the acctName
	 */
//	public String getAcctName() {
//		return acctName;
//	}

	/**
	 * @param acctName
	 *            the acctName to set
	 */
//	public void setAcctName(String acctName) {
//		this.acctName = acctName;
//	}

	/**
	 * @return the acctType
	 */
//	public String getAcctType() {
//		return acctType;
//	}

	/**
	 * @param acctType
	 *            the acctType to set
	 */
//	public void setAcctType(String acctType) {
//		this.acctType = acctType;
//	}

	/**
	 * @return the accountKey
	 */
//	public String getAccountKey() {
//		return accountKey;
//	}

	/**
	 * @param accountKey
	 *            the accountKey to set
	 */
//	public void setAccountKey(String accountKey) {
//		this.accountKey = accountKey;
//	}

	/**
	 * @return the acctOpenDate
	 */
//	public String getAcctOpenDate() {
//		return acctOpenDate;
//	}

	/**
	 * @param acctOpenDate
	 *            the acctOpenDate to set
	 */
//	public void setAcctOpenDate(String acctOpenDate) {
//		this.acctOpenDate = acctOpenDate;
//	}

	/**
	 * @return the acctCancelDate
	 */
//	public String getAcctCancelDate() {
//		return acctCancelDate;
//	}

	/**
	 * @param acctCancelDate
	 *            the acctCancelDate to set
	 */
//	public void setAcctCancelDate(String acctCancelDate) {
//		this.acctCancelDate = acctCancelDate;
//	}

	/**
	 * @return the acctStatus
	 */
//	public String getAcctStatus() {
//		return acctStatus;
//	}

	/**
	 * @param acctStatus
	 *            the acctStatus to set
	 */
//	public void setAcctStatus(String acctStatus) {
//		this.acctStatus = acctStatus;
//	}

}
