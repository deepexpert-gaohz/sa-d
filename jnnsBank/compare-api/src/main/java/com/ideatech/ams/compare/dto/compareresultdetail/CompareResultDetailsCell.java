/**
 * 
 */
package com.ideatech.ams.compare.dto.compareresultdetail;

import lombok.Data;

/**
 * 比对结果详细信息
 * @author zhailiang
 *
 */
@Data
public class CompareResultDetailsCell {
	/**
	 * 字段是否匹配
	 */
	private boolean match;
	/**
	 * 字段值
	 */
	private String value;

	public CompareResultDetailsCell() {
	}

	public CompareResultDetailsCell(String value) {
		this.value = value;
	}
}
