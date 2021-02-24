package com.ideatech.ams.pbc.dto.auth;

import com.ideatech.ams.pbc.enums.LoginStatus;
import lombok.Data;
import org.apache.http.client.CookieStore;

@Data
public class LoginAuth implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	final static private long EXCEED_TIME = 0L;// 30L*60*1000;

	protected String domain;// ip地址

	protected boolean failure;

	protected long expiresTime;

	protected LoginStatus loginStatus;

	protected CookieStore cookie;

	private String loginIp;

	protected String loginName;

	protected String loginPwd;
	
	public LoginAuth(String domain, String loginName, String loginPwd) {
		this.domain = "http://" + domain;
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.loginIp = domain;
		this.expiresTime = System.currentTimeMillis() + EXCEED_TIME;
	}
}
