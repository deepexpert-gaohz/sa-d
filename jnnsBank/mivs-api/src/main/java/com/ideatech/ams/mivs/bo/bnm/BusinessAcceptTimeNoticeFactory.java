package com.ideatech.ams.mivs.bo.bnm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:40 PM CST
//


import com.ideatech.ams.mivs.bo.common.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron9 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class BusinessAcceptTimeNoticeFactory implements ObjectFactory<BusinessAcceptTimeNoticeMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron9
     *
     */
    public BusinessAcceptTimeNoticeFactory() {
    }

    /**
     * Create an instance of {@link BusinessAcceptTimeNoticeMsg }
     *
     */
    public BusinessAcceptTimeNoticeMsg createDocument() {
        return new BusinessAcceptTimeNoticeMsg();
    }

    /**
     * Create an instance of {@link SystemStatusNotificationV1 }
     *
     */
    public SystemStatusNotificationV1 createSystemStatusNotificationV1() {
        return new SystemStatusNotificationV1();
    }

    /**
     * Create an instance of {@link InstructedParty1 }
     *
     */
    public InstructedParty1 createInstructedParty1() {
        return new InstructedParty1();
    }

    /**
     * Create an instance of {@link InstructingParty1 }
     *
     */
    public InstructingParty1 createInstructingParty1() {
        return new InstructingParty1();
    }

    /**
     * Create an instance of {@link MessageHeader1 }
     *
     */
    public MessageHeader1 createMessageHeader1() {
        return new MessageHeader1();
    }

    /**
     * Create an instance of {@link SystemStatusInformation1 }
     *
     */
    public SystemStatusInformation1 createSystemStatusInformation1() {
        return new SystemStatusInformation1();
    }

    /**
     * Create an instance of {@link ServiceInformation1 }
     *
     */
    public ServiceInformation1 createServiceInformation1() {
        return new ServiceInformation1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessAcceptTimeNoticeMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.801.001.01", name = "Document")
    public JAXBElement<BusinessAcceptTimeNoticeMsg> createDocument(BusinessAcceptTimeNoticeMsg value) {
        return new JAXBElement<BusinessAcceptTimeNoticeMsg>(_Document_QNAME, BusinessAcceptTimeNoticeMsg.class, null, value);
    }

}

