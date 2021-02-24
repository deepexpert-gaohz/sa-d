package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="root")
public class XmlRoot {

	
	private XmlBatchBean batchBean;

	@XmlElement(name="BatchBean")
	public XmlBatchBean getBatchBean() {
		return batchBean;
	}

	
	public void setBatchBean(XmlBatchBean batchBean) {
		this.batchBean = batchBean;
	}
	
	
}
