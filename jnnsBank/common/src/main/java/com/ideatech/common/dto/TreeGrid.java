/**
 * 
 */
package com.ideatech.common.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhailiang
 *
 */
public class TreeGrid {
	
	private String parent;
	
	private List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
	
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the rows
	 */
	public List<Map<String, Object>> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
	

}
