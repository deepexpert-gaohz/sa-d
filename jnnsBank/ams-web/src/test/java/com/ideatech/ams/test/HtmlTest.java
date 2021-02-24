package com.ideatech.ams.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlTest {
    public static void main(String[] args) {
        String html = "\n" +
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
                "\t<META content=\"/WEB-INF/pages/businessothers/printopenedacc/PAG10090103.jsp\">\n" +
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
                "19bf\n" +
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
                "<!--\n" +
                " 页面说明： 中国人民银行账户管理系统页面\n" +
                " 版权：北京金电\n" +
                " 功能：其它->打印已开立银行账户清单->已开立其他银行账户查询结果\n" +
                "-->\n" +
                "\n" +
                "\n" +
                "<script language=\"JavaScript\">\n" +
                "\n" +
                "function goToPage(){\n" +
                "    document.forms[0].action=\"/ams/printOpenedAcc.do?method=forInputSaccInfo\";\n" +
                "\tdocument.forms[0].submit();\n" +
                "}\n" +
                "\n" +
                "function print(){\n" +
                "    document.forms[0].action=\"/ams/printOpenedAcc.do?method=forPrint\";\n" +
                "\tdocument.forms[0].submit();\n" +
                "}\n" +
                "\n" +
                "</script>\n" +
                "\n" +
                "<font color=\"red\">  </font>\n" +
                "<form name=\"accChangeForm\" method=\"post\" action=\"/ams/printOpenedAcc.do\">\n" +
                "\n" +
                "\t\n" +
                "\t\n" +
                "\n" +
                "\t<TABLE class=\"TitleTable\" align=\"center\">\n" +
                "\t\t<TR class=\"TitleRow\">\n" +
                "\t\t\t<td class=\"TitleLCell\" width=\"34%\">\n" +
                "\t\t\t\t已开立其他银行账户查询结果\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<td width=\"50%\">\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t</table>\n" +
                "\t<br>\n" +
                "\n" +
                "\t<TABLE width=\"98%\" align=\"center\" cellpadding=\"0\" cellspacing=\"1\"\n" +
                "\t\tclass=\"LstTable\" id=\"myTable\">\n" +
                "\t\t<TR class=\"EdtTbHRow\">\n" +
                "\t\t\t<TD class=\"EdtTbHCCell\" width=\"100%\" colspan=\"10\">\n" +
                "\t\t\t\t:::已开立其他银行账户清单:::\n" +
                "\t\t\t</TD>\n" +
                "\t\t</TR>\n" +
                "\t\t<tr class=\"EdtTbRow\">\n" +
                "\t\t\t<td class=\"EdtTbRCell\" width=\"20%\">\n" +
                "\t\t\t\t存款人名称\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<TD class=\"EdtTbLCell\" colspan=\"9\">\n" +
                "\t\t\t\t浙江泓浄检测技术有限公司\n" +
                "\t\t\t</TD>\n" +
                "\t\t</TR>\n" +
                "\t\t<tr class=\"EdtTbRow\">\n" +
                "\t\t\t<td class=\"EdtTbRCell\">\n" +
                "\t\t\t\t基本存款账户开户许可证核准号\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<TD class=\"EdtTbLCell\" colspan=\"9\">\n" +
                "\t\t\t\tJ3310108057301\n" +
                "\t\t\t</TD>\n" +
                "\t\t</TR>\n" +
                "\t\t<tr class=\"EdtTbRow\">\n" +
                "\t\t\t<td class=\"EdtTbRCell\">\n" +
                "\t\t\t\t基本存款账户销户日期\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<TD colspan=\"9\" class=\"EdtTbLCell\">\n" +
                "\t\t\t\t\n" +
                "\t\t\t</TD>\n" +
                "\t\t</tr>\n" +
                "\n" +
                "\t\t<tr class=\"EdtTbRow\">\n" +
                "\t\t\t<TD class=\"LstTbHCell\" colspan=\"10\">\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\t<table width=\"100%\" class=\"classicLook\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\tcellpadding=\"0\">\n" +
                "\t\t\t\t\t\t<tr><td><div id='defaulttabletagname'><table class='classicLook' cellspacing='0' cellpadding='0'  width='100%'><tr>    <th nowrap=\"true\" >&nbsp;序号&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;开户地地区代码&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;开户银行名称&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;账户性质&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;账号&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;账户名称&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;开户日期&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;账户状态&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;久悬日期&nbsp;</th>\n" +
                "    <th nowrap=\"true\" >&nbsp;销户日期&nbsp;</th>\n" +
                "</tr>\n" +
                "<tr class=\"classicLook0\" >\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<td class=\"classicLook0\" >&nbsp;1&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;331000&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;杭州银行滨江支行&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;基本存款账户&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;3301040160012647551&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;浙江泓浄检测技术有限公司&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;2019-03-22&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;正常&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;-&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<td align=\"left\" class=\"classicLook0\" >&nbsp;-&nbsp;</td>\n" +
                "\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "</table></div></td></tr>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" >\n" +
                "<tr>\n" +
                "<td align=\"left\">\n" +
                "共1行,  第1页 共1页 </td>\n" +
                "<td align=\"right\">\n" +
                "\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" >\n" +
                "  <tr>\n" +
                "   <td><img alt=\"First\" src=\"jsp/images/table/first(off).gif\" border=\"0\"/></td>\n" +
                "   <td><img alt=\"Previous\" src=\"jsp/images/table/previous(off).gif\" border=\"0\"/></td>\n" +
                "   <td><img alt=\"Next\" src=\"jsp/images/table/forward(off).gif\" border=\"0\"/></td>\n" +
                "   <td><img alt=\"Last\" src=\"jsp/images/table/last(off).gif\" border=\"0\"/></td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "<script language='JavaScript'> \n" +
                " function doPage(params) {\n" +
                "var ipage=window.document.accChangeForm.elements['pagenum'].value;\n" +
                " if(isWhitespace(ipage))\n" +
                "{\n" +
                " alert('请录入页码！');\n" +
                "return false;\n" +
                "}\n" +
                " if(!IsNumber(ipage))\n" +
                "{\n" +
                " alert('页码必须是数字！');\n" +
                "return false;\n" +
                "}\n" +
                " if(ipage<=0)\n" +
                "{\n" +
                " alert('页码应该大于0！');\n" +
                "return false;\n" +
                "}\n" +
                " if(ipage>1)\n" +
                "{\n" +
                " alert('超过最大页数！');\n" +
                "return false;\n" +
                "}\n" +
                "window.document.accChangeForm.action=\"/ams/printOpenedAcc.do?method=forSearchPrintBasicSubmit&sdeletereason=&back=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B&accEntAccountInfo.sacclicno=J3310108057301&sdeppasswork=695993&\"+\"pagingPage=\"+window.document.accChangeForm.elements['pagenum'].value+'&ongopagesub=true';\n" +
                " window.document.accChangeForm.submit(); \n" +
                "}\n" +
                "</script>\n" +
                "<td>\n" +
                "<input type=\"text\" name=\"pagenum\" value=\"\" size=\"3\" height=\"1\" align=\"middle\" />\n" +
                "<a href=\"#\" onclick=\"javascript:doPage();\"><img alt=\"GoPage\" src=\"jsp/images/table/forward(on).gif\" border=\"0\" align=\"middle\" /></a>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "\n" +
                "\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\n" +
                "\t\t\t</TD>\n" +
                "\t\t</tr>\n" +
                "\t</TABLE>\n" +
                "\n" +
                "\n" +
                "\t<TABLE class=\"FooterTable\" align=\"center\">\n" +
                "\t\t<TR>\n" +
                "\t\t\t<TD>\n" +
                "\t\t\t\t<p align=\"center\">\n" +
                "\t\t\t\t\t<font color=\"#FF0000\">&nbsp;</font>\n" +
                "\t\t\t\t</p>\n" +
                "\t\t\t</TD>\n" +
                "\t\t</TR>\n" +
                "\t</TABLE>\n" +
                "\t<!-- Button -->\n" +
                "\t<TABLE class=\"BtnTable\" align=\"center\">\n" +
                "\t\t<TR>\n" +
                "\t\t\t<TD class=\"BtnOK\">\n" +
                "\t\t\t\t<button class=btns_crams\n" +
                "\t\t\t\t\tonmouseover=\"this.className='btns_mouseover'\"\n" +
                "\t\t\t\t\tonmouseout=\"this.className='btns_mouseout'\"\n" +
                "\t\t\t\t\tonmousedown=\"this.className='btns_mousedown'\"\n" +
                "\t\t\t\t\tonmouseup=\"this.className='btns_mouseup'\" name=\"sub\"\n" +
                "\t\t\t\t\tonClick=\"print()\">\n" +
                "\t\t\t\t\t&nbsp;打&nbsp;&nbsp;印&nbsp;\n" +
                "\t\t\t\t</button>\n" +
                "\t\t\t\t&nbsp;&nbsp;\n" +
                "\t\t\t\t<button class=btns_crams\n" +
                "\t\t\t\t\tonmouseover=\"this.className='btns_mouseover'\"\n" +
                "\t\t\t\t\tonmouseout=\"this.className='btns_mouseout'\"\n" +
                "\t\t\t\t\tonmousedown=\"this.className='btns_mousedown'\"\n" +
                "\t\t\t\t\tonmouseup=\"this.className='btns_mouseup'\" name=\"back\"\n" +
                "\t\t\t\t\tonClick=\"goToPage()\">\n" +
                "\t\t\t\t\t&nbsp;完&nbsp;&nbsp;成&nbsp;\n" +
                "\t\t\t\t</button>\n" +
                "\t\t</TR>\n" +
                "\t</TABLE>\n" +
                "</form>\n" +
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
                "\n";
        Document doc = Jsoup.parse(html);
        Element elements1 = doc.getElementById("defaulttabletagname");
        Elements trs = elements1.select(".classicLook").select("tr");
        for (Element tr:trs) {
            String linkText2 = tr.text();
            if(linkText2.contains("开户地地区代码 ")){
                continue;
            }
            Elements tds = tr.select("td");
            for (Element td:tds) {
                String tdText = td.text();
                System.out.println("td:"+tdText);
            }

        }
        /*for (int i = 0; i < elements1.size(); i++) {
            Elements tds = elements1.get(i).select("td");
            for (int j = 0; j < tds.size(); j++)
            {
                String oldClose = tds.get(j).text();
                if(oldClose.contains("存款人名称")){
                    System.out.println("存款人名称："+tds.get(1).text());
                    continue;
                }else if(oldClose.contains("基本存款账户开户许可证核准号")){
                    System.out.println("基本存款账户开户许可证核准号："+tds.get(1).text());
                    continue;
                }else{
                }
            }
        }*/
        String html2 = "781\n" +
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
                "\t<META content=\"/WEB-INF/pages/accmanage/accclose/pilot/1PAG10030104.jsp\">\n" +
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
                "163e\n" +
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
                "<head>\n" +
                "\n" +
                "<!--\n" +
                " 页面说明： 中国人民银行账户管理系统页面\n" +
                " 版权：北京金电\n" +
                " 功能：账户管理 基本存款账户开户-新开户成功打印页面\n" +
                "-->\n" +
                "\n" +
                "<script language=\"JavaScript\">\n" +
                "    function initPage() {\n" +
                "\n" +
                "\n" +
                "\n" +
                "        alert(\"请打印已开立其他银行账户清单!\");\n" +
                "    \n" +
                "    \n" +
                "                                                          \n" +
                "    }\n" +
                "\n" +
                "\n" +
                "function goFinish(){\n" +
                "\tself.location=\"/ams/closeBasicPilot.do?method=forSearchCloseBasic\";\n" +
                "\treturn true; \n" +
                "}\n" +
                "\n" +
                "function openNewWindow(URL,Win){\n" +
                "\tif(Win==\"\" || Win==null){\n" +
                "\t\tWin=\"Popup\"\n" +
                "\t}\n" +
                "\twindowprops\t= \"width=430,height=250,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,top=170,left=200\";\n" +
                "\t//windowprops\t= \"height=\" + Hei + \",width=\"+ Wid + \",location=no,scrollbars=yes,status=no,menubars=no,toolbars=no\";\n" +
                "\twindow.open(URL, \"Popup\", windowprops);\n" +
                "}\n" +
                "\n" +
                "\n" +
                "function openNewWindow1(URL,Win){\n" +
                "\tif(Win==\"\" || Win==null){\n" +
                "\t\tWin=\"Popup\"\n" +
                "\t}\n" +
                "\twindowprops\t= \"width=620,height=430,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes,top=170,left=200\";\n" +
                "\t//windowprops\t= \"height=\" + Hei + \",width=\"+ Wid + \",location=no,scrollbars=yes,status=no,menubars=no,toolbars=no\";\n" +
                "\twindow.open(URL, \"Popup\", windowprops);\n" +
                "}\n" +
                "</script>\n" +
                "\n" +
                "</head>\n" +
                "<body onload=\"initPage()\">\n" +
                "<font color=\"red\">  </font>\n" +
                "<form name=\"accChangeForm\" method=\"post\" action=\"/ams/closeBasicPilot.do\">\n" +
                "\n" +
                "\n" +
                "<input type=\"hidden\" name=\"sdeletereason\" value=\"1\">\n" +
                "\n" +
                "<input type=\"hidden\" name=\"iaccstate\" value=\"4\">\n" +
                "\n" +
                "<br>\n" +
                "<!-- Title -->\n" +
                "\t<TABLE class=\"TitleTable\" align=\"center\">\n" +
                "\t\t<TR class=\"TitleRow\">  \n" +
                "\t      \n" +
                "    <td  class=\"TitleLCell\" width=\"34%\" >撤销银行结算账户－>完成销户</td>\n" +
                "\t      <td width=\"50%\"></td>\n" +
                "\t    </tr>                                                        \n" +
                "\t  </table>\n" +
                "\n" +
                "\n" +
                "<br><br>    \n" +
                "<TABLE cellspacing=\"1\" cellpadding=\"0\" align=\"center\" width=\"70%\">\n" +
                "      <tr>\t\n" +
                "\t<TD class=\"RltShowResult\"  align=\"center\">销户成功!</TD>\n" +
                "      </tr>\n" +
                "</TABLE>\n" +
                "\n" +
                "<br><br>\n" +
                "\n" +
                "<TABLE class=\"FooterTable\" align=\"center\">\n" +
                "\t<TR>\n" +
                "\t\t<TD></TD>\n" +
                "\t</TR>\n" +
                "</TABLE>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div>\n" +
                "<TABLE width=\"800\" align=\"center\" cellpadding=\"0\" cellspacing=\"1\" class=\"classicLook\"  id=\"defaultprinttagname\">\n" +
                "\t<TR class=\"EdtTbHRow\">\n" +
                "\t\t<TD class=\"EdtTbHCCell\" width=\"100%\" colspan=\"10\">:::已开立其他银行账户清单:::</TD>\n" +
                "\t</TR>\n" +
                "\t<tr class=\"LstTbEveRow\">\n" +
                "\t\t<td class=\"LstTbCell\" colspan=\"2\">存款人名称</td>\n" +
                "\t    <TD class=\"EdtTbLCell\" colspan=\"8\">测试十一</TD>\n" +
                "\t</TR>\n" +
                "\t<tr class=\"LstTbEveRow\">\n" +
                "\t\t<td class=\"LstTbCell\" colspan=\"2\">基本存款账户编号</td>\n" +
                "\t    <TD class=\"EdtTbLCell\" colspan=\"8\">J1000229281309</TD>\n" +
                "\t</TR>\n" +
                "\t<tr class=\"LstTbHRow\">\n" +
                "\t  <td class=\"LstTbCell\" colspan=\"2\">基本存款账户销户日期</td>\n" +
                "\t  <TD colspan=\"8\" class=\"LstTbHCell\"><div align=\"left\" class=\"EdtTbLCell\">2019-04-18</div></TD>\n" +
                "    </tr>\n" +
                "    \n" +
                "    <tr class=\"LstTbHRow\">\n" +
                "\t\t<td class=\"LstTbHCell\">顺序</td>\n" +
                "\t    <TD class=\"LstTbHCell\">开户地地区代码</TD>\n" +
                "\t    <TD class=\"LstTbHCell\" >开户银行名称</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\">账户性质</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\" width=\"100\">账号</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\" width=\"200\">账户名称</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\" nowrap=\"true\">开户日期</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\" nowrap=\"true\">账户状态</TD>\n" +
                "\t\t<TD class=\"LstTbHCell\" nowrap=\"true\">销户日期</TD>\n" +
                "    </tr>\n" +
                "        \n" +
                "        \n" +
                "\n" +
                "\t<tr >\n" +
                "\t   \t<td >1&nbsp;</td>\n" +
                "\t   \t<td >\n" +
                "               100000（北京市）&nbsp;\n" +
                "           </td>\n" +
                "\t   \t<td  >\n" +
                "               盛京银行股份有限公司北京通州支行&nbsp;</td>\n" +
                "\t   \t<td >\n" +
                "               基本存款账户&nbsp;</td>\n" +
                "\t   \t<td >0110900102000001&nbsp;</td>    \n" +
                "\t   \t<td width=\"250\"><div>\n" +
                "\n" +
                "               测试十一\n" +
                "               <!--</textarea>-->\n" +
                "           </div>&nbsp;</td>                                               \n" +
                "\t\t<td nowrap=\"true\">2019-04-08&nbsp;</td>\n" +
                "\t\t<td nowrap=\"true\">撤销&nbsp;</td>\n" +
                "\t\t<td nowrap=\"true\">2019-04-18&nbsp;</td>\n" +
                "\t</tr>\n" +
                "    \n" +
                "\t\n" +
                "\n" +
                "\n" +
                "</TABLE>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<!-- Button -->\n" +
                "<TABLE class=\"BtnTable\" align=\"center\">\n" +
                "\t<TR>\n" +
                "\t\t<TD class=\"BtnOK\">\n" +
                "                \n" +
                "                \n" +
                "\n" +
                "        <button class=btns_crams onmouseover=\"this.className='btns_mouseover'\" onmouseout=\"this.className='btns_mouseout'\"\n" +
                "                    onmousedown=\"this.className='btns_mousedown'\" onmouseup=\"this.className='btns_mouseup'\" name=\"prn\"\n" +
                "                    onClick=\"printpgWithBorder();\">\n" +
                "                    &nbsp;打&nbsp;&nbsp;印&nbsp;\n" +
                "                </button>   &nbsp;&nbsp;\n" +
                "        \n" +
                "        \n" +
                "            \n" +
                "  <button class=btns_crams onmouseover=\"this.className='btns_mouseover'\"\n" +
                "\t\t  onmouseout=\"this.className='btns_mouseout'\"\n" +
                "\t\t  onmousedown=\"this.className='btns_mousedown'\"\n" +
                "\t\t  onmouseup=\"this.className='btns_mouseup'\" name=\"finish\"  onClick=\"javascript:goFinish();\">&nbsp;完&nbsp;&nbsp;成&nbsp;</button>  \n" +
                "\t</TR>\n" +
                "</TABLE>\n" +
                "\n" +
                "<br>\n" +
                "</body>\n" +
                "    </form>\n" +
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
        Element elements2 = doc2.getElementById("defaultprinttagname");
        Elements trs2 = elements2.select("tr");
        for (Element tr:trs2) {
            Elements tds = tr.select("td");
            if(tds.size()==9){
                for (Element td:tds) {
                    String tdText = td.text();
                    System.out.println("销户td:"+tdText);
                }
            }else{
                continue;
            }
        }

    }
}
