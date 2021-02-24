package com.ideatech.ams.mivs.bo.mrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:57 PM CST
//


import com.ideatech.ams.mivs.bo.common.MessageHeader2;
import com.ideatech.ams.mivs.bo.common.OrgnlBizQry1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RtrIdVrfctnV1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="RtrIdVrfctnV1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgHdr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}MessageHeader1"/>
 *         &lt;element name="OrgnlBizQry" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}OrgnlBizQry1"/>
 *         &lt;element name="Rspsn" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Response1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RtrIdVrfctnV1", propOrder = {
        "msgHdr",
        "orgnlBizQry",
        "rspsn"
})
public class RtrIdVrfctnV1 {

    /**
     * MessageHeader
     * <MsgHdr>  [1..1]
     */
    @XmlElement(name = "MsgHdr", required = true)
    protected MessageHeader2 msgHdr;

    /**
     * OriginalBusinessQuery
     * <OrgnlBizQry>  [1..1]
     */
    @XmlElement(name = "OrgnlBizQry", required = true)
    protected OrgnlBizQry1 orgnlBizQry;

    /**
     * Response
     * <Rspsn>  [1..1]
     */
    @XmlElement(name = "Rspsn", required = true)
    protected Response1 rspsn;

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
     * 获取orgnlBizQry属性的值。
     *
     * @return
     *     possible object is
     *     {@link OrgnlBizQry1 }
     *
     */
    public OrgnlBizQry1 getOrgnlBizQry() {
        return orgnlBizQry;
    }

    /**
     * 设置orgnlBizQry属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OrgnlBizQry1 }
     *
     */
    public void setOrgnlBizQry(OrgnlBizQry1 value) {
        this.orgnlBizQry = value;
    }

    /**
     * 获取rspsn属性的值。
     *
     * @return
     *     possible object is
     *     {@link Response1 }
     *
     */
    public Response1 getRspsn() {
        return rspsn;
    }

    /**
     * 设置rspsn属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Response1 }
     *
     */
    public void setRspsn(Response1 value) {
        this.rspsn = value;
    }

}

