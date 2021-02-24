package com.ideatech.ams.mivs.bo.trm;

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
 * <p>Document complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RtrTxPmtVrfctn" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.323.001.01}ReturnTaxPaymentVerification1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * mivs.323.001.01	纳税信息联网核查应答报文	参与机构<-MIVS
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
        "rtrTxPmtVrfctn"
})
public class TaxInformationReplyMsg {

    /**
     * Message root
     * <RtrTxPmtVrfctn>  [1..1]
     */
    @XmlElement(name = "RtrTxPmtVrfctn", required = true)
    protected ReturnTaxPaymentVerification1 rtrTxPmtVrfctn;

    /**
     * 获取rtrTxPmtVrfctn属性的值。
     *
     * @return
     *     possible object is
     *     {@link ReturnTaxPaymentVerification1 }
     *
     */
    public ReturnTaxPaymentVerification1 getRtrTxPmtVrfctn() {
        return rtrTxPmtVrfctn;
    }

    /**
     * 设置rtrTxPmtVrfctn属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ReturnTaxPaymentVerification1 }
     *
     */
    public void setRtrTxPmtVrfctn(ReturnTaxPaymentVerification1 value) {
        this.rtrTxPmtVrfctn = value;
    }

}

