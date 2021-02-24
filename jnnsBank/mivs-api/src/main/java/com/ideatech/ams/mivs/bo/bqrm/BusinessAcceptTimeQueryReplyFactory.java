package com.ideatech.ams.mivs.bo.bqrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:05:29 PM CST
//


import com.ideatech.ams.mivs.bo.common.InstructingParty1;
import com.ideatech.ams.mivs.bo.common.MessageHeader2;
import com.ideatech.ams.mivs.bo.common.ObjectFactory;
import com.ideatech.ams.mivs.bo.common.ServiceInformation1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron6 package.
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
public class BusinessAcceptTimeQueryReplyFactory implements ObjectFactory<BusinessAcceptTimeQueryReplyMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron6
     *
     */
    public BusinessAcceptTimeQueryReplyFactory() {
    }

    /**
     * Create an instance of {@link BusinessAcceptTimeQueryReplyMsg }
     *
     */
    public BusinessAcceptTimeQueryReplyMsg createDocument() {
        return new BusinessAcceptTimeQueryReplyMsg();
    }

    /**
     * Create an instance of {@link InstructingParty1 }
     *
     */
    public InstructingParty1 createInstructingParty1() {
        return new InstructingParty1();
    }

    /**
     * Create an instance of {@link OriginalQueryInformation1 }
     *
     */
    public OriginalQueryInformation1 createOriginalQueryInformation1() {
        return new OriginalQueryInformation1();
    }

    /**
     * Create an instance of {@link ReturnSystemStatusV1 }
     *
     */
    public ReturnSystemStatusV1 createReturnSystemStatusV1() {
        return new ReturnSystemStatusV1();
    }

    /**
     * Create an instance of {@link MessageHeader2 }
     *
     */
    public MessageHeader2 createMessageHeader2() {
        return new MessageHeader2();
    }

    /**
     * Create an instance of {@link ReplyInformation1 }
     *
     */
    public ReplyInformation1 createReplyInformation1() {
        return new ReplyInformation1();
    }

    /**
     * Create an instance of {@link ServiceInformation1 }
     *
     */
    public ServiceInformation1 createServiceInformation1() {
        return new ServiceInformation1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessAcceptTimeQueryReplyMsg }{@code >}}
     *
     */

    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.346.001.01", name = "Document")
    public JAXBElement<BusinessAcceptTimeQueryReplyMsg> createDocument(BusinessAcceptTimeQueryReplyMsg value) {
        return new JAXBElement<BusinessAcceptTimeQueryReplyMsg>(_Document_QNAME, BusinessAcceptTimeQueryReplyMsg.class, null, value);
    }

}

