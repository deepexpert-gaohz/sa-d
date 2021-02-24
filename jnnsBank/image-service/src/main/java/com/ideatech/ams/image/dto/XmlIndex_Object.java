package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="index_Object")
public class XmlIndex_Object {
	
	
	private String CONTENT_STATUS;
	
	
	private String SERVER_ID;
	
	
	private String UPLOAD_USER;
	
	
	private String CONTENT_ID;
	
	
	private String SERVER_IP;
	
	
	private String SOCKET_PORT;
	
	
	private String HTTP_PORT;
	
	
	private String VERSION;
	
	
	private String MAX_VERSION;
	
	
	private String GROUP_ID;
	
	
	private XmlCustomMap customMap;

	@XmlAttribute(name="CONTENT_STATUS")
	public String getCONTENT_STATUS() {
		return CONTENT_STATUS;
	}

	public void setCONTENT_STATUS(String cONTENT_STATUS) {
		CONTENT_STATUS = cONTENT_STATUS;
	}

	@XmlAttribute(name="SERVER_ID")
	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String sERVER_ID) {
		SERVER_ID = sERVER_ID;
	}

	@XmlAttribute(name="UPLOAD_USER")
	public String getUPLOAD_USER() {
		return UPLOAD_USER;
	}

	public void setUPLOAD_USER(String uPLOAD_USER) {
		UPLOAD_USER = uPLOAD_USER;
	}

	@XmlAttribute(name="CONTENT_ID")
	public String getCONTENT_ID() {
		return CONTENT_ID;
	}

	public void setCONTENT_ID(String cONTENT_ID) {
		CONTENT_ID = cONTENT_ID;
	}

	@XmlAttribute(name="SERVER_IP")
	public String getSERVER_IP() {
		return SERVER_IP;
	}

	public void setSERVER_IP(String sERVER_IP) {
		SERVER_IP = sERVER_IP;
	}

	@XmlAttribute(name="SOCKET_PORT")
	public String getSOCKET_PORT() {
		return SOCKET_PORT;
	}

	public void setSOCKET_PORT(String sOCKET_PORT) {
		SOCKET_PORT = sOCKET_PORT;
	}

	@XmlAttribute(name="HTTP_PORT")
	public String getHTTP_PORT() {
		return HTTP_PORT;
	}

	public void setHTTP_PORT(String hTTP_PORT) {
		HTTP_PORT = hTTP_PORT;
	}

	@XmlAttribute(name="VERSION")
	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	@XmlAttribute(name="MAX_VERSION")
	public String getMAX_VERSION() {
		return MAX_VERSION;
	}

	public void setMAX_VERSION(String mAX_VERSION) {
		MAX_VERSION = mAX_VERSION;
	}

	@XmlAttribute(name="GROUP_ID")
	public String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}

	@XmlElement(name="customMap")
	public XmlCustomMap getCustomMap() {
		return customMap;
	}

	public void setCustomMap(XmlCustomMap customMap) {
		this.customMap = customMap;
	}
	
	

}
