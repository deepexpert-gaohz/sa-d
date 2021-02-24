/**
 * 
 */
package com.ideatech.common.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.ideatech.common.util.BeanValueUtils;


/**
 * dhtmlx表格封装类
 * 
 * @author zhailiang
 *
 */
public class DhtmlxGrid {

	private long totalElements;
	private int totalPages;
	private int numberOfElements;
	private boolean last;
	private boolean first;
	private int size;
	private int number;

	private List<Map<String, Object>> rows;

	public DhtmlxGrid() {
	}

	public DhtmlxGrid(List<?> items, String[] columns) {
		initRows(items, columns);
	}

	public DhtmlxGrid(Page<?> items, String[] columns) {
		initRows(items.getContent(), columns);
		setTotalElements(items.getTotalElements());
		setTotalPages(items.getTotalPages());
		setNumber(items.getNumber());
		setNumberOfElements(items.getNumberOfElements());
		setSize(items.getSize());
		setFirst(items.isFirst());
		setLast(items.isLast());
	}

	private void initRows(List<?> items, String[] columns) {
		this.rows = new ArrayList<Map<String, Object>>();
		for (Object item : items) {
			rows.add(buildDhtmlxGridRow(item, columns));
		}
	}

	public Map<String, Object> buildDhtmlxGridRow(Object item, String[] columns) {
		Map<String, Object> row = new HashMap<String, Object>();
		// row.put("id", new Long(BeanValueUtils.getValue(item, "id").toString()));
		row.put("id", BeanValueUtils.getValue(item, "id").toString());
		List<Object> data = new ArrayList<Object>();
		for (String column : columns) {
			Object value = BeanValueUtils.getValue(item, column);
			if (value == null) {
				data.add("");
			} else {
				data.add(BeanValueUtils.getValue(item, column));
			}
		}
		row.put("data", data);
		return row;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

}
