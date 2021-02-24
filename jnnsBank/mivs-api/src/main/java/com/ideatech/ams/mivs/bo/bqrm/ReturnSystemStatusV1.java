package com.ideatech.ams.mivs.bo.bqrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:29 PM CST
//


import com.ideatech.ams.mivs.bo.common.MessageHeader2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ReturnSystemStatusV1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ReturnSystemStatusV1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgHdr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}MessageHeader1"/>
 *         &lt;element name="OrgnlQryInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}OriginalQueryInformation1"/>
 *         &lt;element name="RplyInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}ReplyInformation1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnSystemStatusV1", propOrder = {
        "msgHdr",
        "orgnlQryInf",
        "rplyInf"
})
public class ReturnSystemStatusV1 {

    /**
     * MessageHeader
     * <MsgHdr>  [1..1]
     */
    @XmlElement(name = "MsgHdr", required = true)
    protected MessageHeader2 msgHdr;

    /**
     * OriginalQueryInformation
     * <OrgnlQryInf>  [1..1]
     */
    @XmlElement(name = "OrgnlQryInf", required = true)
    protected OriginalQueryInformation1 orgnlQryInf;

    /**
     * ReplyInformation
     * <RplyInf>  [1..1]
     */
    @XmlElement(name = "RplyInf", required = true)
    protected ReplyInformation1 rplyInf;

    /**
     * 获取msgHdr属性的值。
     *
     * @return
     *     possible object is
     *     {@link MessageHeader2 }
     *
     */
    public MessageHeader2 getMsgHdr() {
        return msgHdr;
    }

    /**
     * 设置msgHdr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link MessageHeader2 }
     *
     */
    public void setMsgHdr(MessageHeader2 value) {
        this.msgHdr = value;
    }

    /**
     * 获取orgnlQryInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link OriginalQueryInformation1 }
     *
     */
    public OriginalQueryInformation1 getOrgnlQryInf() {
        return orgnlQryInf;
    }

    /**
     * 设置orgnlQryInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OriginalQueryInformation1 }
     *
     */
    public void setOrgnlQryInf(OriginalQueryInformation1 value) {
        this.orgnlQryInf = value;
    }

    /**
     * 获取rplyInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link ReplyInformation1 }
     *
     */
    public ReplyInformation1 getRplyInf() {
        return rplyInf;
    }

    /**
     * 设置rplyInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ReplyInformation1 }
     *
     */
    public void setRplyInf(ReplyInformation1 value) {
        this.rplyInf = value;
    }

}

