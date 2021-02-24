/**
 * 
 */
package com.ideatech.common.excel.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhailiang
 *
 */
public class ExcelReadConfig {
	
	private int startSheetNo = 0;
	
	private int startRowNo = 1;
	
	private Map<Integer, String> mapping;
	
	public void addMapping(Integer column, String field){
		if(mapping == null) {
			mapping = new HashMap<Integer, String>();
		}
		mapping.put(column, field);
	}

	/**
	 * @return the startSheetNo
	 */
	public int getStartSheetNo() {
		return startSheetNo;
	}

	/**
	 * @param startSheetNo the startSheetNo to set
	 */
	public void setStartSheetNo(int startSheetNo) {
		this.startSheetNo = startSheetNo;
	}

	/**
	 * @return the startRowNo
	 */
	public int getStartRowNo() {
		return startRowNo;
	}

	/**
	 * @param startRowNo the startRowNo to set
	 */
	public void setStartRowNo(int startRowNo) {
		this.startRowNo = startRowNo;
	}

	/**
	 * @return the mapping
	 */
	public Map<Integer, String> getMapping() {
		return mapping;
	}

	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(Map<Integer, String> mapping) {
		this.mapping = mapping;
	}
	
}
