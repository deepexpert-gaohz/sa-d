package com.ideatech.common.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobUtils {

	public static Clob str2Clob(String str) {
		if (null == str)
			return null;
		else {
			try {
				Clob c = new javax.sql.rowset.serial.SerialClob(str.toCharArray());
				return c;
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * 将Clob转成String ,静态方法
	 * 
	 * @param clob
	 *            字段
	 * @return 内容字串，如果出现错误，返回 null
	 */
	public static String clob2Str(Clob clob) {
		if (clob == null)
			return null;
		try {
			return IOUtils.toString(clob.getCharacterStream());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
