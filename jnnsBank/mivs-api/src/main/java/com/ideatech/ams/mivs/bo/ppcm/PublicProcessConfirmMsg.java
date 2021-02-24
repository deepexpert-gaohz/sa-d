package com.ideatech.ams.mivs.bo.ppcm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:08:32 PM CST
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
 *         &lt;element name="CmonConf" type="{urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02}CmonConfV01"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * ccms.900.001.02	通用处理确认报文	参与机构<-MIVS
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
        "cmonConf"
})
public class PublicProcessConfirmMsg {

    /**
     * Message root
     * <CmonConf>  [1..1]
     */
    @XmlElement(name = "CmonConf", required = true)
    protected CmonConfV01 cmonConf;

    /**
     * 获取cmonConf属性的值。
     *
     * @return
     *     possible object is
     *     {@link CmonConfV01 }
     *
     */
    public CmonConfV01 getCmonConf() {
        return cmonConf;
    }

    /**
     * 设置cmonConf属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link CmonConfV01 }
     *
     */
    public void setCmonConf(CmonConfV01 value) {
        this.cmonConf = value;
    }

}

