package com.ideatech.common.excel.dto;

import lombok.Data;

/**
 * excel头部属性
 * @author van
 * @date 18:48 2018/8/30
 */
@Data
public class ExcelHeadDto {

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 是否合并单元格
	 */
	private Boolean cellRange;

	/**
	 * 合并的行
	 */
	private int row;

	/**
	 * 合并的列
	 */
	private int col;

	public ExcelHeadDto(String content, Boolean cellRange) {
		this.content = content;
		this.cellRange = cellRange;
	}

	public ExcelHeadDto(String content, Boolean cellRange, int row, int col) {
		this.content = content;
		this.cellRange = cellRange;
		this.row = row;
		this.col = col;
	}
}
