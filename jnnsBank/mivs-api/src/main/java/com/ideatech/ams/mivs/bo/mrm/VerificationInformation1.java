package com.ideatech.ams.mivs.bo.mrm;

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
 * <p>VerificationInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="VerificationInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MobNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max13NumericText"/>
 *         &lt;element name="Rslt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text"/>
 *         &lt;element name="MobCrr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text" minOccurs="0"/>
 *         &lt;element name="LocMobNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text" minOccurs="0"/>
 *         &lt;element name="LocNmMobNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max10Text" minOccurs="0"/>
 *         &lt;element name="CdTp" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text" minOccurs="0"/>
 *         &lt;element name="Sts" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerificationInformation1", propOrder = {
        "mobNb",
        "rslt",
        "mobCrr",
        "locMobNb",
        "locNmMobNb",
        "cdTp",
        "sts"
})
public class VerificationInformation1 {

    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164 (Max13NumericText) 禁止中文
     */
    @XmlElement(name = "MobNb", required = true)
    protected String mobNb;

    /**
     * Result
     * 手机号码核查结果
     * <Rslt>  [1..1] PhoneNumberVerificationResultCode (Max4Text)
     */
    @XmlElement(name = "Rslt", required = true)
    protected String rslt;

    /**
     * MobileCarrier
     * 手机运营商
     * <MobCrr>  [0..1] MobileCarrierCode(Max4Text)  禁止中文
     * 当“手机号码核查结果”为 MCHD 时 填写
     */
    @XmlElement(name = "MobCrr")
    protected String mobCrr;

    /**
     * LocationOfMobileNumber
     * 手机号归属地代码 <LocMobNb>  [0..1] Max4Text 禁止中文
     * 当“手机号码核查结果”为 MCHD 时填写
     */
    @XmlElement(name = "LocMobNb")
    protected String locMobNb;

    /**
     * LocationNameOfMobileNumber
     * 手机号归属地名称 <LocNmMobNb>  [0..1] Max10Text
     * 当“手机号码核查结果”为 MCHD 时填写
     */
    @XmlElement(name = "LocNmMobNb")
    protected String locNmMobNb;

    /**
     *
     * CardType
     * 客户类型 <CdTp>  [0..1] Max4Text
     *
     * 当“手机号码核查结果”为 MCHD 时填写
     * INDV：个人用户
     * ENTY：单位用户
     */
    @XmlElement(name = "CdTp")
    protected String cdTp;

    /**
     * Status
     * 手机号码状态 <Sts>  [0..1] Max4Text
     * 当“手机号码核查结果”为 MCHD 时填写
     * ENBL 正常
     * DSBL 停机
     */
    @XmlElement(name = "Sts")
    protected String sts;

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

    /**
     * 获取mobCrr属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMobCrr() {
        return mobCrr;
    }

    /**
     * 设置mobCrr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMobCrr(String value) {
        this.mobCrr = value;
    }

    /**
     * 获取locMobNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLocMobNb() {
        return locMobNb;
    }

    /**
     * 设置locMobNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLocMobNb(String value) {
        this.locMobNb = value;
    }

    /**
     * 获取locNmMobNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLocNmMobNb() {
        return locNmMobNb;
    }

    /**
     * 设置locNmMobNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLocNmMobNb(String value) {
        this.locNmMobNb = value;
    }

    /**
     * 获取cdTp属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCdTp() {
        return cdTp;
    }

    /**
     * 设置cdTp属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCdTp(String value) {
        this.cdTp = value;
    }

    /**
     * 获取sts属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSts() {
        return sts;
    }

    /**
     * 设置sts属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSts(String value) {
        this.sts = value;
    }

}

