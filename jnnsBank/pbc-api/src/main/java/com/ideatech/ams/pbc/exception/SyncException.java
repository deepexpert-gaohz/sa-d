package com.ideatech.ams.pbc.exception;

import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

@Data
public class SyncException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4813644106033941753L;
	/**
	 * 错误码
	 */
	private String code;
	/**
	 * 错误信息参数
	 */
	private Object[] params;

	/**
	 * @param code
	 *            错误码
	 */
	public SyncException(String code) {
		this(code, new Object[] {});
	}

	@Override
	public String getMessage() {
		if (StringUtils.isNotEmpty(super.getMessage())) {
			return super.getMessage();
		}
		return code;
	}

	/**
	 * @param code
	 *            错误码
	 * @param params
	 *            错误信息参数
	 */
	public SyncException(String code, Object... params) {
		super(code + " " + ArrayUtils.toString(params));
		this.code = code;
		this.params = params;

	}
}
