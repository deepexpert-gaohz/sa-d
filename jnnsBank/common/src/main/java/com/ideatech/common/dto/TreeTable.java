/**
 * 
 */
package com.ideatech.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rjq
 *
 */
@Data
public class TreeTable {

	//父类id
	private String pid;
	
	private List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();

}
