package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:57 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OrgnlBizQry1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OrgnlBizQry1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgId" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max35Text"/>
 *         &lt;element name="InstgPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}InstructingParty1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrgnlBizQry1", propOrder = {
        "msgId",
        "instgPty"
})
public class OrgnlBizQry1 {

    /**
     * MessageIdentification
     * 原申请报文标识号
     * <MsgId>  [1..1] Max35Text  禁止中文
     */
    @XmlElement(name = "MsgId", required = true)
    protected String msgId;

    /**
     * InstructingParty
     * <InstgPty>  [1..1]
     */
    @XmlElement(name = "InstgPty", required = true)
    protected InstructingParty1 instgPty;

    /**
     * 获取msgId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * 设置msgId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    /**
     * 获取instgPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link InstructingParty1 }
     *
     */
    public InstructingParty1 getInstgPty() {
        return instgPty;
    }

    /**
     * 设置instgPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link InstructingParty1 }
     *
     */
    public void setInstgPty(InstructingParty1 value) {
        this.instgPty = value;
    }

}

