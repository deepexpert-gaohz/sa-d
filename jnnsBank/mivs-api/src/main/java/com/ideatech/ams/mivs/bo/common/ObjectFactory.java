package com.ideatech.ams.mivs.bo.common;

import javax.xml.bind.JAXBElement;

/**
 * @author jzh
 * @date 2019/7/12.
 */
public interface ObjectFactory<T> {

    JAXBElement<T> createDocument(T t);

    T createDocument();
}
