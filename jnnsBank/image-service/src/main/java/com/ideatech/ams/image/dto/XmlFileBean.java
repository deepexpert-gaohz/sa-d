package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FileBean")
public class XmlFileBean {
	
	
	
	private String VERSION;
	
	
	private String UPLOAD_TIME;
	
	
	private String FILE_FORMAT;
	
	
	private String FILE_NO;
	
	
	private String SAVE_NAME;
	
	
	private String CONTENT_ID;
	
	
	private String FILE_NAME;
	
	
	private String FILE_SIZE;
	
	
	private String SERVER_ID;
	
	
	private String URL;
	
	
	private XmlOtherAtt otherAtt;

	@XmlAttribute(name="VERSION")
	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	@XmlAttribute(name="UPLOAD_TIME")
	public String getUPLOAD_TIME() {
		return UPLOAD_TIME;
	}

	public void setUPLOAD_TIME(String uPLOAD_TIME) {
		UPLOAD_TIME = uPLOAD_TIME;
	}

	@XmlAttribute(name="FILE_FORMAT")
	public String getFILE_FORMAT() {
		return FILE_FORMAT;
	}

	public void setFILE_FORMAT(String fILE_FORMAT) {
		FILE_FORMAT = fILE_FORMAT;
	}

	@XmlAttribute(name="FILE_NO")
	public String getFILE_NO() {
		return FILE_NO;
	}

	public void setFILE_NO(String fILE_NO) {
		FILE_NO = fILE_NO;
	}

	@XmlAttribute(name="SAVE_NAME")
	public String getSAVE_NAME() {
		return SAVE_NAME;
	}

	public void setSAVE_NAME(String sAVE_NAME) {
		SAVE_NAME = sAVE_NAME;
	}

	@XmlAttribute(name="CONTENT_ID")
	public String getCONTENT_ID() {
		return CONTENT_ID;
	}

	public void setCONTENT_ID(String cONTENT_ID) {
		CONTENT_ID = cONTENT_ID;
	}

	@XmlAttribute(name="FILE_NAME")
	public String getFILE_NAME() {
		return FILE_NAME;
	}

	public void setFILE_NAME(String fILE_NAME) {
		FILE_NAME = fILE_NAME;
	}

	@XmlAttribute(name="FILE_SIZE")
	public String getFILE_SIZE() {
		return FILE_SIZE;
	}

	public void setFILE_SIZE(String fILE_SIZE) {
		FILE_SIZE = fILE_SIZE;
	}

	@XmlAttribute(name="SERVER_ID")
	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String sERVER_ID) {
		SERVER_ID = sERVER_ID;
	}

	@XmlAttribute(name="URL")
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	@XmlElement(name="otherAtt")
	public XmlOtherAtt getOtherAtt() {
		return otherAtt;
	}

	public void setOtherAtt(XmlOtherAtt otherAtt) {
		this.otherAtt = otherAtt;
	}
	
	
	

}
