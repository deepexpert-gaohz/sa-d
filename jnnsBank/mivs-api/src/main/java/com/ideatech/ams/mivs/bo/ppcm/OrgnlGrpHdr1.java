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
 * <p>OrgnlGrpHdr1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OrgnlGrpHdr1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrgnlMsgId" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max35Text"/>
 *         &lt;element name="OrgnlInstgPty" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max14Text"/>
 *         &lt;element name="OrgnlMT" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max35Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrgnlGrpHdr1", propOrder = {
        "orgnlMsgId",
        "orgnlInstgPty",
        "orgnlMT"
})
public class OrgnlGrpHdr1 {

    /**
     * OriginalMessageIdentification
     * 原报文标识号
     * <OrgnlMsgId>  [1..1] Max35Text  禁止中文
     */
    @XmlElement(name = "OrgnlMsgId", required = true)
    protected String orgnlMsgId;

    /**
     * OriginalInstructingParty
     * 原发起参与机构
     * <OrgnlInstgPty>  [1..1] Max14Text  禁止中文
     */
    @XmlElement(name = "OrgnlInstgPty", required = true)
    protected String orgnlInstgPty;

    /**
     * OriginalMessageType
     * 原报文类型
     * <OrgnlMT>  [1..1] Max35Text  禁止中文
     */
    @XmlElement(name = "OrgnlMT", required = true)
    protected String orgnlMT;

    /**
     * 获取orgnlMsgId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrgnlMsgId() {
        return orgnlMsgId;
    }

    /**
     * 设置orgnlMsgId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrgnlMsgId(String value) {
        this.orgnlMsgId = value;
    }

    /**
     * 获取orgnlInstgPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrgnlInstgPty() {
        return orgnlInstgPty;
    }

    /**
     * 设置orgnlInstgPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrgnlInstgPty(String value) {
        this.orgnlInstgPty = value;
    }

    /**
     * 获取orgnlMT属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrgnlMT() {
        return orgnlMT;
    }

    /**
     * 设置orgnlMT属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrgnlMT(String value) {
        this.orgnlMT = value;
    }

}

