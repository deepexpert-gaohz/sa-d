package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NEAR_PATH")
public class XmlNEAR_PATH {

	
	private String string;

	@XmlElement(name="string")
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
