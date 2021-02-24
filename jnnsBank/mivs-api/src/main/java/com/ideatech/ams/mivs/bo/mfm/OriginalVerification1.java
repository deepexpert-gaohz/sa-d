package com.ideatech.ams.mivs.bo.mfm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:16 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OriginalVerification1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OriginalVerification1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrgnlDlvrgMsgId" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max35Text"/>
 *         &lt;element name="OrgnlRcvgMsgId" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max35Text"/>
 *         &lt;element name="OrgnlVrfctnInfo" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}OriginalVerificationInfomation1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginalVerification1", propOrder = {
        "orgnlDlvrgMsgId",
        "orgnlRcvgMsgId",
        "orgnlVrfctnInfo"
})
public class OriginalVerification1 {

    /**
     * OriginnalDeliveringMessageIdentification
     * 原申请报文标识号
     * <OrgnlDlvrgMsgId>  [1..1]  Max35Text
     * 原mivs.320.001.01 的报文标识号
     */
    @XmlElement(name = "OrgnlDlvrgMsgId", required = true)
    protected String orgnlDlvrgMsgId;

    /**
     * OriginnalReceivingMessageIdentification
     * 原应答报文标识号
     * <OrgnlRcvgMsgId>  [1..1]  Max35Text
     * 原mivs.321.001.01 的报文标识号
     */
    @XmlElement(name = "OrgnlRcvgMsgId", required = true)
    protected String orgnlRcvgMsgId;

    /**
     * OriginalVerificationInfomation
     * <OrgnlVrfctnInfo>  [1..1]
     */
    @XmlElement(name = "OrgnlVrfctnInfo", required = true)
    protected OriginalVerificationInfomation1 orgnlVrfctnInfo;

    /**
     * 获取orgnlDlvrgMsgId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrgnlDlvrgMsgId() {
        return orgnlDlvrgMsgId;
    }

    /**
     * 设置orgnlDlvrgMsgId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrgnlDlvrgMsgId(String value) {
        this.orgnlDlvrgMsgId = value;
    }

    /**
     * 获取orgnlRcvgMsgId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrgnlRcvgMsgId() {
        return orgnlRcvgMsgId;
    }

    /**
     * 设置orgnlRcvgMsgId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrgnlRcvgMsgId(String value) {
        this.orgnlRcvgMsgId = value;
    }

    /**
     * 获取orgnlVrfctnInfo属性的值。
     *
     * @return
     *     possible object is
     *     {@link OriginalVerificationInfomation1 }
     *
     */
    public OriginalVerificationInfomation1 getOrgnlVrfctnInfo() {
        return orgnlVrfctnInfo;
    }

    /**
     * 设置orgnlVrfctnInfo属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OriginalVerificationInfomation1 }
     *
     */
    public void setOrgnlVrfctnInfo(OriginalVerificationInfomation1 value) {
        this.orgnlVrfctnInfo = value;
    }

}

