package com.ideatech.ams.mivs.bo.mfm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:16 PM CST
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
 * generated in the cn.com.bron7 package.
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
public class MobilePhoneNumberFeedbackFactory implements ObjectFactory<MobilePhoneNumberFeedbackMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron7
     *
     */
    public MobilePhoneNumberFeedbackFactory() {
    }

    /**
     * Create an instance of {@link MobilePhoneNumberFeedbackMsg }
     *
     */
    public MobilePhoneNumberFeedbackMsg createDocument() {
        return new MobilePhoneNumberFeedbackMsg();
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
     * Create an instance of {@link OriginalVerificationInfomation1 }
     *
     */
    public OriginalVerificationInfomation1 createOriginalVerificationInfomation1() {
        return new OriginalVerificationInfomation1();
    }

    /**
     * Create an instance of {@link IdentityVerificationFeedback1 }
     *
     */
    public IdentityVerificationFeedback1 createIdentityVerificationFeedback1() {
        return new IdentityVerificationFeedback1();
    }

    /**
     * Create an instance of {@link Feedback1 }
     *
     */
    public Feedback1 createFeedback1() {
        return new Feedback1();
    }

    /**
     * Create an instance of {@link MessageHeader1 }
     *
     */
    public MessageHeader1 createMessageHeader1() {
        return new MessageHeader1();
    }

    /**
     * Create an instance of {@link OriginalVerification1 }
     *
     */
    public OriginalVerification1 createOriginalVerification1() {
        return new OriginalVerification1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MobilePhoneNumberFeedbackMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.347.001.01", name = "Document")
    public JAXBElement<MobilePhoneNumberFeedbackMsg> createDocument(MobilePhoneNumberFeedbackMsg value) {
        return new JAXBElement<MobilePhoneNumberFeedbackMsg>(_Document_QNAME, MobilePhoneNumberFeedbackMsg.class, null, value);
    }

}


