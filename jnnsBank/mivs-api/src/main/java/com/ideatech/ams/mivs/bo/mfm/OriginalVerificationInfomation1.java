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
 * <p>OriginalVerificationInfomation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OriginalVerificationInfomation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MobNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max13NumericText"/>
 *         &lt;element name="Nm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max140Text"/>
 *         &lt;element name="IdTp" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max4Text"/>
 *         &lt;element name="Id" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max35Text"/>
 *         &lt;choice>
 *           &lt;element name="UniSocCdtCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max18Text"/>
 *           &lt;element name="BizRegNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max15Text"/>
 *         &lt;/choice>
 *         &lt;element name="Rslt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}Max4Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginalVerificationInfomation1", propOrder = {
        "mobNb",
        "nm",
        "idTp",
        "id",
        "uniSocCdtCd",
        "bizRegNb",
        "rslt"
})
public class OriginalVerificationInfomation1 {

    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164(Max13NumericText) 禁止中文
     */
    @XmlElement(name = "MobNb", required = true)
    protected String mobNb;

    /**
     * Name
     * 姓名
     * <Nm>  [1..1]  Max140Text
     */
    @XmlElement(name = "Nm", required = true)
    protected String nm;

    /**
     * IdentificationType
     * 证件类型
     * <IdTp>  [1..1]IdTypeCode(Max4Text)禁止中文
     */
    @XmlElement(name = "IdTp", required = true)
    protected String idTp;

    /**
     * Identification
     * 证件号码
     * <Id>  [1..1]  Max35Text  禁止中文
     */
    @XmlElement(name = "Id", required = true)
    protected String id;

    /**
     * {Or
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1]  Max18Text企业客户的统一社会信用代码
     */
    @XmlElement(name = "UniSocCdtCd")
    protected String uniSocCdtCd;

    /**
     * Or}
     * BusinessRegistrationNumber
     * 工商注册号
     * <BizRegNb>  [1..1]  Max15Text企业客户的工商注册号
     */
    @XmlElement(name = "BizRegNb")
    protected String bizRegNb;

    /**
     * Result
     * 手机号码核查结果
     * <Rslt>  [1..1] PhoneNumberVerificationResultCode (Max4Text)
     */
    @XmlElement(name = "Rslt", required = true)
    protected String rslt;

    /**
     * 获取mobNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMobNb() {
        return mobNb;
    }

    /**
     * 设置mobNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMobNb(String value) {
        this.mobNb = value;
    }

    /**
     * 获取nm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNm() {
        return nm;
    }

    /**
     * 设置nm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNm(String value) {
        this.nm = value;
    }

    /**
     * 获取idTp属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdTp() {
        return idTp;
    }

    /**
     * 设置idTp属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdTp(String value) {
        this.idTp = value;
    }

    /**
     * 获取id属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * 获取uniSocCdtCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUniSocCdtCd() {
        return uniSocCdtCd;
    }

    /**
     * 设置uniSocCdtCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUniSocCdtCd(String value) {
        this.uniSocCdtCd = value;
    }

    /**
     * 获取bizRegNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBizRegNb() {
        return bizRegNb;
    }

    /**
     * 设置bizRegNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBizRegNb(String value) {
        this.bizRegNb = value;
    }

    /**
     * 获取rslt属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRslt() {
        return rslt;
    }

    /**
     * 设置rslt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRslt(String value) {
        this.rslt = value;
    }

}

