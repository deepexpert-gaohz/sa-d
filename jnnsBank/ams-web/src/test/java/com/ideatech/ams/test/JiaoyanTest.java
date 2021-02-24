package com.ideatech.ams.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JiaoyanTest {
    public static void main(String[] args) {
        String html ="<DIV id=\"showtips\"\n" +
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
                "\t                                                                          \n" +
                "\t\t\t\t  <DIV id=\"errormsg\" align=\"center\"\n" +
                "\t\t           STYLE=\"border-style:double; \n" +
                "   \t\t\tborder-width:1; \n" +
                "   \t\t\tposition: absolute;\n" +
                "   \t\t\twidth: 200px;  \n" +
                "   \t\t\tleft:0;\n" +
                "   \t\t\ttop: 0; \n" +
                "   \t\t\tbackground-color:#f0f0f0;\n" +
                "   \t\t\theight: 21px; \n" +
                "   \t\t\tfont-family: 宋体,Arial; font-size: 13px; font-weight: bold;  \n" +
                "   \t\t\tpadding-left: 6px; padding-top: 3px; padding-bottom: 3px;    \n" +
                "   \t\t\tline-height: 145%;\n" +
                "   \t\t\ttext-align: center;  \n" +
                "   \t\t\tcolor: red;                                                               \n" +
                "   \t\t\tfont: bold;\n" +
                "   \t\t\ttext-align:center; filter:alpha(Opacity=90);\">   \n" +
                "\t\t           <li><font color=\"red\">校验信息：该工商营业执照注册号不唯一,已被存款人交行测试３使用,注册地地区代码为100000,基本存款账户开户地地区代码为100000!<br></font></li>\n" +
                "\n" +
                "\t\t           </DIV>";
        Document doc = Jsoup.parse(html);
        Element elements1 = doc.getElementById("errormsg");
        Elements trs = elements1.select("font");
        for (Element tr:trs) {
            String linkText2 = tr.text();
            System.out.println(linkText2);
        }
        String html2 = "792\n" +
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
                "\t<META content=\"/WEB-INF/pages/accmanage/apprnontempinsttemppilot/OpenBasicAccRePrint.jsp\">\n" +
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
                "25b3\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                "\n" +
                "<html>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "<body onscroll=\"workaround();\">\n" +
                "</body>\n" +
                "</html>\n" +
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
                "<html>\n" +
                "<script language=\"JavaScript\">\n" +
                "function doPrint(){\n" +
                "\t//首先打印主页\n" +
                "\t\t//alert(\"准备打印临时存款账户许可证,请将临时存款账户许可证放入打印机!\");\n" +
                "\t\t//再打印副本\n" +
                "\t\t//alert(\"准备打印副本,请放入纸张!\");\n" +
                "       //\talert(\"准备打印存款人查询密码!\");\n" +
                "\n" +
                "\n" +
                "  var actionStr=\"/ams/apprnontempinsttempPilot.do?method=forPrintLic\";\n" +
                "  window.document.forms[0].action=actionStr;\n" +
                "  window.document.forms[0].submit();\n" +
                "\n" +
                "}\n" +
                "function doAction(){\n" +
                "   var actionStr=\"/ams/apprnontempinsttempPilot.do?method=forComplete\";\n" +
                "  window.document.forms[0].action=actionStr;\n" +
                "  window.document.forms[0].submit();\n" +
                "}\n" +
                "\n" +
                "function PrintLicAgainSubmit(){\n" +
                " // if(!doCheck()) return false;\n" +
                "  OpenPrintSelectWindow(\"重打\");\n" +
                "    var printTypeList = document.forms[0].printTypeList.value;\n" +
                "   if (printTypeList != null && printTypeList != \"\"){\n" +
                "       var printTypeArray = printTypeList.split(\"#\");\n" +
                "       var confirmStr = \"\";\n" +
                "       if (printTypeArray[0] == \"ACCOUNT_REPORT_INFO\") {\n" +
                "           confirmStr = \"准备打印账户开户信息,请将纸张放入打印机!\";\n" +
                "       } else if (printTypeArray[0] == \"REPORT_LIC_PWD\") {\n" +
                "           confirmStr = \"准备打印存款人查询密码,请将纸张放入打印机!\";\n" +
                "       }else if (printTypeArray[0] == \"REPORT_BASIC_LIC\") {\n" +
                "           confirmStr = \"准备打印临时存款账户许可证,请将临时存款账户许可证放入打印机!\";\n" +
                "       }\n" +
                "       if(confirmStr != \"\"){\n" +
                "       \talert(confirmStr);\n" +
                "       }\n" +
                "       RePrintLic(); //调用重打!\n" +
                "   }\n" +
                "}\n" +
                "\n" +
                "function checkSacclicNo() {\n" +
                "    var slicno = document.forms[0].elements[\"slicno2\"].value;\n" +
                "    //通过ajax校验开户许可证号!\n" +
                "    var usercode = \"41IL64\";\n" +
                "    theIBankAccessBOProxy.checkSaccLicStateRigth(slicno, \"3\", usercode, setSacclicState);\n" +
                "}\n" +
                "\n" +
                "function setSacclicState(sacclicState) {\n" +
                "    if (sacclicState != \"\" && sacclicState != null && sacclicState != \"0\") {\n" +
                "        alert(sacclicState);\n" +
                "        return false;\n" +
                "    }else{\n" +
                "         document.forms[0].elements[\"sacclicState\"].value = \"true\";\n" +
                "    }\n" +
                "        if( document.forms[0].elements[\"sacclicState\"].value !=\"true\"){\n" +
                "        return false;\n" +
                "    }\n" +
                "\n" +
                "  var returnURL =  \"/ams/printAccPaperPilot.do?method=forFrameForward&headStr=补打\";\n" +
                "  OpenAuthorizeWindow(\"补打许可证\",returnURL);\n" +
                "   var authorize_result =  document.forms[0].authorize_result.value; //授权是否通过\n" +
                "    var printTypeList = document.forms[0].printTypeList.value;\n" +
                "   if (authorize_result == \"1\" && printTypeList != null && printTypeList != \"\"){\n" +
                "       //alert(\"准备打印临时存款账户许可证,请将临时存款账户许可证放入打印机!\")\n" +
                "       RePrintLicAgain(); //调用补打!\n" +
                "   }\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "function RePrintLicSubmit(){\n" +
                "  if(!doCheck()) return false;\n" +
                "\n" +
                "    checkSacclicNo();\n" +
                "\n" +
                "}\n" +
                "\n" +
                "/*  打开打印选择页面*/\n" +
                "function OpenPrintSelectWindow(headStr) {\n" +
                "   document.forms[0].printTypeList.value = \"\"; //再打开选择页面前,将打印类型选择项清空!\n" +
                "   var xposition = 0;\n" +
                "   var yposition = 0;\n" +
                "    if ((parseInt(navigator.appVersion) >= 4 )) {\n" +
                "        xposition = (screen.width - 430) / 2;\n" +
                "        yposition = (screen.height - 250) / 2;\n" +
                "    }\n" +
                "    var res = window.showModalDialog(\"/ams/printAccPaperPilot.do?method=forFrameForward&headStr=\" + headStr, \"_blank\", \"dialogWidth:430px; dialogHeight:250px; dialogLeft:\" + xposition + \"px; dialogTop:\" + yposition + \"px; status:no;scroll:no;resizable=no;\");\n" +
                "    if (res == null || res == \"\") {\n" +
                "        document.forms[0].printTypeList.value = \"\";\n" +
                "        return;\n" +
                "    } else {\n" +
                "     document.forms[0].printTypeList.value = res;\n" +
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
                "    var res = window.showModalDialog(\"/ams/authorize.do?method=forFrameForward&headStr=\" + headStr+\"&returnURL=\"+returnURL, \"_blank\", \"dialogWidth:430px; dialogHeight:250px; dialogLeft:\" + xposition + \"px; dialogTop:\" + yposition + \"px; status:no;scroll:no;resizable=no;\");\n" +
                "    if (res == null || res == \"\") {\n" +
                "        document.forms[0].authorize_result.value = 0;\n" +
                "        return;\n" +
                "    } else {\n" +
                "        //alert(\"授权成功!\");\n" +
                "        document.forms[0].authorize_result.value = 1;\n" +
                "        document.forms[0].printTypeList.value = res;\n" +
                "        \n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function RePrintLic(){\n" +
                "\n" +
                "  var actionStr=\"/ams/apprnontempinsttempPilot.do?method=forRePrintLic\";\n" +
                "  window.document.forms[0].action=actionStr;\n" +
                "  window.document.forms[0].submit();\n" +
                "}\n" +
                "function RePrintLicAgain(){\n" +
                "\n" +
                "  var actionStr=\"/ams/apprnontempinsttempPilot.do?method=forRePrintLicAgain\";\n" +
                "  window.document.forms[0].action=actionStr;\n" +
                "  window.document.forms[0].submit();\n" +
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
                "\t\t\treturn false;\n" +
                "\t\t}\n" +
                "\n" +
                "\treturn true;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "function gotoIndex() {\n" +
                "//parent.location=\"../index.html\";\n" +
                "window.location=\"LocalBasicOpen.html\";\n" +
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
                "\n" +
                "function printByTime() {\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</script>\n" +
                "\n" +
                "<body onload=\"printByTime()\">\n" +
                "<font color=\"red\">  </font>\n" +
                "<form name=\"basicImportantInfoForm\" method=\"post\" action=\"/ams/apprnontempinsttempPilot.do\">\n" +
                "<input type=\"hidden\" name=\"operateType\" value=\"01\">    \n" +
                "<input type=\"hidden\" name=\"printTypeList\" value=\"ACCOUNT_REPORT_INFO\">    \n" +
                "\n" +
                "    <input type=\"hidden\" name=\"printActionType\" value=\"\">    \n" +
                "    \n" +
                "    <input name=\"authorize_result\" type=\"hidden\">\n" +
                "    \n" +
                "    <input type=\"hidden\" name=\"fromNotChecked\" value=\"0\">\n" +
                "\n" +
                "    <input name=\"sacclicState\" type=\"hidden\" value=\"false\">\n" +
                "\n" +
                "<!-- Title -->\n" +
                "\t<TABLE class=\"TitleTable\" align=\"center\">\n" +
                "\t  <TR class=\"TitleRow\">  \n" +
                "\t      <td class=\"TitleLCell\" width=\"34%\">临时存款账户新开户－>核发开户信息</td>\n" +
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
                "      <TD class=\"RltShow\" width=\"30%\" >&nbsp;&nbsp;临时存款账户编号：</TD>\n" +
                "      <TD class=\"RltShow\" width=\"40%\" >&nbsp;&nbsp;<font color=\"#FF0000\">&nbsp;&nbsp;\n" +
                "       L3930000372001\n" +
                "      </font></TD>\n" +
                "    </tr>\n" +
                "    <tr> \n" +
                "      <td class=\"EdtTbLCell\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    <TR> \n" +
                "      <TD class=\"RltShow\" width=\"30%\" >&nbsp;&nbsp;已打印开户许可证编号：</TD>\n" +
                "      <TD class=\"RltShow\" width=\"40%\" >&nbsp;&nbsp;<font color=\"#FF0000\">&nbsp;&nbsp;\n" +
                "       391099200003\n" +
                "          <input type=\"hidden\" name=\"slicno\" value=\"391099200003\">\n" +
                "          \n" +
                "      </font></TD>\n" +
                "    </TR>\n" +
                "    <TR> \n" +
                "      <TD class=\"RltShow\" >&nbsp;</TD>\n" +
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
                "        <TR>\n" +
                "            <!-- <TD id=\"tmp41\" class=\"BtnOK\" width=\"10%\">\n" +
                "    <button class=btns_crams onmouseover=\"this.className='btns_mouseover'\"\n" +
                "          onmouseout=\"this.className='btns_mouseout'\"\n" +
                "          onmousedown=\"this.className='btns_mousedown'\"\n" +
                "          onmouseup=\"this.className='btns_mouseup'\" name=\"res\"  onClick=\"PrintLicAgainSubmit()\">&nbsp;重打开户信息&nbsp;</button>&nbsp;&nbsp;\n" +
                "        </TD> -->\n" +
                "            <TD class=\"BtnOK\" width=\"10%\">\n" +
                "        <button class=btns_crams onmouseover=\"this.className='btns_mouseover'\"\n" +
                "              onmouseout=\"this.className='btns_mouseout'\"\n" +
                "              onmousedown=\"this.className='btns_mousedown'\"\n" +
                "              onmouseup=\"this.className='btns_mouseup'\" name=\"res\"  onClick=\"doAction();\">&nbsp;完&nbsp;&nbsp;成&nbsp;</button>\n" +
                "            </TD>\n" +
                "        </TR>\n" +
                "    </TABLE>\n" +
                "\n" +
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
                "</html>\n" +
                "\n" +
                "\n" +
                "0\n" +
                "\n";
        Document doc2 = Jsoup.parse(html2);
        Elements trs2 = doc2.select("font");
        for (Element tr:trs2) {
            String linkText1 = tr.text().replaceAll(" ", "");
            if(linkText1.contains("L")){
                System.out.println("取消："+linkText1);
            }

        }


    }
}
