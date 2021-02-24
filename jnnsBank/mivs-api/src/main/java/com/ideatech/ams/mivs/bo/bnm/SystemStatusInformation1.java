package com.ideatech.ams.mivs.bo.bnm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:40 PM CST
//


import com.ideatech.ams.mivs.bo.common.ServiceInformation1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>SystemStatusInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="SystemStatusInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurSysDt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}ISODate"/>
 *         &lt;element name="NxtSysDt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}ISODate"/>
 *         &lt;element name="SvcInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01}ServiceInformation1" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemStatusInformation1", propOrder = {
        "curSysDt",
        "nxtSysDt",
        "svcInf"
})
public class SystemStatusInformation1 {

    /**
     * CurrentSystemDate
     * 系统当前日期
     * <CurSysDt>  [1..1]  ISODate  禁止中文
     */
    @XmlElement(name = "CurSysDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar curSysDt;


    /**
     * NextSystemDate
     * 系统下一日期
     * <NxtSysDt>  [1..1]  ISODate  禁止中文
     */
    @XmlElement(name = "NxtSysDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar nxtSysDt;

    /**
     * ServiceInformation  <SvcInf>  [1..n]
     */
    @XmlElement(name = "SvcInf", required = true)
    protected List<ServiceInformation1> svcInf;

    /**
     * 获取curSysDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCurSysDt() {
        return curSysDt;
    }

    /**
     * 设置curSysDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setCurSysDt(XMLGregorianCalendar value) {
        this.curSysDt = value;
    }

    /**
     * 获取nxtSysDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getNxtSysDt() {
        return nxtSysDt;
    }

    /**
     * 设置nxtSysDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setNxtSysDt(XMLGregorianCalendar value) {
        this.nxtSysDt = value;
    }

    /**
     * Gets the value of the svcInf property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the svcInf property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSvcInf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceInformation1 }
     *
     *
     */
    public List<ServiceInformation1> getSvcInf() {
        if (svcInf == null) {
            svcInf = new ArrayList<ServiceInformation1>();
        }
        return this.svcInf;
    }

    public void setSvcInf(List<ServiceInformation1> svcInf){
        this.svcInf = new ArrayList<>(svcInf);
    }

}

