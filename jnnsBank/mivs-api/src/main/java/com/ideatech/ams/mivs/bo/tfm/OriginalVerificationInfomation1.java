package com.ideatech.ams.mivs.bo.tfm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:27 PM CST
//


import com.ideatech.ams.mivs.bo.common.TaxPaymentInformation1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="CoNm" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}Max300Text"/>
 *         &lt;choice>
 *           &lt;element name="UniSocCdtCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}Max18Text"/>
 *           &lt;element name="TxpyrIdNb" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}Max20Text"/>
 *         &lt;/choice>
 *         &lt;element name="Rslt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}Max4Text"/>
 *         &lt;element name="DataResrcDt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}ISODate"/>
 *         &lt;element name="TxpmtInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01}TaxPaymentInformation1" maxOccurs="unbounded" minOccurs="0"/>
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
        "coNm",
        "uniSocCdtCd",
        "txpyrIdNb",
        "rslt",
        "dataResrcDt",
        "txpmtInf"
})
public class OriginalVerificationInfomation1 {

    /**
     * CompanyName
     * 单位名称
     * <CoNm>  [1..1]  Max300Text
     */
    @XmlElement(name = "CoNm", required = true)
    protected String coNm;

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
     * TaxpayerIdentificationNumber
     * 纳税人识别号
     * <TxpyrIdNb>  [1..1]  Max20Text 企业客户的纳税人识别号
     */
    @XmlElement(name = "TxpyrIdNb")
    protected String txpyrIdNb;

    /**
     * Result
     * 纳税信息核查结果
     * <Rslt>  [1..1]TaxPaymentVerificationResultCode(Max4Text)
     */
    @XmlElement(name = "Rslt", required = true)
    protected String rslt;

    /**
     * DataResourceDate
     * 数据源日期
     * <DataResrcDt>  [1..1]  ISODate
     */
    @XmlElement(name = "DataResrcDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataResrcDt;

    /**
     * TaxPaymentInformation
     * <TxpmtInf>  [0..n]
     * 当“纳税信息核查结果”为“MCHD”时填写
     */
    @XmlElement(name = "TxpmtInf")
    protected List<TaxPaymentInformation1> txpmtInf;

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
     * 获取dataResrcDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDataResrcDt() {
        return dataResrcDt;
    }

    /**
     * 设置dataResrcDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDataResrcDt(XMLGregorianCalendar value) {
        this.dataResrcDt = value;
    }

    /**
     * Gets the value of the txpmtInf property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the txpmtInf property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTxpmtInf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxPaymentInformation1 }
     *
     *
     */
    public List<TaxPaymentInformation1> getTxpmtInf() {
        if (txpmtInf == null) {
            txpmtInf = new ArrayList<TaxPaymentInformation1>();
        }
        return this.txpmtInf;
    }

    public void setTxpmtInf(List<TaxPaymentInformation1> taxPaymentInformation1List ){
        this.txpmtInf = new ArrayList<>(taxPaymentInformation1List);
    }

}

