package com.ideatech.ams.mivs.bo.ppcm;

//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.07.10 时间 05:08:32 PM CST
//


import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SystemCode1的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="SystemCode1">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SAPS"/>
 *     &lt;enumeration value="HVPS"/>
 *     &lt;enumeration value="BEPS"/>
 *     &lt;enumeration value="CCMS"/>
 *     &lt;enumeration value="PMIS"/>
 *     &lt;enumeration value="IBPS"/>
 *     &lt;enumeration value="NCIS"/>
 *     &lt;enumeration value="ECDS"/>
 *     &lt;enumeration value="FXPS"/>
 *     &lt;enumeration value="NETS"/>
 *     &lt;enumeration value="PBCS"/>
 *     &lt;enumeration value="MIVS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "SystemCode1")
@XmlEnum
public enum SystemCode1 {

    SAPS,
    HVPS,
    BEPS,
    CCMS,
    PMIS,
    IBPS,
    NCIS,
    ECDS,
    FXPS,
    NETS,
    PBCS,
    MIVS;

    public String value() {
        return name();
    }

    public static SystemCode1 fromValue(String v) {
        return valueOf(v);
    }

}

