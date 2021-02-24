package com.ideatech.ams.image.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BatchBean")
public class XmlBatchBean {

	
	private String MODEL_CODE;
	
	
	private String IS_UNITED_ACCESS;
	
	
	private String IS_SUNECM_COSOLE;
	
	
	private String IS_BREAK_POINT;
	
	
	private String IS_DOWNLOAD;
	
	
	private XmlIndex_Object index_Object;
	
	
	private XmlDocument_Objects document_Objects;

	@XmlAttribute(name="MODEL_CODE")
	public String getMODEL_CODE() {
		return MODEL_CODE;
	}

	public void setMODEL_CODE(String mODEL_CODE) {
		MODEL_CODE = mODEL_CODE;
	}

	@XmlAttribute(name="IS_UNITED_ACCESS")
	public String getIS_UNITED_ACCESS() {
		return IS_UNITED_ACCESS;
	}

	public void setIS_UNITED_ACCESS(String iS_UNITED_ACCESS) {
		IS_UNITED_ACCESS = iS_UNITED_ACCESS;
	}

	@XmlAttribute(name="IS_SUNECM_COSOLE")
	public String getIS_SUNECM_COSOLE() {
		return IS_SUNECM_COSOLE;
	}

	public void setIS_SUNECM_COSOLE(String iS_SUNECM_COSOLE) {
		IS_SUNECM_COSOLE = iS_SUNECM_COSOLE;
	}

	@XmlAttribute(name="IS_BREAK_POINT")
	public String getIS_BREAK_POINT() {
		return IS_BREAK_POINT;
	}

	public void setIS_BREAK_POINT(String iS_BREAK_POINT) {
		IS_BREAK_POINT = iS_BREAK_POINT;
	}

	@XmlAttribute(name="IS_DOWNLOAD")
	public String getIS_DOWNLOAD() {
		return IS_DOWNLOAD;
	}

	public void setIS_DOWNLOAD(String iS_DOWNLOAD) {
		IS_DOWNLOAD = iS_DOWNLOAD;
	}

	@XmlElement(name="index_Object")
	public XmlIndex_Object getIndex_Object() {
		return index_Object;
	}

	public void setIndex_Object(XmlIndex_Object index_Object) {
		this.index_Object = index_Object;
	}

	@XmlElement(name="document_Objects")
	public XmlDocument_Objects getDocument_Objects() {
		return document_Objects;
	}

	public void setDocument_Objects(XmlDocument_Objects document_Objects) {
		this.document_Objects = document_Objects;
	}
	
}
