package com.ideatech.ams.mivs.bo.ppcm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:08:32 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>CmonConfV01 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="CmonConfV01">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrpHdr" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}GroupHeader1"/>
 *         &lt;element name="OrgnlGrpHdr" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}OrgnlGrpHdr1"/>
 *         &lt;element name="CmonConfInf" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}CmonConfInf1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CmonConfV01", propOrder = {
        "grpHdr",
        "orgnlGrpHdr",
        "cmonConfInf"
})
public class CmonConfV01 {

    /**
     * GroupHeader
     * <GrpHdr>  [1..1]  【业务头】
     */
    @XmlElement(name = "GrpHdr", required = true)
    protected GroupHeader1 grpHdr;

    /**
     * OriginalGroupHeader
     * 原业务报文信息
     * <OrgnlGrpHdr>  [1..1]  【原报文主键】
     */
    @XmlElement(name = "OrgnlGrpHdr", required = true)
    protected OrgnlGrpHdr1 orgnlGrpHdr;


    /**
     * CommonConfirmationInformation
     * <CmonConfInf>  [1..1]
     */
    @XmlElement(name = "CmonConfInf", required = true)
    protected CmonConfInf1 cmonConfInf;

    /**
     * 获取grpHdr属性的值。
     *
     * @return
     *     possible object is
     *     {@link GroupHeader1 }
     *
     */
    public GroupHeader1 getGrpHdr() {
        return grpHdr;
    }

    /**
     * 设置grpHdr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link GroupHeader1 }
     *
     */
    public void setGrpHdr(GroupHeader1 value) {
        this.grpHdr = value;
    }

    /**
     * 获取orgnlGrpHdr属性的值。
     *
     * @return
     *     possible object is
     *     {@link OrgnlGrpHdr1 }
     *
     */
    public OrgnlGrpHdr1 getOrgnlGrpHdr() {
        return orgnlGrpHdr;
    }

    /**
     * 设置orgnlGrpHdr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OrgnlGrpHdr1 }
     *
     */
    public void setOrgnlGrpHdr(OrgnlGrpHdr1 value) {
        this.orgnlGrpHdr = value;
    }

    /**
     * 获取cmonConfInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link CmonConfInf1 }
     *
     */
    public CmonConfInf1 getCmonConfInf() {
        return cmonConfInf;
    }

    /**
     * 设置cmonConfInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link CmonConfInf1 }
     *
     */
    public void setCmonConfInf(CmonConfInf1 value) {
        this.cmonConfInf = value;
    }

}

