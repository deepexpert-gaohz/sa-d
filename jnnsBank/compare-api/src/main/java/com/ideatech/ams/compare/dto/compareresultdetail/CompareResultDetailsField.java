/**
 * 
 */
package com.ideatech.ams.compare.dto.compareresultdetail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 一个字段的比对结果
 * 
 * @author zhailiang
 *
 */
public class CompareResultDetailsField {
	//字段中文名
	private String fieldName;
	//字段英文名
	private String fieldCode;
	//key为数据源名称
	private Map<String, CompareResultDetailsCell> content = new HashMap<String, CompareResultDetailsCell>();

	/**
	 * @return the content
	 */
	public Map<String, CompareResultDetailsCell> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Map<String, CompareResultDetailsCell> content) {
		this.content = content;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldCode
	 */
	public String getFieldCode() {
		return fieldCode;
	}

	/**
	 * @param fieldCode the fieldCode to set
	 */
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public boolean isMatch() {
		Collection<CompareResultDetailsCell> cells = content.values();
		for (CompareResultDetailsCell cell : cells) {
			if(!cell.isMatch()){
				return false;
			}
		}
		return true;
	}

	
}
