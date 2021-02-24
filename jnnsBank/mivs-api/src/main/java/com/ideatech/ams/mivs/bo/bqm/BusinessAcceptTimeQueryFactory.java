package com.ideatech.ams.mivs.bo.bqm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:17 PM CST
//


import com.ideatech.ams.mivs.bo.common.InstructedParty1;
import com.ideatech.ams.mivs.bo.common.InstructingParty1;
import com.ideatech.ams.mivs.bo.common.MessageHeader1;
import com.ideatech.ams.mivs.bo.common.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron5 package.
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
public class BusinessAcceptTimeQueryFactory implements ObjectFactory<BusinessAcceptTimeQueryMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.345.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron5
     *
     */
    public BusinessAcceptTimeQueryFactory() {
    }

    /**
     * Create an instance of {@link BusinessAcceptTimeQueryMsg }
     *
     */
    public BusinessAcceptTimeQueryMsg createDocument() {
        return new BusinessAcceptTimeQueryMsg();
    }

    /**
     * Create an instance of {@link QueryInformation1 }
     *
     */
    public QueryInformation1 createQueryInformation1() {
        return new QueryInformation1();
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
     * Create an instance of {@link GetSystemStatusV1 }
     *
     */
    public GetSystemStatusV1 createGetSystemStatusV1() {
        return new GetSystemStatusV1();
    }

    /**
     * Create an instance of {@link MessageHeader1 }
     *
     */
    public MessageHeader1 createMessageHeader1() {
        return new MessageHeader1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessAcceptTimeQueryMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.345.001.01", name = "Document")
    public JAXBElement<BusinessAcceptTimeQueryMsg> createDocument(BusinessAcceptTimeQueryMsg value) {
        return new JAXBElement<BusinessAcceptTimeQueryMsg>(_Document_QNAME, BusinessAcceptTimeQueryMsg.class, null, value);
    }

}

