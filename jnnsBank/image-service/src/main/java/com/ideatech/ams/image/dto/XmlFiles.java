package com.ideatech.ams.image.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="files")
public class XmlFiles {

	
	private List<XmlFileBean> fileBean;

	@XmlElement(name="FileBean")
	public List<XmlFileBean> getFileBean() {
		return fileBean;
	}

	public void setFileBean(List<XmlFileBean> fileBean) {
		this.fileBean = fileBean;
	} 
	
}
