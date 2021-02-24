package com.ideatech.ams.mivs.bo.tfm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:07:27 PM CST
//


import com.ideatech.ams.mivs.bo.common.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron8 package.
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
public class TaxInformationFeedbackFactory implements ObjectFactory<TaxInformationFeedbackMsg> {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron8
     *
     */
    public TaxInformationFeedbackFactory() {
    }

    /**
     * Create an instance of {@link TaxInformationFeedbackMsg }
     *
     */
    public TaxInformationFeedbackMsg createDocument() {
        return new TaxInformationFeedbackMsg();
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
     * Create an instance of {@link TaxPaymentVerificationFeedback1 }
     *
     */
    public TaxPaymentVerificationFeedback1 createTaxPaymentVerificationFeedback1() {
        return new TaxPaymentVerificationFeedback1();
    }

    /**
     * Create an instance of {@link OriginalVerificationInfomation1 }
     *
     */
    public OriginalVerificationInfomation1 createOriginalVerificationInfomation1() {
        return new OriginalVerificationInfomation1();
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
     * Create an instance of {@link TaxPaymentInformation1 }
     *
     */
    public TaxPaymentInformation1 createTaxPaymentInformation1() {
        return new TaxPaymentInformation1();
    }

    /**
     * Create an instance of {@link OriginalVerification1 }
     *
     */
    public OriginalVerification1 createOriginalVerification1() {
        return new OriginalVerification1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TaxInformationFeedbackMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:mivs:2010:tech:xsd:mivs.348.001.01", name = "Document")
    public JAXBElement<TaxInformationFeedbackMsg> createDocument(TaxInformationFeedbackMsg value) {
        return new JAXBElement<TaxInformationFeedbackMsg>(_Document_QNAME, TaxInformationFeedbackMsg.class, null, value);
    }

}

