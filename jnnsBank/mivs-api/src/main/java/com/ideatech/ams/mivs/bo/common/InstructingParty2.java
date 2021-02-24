package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:23 PM CST
//

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InstructingParty1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="InstructingParty1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstgDrctPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01}Max14NumericText"/>
 *         &lt;element name="DrctPtyNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01}Max140Text"/>
 *         &lt;element name="InstgPty" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01}Max14NumericText"/>
 *         &lt;element name="PtyNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01}Max140Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstructingParty1", propOrder = {
        "instgDrctPty",
        "drctPtyNm",
        "instgPty",
        "ptyNm"
})
public class InstructingParty2 {

    /**
     * InstructingDirectParty
     * 发起直接参与机构
     * <InstgDrctPty>  [1..1] Max140Text 禁止中文
     */
    @XmlElement(name = "InstgDrctPty", required = true)
    protected String instgDrctPty;

    /**
     * DirectPartyName
     * 发起直接参与机构行名
     * <DrctPtyNm>  [1..1] Max140Text
     */
    @XmlElement(name = "DrctPtyNm", required = true)
    protected String drctPtyNm;

    /**
     * InstructingParty
     * 发起参与机构
     * <InstgPty>  [1..1] Max14Text  禁止中文
     */
    @XmlElement(name = "InstgPty", required = true)
    protected String instgPty;

    /**
     * PartyName
     * 发起参与机构行名
     * <PtyNm>  [1..1] Max140Text
     */
    @XmlElement(name = "PtyNm", required = true)
    protected String ptyNm;

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
     * 获取drctPtyNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDrctPtyNm() {
        return drctPtyNm;
    }

    /**
     * 设置drctPtyNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDrctPtyNm(String value) {
        this.drctPtyNm = value;
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

    /**
     * 获取ptyNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPtyNm() {
        return ptyNm;
    }

    /**
     * 设置ptyNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPtyNm(String value) {
        this.ptyNm = value;
    }

}

