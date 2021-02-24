package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * zoulang
 * 
 * @version 1.0.0
 */
@Data
public class ResultVO {

	public ResultVO() {
		success = true;
	}

	private boolean success;

	private String msg;


}
