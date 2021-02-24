package com.ideatech.ams.image.dto;

import java.io.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XmlUtils {
	private static Logger logger = LoggerFactory.getLogger(XmlUtils.class);
	public static String getXml(Object object) throws UnsupportedEncodingException{
		
		StringBuilder sb = new StringBuilder();
		try {
			//将类转换成XML格式的文件
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, false);
			marshaller.setProperty(marshaller.JAXB_ENCODING, "UTF-8");
			StringWriter writer = new StringWriter();
			marshaller.marshal(object, writer);
			sb.append(writer.toString());
			logger.info(sb.toString());
			logger.info("报文bean转化xml完成");
		} catch (JAXBException e) {
			logger.error("报文bean转化xml异常"+e.getMessage());
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static XmlRoot dealXml(String requestXml){
		String xmlString =requestXml.substring(10,requestXml.length());
		XmlRoot	object = null;
//		try {
//			JAXBContext context = JAXBContext.newInstance(XmlRoot.class);
//			Unmarshaller unmarshaller = context.createUnmarshaller();
//			object= (XmlRoot) unmarshaller.unmarshal(new StringReader(xmlString));
//			logger.info("报文xml转化bean完成");
//		} catch (JAXBException e) {
//			logger.error("报文xml转化bean异常"+e.getMessage());
//			e.printStackTrace();
//		}
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			//Do unmarshall operation
			Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(new StringReader(xmlString)));
			JAXBContext jc = JAXBContext.newInstance(XmlRoot.class);
			Unmarshaller um = jc.createUnmarshaller();
			object= (XmlRoot) um.unmarshal(xmlSource);
			logger.info("报文xml转化bean完成");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			logger.error("报文xml转化bean异常"+e.getMessage());
			e.printStackTrace();
		}
		return object;
	}

}
