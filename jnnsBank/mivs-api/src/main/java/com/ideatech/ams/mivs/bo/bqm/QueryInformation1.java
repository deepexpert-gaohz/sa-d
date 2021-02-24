package com.ideatech.ams.mivs.bo.bqm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:17 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>QueryInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="QueryInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SysInd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.345.001.01}Max4Text"/>
 *         &lt;element name="QueDt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.345.001.01}ISODate"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryInformation1", propOrder = {
        "sysInd",
        "queDt"
})
public class QueryInformation1 {

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    @XmlElement(name = "SysInd", required = true)
    protected String sysInd;

    /**
     * QueryDate
     * 查询日期
     * <QueDt>  [1..1]  ISODate  禁止中文
     */
    @XmlElement(name = "QueDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar queDt;

    /**
     * 获取sysInd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSysInd() {
        return sysInd;
    }

    /**
     * 设置sysInd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSysInd(String value) {
        this.sysInd = value;
    }

    /**
     * 获取queDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getQueDt() {
        return queDt;
    }

    /**
     * 设置queDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setQueDt(XMLGregorianCalendar value) {
        this.queDt = value;
    }

}
