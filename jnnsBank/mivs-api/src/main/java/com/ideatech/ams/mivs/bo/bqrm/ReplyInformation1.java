package com.ideatech.ams.mivs.bo.bqrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:29 PM CST
//


import com.ideatech.ams.mivs.bo.common.ServiceInformation1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>ReplyInformation1 complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ReplyInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrgnlQueDt" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}ISODate"/>
 *         &lt;element name="ProcSts" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}Max4Text"/>
 *         &lt;element name="ProcCd" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}Max8Text" minOccurs="0"/>
 *         &lt;element name="RjctInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}Max105Text" minOccurs="0"/>
 *         &lt;element name="SvcInf" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01}ServiceInformation1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReplyInformation1", propOrder = {
        "orgnlQueDt",
        "procSts",
        "procCd",
        "rjctInf",
        "svcInf"
})
public class ReplyInformation1 {

    /**
     * OrginalQueryDate
     * 原查询日期
     * <OrgnlQueDt>  [1..1]  ISODate  禁止中文
     */
    @XmlElement(name = "OrgnlQueDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar orgnlQueDt;

    /**
     * ProcessStatus
     * 申请报文处理状态
     * <ProcSts>  [1..1]  ProcessCode
     * PR07 已处理
     * PR09 已拒绝
     */
    @XmlElement(name = "ProcSts", required = true)
    protected String procSts;

    /**
     * ProcessCode
     * 申请报文处理码
     * <ProcCd>  [0..1]  Max8Text
     */
    @XmlElement(name = "ProcCd")
    protected String procCd;

    /**
     * RejectInformation
     * 申请报文拒绝信息
     * <RjctInf>  [0..1]  Max105Text
     * 当申请报文处理状态为 PR09 已拒绝时填写
     */
    @XmlElement(name = "RjctInf")
    protected String rjctInf;

    /**
     * ServiceInformation
     * <SvcInf>  [0..1]
     * 当被查询日期有效且申请报文处理状态不为 PR09 时填写
     */
    @XmlElement(name = "SvcInf")
    protected ServiceInformation1 svcInf;

    /**
     * 获取orgnlQueDt属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getOrgnlQueDt() {
        return orgnlQueDt;
    }

    /**
     * 设置orgnlQueDt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setOrgnlQueDt(XMLGregorianCalendar value) {
        this.orgnlQueDt = value;
    }

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

    /**
     * 获取svcInf属性的值。
     *
     * @return
     *     possible object is
     *     {@link ServiceInformation1 }
     *
     */
    public ServiceInformation1 getSvcInf() {
        return svcInf;
    }

    /**
     * 设置svcInf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ServiceInformation1 }
     *
     */
    public void setSvcInf(ServiceInformation1 value) {
        this.svcInf = value;
    }

}

