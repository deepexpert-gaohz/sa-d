package com.ideatech.ams.mivs.util;

import com.ideatech.ams.mivs.bo.common.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author jzh
 * @date 2019/7/12.
 */

@Slf4j
public class XMLUtil {


    /**
     *
     * @param rootElement 根节点对象
     * @param objectFactory 对应的objectFactory
     * @return
     * @throws Exception
     */
    public static String toXML(Object rootElement, ObjectFactory objectFactory){

        StringWriter out = null;
        XMLWriter writer = null;
        String xml = null;
        try {
            // 获取JAXB的上下文环境，需要传入具体的 Java bean
            JAXBContext context = JAXBContext.newInstance(rootElement.getClass());
            // 创建 Marshaller 实例
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");//设置编码
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//格式化输出
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);//

            out = new StringWriter();
            OutputFormat format = new OutputFormat();
            format.setIndent(true);
            format.setNewlines(true);
            format.setNewLineAfterDeclaration(false);
            writer = new XMLWriter(out, format);

            //前缀过滤
            XMLFilterImpl nsfFilter = new XMLFilterImpl() {

                @Override
                public void startDocument() throws SAXException {
                    super.startDocument();
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

                    super.startElement(uri, localName, localName, atts);
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {

                    super.endElement(uri, localName, localName);
                }

                @Override
                public void startPrefixMapping(String prefix, String url) throws SAXException {
                    super.startPrefixMapping("",url);
                }
            };

            nsfFilter.setContentHandler(writer);

            // 将所需对象序列化 -> 该方法没有返回值
            marshaller.marshal(objectFactory.createDocument(rootElement), nsfFilter);

            xml = out.toString();

        } catch (PropertyException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return xml;
        }
    }

    /**
     * 根据Schema文件校验xml
     * @param sourceXML 被校验XML文件
     * @param xsdFilePath xsd文件(Schema文件)路径
     * @return
     */
    public static boolean check(File sourceXML,String xsdFilePath){
        File file = new File(xsdFilePath);
        return check(sourceXML,file);
    }

    /**
     *
     * @param sourceXML 被校验XML文件
     * @param xsdFile xsd文件(Schema文件)
     * @return
     */
    public static boolean check(File sourceXML,File xsdFile){
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            log.info("成功获取schema和validator");
            validator.validate(new StreamSource(sourceXML));
            log.info("验证成功");
            return true;
        }catch (IOException | SAXException e){
            log.warn(e.getMessage());
            return false;
        }
    }
}
