package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="START_TIME")
public class XmlSTART_TIME {

	
	private String string;

	@XmlElement(name="string")
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
}
