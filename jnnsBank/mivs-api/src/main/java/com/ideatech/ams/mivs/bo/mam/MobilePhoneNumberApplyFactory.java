package com.ideatech.ams.mivs.bo.mam;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:03:23 PM CST
//


import com.ideatech.ams.mivs.bo.common.InstructedParty1;
import com.ideatech.ams.mivs.bo.common.InstructingParty2;
import com.ideatech.ams.mivs.bo.common.MessageHeader3;
import com.ideatech.ams.mivs.bo.common.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron1 package.
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
public class MobilePhoneNumberApplyFactory implements ObjectFactory<MobilePhoneNumberApplyMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron1
     *
     */
    public MobilePhoneNumberApplyFactory() {
    }

    /**
     * Create an instance of {@link MobilePhoneNumberApplyMsg }
     *
     */
    public MobilePhoneNumberApplyMsg createDocument() {
        return new MobilePhoneNumberApplyMsg();
    }

    /**
     * Create an instance of {@link InstructedParty1 }
     *
     */
    public InstructedParty1 createInstructedParty1() {
        return new InstructedParty1();
    }

    /**
     * Create an instance of {@link InstructingParty2 }
     *
     */
    public InstructingParty2 createInstructingParty2() {
        return new InstructingParty2();
    }

    /**
     * Create an instance of {@link VerificationDefinition1 }
     *
     */
    public VerificationDefinition1 createVerificationDefinition1() {
        return new VerificationDefinition1();
    }

    /**
     * Create an instance of {@link MessageHeader3 }
     *
     */
    public MessageHeader3 createMessageHeader3() {
        return new MessageHeader3();
    }

    /**
     * Create an instance of {@link GetIdentityVerificationV1 }
     *
     */
    public GetIdentityVerificationV1 createGetIdentityVerificationV1() {
        return new GetIdentityVerificationV1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MobilePhoneNumberApplyMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.320.001.01", name = "Document")
    public JAXBElement<MobilePhoneNumberApplyMsg> createDocument(MobilePhoneNumberApplyMsg value) {
        return new JAXBElement<MobilePhoneNumberApplyMsg>(_Document_QNAME, MobilePhoneNumberApplyMsg.class, null, value);
    }

}

