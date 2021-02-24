package com.ideatech.ams.pbc.service.protocol;

import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.common.MyLog;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpResult {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public enum RESULT_TYPES {
		HTTP_STRING, HTTP_STREAM
	};

	private int statuCode;
	private byte[] buffer;
	private String html;
	private CookieStore cookies;
	private List<Header> headers;
	private String responseUrl;

	public HttpResult() {
		this.headers = new ArrayList<Header>();
		this.html = "";
	}

	public void addHeader(Header header) {
		this.headers.add(header);
	}

	public int getStatuCode() {
		return statuCode;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public void setStatuCode(int statuCode) {
		this.statuCode = statuCode;
	}

	public CookieStore getCookies() {
		return cookies;
	}

	public void setCookies(CookieStore cookies) {
		this.cookies = cookies;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public String makeHtml(String encoding) {
		return makeHtml("", encoding);
	}

	public String makeHtml(String errorInfo, String encoding) {
		try {
			if (this.buffer != null && this.buffer.length > 0) {
				this.html = new String(this.buffer, encoding);
				if (this.html != null) {
					this.html = this.html.trim();
				} else {
					this.html = "";
				}
				if (HttpConfig.HTTP_PRINT_HTML) {
					logger.info(html);
				}
			} else {
				MyLog.error("采集失败: " + html);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.html = "";
		}
		return this.html;
	}
}
