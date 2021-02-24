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
 * <p>GroupHeader1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="GroupHeader1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgId" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max35Text"/>
 *         &lt;element name="CreDtTm" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}ISODateTime"/>
 *         &lt;element name="InstgPty" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}InstgPty1"/>
 *         &lt;element name="InstdPty" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}InstdPty1"/>
 *         &lt;element name="SysCd" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}SystemCode1"/>
 *         &lt;element name="Rmk" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max256Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupHeader1", propOrder = {
        "msgId",
        "creDtTm",
        "instgPty",
        "instdPty",
        "sysCd",
        "rmk"
})
public class GroupHeader1 {

    /**
     * MessageIdentification
     * 报文标识号
     * <MsgId>  [1..1] Max35Text  禁止中文
     */
    @XmlElement(name = "MsgId", required = true)
    protected String msgId;

    /**
     * CreationDateTime
     * 报文发送时间
     * <CreDtTm>  [1..1] ISODateTime  禁止中文
     */
    @XmlElement(name = "CreDtTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creDtTm;

    /**
     * InstructingParty
     * <InstgPty>  [1..1]
     */
    @XmlElement(name = "InstgPty", required = true)
    protected InstgPty1 instgPty;

    /**
     * InstructedParty
     * <InstdPty>  [1..1]
     */
    @XmlElement(name = "InstdPty", required = true)
    protected InstdPty1 instdPty;

    /**
     * SystemCode
     * 系统编号
     * <SysCd>  [1..1] SystemCode  禁止中文
     */
    @XmlElement(name = "SysCd", required = true)
    @XmlSchemaType(name = "string")
    protected SystemCode1 sysCd;

    /**
     * Remark
     * 备注
     * <Rmk>  [0..1] Max256Text  允许中文
     */
    @XmlElement(name = "Rmk")
    protected String rmk;

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
     *     {@link InstgPty1 }
     *
     */
    public InstgPty1 getInstgPty() {
        return instgPty;
    }

    /**
     * 设置instgPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link InstgPty1 }
     *
     */
    public void setInstgPty(InstgPty1 value) {
        this.instgPty = value;
    }

    /**
     * 获取instdPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link InstdPty1 }
     *
     */
    public InstdPty1 getInstdPty() {
        return instdPty;
    }

    /**
     * 设置instdPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link InstdPty1 }
     *
     */
    public void setInstdPty(InstdPty1 value) {
        this.instdPty = value;
    }

    /**
     * 获取sysCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link SystemCode1 }
     *
     */
    public SystemCode1 getSysCd() {
        return sysCd;
    }

    /**
     * 设置sysCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link SystemCode1 }
     *
     */
    public void setSysCd(SystemCode1 value) {
        this.sysCd = value;
    }

    /**
     * 获取rmk属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRmk() {
        return rmk;
    }

    /**
     * 设置rmk属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRmk(String value) {
        this.rmk = value;
    }

}

