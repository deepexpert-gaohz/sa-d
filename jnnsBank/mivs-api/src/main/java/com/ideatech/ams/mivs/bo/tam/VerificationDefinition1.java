package com.ideatech.ams.mivs.bo.tam;

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
 * <p>VerificationDefinition1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="VerificationDefinition1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CoNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max300Text"/>
 *         &lt;choice>
 *           &lt;element name="UniSocCdtCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max18Text"/>
 *           &lt;element name="TxpyrIdNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max20Text"/>
 *         &lt;/choice>
 *         &lt;element name="OpNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.322.001.01}Max140Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerificationDefinition1", propOrder = {
        "coNm",
        "uniSocCdtCd",
        "txpyrIdNb",
        "opNm"
})
public class VerificationDefinition1 {

    /**
     * CompanyName
     * 单位名称
     * <CoNm>  [1..1] Max300Text
     */
    @XmlElement(name = "CoNm", required = true)
    protected String coNm;

    /**
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1] Max18Text 企业客户的统一社会信用代码
     */
    @XmlElement(name = "UniSocCdtCd")
    protected String uniSocCdtCd;

    /**
     * TaxpayerIdentificationNumber
     * 纳税人识别号
     * <TxpyrIdNb>  [1..1] Max20Text 企业客户的纳税人识别号
     */
    @XmlElement(name = "TxpyrIdNb")
    protected String txpyrIdNb;

    /**
     * OperatorName
     * 操作员姓名
     * <OpNm>  [1..1] Max140Text 参与机构核查人员姓名
     */
    @XmlElement(name = "OpNm", required = true)
    protected String opNm;

    /**
     * 获取coNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCoNm() {
        return coNm;
    }

    /**
     * 设置coNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCoNm(String value) {
        this.coNm = value;
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
     * 获取txpyrIdNb属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTxpyrIdNb() {
        return txpyrIdNb;
    }

    /**
     * 设置txpyrIdNb属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTxpyrIdNb(String value) {
        this.txpyrIdNb = value;
    }

    /**
     * 获取opNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOpNm() {
        return opNm;
    }

    /**
     * 设置opNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOpNm(String value) {
        this.opNm = value;
    }

}

