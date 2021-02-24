package com.ideatech.ams.kyc.dto;

import java.io.Serializable;
import java.util.Date;

/*
 * IDP返回的工商信息
 */
public class OutInfoOne implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private Date currentTime;
	
	private SaicIdpInfo result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public SaicIdpInfo getResult() {
		return result;
	}

	public void setResult(SaicIdpInfo result) {
		this.result = result;
	}
	
	

}
