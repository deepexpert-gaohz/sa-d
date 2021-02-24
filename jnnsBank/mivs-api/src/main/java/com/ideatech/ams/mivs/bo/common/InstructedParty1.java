package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:04:08 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InstructedParty1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="InstructedParty1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstdDrctPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max14NumericText"/>
 *         &lt;element name="InstdPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max14NumericText"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstructedParty1", propOrder = {
        "instdDrctPty",
        "instdPty"
})
public class InstructedParty1 {

    /**
     * InstructedDirectParty
     * 接收直接参与机构
     * <InstdDrctPty>  [1..1] Max14Text 固定填写“0000”
     */
    @XmlElement(name = "InstdDrctPty", required = true)
    protected String instdDrctPty;

    /**
     * InstructedParty
     * 接收参与机构
     * <InstdPty>  [1..1] Max14Text 固定填写“0000”
     */
    @XmlElement(name = "InstdPty", required = true)
    protected String instdPty;

    /**
     * 获取instdDrctPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInstdDrctPty() {
        return instdDrctPty;
    }

    /**
     * 设置instdDrctPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInstdDrctPty(String value) {
        this.instdDrctPty = value;
    }

    /**
     * 获取instdPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInstdPty() {
        return instdPty;
    }

    /**
     * 设置instdPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInstdPty(String value) {
        this.instdPty = value;
    }

}

