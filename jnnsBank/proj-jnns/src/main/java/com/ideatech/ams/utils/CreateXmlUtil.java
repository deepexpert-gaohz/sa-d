package com.ideatech.ams.utils;

import com.ideatech.ams.domain.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @version V1.0
 * @author: sunhui
 * @date: 2019/12/25 14:26
 * @description:
 */

@Slf4j
public class CreateXmlUtil {

    private CreateXmlUtil(){};

    /**
     * 三个参数的构造，用于生每个子文件的XML
     *
     * @param path
     * @param file
     * @param accountInfo
     */
    public static void createXml(String path, File file, AccountInfo accountInfo) {
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点items
            Element items = document.addElement("items");
            // 3、向items节点添加version属性
            //items.addAttribute("version", "1.0");
            // 4、生成子节点及子节点内容,需要判断flag
            addElementAttribute(items, accountInfo);
            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            // 6、生成xml文件
            File fileXml = new File(file.getName() + ".xml");
            //保存到对应的文件夹路径下
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(path + File.separator + fileXml)), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            log.info("生成" + file.getName() + ".xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成" + file.getName() + ".xml失败");
        }
    }

    /**
     * 两个参数的构造，用于生成仅有根节点的父XML文件
     *
     * @param path
     * @param file
     */
    public static void createXml(String path, File file) {
        try {
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            // 创建根节点items
            Element items = document.addElement("items");
            // 设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            //设置编码格式
            format.setEncoding("UTF-8");
            // 生成xml文件
            File fileXml = new File(file.getName());
            //保存到对应的文件夹路径下
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(path + File.separator + fileXml)), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            log.info("生成" + file.getName() + "成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成" + file.getName() + "失败");
        }
    }

    /**
     * 设置XML子节点属性
     *
     * @param items
     * @param accountInfo
     */
    private static void addElementAttribute(Element items, AccountInfo accountInfo) {
        Element item = items.addElement("item");
        item.addAttribute("imgDir", accountInfo.getImgDir());
        item.addAttribute("accKind", accountInfo.getAccKind().toString());
        item.addAttribute("bizKind", accountInfo.getBizKind().toString());
        item.addAttribute("depKind", accountInfo.getDepKind());
        item.addAttribute("accNo", accountInfo.getAccNo());
        item.addAttribute("accName", accountInfo.getAccName());
        item.addAttribute("depName", accountInfo.getDepName());
        item.addAttribute("creditCode", accountInfo.getCreditCode());
        item.addAttribute("accBankCode", accountInfo.getAccBankCode());
        item.addAttribute("agent", accountInfo.getAgent());
        item.addAttribute("agentIdno", accountInfo.getAgentIdno());
        item.addAttribute("agentTel", accountInfo.getAgentTel());
    }


    /**
     * 合并XML
     *
     * @param newPath
     * @throws DocumentException
     */
    public static void mergeXml(String newPath)  {
        try {
            File file = new File(newPath + File.separator + "accounts.xml");
            createXml(newPath, file);
            SAXReader saxReader = new SAXReader();
            Document rootDocument = saxReader.read(file);
            Element parent = (Element) rootDocument.getRootElement();
            File file1 = new File(newPath);
            File[] files = file1.listFiles();
            for (File fileXml : files) {
                if (fileXml.getName().endsWith(".xml")) {
                    Document document = saxReader.read(fileXml);
                    List<Element> elements = document.getDocument().getRootElement().elements();
                    for (Element element : elements) {
                        parent.add(element.detach());
                        fileXml.delete();
                    }
                }
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(newPath + File.separator + file.getName())), format);
            writer.write(rootDocument);
            writer.close();
            log.info("合并XML成功");
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("document异常，合并XML失败");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("io异常，合并XML失败");
        }
    }

}
