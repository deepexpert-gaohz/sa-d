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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>CmonConfInf1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="CmonConfInf1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PrcSts" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max4Text"/>
 *         &lt;element name="PrcCd" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max8Text" minOccurs="0"/>
 *         &lt;element name="PtyId" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max14Text" minOccurs="0"/>
 *         &lt;element name="PtyPrcCd" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max4Text" minOccurs="0"/>
 *         &lt;element name="RjctInf" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max105Text" minOccurs="0"/>
 *         &lt;element name="PrcDt" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}ISODate" minOccurs="0"/>
 *         &lt;element name="NetgRnd" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max2Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CmonConfInf1", propOrder = {
        "prcSts",
        "prcCd",
        "ptyId",
        "ptyPrcCd",
        "rjctInf",
        "prcDt",
        "netgRnd"
})
public class CmonConfInf1 {

    /**
     * ProcessStatus
     * 业务状态
     * <PrcSts>  [1..1] ProcessCode (Max4Text） 禁止中文
     */
    @XmlElement(name = "PrcSts", required = true)
    protected String prcSts;

    /**
     * ProcessCode
     * 业务处理码
     * <PrcCd>  [0..1]  Max8Text  禁止中文
     */
    @XmlElement(name = "PrcCd")
    protected String prcCd;

    /**
     * PartyIdentification
     * 拒绝业务的参与机构行号
     * <PtyId>  [0..1]  Max14Text  禁止中文
     */
    @XmlElement(name = "PtyId")
    protected String ptyId;

    /**
     * PartyProcessCode
     * 参与机构业务拒绝码
     * <PtyPrcCd>  [0..1] RejectCode （Max4Text） 禁止中文
     */
    @XmlElement(name = "PtyPrcCd")
    protected String ptyPrcCd;

    /**
     * RejectInformation
     * 业务拒绝信息
     * <RjctInf>  [0..1]  Max105Text  允许中文
     */
    @XmlElement(name = "RjctInf")
    protected String rjctInf;

    /**
     * ProcessDate
     * 处理日期（终态日期）
     * <PrcDt>  [0..1]  ISODate  禁止中文
     */
    @XmlElement(name = "PrcDt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar prcDt;

    /**
     * NettingRound
     * 轧差场次
     * <NetgRnd>  [0..1]  Max2Text  禁止中文
     */
    @XmlElement(name = "NetgRnd")
    protected String netgRnd;

    /**
     * 获取prcSts属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPrcSts() {
        return prcSts;
    }

    /**
     * 设置prcSts属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPrcSts(String value) {
        this.prcSts = value;
    }

    /**
     * 获取prcCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPrcCd() {
        return prcCd;
    }

    /**
     * 设置prcCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPrcCd(String value) {
        this.prcCd = value;
    }

    /**
     * 获取ptyId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPtyId() {
        return ptyId;
    }

    /**
     * 设置ptyId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPtyId(String value) {
        this.ptyId = value;
    }

    /**
     * 获取ptyPrcCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPtyPrcCd() {
        return ptyPrcCd;
    }

    /**
     * 设置ptyPrcCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPtyPrcCd(String value) {
        this.ptyPrcCd = value;
    }

    /**
     * 获取rjctInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRjctInf() {
        return rjctInf;
    }

    /**
     * 设置rjctInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRjctInf(String value) {
        this.rjctInf = value;
    }

    /**
     * 获取prcDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getPrcDt() {
        return prcDt;
    }

    /**
     * 设置prcDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setPrcDt(XMLGregorianCalendar value) {
        this.prcDt = value;
    }

    /**
     * 获取netgRnd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNetgRnd() {
        return netgRnd;
    }

    /**
     * 设置netgRnd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNetgRnd(String value) {
        this.netgRnd = value;
    }

}

