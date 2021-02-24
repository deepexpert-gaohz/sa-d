package com.ideatech.ams.mivs.bo.mrm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:57 PM CST
//


import com.ideatech.ams.mivs.bo.common.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron2 package.
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
public class MobilePhoneNumberReplyFactory implements ObjectFactory<MobilePhoneNumberReplyMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron2
     *
     */
    public MobilePhoneNumberReplyFactory() {
    }

    /**
     * Create an instance of {@link MobilePhoneNumberReplyMsg }
     *
     */
    public MobilePhoneNumberReplyMsg createDocument() {
        return new MobilePhoneNumberReplyMsg();
    }

    /**
     * Create an instance of {@link RtrIdVrfctnV1 }
     *
     */
    public RtrIdVrfctnV1 createRtrIdVrfctnV1() {
        return new RtrIdVrfctnV1();
    }

    /**
     * Create an instance of {@link InstructingParty1 }
     *
     */
    public InstructingParty1 createInstructingParty1() {
        return new InstructingParty1();
    }

    /**
     * Create an instance of {@link OrgnlBizQry1 }
     *
     */
    public OrgnlBizQry1 createOrgnlBizQry1() {
        return new OrgnlBizQry1();
    }

    /**
     * Create an instance of {@link OperationalError1 }
     *
     */
    public OperationalError1 createOperationalError1() {
        return new OperationalError1();
    }

    /**
     * Create an instance of {@link Response1 }
     *
     */
    public Response1 createResponse1() {
        return new Response1();
    }

    /**
     * Create an instance of {@link VerificationInformation1 }
     *
     */
    public VerificationInformation1 createVerificationInformation1() {
        return new VerificationInformation1();
    }

    /**
     * Create an instance of {@link MessageHeader2 }
     *
     */
    public MessageHeader2 createMessageHeader2() {
        return new MessageHeader2();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MobilePhoneNumberReplyMsg }{@code >}}
     *
     */
//    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.321.001.01", name = "Document")
    public JAXBElement<MobilePhoneNumberReplyMsg> createDocument(MobilePhoneNumberReplyMsg value) {
        return new JAXBElement<MobilePhoneNumberReplyMsg>(_Document_QNAME, MobilePhoneNumberReplyMsg.class, null, value);
    }

}

