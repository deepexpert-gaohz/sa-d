package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="document_Objects")
public class XmlDocument_Objects {

	
	private XmlBatchFileBean batchFileBean;

	@XmlElement(name="BatchFileBean")
	public XmlBatchFileBean getBatchFileBean() {
		return batchFileBean;
	}

	public void setBatchFileBean(XmlBatchFileBean batchFileBean) {
		this.batchFileBean = batchFileBean;
	}
	
}
