package com.ideatech.ams.mivs.bo.mrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:57 PM CST
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
 *         &lt;element name="RtrIdVrfctn" type="{urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01}RtrIdVrfctnV1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * mivs.321.001.01	手机号码联网核查应答报文	参与机构<-MIVS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
        "rtrIdVrfctn"
})
@XmlRootElement
public class MobilePhoneNumberReplyMsg {

    /**
     * Message root
     * <RtrIdVrfctn>  [1..1]
     */
    @XmlElement(name = "RtrIdVrfctn", required = true)
    protected RtrIdVrfctnV1 rtrIdVrfctn;

    /**
     * 获取rtrIdVrfctn属性的值。
     *
     * @return
     *     possible object is
     *     {@link RtrIdVrfctnV1 }
     *
     */
    public RtrIdVrfctnV1 getRtrIdVrfctn() {
        return rtrIdVrfctn;
    }

    /**
     * 设置rtrIdVrfctn属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link RtrIdVrfctnV1 }
     *
     */
    public void setRtrIdVrfctn(RtrIdVrfctnV1 value) {
        this.rtrIdVrfctn = value;
    }

}

