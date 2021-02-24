package com.ideatech.ams.mivs.bo.bnm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:40 PM CST
//


import com.ideatech.ams.mivs.bo.common.MessageHeader1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SystemStatusNotificationV1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="SystemStatusNotificationV1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgHdr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}MessageHeader1"/>
 *         &lt;element name="SysStsInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}SystemStatusInformation1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemStatusNotificationV1", propOrder = {
        "msgHdr",
        "sysStsInf"
})
public class SystemStatusNotificationV1 {

    /**
     * MessageHeader  <MsgHdr>  [1..1]
     */
    @XmlElement(name = "MsgHdr", required = true)
    protected MessageHeader1 msgHdr;

    /**
     * SystemStatusInformation  <SysStsInf>  [1..1]
     */
    @XmlElement(name = "SysStsInf", required = true)
    protected SystemStatusInformation1 sysStsInf;

    /**
     * 获取msgHdr属性的值。
     *
     * @return
     *     possible object is
     *     {@link MessageHeader1 }
     *
     */
    public MessageHeader1 getMsgHdr() {
        return msgHdr;
    }

    /**
     * 设置msgHdr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link MessageHeader1 }
     *
     */
    public void setMsgHdr(MessageHeader1 value) {
        this.msgHdr = value;
    }

    /**
     * 获取sysStsInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link SystemStatusInformation1 }
     *
     */
    public SystemStatusInformation1 getSysStsInf() {
        return sysStsInf;
    }

    /**
     * 设置sysStsInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link SystemStatusInformation1 }
     *
     */
    public void setSysStsInf(SystemStatusInformation1 value) {
        this.sysStsInf = value;
    }

}

