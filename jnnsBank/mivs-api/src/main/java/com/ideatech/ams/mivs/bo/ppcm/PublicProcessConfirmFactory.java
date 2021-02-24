package com.ideatech.ams.mivs.bo.ppcm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:08:32 PM CST
//


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.com.bron0 package.
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
public class PublicProcessConfirmFactory {

    private final static QName _Document_QNAME = new QName("urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02", "Document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.bron0
     *
     */
    public PublicProcessConfirmFactory() {
    }

    /**
     * Create an instance of {@link PublicProcessConfirmMsg }
     *
     */
    public PublicProcessConfirmMsg createDocument() {
        return new PublicProcessConfirmMsg();
    }

    /**
     * Create an instance of {@link InstgPty1 }
     *
     */
    public InstgPty1 createInstgPty1() {
        return new InstgPty1();
    }

    /**
     * Create an instance of {@link CmonConfInf1 }
     *
     */
    public CmonConfInf1 createCmonConfInf1() {
        return new CmonConfInf1();
    }

    /**
     * Create an instance of {@link InstdPty1 }
     *
     */
    public InstdPty1 createInstdPty1() {
        return new InstdPty1();
    }

    /**
     * Create an instance of {@link OrgnlGrpHdr1 }
     *
     */
    public OrgnlGrpHdr1 createOrgnlGrpHdr1() {
        return new OrgnlGrpHdr1();
    }

    /**
     * Create an instance of {@link GroupHeader1 }
     *
     */
    public GroupHeader1 createGroupHeader1() {
        return new GroupHeader1();
    }

    /**
     * Create an instance of {@link CmonConfV01 }
     *
     */
    public CmonConfV01 createCmonConfV01() {
        return new CmonConfV01();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PublicProcessConfirmMsg }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:cnaps:std:ccms:2010:tech:xsd:ccms.900.001.02", name = "Document")
    public JAXBElement<PublicProcessConfirmMsg> createDocument(PublicProcessConfirmMsg value) {
        return new JAXBElement<PublicProcessConfirmMsg>(_Document_QNAME, PublicProcessConfirmMsg.class, null, value);
    }

}

