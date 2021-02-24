package com.ideatech.ams.mivs.bo.common;

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
 * <p>OperationalError1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OperationalError1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProcSts" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max4Text"/>
 *         &lt;element name="ProcCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max8Text"/>
 *         &lt;element name="RjctInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}Max105Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationalError1", propOrder = {
        "procSts",
        "procCd",
        "rjctInf"
})
public class OperationalError1 {

    /**
     * ProcessStatus
     * 申请报文拒绝状态
     * <ProcSts>  [1..1] ProcessCode (Max4Text) 禁止中文
     */
    @XmlElement(name = "ProcSts", required = true)
    protected String procSts;

    /**
     * ProcessCode
     * 申请报文拒绝码
     * <ProcCd>  [1..1] Max8Text  禁止中文
     */
    @XmlElement(name = "ProcCd", required = true)
    protected String procCd;

    /**
     * RejectInformation
     * 申请报文拒绝信息
     * <Rjctinf>  [1..1] Max105Text
     */
    @XmlElement(name = "RjctInf", required = true)
    protected String rjctInf;

    /**
     * 获取procSts属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProcSts() {
        return procSts;
    }

    /**
     * 设置procSts属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProcSts(String value) {
        this.procSts = value;
    }

    /**
     * 获取procCd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProcCd() {
        return procCd;
    }

    /**
     * 设置procCd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProcCd(String value) {
        this.procCd = value;
    }

    /**
     * 获取rjctInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRjctInf() {
        return rjctInf;
    }

    /**
     * 设置rjctInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRjctInf(String value) {
        this.rjctInf = value;
    }

}

