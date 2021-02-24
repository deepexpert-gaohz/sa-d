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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InstgPty1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="InstgPty1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstgDrctPty" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max14Text"/>
 *         &lt;element name="InstgPty" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}Max14Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstgPty1", propOrder = {
        "instgDrctPty",
        "instgPty"
})
public class InstgPty1 {

    /**
     * InstructingDirectParty
     * 发起直接参与机构
     * <InstgDrctPty>  [1..1] Max14Text  禁止中文
     */
    @XmlElement(name = "InstgDrctPty", required = true)
    protected String instgDrctPty;

    /**
     * InstructingParty
     * 发起参与机构
     * <InstgPty>  [1..1] Max14Text  禁止中文
     */
    @XmlElement(name = "InstgPty", required = true)
    protected String instgPty;

    /**
     * 获取instgDrctPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInstgDrctPty() {
        return instgDrctPty;
    }

    /**
     * 设置instgDrctPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInstgDrctPty(String value) {
        this.instgDrctPty = value;
    }

    /**
     * 获取instgPty属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInstgPty() {
        return instgPty;
    }

    /**
     * 设置instgPty属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInstgPty(String value) {
        this.instgPty = value;
    }

}

