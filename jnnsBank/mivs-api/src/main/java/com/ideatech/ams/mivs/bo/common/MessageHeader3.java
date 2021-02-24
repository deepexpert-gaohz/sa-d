package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:40 PM CST
//


import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>MessageHeader1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="MessageHeader1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgId" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}Max35Text"/>
 *         &lt;element name="CreDtTm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}ISODateTime"/>
 *         &lt;element name="InstgPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}InstructingParty1"/>
 *         &lt;element name="InstdPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}InstructedParty1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageHeader1", propOrder = {
        "msgId",
        "creDtTm",
        "instgPty",
        "instdPty"
})
public class MessageHeader3 {

    /**
     * MessageIdentification
     * 报文标识号
     * <MsgId>  [1..1] Max35Text
     */
    @XmlElement(name = "MsgId", required = true)
    protected String msgId;

    /**
     * CreationDateTime
     * 报文发送时间
     * <CreDtTm>  [1..1] ISODateTime
     */
    @XmlElement(name = "CreDtTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creDtTm;

    /**
     * 报文要素:InstructingParty
     * <InstgPty>  [1..1]
     */
    @XmlElement(name = "InstgPty", required = true)
    protected InstructingParty2 instgPty;

    /**
     * 报文要素:InstructedParty
     * <InstdPty>  [1..1]
     */
    @XmlElement(name = "InstdPty", required = true)
    protected InstructedParty1 instdPty;

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
     * 获取creDtTm属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCreDtTm() {
        return creDtTm;
    }

    /**
     * 设置creDtTm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setCreDtTm(XMLGregorianCalendar value) {
        this.creDtTm = value;
    }

    /**
     * 获取instgPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link InstructingParty1 }
     *
     */
    public InstructingParty2 getInstgPty() {
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
    public void setInstgPty(InstructingParty2 value) {
        this.instgPty = value;
    }

    /**
     * 获取instdPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link InstructedParty1 }
     *
     */
    public InstructedParty1 getInstdPty() {
        return instdPty;
    }

    /**
     * 设置instdPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link InstructedParty1 }
     *
     */
    public void setInstdPty(InstructedParty1 value) {
        this.instdPty = value;
    }

}

