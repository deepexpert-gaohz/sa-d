package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="otherAtt")
public class XmlOtherAtt {

	
	private XmlSTART_TIME start_TIME;
	
	
	private XmlIDCARD idcard;
	
	
	private XmlNEAR_PATH near_PATH;

	@XmlElement(name="START_TIME")
	public XmlSTART_TIME getStart_TIME() {
		return start_TIME;
	}

	public void setStart_TIME(XmlSTART_TIME start_TIME) {
		this.start_TIME = start_TIME;
	}

	@XmlElement(name="IDCARD")
	public XmlIDCARD getIdcard() {
		return idcard;
	}

	public void setIdcard(XmlIDCARD idcard) {
		this.idcard = idcard;
	}

	@XmlElement(name="NEAR_PATH")
	public XmlNEAR_PATH getNear_PATH() {
		return near_PATH;
	}

	public void setNear_PATH(XmlNEAR_PATH near_PATH) {
		this.near_PATH = near_PATH;
	}
	
}
