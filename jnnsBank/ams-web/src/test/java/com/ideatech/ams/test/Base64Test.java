package com.ideatech.ams.test;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
@Slf4j
public class Base64Test extends TestCase {

    public static void main(String[] args) {
        try {
            String a = "H4sIAAAAAAAAAO2a71PTSBjHX8tfEdfXQDa726ZOWwdQdBx/3Iw698JxmNhsIWeaxHTVeq/gxd3hIaLDcXc4nMg4/niB6I2MaEH9Z5q0vOJfcLeBa2gBgUFuAsmLTfr0SfbZ/X6eTfJM0idKBVO6Td2iYVsZADtkIFErZ+uG1Z8BVy73tqvgRLYt/ZNWdKj7g2tYTLK0As0A08j1udSxXdbn3NGB5Gj99EdDZwMZQFIk+H2GGv0DLANUrIBs25G0sPH9kTSjJSZ6ZUZOM7tMo98qUIs7njd03aRAEv+vnawoHQhjghGQTMOilxwtx4Pr1XLMdkXECoYogXiPJtVE2Bfz+SLl57UT8ZesqKJr3mcQ7CmTiq6kEr8w4oO9y/cy398JQkcKPx5Y7TnBjx0x5CK7a/Ihd507BzqDi123SxKznW7b1SmP4oJtiajXDD22KWI7Jtc3EVmerXdtWJp9r9uM2YX13mFbs78rYl3vHjI1ea9Gn7f5DIjmQl3KyuIXb26k73S3gqACpKLxMzdCdc1biNHDnfm0ZdNHr/ac7LrcddX/57W38KQ6N1j9dbLyZcYfelN9+cj/fdCfurc8+dD7ZdYb+3DtWjbdGT5bSF83bAlBd320+w1BMgQBTjYgQCiGYDMIEJKhjGWYkGWoIKKoEO9S8/8l8evJvlHikzjxv5H4y+PvazP3Kx9H/H+nvfILb+g3737Z/+MNN0Yp61GSxFm/UwKqD0b8yXF/YixKSvOLx0rvVOmzCEG+widURFQZRkFuhAO5E6Glncix2ttRW5FhKgoaY6VVYxRrvC2NZSUSCquxwrvO4sR2FBZNSN0e3lIXbPfJHOEOVUmmkjwm12Yaq9cOVuevAUIGXOKtSTelA6kkobTSoXZAgiHGG9JRsHU+Bxcd7eYtfuFSwAVHBYYXfEJCrKiAz6hLc81yarkba7be+nY4mFpefFT5NN7EFE9FySh226aeAcwVM+vo+d7/zjlDzdtUgNEuXLbEz5su+1Oz3uu//dnPtfkX/vBCUB3YBZRnbxWZkTeoviGXmy1IqorV1BbIyTi18wUpsVokgA3I+GPR4VuQ/OcLrfDAPYMnAMZ7OFp9+fb4wSAmVFYSS1pMzHchZmyh+nToYBATLkrFxOw9Md7SICcmqF8dEGLCa0y4jKmQmJg9IMZ/N+HNPa4sPqvNvKqUyytLj6ONjaiECWxSjYoYJDE2e43NytKwNzpR+TRam5+uzT+rk3Mv2uQQ0lJLhclD+F7+vW9RLe9Q1aU/+TNOxG9XKBVXdfaDno/z0eYEk5iT/XismRqONicExpzsByd/Pd+Sk3Rn8FlXujP0kVi27St40pFRWiYAAA==";

            String b = "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";

            String e = "H4sIAAAAAAAAAO2a71PTSBjHX8tfEdfXQDa726ZOWwdQdBx/3Iw698JxmNhsIWeaxHTVeq/gxd3hIaLDcXc4nMg4/niB6I2MaEH9Z5q0vOJfcLeBa2gBgUFuAsmLTfr0SfbZ/X6eTfJM0idKBVO6Td2iYVsZADtkIFErZ+uG1Z8BVy73tqvgRLYt/ZNWdKj7g2tYTLK0As0A08j1udSxXdbn3NGB5Gj99EdDZwMZQFIk+H2GGv0DLANUrIBs25G0sPH9kTSjJSZ6ZUZOM7tMo98qUIs7njd03aRAEv+vnawoHQhjghGQTMOilxwtx4Pr1XLMdkXECoYogXiPJtVE2Bfz+SLl57UT8ZesqKJr3mcQ7CmTiq6kEr8w4oO9y/cy398JQkcKPx5Y7TnBjx0x5CK7a/Ihd507BzqDi123SxKznW7b1SmP4oJtiajXDD22KWI7Jtc3EVmerXdtWJp9r9uM2YX13mFbs78rYl3vHjI1ea9Gn7f5DIjmQl3KyuIXb26k73S3gqACpKLxMzdCdc1biNHDnfm0ZdNHr/ac7LrcddX/57W38KQ6N1j9dbLyZcYfelN9+cj/fdCfurc8+dD7ZdYb+3DtWjbdGT5bSF83bAlBd320+w1BMgQBTjYgQCiGYDMIEJKhjGWYkGWoIKKoEO9S8/8l8evJvlHikzjxv5H4y+PvazP3Kx9H/H+nvfILb+g3737Z/+MNN0Yp61GSxFm/UwKqD0b8yXF/YixKSvOLx0rvVOmzCEG+widURFQZRkFuhAO5E6Glncix2ttRW5FhKgoaY6VVYxRrvC2NZSUSCquxwrvO4sR2FBZNSN0e3lIXbPfJHOEOVUmmkjwm12Yaq9cOVuevAUIGXOKtSTelA6kkobTSoXZAgiHGG9JRsHU+Bxcd7eYtfuFSwAVHBYYXfEJCrKiAz6hLc81yarkba7be+nY4mFpefFT5NN7EFE9FySh226aeAcwVM+vo+d7/zjlDzdtUgNEuXLbEz5su+1Oz3uu//dnPtfkX/vBCUB3YBZRnbxWZkTeoviGXmy1IqorV1BbIyTi18wUpsVokgA3I+GPR4VuQ/OcLrfDAPYMnAMZ7OFp9+fb4wSAmVFYSS1pMzHchZmyh+nToYBATLkrFxOw9Md7SICcmqF8dEGLCa0y4jKmQmJg9IMZ/N+HNPa4sPqvNvKqUyytLj6ONjaiECWxSjYoYJDE2e43NytKwNzpR+TRam5+uzT+rk3Mv2uQQ0lJLhclD+F7+vW9RLe9Q1aU/+TNOxG9XKBVXdfaDno/z0eYEk5iT/XismRqONicExpzsByd/Pd+Sk3Rn8FlXujP0kVi27St40pFRWiYAAA==";

            String c = "H4sIAAAAAAAAAO2W0U/TQBzHn+GvqOczW9fettZ0IxuKxCCaiPGBEDLW26jp2qUrMHwaBhAEAmaKYkDECDzINmKMQ0D3x7hrx5P/gnersA0FJcHECH3oul+/v7vv3feTtlJrOqEyQ8hIKboWAB4XCxikRXVZ0eIBcLe7vUUArcFm6X4klUTGbUPRTEaLJFAAqEq0z0BJ3TD7ksMyYJKROLqnyOZAAHhFr/O/AynxATMABMiBYHOTRGvkt0kyUdqks5pKNKKGVCWuJZBGhGHdNPUEYOj9g2aOc/EQeiEPGFXR0J1kJErMtUeipm5Qxxz08D6ezKiiCLV9KxZLIdLX4qW3WE6gU5M5HbPXVESnYtKk1ecBzEgAQAEww45zXiDjDBy49pNV0BWnzBGVrDjU2Qnczlj9epox9WRYN2RETHTpGgK1QpuuUmuX2epBjcXMRmmtclTbX92BRnV97ajeoF4b5XWlI+of7mM62QB66qomWd4t4fx03/Uwx3s4wKSUB6ToEQ7UNIs2Iia7FpQu9bRdDXWHeqzlHC6+svMZe2KxXFq1Rgv2xhPrccZamtpfnMfjm3huu7c3KLnru2ny1cI/xIAoVhnweOsggP4aBD54AcFxEAhe3isKfxIzPdVFfGMwZSoxBZHHxikAEAQoiMcCwAssFE8PgJd18me5w/w9rK+WP8+fj/yttWL5c/bn/BklFdZVOQBMY5AMm5Rj7Yc9HUgdQjS9Fio5ERWce2Ftfinv7OD5WXtj68r/AY3I1qCB8AKavwWNtbJWKbzBhQn79ejZo3NTkWUVNaLDQ5fA8T7/8eT4We4X7xvoYv1Q5Fj/b9DhhNr7Bop16Ph95xkdeFboMOSoFIp4fQ0vjJVLy3Z+9dveTO0pNFfAj3JW7q3DFJ7cwis71tKmI6h8WLcmi/bLMbyXIRf72Y+V1ZlyKW89/eTorckFayqLZ7dw8T3R2O+mHY29m7Xzz512a3sczz77mnl4Iq+S2/kmltx1X9jB5u8+5AFmlwsAAA==";

            String html ="<html>\n" +
                    "\n" +
                    "<object id= \"webPrint\" classid=\"clsid:A705A992-4752-4A85-8506-4A1CB02C68C7\"   WIDTH=\"0\" HEIGHT=\"0\" ALIGN=center HSPACE=0 VSPACE=0></object>\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\n" +
                    "<title>Web Report Print</title>\n" +
                    "</head>\n" +
                    "<script  language=\"javascript\" > \n" +
                    "\tfunction detectPlugin()\n" +
                    "\t{\n" +
                    "   \t try\n" +
                    "   \t {\n" +
                    "   \t   webPrint.SetXML(\"\");\n" +
                    "   \t   return true;\n" +
                    "\t\t}\n" +
                    "    catch(e)\n" +
                    "    {\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "\t}\n" +
                    " \n" +
                    "\tfunction btclick(){\n" +
                    "\n" +
                    "\t   \n" +
                    "\t\t\twebPrint.SetXML(\"H4sIAAAAAAAAAO2W0U/TQBzHn+GvqOczW9fettZ0IxuKxCCaiPGBEDLW26jp2qUrMHwaBhAEAmaKYkDECDzINmKMQ0D3x7hrx5P/gnersA0FJcHECH3oul+/v7vv3feTtlJrOqEyQ8hIKboWAB4XCxikRXVZ0eIBcLe7vUUArcFm6X4klUTGbUPRTEaLJFAAqEq0z0BJ3TD7ksMyYJKROLqnyOZAAHhFr/O/AynxATMABMiBYHOTRGvkt0kyUdqks5pKNKKGVCWuJZBGhGHdNPUEYOj9g2aOc/EQeiEPGFXR0J1kJErMtUeipm5Qxxz08D6ezKiiCLV9KxZLIdLX4qW3WE6gU5M5HbPXVESnYtKk1ecBzEgAQAEww45zXiDjDBy49pNV0BWnzBGVrDjU2Qnczlj9epox9WRYN2RETHTpGgK1QpuuUmuX2epBjcXMRmmtclTbX92BRnV97ajeoF4b5XWlI+of7mM62QB66qomWd4t4fx03/Uwx3s4wKSUB6ToEQ7UNIs2Iia7FpQu9bRdDXWHeqzlHC6+svMZe2KxXFq1Rgv2xhPrccZamtpfnMfjm3huu7c3KLnru2ny1cI/xIAoVhnweOsggP4aBD54AcFxEAhe3isKfxIzPdVFfGMwZSoxBZHHxikAEAQoiMcCwAssFE8PgJd18me5w/w9rK+WP8+fj/yttWL5c/bn/BklFdZVOQBMY5AMm5Rj7Yc9HUgdQjS9Fio5ERWce2Ftfinv7OD5WXtj68r/AY3I1qCB8AKavwWNtbJWKbzBhQn79ejZo3NTkWUVNaLDQ5fA8T7/8eT4We4X7xvoYv1Q5Fj/b9DhhNr7Bop16Ph95xkdeFboMOSoFIp4fQ0vjJVLy3Z+9dveTO0pNFfAj3JW7q3DFJ7cwis71tKmI6h8WLcmi/bLMbyXIRf72Y+V1ZlyKW89/eTorckFayqLZ7dw8T3R2O+mHY29m7Xzz512a3sczz77mnl4Iq+S2/kmltx1X9jB5u8+5AFmlwsAAA==\");\n" +
                    "\t\t\twebPrint.SetXLS(\"H4sIAAAAAAAAAO1YXWwVRRQ+szv7B225LVjaAvVyy19/qBXElhLphcSYGKXQWsG0zYVrS2la21oasTbqRSGRVBISicbElz744JP6olGJtj4YE4l/PJiYaFoSnoxa/IMY7PXM2d1zd2+ptIlgVGYys2e+OfPNmd+zu59/lj859mbJFGSFbaDDTNoBM4CJoEIEwPawmXQ6rSAHU/pm+FeFJujHOARRuBv68DkIw9lb4S9DIRjC5xLXVud9FM915b3Y+yD0QJLs6FlQ3yoUgCaC45lvu1jugru6atAgvOf/HtYb179j40E2DTgXOWvtwLI671PI+oacIO7zmNpgACTAYH//UPRGhZ1kwwGhbLiL8lcQyYNisqqA8qWUv061Z1xNrDkk8kRb/ftldTQ+Afu0OOmdpDxGeR7mAt6mNl8TcjuUwMdqLp8+5W1jQ+zAndkNB6D3utbGqNYGv9YRh7GmDw5j7CS9g6R1SIS10rtGWxNwD+yETbAZNTYFtM5ovtaR7ubG2VpF19QqlSvBgOU4YWtiZVVlZTWJ8m1tG/xCW3mpXI17ZWWovrWps6M9rBQDC1ZllKprwjxYVlrr0ZhYtlYWm6daCxVoHSQUXhH1e/aKbRu8jn0gtjFGtfEENq2GcozYNNgu3CirRQNUQX24M9e2cH9kWqDLhoYMxZ1QifOZ6dRvP6txVstJWEZr82s6Gjit41GFCx//ZX64tkAc/oe4QA+YCf58Vrr6l7Px8jnwqjnwijlw56p40B5b2hLkCcINwvEtC6+2E/IE4ybh5YQH9S3CN87C3fe2VSH8BU1CJKWn1TM/ZdKzICXpuTRl0RNSQM9lKSO9n+7O4+j/j6tu8Ebt7X44Mdg50D84lBg40vE8egyB8TtogiUA0/fSaMzpiHrI8LvkZlg8XYDPD9AyG44KHKE9LuKYx6H0omqBztrOwYT3kX0J3z2vwDs0GyrlgHik69v21MjLj0419u//YjiVdLp2v9i753KzCWJf6/aed3flPjA9XI/zJOpgC3a3Bbbi2G2/9kLX+dGPWsXj9XhSRRTceGn01YeOtXzTODaSk3bVfmz+fk9Hj1Ksafp9b1WXQtedrE0eewwStcnfTt/33A+NWztVfWGyOTnQ/FM7JD55QuHnnnppRGkafS3NWk0aFmHfH5rKdEDZd8IReC/3rDUBrhPOJReVg3kHTqCS8+nARrDtldcufnl/cndDgvAKwispf4aQFC2xG9aqhYM0HMWaCbmCtEooVxvtFkzPUukY5WN4DwvsUVDUoEpUeSs12ZB5tmCNDU+S/QAH7SXUVpVVq0/zM2UN41eQKesYR41MGfdeqN7A+PPWTNnE+GCg3sK4zesPtDzqfxHltNk193Ol0JMESxpLOkuSJYMlkyXLkwTzCeYTxPcH9iyQb7l7YAmXWHIlgyWTJZ9TI07VXq1WEfXh4hpr6F4rjTk15tSYU2NOne3U2U6d+XQet87j1nncOo9bZz7JfJL5JPNJ5pPEp8YiyT7NG4tkGyVzGjxug8ate7oGtiryJJ0lya38cRvMaTCnyXaa7lySpLGksyRZz2DJZMnns5jP4nFbPG6Lx20xn8V8FvNZzGfzmPG+wddX6Y3ZRs5iT9JZkiwZLJnM5HM6bKPDNjpso8M2OmyjwzY6zOcQn61FvPMzrpYD4urVEDFBWJ0MYhphp7UgphN2VgQxSVgqpGdcBXPPmevwfMxyMSOI2YRNhzCHsH1rfWy7VgBv0fGPB1zLKrw7dFf0PY777XABLzpJB1mnrWHQ5jJoE+nkh9QRVlfL9f3+/qeDAPcbUqcL0fWopnedqml3vAt2MbjuSn02q2sXfbr6BYROSc2o+hbLOJRCTOpSLMJUDMrdAKzAtJIWBKAU062YophWg/omAyjDtAaTWs91mNZj2oAJ32rQwSn3Bvh2BeptBqox3YapBtR3G6jvAXTqAHeA+y/qZph/UPM11/rPePU35/S/G/4E59iswgAWAAA=\");\n" +
                    "\t\t\n" +
                    "\t    \n" +
                    "\t\t\n" +
                    "\t\t\n" +
                    "\t\t\twebPrint.InvokePrintDirect();\n" +
                    "\t\t\t\n" +
                    "\t\t\n" +
                    "\t\t//window.close();\n" +
                    "   }\n" +
                    "   \n" +
                    "   if(detectPlugin()){   \t\t\n" +
                    "   \t\t\t\tbtclick();\n" +
                    "   }\n" +
                    "    else{\n" +
                    "       alert(\"请确认报表控件已下载并安装完成,并允许该控件进行页面交互!\");\n" +
                    "   }\n" +
                    "   function workaround()\n" +
                    "   {\n" +
                    "    // \"a\" is the name of the control.\n" +
                    "    window.document.all.item(\"webPrint\").style.display = \"none\"\n" +
                    "    window.document.all.item(\"webPrint\").style.display = \"\"\n" +
                    "  }\n" +
                    "   \n" +
                    "     \n" +
                    "</script> \n" +
                    "  \n" +
                    "\n" +
                    "\n" +
                    "<body onscroll=\"workaround();\">\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n" +
                    "<html>\n" +
                    "<script language=\"JavaScript\">\n" +
                    "function doPrint(){\n" +
                    "\t//首先打印主页\n" +
                    "\t\talert(\"准备打印基本存款账户许可证,请将基本存款账户许可证放入打印机!\");\n" +
                    "\t\t//再打印副本\n" +
                    "\t\t//alert(\"准备打印副本,请放入纸张!\");\n" +
                    "       //\talert(\"准备打印存款人查询密码!\");\n" +
                    "\t\t\n" +
                    "\t\t        \n" +
                    "  var actionStr=\"/ams/apprLocalBasicPilot.do?method=forPrintLic\";\n" +
                    "  window.document.forms[0].action=actionStr;\n" +
                    "  trimAllText();\n" +
                    "  window.document.forms[0].submit();\n" +
                    "    disabledButton();\n" +
                    "\n" +
                    "\t\t        \n" +
                    "}\n" +
                    "function doAction(){\n" +
                    "   var actionStr=\"/ams/apprLocalBasicPilot.do?method=forComplete\";\n" +
                    "  window.document.forms[0].action=actionStr;\n" +
                    "  window.document.forms[0].submit();\n" +
                    "    disabledButton();\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "function PrintLicAgainSubmit(){\n" +
                    " // if(!doCheck()) return false;\n" +
                    "  OpenPrintSelectWindow(\"重打\",\"AMS_PRINT_TYPE_REPRINT_PILOT\");\n" +
                    "    var printTypeList = document.forms[0].printTypeList.value;\n" +
                    "   if (printTypeList != null && printTypeList != \"\"){\n" +
                    "       var printTypeArray = printTypeList.split(\"#\");\n" +
                    "       var confirmStr = \"\";\n" +
                    "        if (printTypeArray[0] == \"ACCOUNT_REPORT_INFO\") {\n" +
                    "           confirmStr = \"准备打印基本存款账户开户信息,请将纸张放入打印机!\";\n" +
                    "       }else if (printTypeArray[0] == \"REPORT_LIC_PWD\") {\n" +
                    "           confirmStr = \"准备打印存款人查询密码,请将纸张放入打印机!\";\n" +
                    "       }else if (printTypeArray[0] == \"REPORT_BASIC_LIC_PILOT\") {\n" +
                    "           confirmStr = \"准备打印基本存款账户许可证核发试点证明，请将纸张放入打印机!\";\n" +
                    "       }\n" +
                    "       if(confirmStr != \"\"){\n" +
                    "       \talert(confirmStr);\n" +
                    "       }       \n" +
                    "       RePrintLic(); //调用重打!\n" +
                    "   }\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "function printByTime() {\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "function checkSacclicNo() {\n" +
                    "    var slicno = document.forms[0].elements[\"slicno2\"].value;\n" +
                    "    //通过ajax校验开户许可证号!\n" +
                    "    var usercode = \"4R7Q88\";\n" +
                    "    theIBankAccessBOProxy.checkSaccLicStateRigth(slicno, \"1\", usercode, setSacclicState);\n" +
                    "}\n" +
                    "\n" +
                    "function setSacclicState(sacclicState) {\n" +
                    "    if (sacclicState != \"\" && sacclicState != null && sacclicState != \"0\") {\n" +
                    "        return false;\n" +
                    "    }else{\n" +
                    "         document.forms[0].elements[\"sacclicState\"].value = \"true\";\n" +
                    "    }\n" +
                    "\n" +
                    "        if( document.forms[0].elements[\"sacclicState\"].value !=\"true\"){\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "  var returnURL =  \"/ams/printAccPaperPilot.do?method=forFrameForward$headStr=补打$accountType=1\";\n" +
                    "  OpenAuthorizeWindow(\"补打许可证\",returnURL);\n" +
                    "  OpenPrintSelectWindow(\"补打\",\"AMS_PRINT_TYPE_REPRINTAGAIN_PILOT\");\n" +
                    "   var authorize_result =  document.forms[0].authorize_result.value; //授权是否通过\n" +
                    "    var printTypeList = document.forms[0].printTypeList.value;\n" +
                    "   if (authorize_result == \"1\" && printTypeList != null && printTypeList != \"\"){\n" +
                    "       alert(\"准备打印基本存款账户许可证核发试点证明,请将纸张放入打印机!\")\n" +
                    "       RePrintLicAgain(); //调用补打!\n" +
                    "   }\n" +
                    "}\n" +
                    "\n" +
                    "function RePrintLicSubmit(){\n" +
                    "  if(!doCheck()) return false;\n" +
                    "\n" +
                    "    checkSacclicNo();\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "/*  打开打印选择页面*/\n" +
                    "function OpenPrintSelectWindow(headStr,accountType) {\n" +
                    "   document.forms[0].printTypeList.value = \"\"; //再打开选择页面前,将打印类型选择项清空!\n" +
                    "   var xposition = 0;\n" +
                    "   var yposition = 0;\n" +
                    "    if ((parseInt(navigator.appVersion) >= 4 )) {\n" +
                    "        xposition = (screen.width - 430) / 2;\n" +
                    "        yposition = (screen.height - 250) / 2;\n" +
                    "    }\n" +
                    "    var res = window.showModalDialog(\"/ams/printAccPaperPilot.do?method=forFrameForward&headStr=\" + headStr+\"&accountType=1\"+\"&operateType=\"+accountType, \"_blank\", \"dialogWidth:430px; dialogHeight:250px; dialogLeft:\" + xposition + \"px; dialogTop:\" + yposition + \"px; status:no;scroll:no;resizable=no;\");\n" +
                    "    //alert(\"res\"+res);\n" +
                    "    if (res == null || res == \"\") {\n" +
                    "        document.forms[0].printTypeList.value = \"\";\n" +
                    "        return;\n" +
                    "    } else {\n" +
                    "     document.forms[0].printTypeList.value = res;\n" +
                    "     return;\n" +
                    "\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "/*弹出授权窗口*/\n" +
                    "function OpenAuthorizeWindow(headStr,returnURL) {\n" +
                    "    var xposition = 0;\n" +
                    "    var yposition = 0;\n" +
                    "    if ((parseInt(navigator.appVersion) >= 4 )) {\n" +
                    "        xposition = (screen.width - 430) / 2;\n" +
                    "        yposition = (screen.height - 250) / 2;\n" +
                    "    }\n" +
                    "    //var res = window.showModalDialog(\"/ams/authorize.do?method=forFrameForward&headStr=\" + headStr+\"&returnURL=\"+returnURL, \"_blank\", \"dialogWidth:430px; dialogHeight:250px; dialogLeft:\" + xposition + \"px; dialogTop:\" + yposition + \"px; status:no;scroll:no;resizable=no;\");\n" +
                    "    //if (res == null || res == \"\") {\n" +
                    "    //    document.forms[0].authorize_result.value = \"0\";\n" +
                    "    //    return;\n" +
                    "    //} else {\n" +
                    "        document.forms[0].authorize_result.value = \"1\";\n" +
                    "        document.forms[0].printTypeList.value = \"REPORT_BASIC_LIC_PILOT#\";\n" +
                    "        return;\n" +
                    "    //}\n" +
                    "}\n" +
                    "        \n" +
                    "function RePrintLic(){\n" +
                    "  var actionStr=\"/ams/apprLocalBasicPilot.do?method=forRePrintLic\";\n" +
                    "  window.document.forms[0].action=actionStr;\n" +
                    "  window.document.forms[0].submit();\n" +
                    "    disabledButton();\n" +
                    "\n" +
                    "}\n" +
                    "function RePrintLicAgain(){\n" +
                    "\n" +
                    "  var actionStr=\"/ams/apprLocalBasicPilot.do?method=forRePrintLicAgain\";\n" +
                    "  window.document.forms[0].action=actionStr;\n" +
                    "  window.document.forms[0].submit();\n" +
                    "    disabledButton();\n" +
                    "    \n" +
                    "}\n" +
                    "function doCheck(){\n" +
                    "  var slicno2=document.forms[0].elements[\"slicno2\"].value;\n" +
                    " \tif(isWhitespace(slicno2)){\n" +
                    "\t  alert(\"请录入补打的开户许可证编号!\");\n" +
                    "\t  document.forms[0].elements[\"slicno2\"].focus();\n" +
                    "\t  return false;\n" +
                    "\t}\n" +
                    "\tif(slicno2.length!=12 || !IsNumber(slicno2)){\n" +
                    "\t\t\talert(\"请录入正确的12位开户许可证编号!\");\n" +
                    "\t\t\t document.forms[0].elements[\"slicno2\"].focus();\n" +
                    "\t\t\treturn false;\t\t\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\treturn true;\n" +
                    "}\n" +
                    "        \n" +
                    "\n" +
                    "\n" +
                    "function gotoIndex() {\n" +
                    "//parent.location=\"../index.html\";\n" +
                    "window.location=\"LocalBasicOpen.html\";\t\n" +
                    "}\n" +
                    "\n" +
                    "function openNewWindow(URL,Win){\n" +
                    "\tif(Win==\"\" || Win==null){\n" +
                    "\t\tWin=\"Popup\"\n" +
                    "\t}\n" +
                    "\tvar windowprops\t= \"width=430,height=220,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,top=170,left=200\";\n" +
                    "\t//windowprops\t= \"height=\" + Hei + \",width=\"+ Wid + \",location=no,scrollbars=yes,status=no,menubars=no,toolbars=no\";\n" +
                    "\twindow.open(URL, \"Popup\", windowprops);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "function openNewWindow1(URL,Win){\n" +
                    "\tif(Win==\"\" || Win==null){\n" +
                    "\t\tWin=\"Popup\"\n" +
                    "\t}\n" +
                    "\twindowprops\t= \"width=643,height=420,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,top=170,left=200\";\n" +
                    "\twindow.open(URL, \"Popup\", windowprops);\n" +
                    "}\n" +
                    "</script>\n" +
                    "\n" +
                    "<body onload=\"printByTime()\";>\n" +
                    "<font color=\"red\">  </font>\n" +
                    "<form name=\"basicImportantInfoForm\" method=\"post\" action=\"/ams/apprLocalBasicPilot.do\">\n" +
                    "<input type=\"hidden\" name=\"operateType\" value=\"01\">    \n" +
                    "<input type=\"hidden\" name=\"printTypeList\" value=\"REPORT_LIC_PWD\">    \n" +
                    "<input type=\"hidden\" name=\"printActionType\" value=\"\">    \n" +
                    "<input type=\"hidden\" name=\"printTime\" value=\"3\">    \n" +
                    "<input name=\"authorize_result\" type=\"hidden\">\n" +
                    "\n" +
                    "<input name=\"sacclicState\" type=\"hidden\" value=\"false\">\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "    <input type=\"hidden\" name=\"areaStr\" value=\"331000\">\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "    <input type=\"hidden\" name=\"fromNotChecked\" value=\"0\">      <!--是否从待核准过来-->\n" +
                    "\n" +
                    "\n" +
                    "    \n" +
                    "<!-- Title -->\n" +
                    "\t<TABLE class=\"TitleTable\" align=\"center\">\n" +
                    "\t  <TR class=\"TitleRow\">  \n" +
                    "\t      <td class=\"TitleLCell\" width=\"34%\">\n" +
                    "              \n" +
                    "              \n" +
                    "                 基本存款账户新开户－>基本存款账户开户信息\n" +
                    "              \n" +
                    "          </td>\n" +
                    "\t  </tr>\n" +
                    "\t</table>\n" +
                    "\n" +
                    "<br><br><br>\n" +
                    "  <TABLE cellspacing=\"1\" cellpadding=\"0\" align=\"center\" width=\"65%\">\n" +
                    "    <tr>\n" +
                    "      <TD class=\"RltShow\" width=\"30%\" >&nbsp;&nbsp;<font color=\"#FF0000\" >开户成功!</font></td>\n" +
                    "    </tr>\n" +
                    "    <tr> \n" +
                    "      <td class=\"EdtTbLCell\" >&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <tr> \n" +
                    "      <TD class=\"RltShow\" width=\"30%\" >&nbsp;&nbsp;基本存款账户编号：</TD>\n" +
                    "      <TD class=\"RltShow\" width=\"40%\" >&nbsp;&nbsp;<font color=\"#FF0000\">&nbsp;&nbsp;\n" +
                    "       J3310106835801\n" +
                    "      </font></TD>\n" +
                    "    </tr>\n" +
                    "    <tr> \n" +
                    "      <td class=\"EdtTbLCell\">&nbsp;</td>\n" +
                    "    </tr>\n" +
                    "    <TR> \n" +
                    "      <TD class=\"RltShow\" width=\"30%\" >&nbsp;&nbsp;已打印开户许可证编号：</TD>\n" +
                    "      <TD class=\"RltShow\" width=\"40%\" >&nbsp;&nbsp;<font color=\"#FF0000\">&nbsp;&nbsp;\n" +
                    "       331098162555\n" +
                    "          <input type=\"hidden\" name=\"slicno\" value=\"331098162555\">\n" +
                    "          \n" +
                    "      </font></TD>\n" +
                    "    </TR>\n" +
                    "    <TR> \n" +
                    "      <TD class=\"RltShow\" >&nbsp;</TD>\n" +
                    "      <TD class=\"EdtTbLCell\">&nbsp;</TD>\n" +
                    "    </TR>\n" +
                    "     \n" +
                    "    <TR> \n" +
                    "      <TD class=\"RltShow\">&nbsp;</TD>\n" +
                    "      <TD class=\"EdtTbLCell\">&nbsp;</TD>\n" +
                    "    </TR>\n" +
                    "   <TR > \n" +
                    " \n" +
                    "    </TR>\n" +
                    "\n" +
                    "  </TABLE>\n" +
                    "<br><br>\n" +
                    "<TABLE class=\"FooterTable\" align=\"center\">\n" +
                    "\t<TR>\n" +
                    "\t\t<TD>&nbsp;</TD>\n" +
                    "\t</TR>\n" +
                    "</TABLE>\n" +
                    "\n" +
                    "\n" +
                    "<TABLE class=\"BtnTable_add\" align=\"center\">\n" +
                    "\t<TR>\n" +
                    "\t \n" +
                    "     <TD id=\"tmp41\" class=\"BtnOK\" width=\"10%\">\n" +
                    "\t<button class=btns_crams onmouseover=\"this.className='btns_mouseover'\"\n" +
                    "      onmouseout=\"this.className='btns_mouseout'\"\n" +
                    "      onmousedown=\"this.className='btns_mousedown'\"\n" +
                    "      onmouseup=\"this.className='btns_mouseup'\" name=\"res\"  onClick=\"PrintLicAgainSubmit()\">&nbsp;重打账户开户信息&nbsp;</button>&nbsp;&nbsp;\n" +
                    "    </TD>      \n" +
                    "\t\t<TD class=\"BtnOK\" width=\"10%\">\n" +
                    "\t<button class=btns_crams onmouseover=\"this.className='btns_mouseover'\"\n" +
                    "\t\t  onmouseout=\"this.className='btns_mouseout'\"\n" +
                    "\t\t  onmousedown=\"this.className='btns_mousedown'\"\n" +
                    "\t\t  onmouseup=\"this.className='btns_mouseup'\" name=\"res\"  onClick=\"doAction();\">&nbsp;完&nbsp;&nbsp;成&nbsp;</button>\n" +
                    "\t\t</TD>\n" +
                    "\t</TR>\n" +
                    "</TABLE>\n" +
                    "</form>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n" +
                    "\t</div>\n" +
                    "\t<!-- content end -->\n" +
                    "\t\n" +
                    "\n" +
                    "\t<DIV id=\"showtips\"\n" +
                    "\t\tSTYLE=\"   \t\t\t  \n" +
                    "   \t\t\tborder-width:0; \n" +
                    "   \t\t\tposition: absolute;\n" +
                    "   \t\t\twidth: 100px;  \n" +
                    "   \t\t\tright:0;\n" +
                    "   \t\t\ttop: 0;   \t\t\t\n" +
                    "   \t\t\theight: 21px; \n" +
                    "   \t\t\tfont-family: 宋体,Arial; font-size: 14px; font-weight: bold; \n" +
                    "   \t\t\tpadding-left: 6px; padding-top: 3px; padding-bottom: 3px;\n" +
                    "   \t\t\tline-height: 145%;\n" +
                    "   \t\t\ttext-align: center;\n" +
                    "   \t\t\tcolor: #07868D;\n" +
                    "   \t\t\tfont: bold;\n" +
                    "   \t\t\ttext-align:center;\n" +
                    "   \t\t\tdisplay: none;\">\n" +
                    "   \t\t\t<TABLE  cellpadding=\"0\" cellspacing=\"0\" border=0>  \t\t\t \n" +
                    "  \t\t\t\t<TR>\n" +
                    "    \t\t\t\t<TD align=left>\n" +
                    "      \t\t\t\t\t<IMG style=\"MARGIN: 3px\" alt=\"提交中请等待......\" \n" +
                    "      \t\t\t\t\tsrc=\"/ams/jsp/images/load.gif\" \n" +
                    "      \t\t\t\t\talign=left>\n" +
                    "    \t\t\t\t</TD>\n" +
                    "  \t\t\t\t</TR>  \t\t\t\n" +
                    "\t\t   </TABLE>\t\t\t\n" +
                    "\t</DIV>\n" +
                    "\t\n" +
                    "\t\t\t\n" +
                    "\t\t\t\n" +
                    "</body>\n" +
                    "</html>\n";
            html = "77d\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<script language=\"javascript\">\n" +
                    "    \n" +
                    "</script>\n" +
                    "\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "\n" +
                    "\n" +
                    "\t<META http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\n" +
                    "\t<META content=\"/WEB-INF/pages/systemmanage/depositorpwd/success.jsp\">\n" +
                    "\n" +
                    "\t<link rel=\"stylesheet\" href=\"/ams/jsp/css/common.css\" type=\"text/css\" />\n" +
                    "\n" +
                    "\t\n" +
                    "\t\n" +
                    "\t<title>人民币银行结算账户管理系统</title>\t\n" +
                    "</head>\n" +
                    "\n" +
                    "<body background=\"/ams/jsp/images/bjb2.gif\">\n" +
                    "\t\n" +
                    "\t\n" +
                    "\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/common.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/verify.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/date.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/select.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/combobox.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/validate.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/xtree.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/engine.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/util.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/theIUserAccessBOProxy.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/theAccountAccessProxy.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/theIBankAccessBOProxy.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/theIFileAccessBOProxy.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/MyAreaTree.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/WatchAccess.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"/ams/dwr/interface/validateDepProxy.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/statcheck.js\"></script>\n" +
                    "\t\n" +
                    "\t\t<script type=\"text/javascript\" src=\"jsp/js/print.js\"></script>\n" +
                    "\t\n" +
                    "\t\t\t\n" +
                    "\t\n" +
                    "\t<!-- content -->\n" +
                    "\t<div id=\"content\">\n" +
                    "\t\t\n" +
                    "1824\n" +
                    "<!--\n" +
                    " \t页面说明： 中国人民银行账户管理系统页面\n" +
                    " \t版权：人民银行软件开发中心\n" +
                    " \t功能：系统管理―系统管理数据下载页面\n" +
                    "-->\n" +
                    "\n" +
                    "\n" +
                    "       \n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                    "\n" +
                    "<html>\n" +
                    "\n" +
                    "<object id= \"webPrint\" classid=\"clsid:A705A992-4752-4A85-8506-4A1CB02C68C7\"   WIDTH=\"0\" HEIGHT=\"0\" ALIGN=center HSPACE=0 VSPACE=0></object>\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\n" +
                    "<title>Web Report Print</title>\n" +
                    "</head>\n" +
                    "<script  language=\"javascript\" > \n" +
                    "\tfunction detectPlugin()\n" +
                    "\t{\n" +
                    "   \t try\n" +
                    "   \t {\n" +
                    "   \t   webPrint.SetXML(\"\");\n" +
                    "   \t   return true;\n" +
                    "\t\t}\n" +
                    "    catch(e)\n" +
                    "    {\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "\t}\n" +
                    " \n" +
                    "\tfunction btclick(){\n" +
                    "\n" +
                    "\t   \n" +
                    "\t\t\twebPrint.SetXML(\"H4sIAAAAAAAAAO2Wb0/TQBzHH8OrqOdjtv65ba3pRjYUiUE0EeMDQshYb6Oma5fugOEjNKgYIEKGiEaDGMEHwkaMcQpTXoxrNx/tLXC3OtqhoCSYGKFpbt2v37v73n0/aSt35tIaM4bMrGroYcD5WMAgPWEoqp4Kg5v93R0i6Iy0y7fj2Qwyr5uqjhk9nkZhoKmJIRNlDBMPZcYVwGTiKXRLVfBIGASkgPO/B6mpERwGIuRBpL1NpjXy2yZjlMN0Vqwm4lpUU1N6GulEGDMwNtKAofebnXneJ0AYgAJgNFVHNzLxBDHXHU9gw6SOecgJQYHMqKE4tX0tmcwi0q8jQG+xvEinJnM6Zi9piE7F5EjXIAeYiTCAImDGHeeCSMYZaboOkVXQFWfxhEZWHO3tBX5nrGEjx2AjEzNMBRETfYaOgFvoMjRq7TzbOKixJG6VupWD2uHGDrSqvbWDepN6bZV7SgfUP9wnDbIBtOlrJFnZ2bUKM0OXY7zA8YDJqndIkRObappFFxGTXYvI5wa6Lkb7owP18kK9PF8vP2u05FxstMvNC+dcGByMyH7vADT8RuEfwkCSGhhwAQ8HMORyEIRnHBzGAccLMBD8k5hp44n4ymgWq0kVkSfHMQAQRShKhwIgiCyUjg9AgHXyZ/n9/Dk26OYvCKcjf3utVPmS/zl/Rs3GDE0JA2yOkmEzSrJ7v08P0sYQTa+DSo5Exdpctje+Vra3rfm56tutC/8HNBLrQgPhGTR/Cxp7Za1WfG0VH1Rf3T15dK6qiqKhVnQE6BN5IRg6nJwQy//ifQN9bAhKPBv6DTq86L5voORBJxQ8zejAk0KHIUetWLLW16ylqcruy2phtV6edZ9Cj4vWw017843DlDW9Za1s2y82HEHtw7o9Xao+n7LKk+Tie/5jbXW2sluwFz87ent6yX6Ut+a2rNJ7oqm+m3E01Z18tfDU6W5/um/NPfk2ee9IXmW/81ks+z0f2ZH2Pd4KdcWaCwAA\");\n" +
                    "\t\t\twebPrint.SetXLS(\"H4sIAAAAAAAAAO1YXWwVRRQ+Mzv7B7TcW2pp+amX2wL9uWAB5adEeiExJkaptlYwLblybYGmhDaFiEjUi2K0qSQmkvjiSx988El90USIUh+MiY0aeTAxMSkkPBm1+IcxyHrm7O65u7etQCIYlZmc2TPfnDlzzvyd3f3i8+Tk2DuLzkFJ2gwGXPFcsCKYiAokAJwAu+J5noZcJO9W+lelDhjEfAhScA8cwOcwHCndCn+ZqsAUoS5xdXHeR9kyn9+Bow/DAOTJjoHrGlunCpAi6s+19qsvu+6hZkwS4nv+79F688Z3HTzIlglnExP2Vqzr834Otb6txkn3eaQeGAIFMDw4eCh1s9I2smG30DbcTeXriJRDDVlVQeUCKt+i1tO+JLbsE+Wip/WDuo3kn4CdMktyJ6hMU1mOpYD3qM/XhKyBRfCJnstnXwm2sSm24s7sh92w/4a2pqnVgbDVFQex5QAcxNxHcntIap+IS3nbR7tzcC9sg7WwDiXWRqROy1DqcH9n+3Sp6qtK1aolYMJCnLD6dF2mrq4l17i5pyGs9DTWqmW4V5bE2rs7+np3xYXSYMPSotDqlrgerGuplWhMulSqRFsgugGa0DrIabwpFY4cVHsagoFDIL0qTa3ZHHZdDY2YsWu0X7xTSY82yEBrfDDftvh4ZFpkyLa2oor10IzzWRw07D+tc0nPSaiktfnFS0VO65mUxkWI/3xtuLxOHP6HuMAIWEzhfDb78r+V4o2z4JlZ8KZZcHdGPGqPoxwFaoRwk3B8y8KrbUSNMG4R3kh4VN4mfNU03H9vWxrDX5UKEgXD089kwaJnRUHRc0HBpicUgJ6VBdN7jO7OFzD+v6iHwRt1f//jueG+ocHhQ7mhw70vY8QQmL+FDpgPMHUfeWNNJfRDxd8l18HcqQp8foiWOXBMoIfOGZHFMgu1F3UPDNbOPCS8j5xL+O55GU7RbJjUJpJewqtBSniVSNVU6pz0LBA7u7cMvL+97OGpI604U2INXXF3wl2wHgcLWi/sPT/6cbd4shXPqkiBny+NvvHo8a5v2seOzvN8sR86v3uod0ALtnT8viOzV6MrTmzIH38Cchvyv568/6Xv2zf16faqfGd+qPPHXZD79CmNn33mtaNa0jzQ1SlbPJiDY3+E64ZOIR+G4QScKpuwx8EPw2UUpOZh2YtTqPkkHdkE9r385sUvH8g/2JYjvInwZiqfI6RAi+yn5XrpwINj2DKuFpPUIir1VrsN6XmqHadyDG9igSMKyhIyIhOs1WRb8dmFLQ48TfYD7HHmU19d170+SxbrEvNXUKwbmEfNYh13X6zdxPzTpmLdwvxIpN3GvDkYD2Q5jT+HStru0v9gqQo4wZxkzmBOMWcyZzFnB5xgfYL1CdL3B44sUN9C/8gSrrDmcyZzFnOhTkk6dX+9WtU0ho9LljCCXpJ1StYpWadknQbbabCdBusz2G+D/TbYb4P9NlifYn2K9SnWp1ifIn3aF0X2ycAXxTYq1mmy3yb5bQSyJvaqDjiDOcW9Qr9N1mmyTovttPy5JE4yZzCnWM5kzmIu1GezPpv9ttlvm/22WZ/N+mzWZ7M+h33G+wZfYFXgs4M6awLOYE4xZzJnsaZQp8s2umyjyza6bKPLNrpso8v6XNLnyERwfs7o5YCsfjlETBC2UUUxSdhJGcUMwiZEFFOEFWJy5gyYf878kBdito+ZUcwhbCqGuYTtXB5iW2QFvEvHPxsJLkvx7jB8Now5/tfDBbzoFB1kg7aGSZvLpE1kUCTSR1hfLTf2C/yfTQL8b0iDrkM/olrBZaon3Q2u17ngByv92awvXYzp+hcQhiQ9n/pbrBhOqpD0lViNVAM62AAsRlpCywFQi3Q7UgppGehvMoA6pHokvZorkFYiNSDhWw2GNx3cAN+uQL/NwGqkO5BaQH+3gf4ewJAOGNT9f1G30rUnPV+zrf+VoP3WnP53059n6+xNABYAAA==\");\t\n" +
                    "\t\t\n" +
                    "\t    \n" +
                    "\t\t\n" +
                    "\t\t\n" +
                    "\t\t\twebPrint.InvokePrintDirect();\n" +
                    "\t\t\t\n" +
                    "\t\t\n" +
                    "\t\t//window.close();\n" +
                    "   }\n" +
                    "   \n" +
                    "   if(detectPlugin()){   \t\t\n" +
                    "   \t\t\t\tbtclick();\n" +
                    "   }\n" +
                    "    else{\n" +
                    "       alert(\"请确认报表控件已下载并安装完成,并允许该控件进行页面交互!\");\n" +
                    "   }\n" +
                    "   function workaround()\n" +
                    "   {\n" +
                    "    // \"a\" is the name of the control.\n" +
                    "    window.document.all.item(\"webPrint\").style.display = \"none\"\n" +
                    "    window.document.all.item(\"webPrint\").style.display = \"\"\n" +
                    "  }\n" +
                    "   \n" +
                    "     \n" +
                    "</script> \n" +
                    "  \n" +
                    "\n" +
                    "\n" +
                    "<body onscroll=\"workaround();\">\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n" +
                    "<body>\n" +
                    "<TABLE class=\"TitleTable\" align=\"center\">\n" +
                    "\t<TR class=\"TitleRow\">\n" +
                    "\t\t<td class=\"TitleLCell\" width=\"34%\">\n" +
                    "\t\t\t存款人查询密码管理->完成重置\n" +
                    "\t\t</td>\n" +
                    "\t</tr>\n" +
                    "</table>\n" +
                    "<br>\n" +
                    "\n" +
                    "\n" +
                    "<BR>\n" +
                    "<!-- Detail -->\n" +
                    "\n" +
                    "<form name=\"depositorPwdForm\" method=\"post\" action=\"/ams/depositorPwdReset.do?method=forResetDepositorPwd\">\n" +
                    "\t<!-- detail -->\n" +
                    "\t<TABLE class=\"LstTable\" id=\"myTable\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"position:relative;display:block\">\n" +
                    "\t\t<TR class=\"LstTBOddRow\">\n" +
                    "\n" +
                    "\t\t\t<TD class=\"LstTbLCell\" nowrap>\n" +
                    "\t\t\t\t存款人查询密码重置成功！\n" +
                    "\t\t\t</TD>\n" +
                    "\t\t</TR>\n" +
                    "\t</TABLE>\n" +
                    "\t<TABLE class=\"FooterTable\" align=\"center\">\n" +
                    "\t\t<TR>\n" +
                    "\t\t\t<TD>\n" +
                    "\t\t\t\t&nbsp;\n" +
                    "\t\t\t</TD>\n" +
                    "\t\t</TR>\n" +
                    "\t</TABLE>\n" +
                    "\t<!-- Button -->\n" +
                    "\t<TABLE class=\"BtnTable\" align=\"center\">\n" +
                    "\t\t<TR>\n" +
                    "\t\t\t<TD class=\"BtnOK\">\n" +
                    "\t\t\t\t<input type=\"submit\" value=\"&nbsp;返&nbsp;&nbsp;回&nbsp;\" onmouseover=\"this.className='btns_mouseover'\" onmouseout=\"this.className='btns_mouseout'\" onmousedown=\"this.className='btns_mousedown'\" onmouseup=\"this.className='btns_mouseup'\" class=\"btns_crams\">\n" +
                    "\t\t</TR>\n" +
                    "\t</TABLE>\n" +
                    "</form>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "\t</div>\n" +
                    "\t<!-- content end -->\n" +
                    "\t\n" +
                    "\n" +
                    "\t<DIV id=\"showtips\"\n" +
                    "\t\tSTYLE=\"   \t\t\t  \n" +
                    "   \t\t\tborder-width:0; \n" +
                    "   \t\t\tposition: absolute;\n" +
                    "   \t\t\twidth: 100px;  \n" +
                    "   \t\t\tright:0;\n" +
                    "   \t\t\ttop: 0;   \t\t\t\n" +
                    "   \t\t\theight: 21px; \n" +
                    "   \t\t\tfont-family: 宋体,Arial; font-size: 14px; font-weight: bold; \n" +
                    "   \t\t\tpadding-left: 6px; padding-top: 3px; padding-bottom: 3px;\n" +
                    "   \t\t\tline-height: 145%;\n" +
                    "   \t\t\ttext-align: center;\n" +
                    "   \t\t\tcolor: #07868D;\n" +
                    "   \t\t\tfont: bold;\n" +
                    "   \t\t\ttext-align:center;\n" +
                    "   \t\t\tdisplay: none;\">\n" +
                    "   \t\t\t<TABLE  cellpadding=\"0\" cellspacing=\"0\" border=0>  \t\t\t \n" +
                    "  \t\t\t\t<TR>\n" +
                    "    \t\t\t\t<TD align=left>\n" +
                    "      \t\t\t\t\t<IMG style=\"MARGIN: 3px\" alt=\"提交中请等待......\" \n" +
                    "      \t\t\t\t\tsrc=\"/ams/jsp/images/load.gif\" \n" +
                    "      \t\t\t\t\talign=left>\n" +
                    "    \t\t\t\t</TD>\n" +
                    "  \t\t\t\t</TR>  \t\t\t\n" +
                    "\t\t   </TABLE>\t\t\t\n" +
                    "\t</DIV>\n" +
                    "\t\n" +
                    "\t\t\t\n" +
                    "\t\t\t\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n" +
                    "\n" +
                    "0\n" +
                    "\n";
           Matcher matcher = Pattern.compile("webPrint.SetXML\\(\"(.*?)\"\\)").matcher(html);

            while (matcher.find()) {
                String str = matcher.group(1);
                if(StringUtils.isBlank(str)){
                    continue;
                }
                System.out.println(str.startsWith("H4sIAAAA"));
                if(!str.startsWith("H4sIAAAA")){
                    System.out.println("截取后1："+str);
                    continue;
                }
                System.out.println("截取后："+str);
                str = str.replaceAll("webPrint.SetXML","")
                        .replaceAll("webPrint.SetXLS","")
                        .replaceAll("\\(","")
                        .replaceAll("\\);","")
                        .replaceAll("\"","")
                        .replaceAll("[\\t\\n\\r]", "")
                        .replaceAll("\\s+", "");
                System.out.println("替换后："+str);
                //System.out.println("解压后："+unzipString(str));
                str = unzipString(str);
                Document doc = Jsoup.parse(str);
                Elements slicnoInputs = doc.getElementsByTag("textContent");
                for (Element slicnoInput1 : slicnoInputs) {
                    String linkText = slicnoInput1.text();
                    System.out.println("<textContent>==="+linkText);
                }
            }
          /* String[] strings = dealPrintHtml(html);
            for (String str:strings) {
                System.out.println(str);
            }*/
            //System.out.println("A==="+unzipString(a));
            //System.out.println("B==="+unzipString(b));
            //System.out.println("C==="+unzipString(c));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static final String unzipString(String compressedStr) throws IOException {
        InputStream bais = new ByteArrayInputStream(compressedStr.getBytes());
        Base64InputStream b64io = new Base64InputStream(bais);
        GZIPInputStream gin = new GZIPInputStream(b64io);

        //FileUtils.copyInputStreamToFile(gin, new File("C:\\Users\\yang\\Desktop\\a.html"));

        //toString 方法建议制定编码，否则采用系统默认编码，出现中文编码错误的问题
       return IOUtils.toString(gin,"utf-8");
    }
    private static String[] dealPrintHtml(String  html){
        String[] array = null;
        log.info("原始html："+html);
        Matcher matcher = Pattern.compile("\\webPrint.SetXML\\(\"H4s[^>]*\\webPrint.SetXLS").matcher(html);
        if (matcher.find()) {
            html = matcher.group();
            log.info("抓取后的html截取前："+html);
            html = html.replaceAll("webPrint.SetXML","")
                    .replaceAll("webPrint.SetXLS","")
                    .replaceAll("\\(","")
                    .replaceAll("\\);","")
                    .replaceAll("\"","")
                    .replaceAll(" ","");
            log.info("截取后且没解压的html："+html);
            //System.out.println("解压后："+unzipString(str));
            try {
                html = unzipString(html);
                log.info("解压后的html："+html);
            } catch (IOException e) {
                log.error("解压打印信息异常："+e.toString());
                return array;
            }
            Document doc = Jsoup.parse(html);
            Elements slicnoInputs = doc.getElementsByTag("textContent");
            array = new String[slicnoInputs.size()];
            int i = 0;
            for (Element slicnoInput1 : slicnoInputs) {
                String linkText = slicnoInput1.text();
                array[i] = linkText;
                i++;
            }
        }
        return array;
    }
}
