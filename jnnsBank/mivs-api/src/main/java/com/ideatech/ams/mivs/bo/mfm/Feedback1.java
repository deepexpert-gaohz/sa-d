package com.ideatech.ams.mivs.bo.mfm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:16 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Feedback1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Feedback1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SysInd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max4Text"/>
 *         &lt;element name="OrgnlVrfctn" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}OriginalVerification1"/>
 *         &lt;element name="Cntt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max256Text"/>
 *         &lt;element name="ContactNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max140Text"/>
 *         &lt;element name="ContactNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max30Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Feedback1", propOrder = {
        "sysInd",
        "orgnlVrfctn",
        "cntt",
        "contactNm",
        "contactNb"
})
public class Feedback1 {

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1]  SystemTypeCode  (Max4Text)  固定填写“MIIT”
     */
    @XmlElement(name = "SysInd", required = true)
    protected String sysInd;

    /**
     * OriginalVerification
     * <OrgnlVrfctn>  [1..1]
     */
    @XmlElement(name = "OrgnlVrfctn", required = true)
    protected OriginalVerification1 orgnlVrfctn;

    /**
     * Content
     * 疑义反馈内容
     * <Cntt>  [1..1]  Max256Text
     */
    @XmlElement(name = "Cntt", required = true)
    protected String cntt;

    /**
     * ContactName
     * 联系人姓名
     * <ContactNm>  [1..1]  Max140Text
     */
    @XmlElement(name = "ContactNm", required = true)
    protected String contactNm;

    /**
     * ContactNumber
     * 联系人电话
     * <ContactNb>  [1..1]  Max30Text  禁止中文
     */
    @XmlElement(name = "ContactNb", required = true)
    protected String contactNb;

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
     * 获取orgnlVrfctn属性的值。
     *
     * @return
     *     possible object is
     *     {@link OriginalVerification1 }
     *
     */
    public OriginalVerification1 getOrgnlVrfctn() {
        return orgnlVrfctn;
    }

    /**
     * 设置orgnlVrfctn属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OriginalVerification1 }
     *
     */
    public void setOrgnlVrfctn(OriginalVerification1 value) {
        this.orgnlVrfctn = value;
    }

    /**
     * 获取cntt属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCntt() {
        return cntt;
    }

    /**
     * 设置cntt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCntt(String value) {
        this.cntt = value;
    }

    /**
     * 获取contactNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContactNm() {
        return contactNm;
    }

    /**
     * 设置contactNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContactNm(String value) {
        this.contactNm = value;
    }

    /**
     * 获取contactNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContactNb() {
        return contactNb;
    }

    /**
     * 设置contactNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContactNb(String value) {
        this.contactNb = value;
    }

}

