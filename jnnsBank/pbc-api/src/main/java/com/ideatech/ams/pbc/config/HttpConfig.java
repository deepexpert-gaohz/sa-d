package com.ideatech.ams.pbc.config;

import com.ideatech.common.Config;
import com.ideatech.common.MyLog;
import com.ideatech.common.utils.StringUtils;

public class HttpConfig {
	/** HTTP* */
	public static String HTTP_ENCODING = "";
	/**
	 * 是否开启代理
	 */
	public static boolean HTTP_PROXY_ISON = false;
	/**
	 * 普通代理模式,用户认证模式
	 */
	public static boolean HTTP_PROXY_BASE = false;
	public static boolean HTTP_USESSL = false;
	public static String HTTP_SCHEME = "";
	public static String HTTP_PROXY_HOST = "";
	public static int HTTP_PROXY_PORT = 8080;
	public static String HTTP_PROXY_USERNAME = "";
	public static boolean HTTP_PRINT_HTML = false;// 是否打印访问的页面
	public static String HTTP_PROXY_PASSWORD = "";
	public static int HTTP_SOCKET_TIMEOUT = 1200; // 网页超时时间
	public static long REQUEST_HTML_TIME = 1L; // 每次请求网页间隔时间
	public static String HTTP_HEADS_CONFIG = "";
	static {
		HttpConfig.HTTP_ENCODING = Config.getProperty("http.encoding"); // Config.getProperty("http.encoding");
		if (StringUtils.isEmpty(HTTP_ENCODING)) {
			HTTP_ENCODING = "gbk";
		}
		HttpConfig.HTTP_USESSL = Config.useSSL();
		HttpConfig.HTTP_SCHEME = Config.getScheme();
		HttpConfig.HTTP_PROXY_HOST = Config.getProxyHost();
		HttpConfig.HTTP_PROXY_PORT = Config.getProxyPort();
		HttpConfig.HTTP_PROXY_USERNAME = Config.getProxyUserName();
		HttpConfig.HTTP_PROXY_PASSWORD = Config.getProxyPassword();
		HttpConfig.HTTP_PRINT_HTML = Config.getBoolean("http.print.html");
		HttpConfig.REQUEST_HTML_TIME = Config.getRequestHtmlTime();
		HttpConfig.HTTP_SOCKET_TIMEOUT = Config.gethttpSocketTimeout();
		if (HTTP_SOCKET_TIMEOUT == 0 || HTTP_SOCKET_TIMEOUT == -1) {
			HTTP_SOCKET_TIMEOUT = 1000;
		}
		HttpConfig.HTTP_PROXY_ISON = Config.getBoolean("http.proxy.on");
		HttpConfig.HTTP_HEADS_CONFIG = "http_heads.xml"; // Config.getProperty("http.heads.config");
		if (StringUtils.isEmpty(HttpConfig.HTTP_PROXY_HOST)) {
			HttpConfig.HTTP_PROXY_ISON = false;
		}
		if (HttpConfig.HTTP_PROXY_ISON) {
			if (StringUtils.isEmpty(HttpConfig.HTTP_PROXY_USERNAME) || StringUtils.isEmpty(HttpConfig.HTTP_PROXY_PASSWORD)) {
				HttpConfig.HTTP_PROXY_BASE = true;
			}
		}
		MyLog.info("连接模式: " + (HttpConfig.HTTP_PROXY_ISON ? "代理连接" : "普通连接"));
		if (HttpConfig.HTTP_PROXY_ISON) {
			MyLog.info("代理认证模式: " + (HttpConfig.HTTP_PROXY_BASE ? "基础代理" : "用户名-密码认证"));
			MyLog.info("代理协议: " + HttpConfig.HTTP_SCHEME);
			MyLog.info("代理IP: " + HttpConfig.HTTP_PROXY_HOST);
			MyLog.info("代理端口: " + HttpConfig.HTTP_PROXY_PORT);
			MyLog.info("代理用户: " + HttpConfig.HTTP_PROXY_USERNAME);
			MyLog.info("代理密码: " + HttpConfig.HTTP_PROXY_PASSWORD);
		}
		MyLog.info("网页编码格式: " + HttpConfig.HTTP_ENCODING);
		MyLog.info("请求网页间隔时间: " + HttpConfig.REQUEST_HTML_TIME);
		MyLog.info("请求网页超时时间: " + HttpConfig.HTTP_SOCKET_TIMEOUT);
		MyLog.info("是否打印访问页面: " + (HttpConfig.HTTP_PRINT_HTML ? "打印" : "不打印"));
	}

	public static String getEncoding() {
		return HttpConfig.HTTP_ENCODING;
	}
}
