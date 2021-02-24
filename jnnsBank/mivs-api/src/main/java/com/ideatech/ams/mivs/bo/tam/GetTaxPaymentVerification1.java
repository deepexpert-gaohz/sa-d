package com.ideatech.ams.mivs.bo.tam;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:04:08 PM CST
//


import com.ideatech.ams.mivs.bo.common.MessageHeader3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>GetTaxPaymentVerification1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="GetTaxPaymentVerification1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgHdr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}MessageHeader1"/>
 *         &lt;element name="VryDef" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}VerificationDefinition1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetTaxPaymentVerification1", propOrder = {
        "msgHdr",
        "vryDef"
})
public class GetTaxPaymentVerification1 {

    /**
     * MessageHeader
     * <MsgHdr>  [1..1]
     */
    @XmlElement(name = "MsgHdr", required = true)
    protected MessageHeader3 msgHdr;

    /**
     * VerificationDefinition
     * <VryDef>  [1..1]
     */
    @XmlElement(name = "VryDef", required = true)
    protected VerificationDefinition1 vryDef;

    /**
     * 获取msgHdr属性的值。
     *
     * @return
     *     possible object is
     *     {@link MessageHeader3 }
     *
     */
    public MessageHeader3 getMsgHdr() {
        return msgHdr;
    }

    /**
     * 设置msgHdr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link MessageHeader3 }
     *
     */
    public void setMsgHdr(MessageHeader3 value) {
        this.msgHdr = value;
    }

    /**
     * 获取vryDef属性的值。
     *
     * @return
     *     possible object is
     *     {@link VerificationDefinition1 }
     *
     */
    public VerificationDefinition1 getVryDef() {
        return vryDef;
    }

    /**
     * 设置vryDef属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link VerificationDefinition1 }
     *
     */
    public void setVryDef(VerificationDefinition1 value) {
        this.vryDef = value;
    }

}

