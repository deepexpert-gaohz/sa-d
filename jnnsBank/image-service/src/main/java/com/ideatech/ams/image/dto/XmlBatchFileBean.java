package com.ideatech.ams.image.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BatchFileBean")
public class XmlBatchFileBean {

	
	private String FILE_PART_NAME;
	
	
	private XmlFiles files;

	@XmlAttribute(name="FILE_PART_NAME")
	public String getFILE_PART_NAME() {
		return FILE_PART_NAME;
	}

	public void setFILE_PART_NAME(String fILE_PART_NAME) {
		FILE_PART_NAME = fILE_PART_NAME;
	}

	@XmlElement(name="files")
	public XmlFiles getFiles() {
		return files;
	}

	public void setFiles(XmlFiles files) {
		this.files = files;
	}
	
}
