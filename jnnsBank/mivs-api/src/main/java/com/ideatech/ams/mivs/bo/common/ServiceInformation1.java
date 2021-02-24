package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:29 PM CST
//


import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>ServiceInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ServiceInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SysInd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}Max4Text"/>
 *         &lt;element name="SvcInd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}Max4Text"/>
 *         &lt;element name="SysOpTm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}ISODateTime" minOccurs="0"/>
 *         &lt;element name="SysClTm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}ISODateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceInformation1", propOrder = {
        "sysInd",
        "svcInd",
        "sysOpTm",
        "sysClTm"
})
public class ServiceInformation1 {

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    @XmlElement(name = "SysInd", required = true)
    protected String sysInd;

    /**
     * ServiceIndicator
     * 被查询日期受理业务状态
     * <SvcInd>  [1..1] ServiceCode (Max4Text)
     */
    @XmlElement(name = "SvcInd", required = true)
    protected String svcInd;

    /**
     * OpenTime
     * 被查询日期起始受理时间
     * <SysOpTm>  [0..1]  ISODateTime
     * 禁止中文；在“被查询日期受理业务状态”为 ENBL：正常的情况下填写。
     */
    @XmlElement(name = "SysOpTm")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sysOpTm;

    /**
     * CloseTime
     * 被查询日期结束受理时间
     * <SysClTm>  [0..1]  ISODateTime
     * 禁止中文；在“被查询日期受理业务状态”为 ENBL：正常的情况下填写。
     */
    @XmlElement(name = "SysClTm")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sysClTm;

    /**
     * 获取sysInd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSysInd() {
        return sysInd;
    }

    /**
     * 设置sysInd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSysInd(String value) {
        this.sysInd = value;
    }

    /**
     * 获取svcInd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSvcInd() {
        return svcInd;
    }

    /**
     * 设置svcInd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSvcInd(String value) {
        this.svcInd = value;
    }

    /**
     * 获取sysOpTm属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getSysOpTm() {
        return sysOpTm;
    }

    /**
     * 设置sysOpTm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setSysOpTm(XMLGregorianCalendar value) {
        this.sysOpTm = value;
    }

    /**
     * 获取sysClTm属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getSysClTm() {
        return sysClTm;
    }

    /**
     * 设置sysClTm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setSysClTm(XMLGregorianCalendar value) {
        this.sysClTm = value;
    }

}

