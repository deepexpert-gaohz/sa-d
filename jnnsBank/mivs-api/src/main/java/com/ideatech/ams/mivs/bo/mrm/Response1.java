package com.ideatech.ams.mivs.bo.mrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:57 PM CST
//


import com.ideatech.ams.mivs.bo.common.OperationalError1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Response1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Response1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="VrfctnInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}VerificationInformation1"/>
 *           &lt;element name="OprlErr" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}OperationalError1"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response1", propOrder = {
        "vrfctnInf",
        "oprlErr"
})
public class Response1 {

    /**
     * VerificationInformation
     * <VrfctnInf>  [1..1]
     */
    @XmlElement(name = "VrfctnInf")
    protected VerificationInformation1 vrfctnInf;

    /**
     * OperationalError
     * <OprlErr>  [1..1]
     */
    @XmlElement(name = "OprlErr")
    protected OperationalError1 oprlErr;

    /**
     * 获取vrfctnInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link VerificationInformation1 }
     *
     */
    public VerificationInformation1 getVrfctnInf() {
        return vrfctnInf;
    }

    /**
     * 设置vrfctnInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link VerificationInformation1 }
     *
     */
    public void setVrfctnInf(VerificationInformation1 value) {
        this.vrfctnInf = value;
    }

    /**
     * 获取oprlErr属性的值。
     *
     * @return
     *     possible object is
     *     {@link OperationalError1 }
     *
     */
    public OperationalError1 getOprlErr() {
        return oprlErr;
    }

    /**
     * 设置oprlErr属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OperationalError1 }
     *
     */
    public void setOprlErr(OperationalError1 value) {
        this.oprlErr = value;
    }

}

