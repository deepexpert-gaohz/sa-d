package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="customMap")
public class XmlCustomMap {

	
	private XmlEND_TIME end_TIME;
	
	
	private XmlSTART_TIME start_TIME;
	
	
	private XmlNEAR_PATH near_PATH;

	@XmlElement(name="END_TIME")
	public XmlEND_TIME getEnd_TIME() {
		return end_TIME;
	}

	public void setEnd_TIME(XmlEND_TIME end_TIME) {
		this.end_TIME = end_TIME;
	}

	@XmlElement(name="START_TIME")
	public XmlSTART_TIME getStart_TIME() {
		return start_TIME;
	}

	public void setStart_TIME(XmlSTART_TIME start_TIME) {
		this.start_TIME = start_TIME;
	}

	@XmlElement(name="NEAR_PATH")
	public XmlNEAR_PATH getNear_PATH() {
		return near_PATH;
	}

	public void setNear_PATH(XmlNEAR_PATH near_PATH) {
		this.near_PATH = near_PATH;
	}
	
	
}
