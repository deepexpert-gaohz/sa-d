package com.ideatech.ams.mivs.bo.mam;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:23 PM CST
//

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="GetIdVrfctn" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01}GetIdentityVerificationV1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
        "getIdVrfctn"
})
public class MobilePhoneNumberApplyMsg {


    /**
     * 报文要素:Message root
     * <GetIdVrfctn>  [1..1]
     */
    @XmlElement(name = "GetIdVrfctn", required = true)
    protected GetIdentityVerificationV1 getIdVrfctn;

    /**
     * 获取getIdVrfctn属性的值。
     *
     * @return
     *     possible object is
     *     {@link GetIdentityVerificationV1 }
     *
     */
    public GetIdentityVerificationV1 getGetIdVrfctn() {
        return getIdVrfctn;
    }

    /**
     * 设置getIdVrfctn属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link GetIdentityVerificationV1 }
     *
     */
    public void setGetIdVrfctn(GetIdentityVerificationV1 value) {
        this.getIdVrfctn = value;
    }

}

