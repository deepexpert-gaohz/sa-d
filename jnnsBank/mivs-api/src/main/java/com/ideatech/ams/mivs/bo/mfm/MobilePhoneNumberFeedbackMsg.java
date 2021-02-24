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
 * <p>Document complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdVrfctnFdbk" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01}IdentityVerificationFeedback1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * mivs.347.001.01	手机号码核查结果疑义反馈报文	参与机构->MIVS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
        "idVrfctnFdbk"
})
public class MobilePhoneNumberFeedbackMsg {

    /**
     * Message root  <IdVrfctnFdbk>  [1..1]
     */
    @XmlElement(name = "IdVrfctnFdbk", required = true)
    protected IdentityVerificationFeedback1 idVrfctnFdbk;

    /**
     * 获取idVrfctnFdbk属性的值。
     *
     * @return
     *     possible object is
     *     {@link IdentityVerificationFeedback1 }
     *
     */
    public IdentityVerificationFeedback1 getIdVrfctnFdbk() {
        return idVrfctnFdbk;
    }

    /**
     * 设置idVrfctnFdbk属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link IdentityVerificationFeedback1 }
     *
     */
    public void setIdVrfctnFdbk(IdentityVerificationFeedback1 value) {
        this.idVrfctnFdbk = value;
    }

}

