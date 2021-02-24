package com.ideatech.ams.pbc.utils;

import com.ideatech.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	/**
	 * 去除空格、\r\n\t等符号
	 * 
	 * @param str
	 * @return
	 * @exception
	 */
	public static String getStringNoBlank(String str) {
		if (str != null && !"".equals(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			String strNoBlank = m.replaceAll("");
			return strNoBlank;
		} else {
			return str;
		}
	}

	/**
	 * 线程暂停
	 * 
	 * @param second
	 *            暂停秒数
	 */
	public static void sleep(int second) {
		try {
			Thread.sleep(1000 * second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取url完整路径
	 * 
	 * @param url
	 * @return
	 */
	public static String getFullDomain(String url) {
		String temp = "";
		if (url.indexOf("http://") == 0) {
			temp = url.substring(7);
		} else if (url.indexOf("https://") == 0) {
			temp = url.substring(8);
		}
		if (temp.indexOf("/") > -1) {
			temp = new String(temp.substring(0, temp.indexOf("/")));
		}
		return temp;
	}

	public static UUID getUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid;
	}

	public static String format(String url, Map<String, String> args) {
		for (String key : args.keySet()) {
			url = url.replace("${" + key + "}", args.get(key));
		}
		return url;
	}
	
	public static String dayBefore(String format) {
		if (StringUtils.isBlank(format)) {
			format = "yyy-MM-dd";
		}
		SimpleDateFormat dft = new SimpleDateFormat(format);
		Date now = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(now);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		String daybefor = "";
		try {
			daybefor = dft.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daybefor;
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static String getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static String getNowDateLong() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static Object frontZero(int len, int num) {
		
		return String.format("%0"+num+"d", len);
	}
	
	
}
