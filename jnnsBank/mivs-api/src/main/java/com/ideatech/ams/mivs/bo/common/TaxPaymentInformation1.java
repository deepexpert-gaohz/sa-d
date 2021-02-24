package com.ideatech.ams.mivs.bo.common;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:04:39 PM CST
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TaxPaymentInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="TaxPaymentInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TxAuthCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.323.001.01}Max11Text"/>
 *         &lt;element name="TxAuthNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.323.001.01}Max300Text"/>
 *         &lt;element name="TxpyrSts" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.323.001.01}Max4Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxPaymentInformation1", propOrder = {
        "txAuthCd",
        "txAuthNm",
        "txpyrSts"
})
public class TaxPaymentInformation1 {

    /**
     * TaxAuthorityCode
     * 税务机关代码
     * <TxAuthCd>  [1..1] TxAuthCd (Max11Text)
     */
    @XmlElement(name = "TxAuthCd", required = true)
    protected String txAuthCd;

    /**
     * TaxAuthorityName
     * 税务机关名称
     * <TxAuthNm>  [1..1] Max300Text
     */
    @XmlElement(name = "TxAuthNm", required = true)
    protected String txAuthNm;

    /**
     * TaxpayerStatus
     * 纳税人状态
     * <TxpyrSts>  [1..1] TaxpayerStatusCode (Max4Text)
     */
    @XmlElement(name = "TxpyrSts", required = true)
    protected String txpyrSts;

    /**
     * 获取txAuthCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTxAuthCd() {
        return txAuthCd;
    }

    /**
     * 设置txAuthCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTxAuthCd(String value) {
        this.txAuthCd = value;
    }

    /**
     * 获取txAuthNm属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTxAuthNm() {
        return txAuthNm;
    }

    /**
     * 设置txAuthNm属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTxAuthNm(String value) {
        this.txAuthNm = value;
    }

    /**
     * 获取txpyrSts属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTxpyrSts() {
        return txpyrSts;
    }

    /**
     * 设置txpyrSts属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTxpyrSts(String value) {
        this.txpyrSts = value;
    }

}

