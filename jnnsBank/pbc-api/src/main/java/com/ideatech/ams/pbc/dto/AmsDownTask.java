package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 下载人行数据任务表
 * 
 * @author zoulang
 *
 */
@Data
public class AmsDownTask {

	/**
	 * 要下的文件文件夹路径
	 */
	private String folderPath;
	/**
	 * 下载开始日期 yyyy-mm-dd
	 */
	private String beginDay;
	/**
	 * 下载截止日期 yyyy-mm-dd
	 */
	private String endDay;
	/**
	 * 银行机构号（14位人行）
	 */
	private String bankId;

	private String currencyType;
	private String cruArray;
	public String format(int num, int len) {
		String numStr = "00" + String.valueOf(num);
		numStr = numStr.substring(numStr.length() - len, numStr.length());
		return numStr;
	}
}
