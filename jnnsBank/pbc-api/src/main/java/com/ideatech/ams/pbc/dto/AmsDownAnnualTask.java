package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 下载人行数据任务表 zl Feb 6, 2015 9:54:14 AM
 * 
 * @version 1.0.0
 */
@Data
public class AmsDownAnnualTask {
	/**
	 * 下载的文件夹路径
	 */
	private String folderPath;
	/**
	 * 年检年份 若查询本年的年检情况excel 当前年份-1
	 */
	private String annualYear;

	/**
	 * 下载的年检状态 0：全部 1 已进行年检 2未进行年检
	 */
	private String checkYearType;
	/**
	 * 银行机构号（14位人行）
	 */
	private String bankId;
}
