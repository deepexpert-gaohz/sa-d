package com.ideatech.ams.pbc.common;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

import java.util.*;
public class HeadsCache {
    
    // 账管、信用代码证、农信经管、支票违规
    public static enum HEADER_TYPES {
        RHZG, XYDMZ, NXJG, ZPWG
    };
    
    final private static String[] HEADER_TYPES_KEY = {
        "rhzg", "xydmz", "nxjg", "zpwg"
    };
    
    public static Map<String, List<Header>> HEAD_CACHE = new HashMap<String, List<Header>>();
    
    private static Document document = null;
    static {
        init();
    }
    
    public static void init() {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <root>  <!-- 人行账管 -->   <headers type=\"rhzg\">         <header id=\"common\">          <item key=\"Pragma\">no-cache</item>            <item key=\"Host\">${config.zg.ip}</item>           <item key=\"Proxy-Connection\">Keep-Alive</item>            <item key=\"Accept\">text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8</item>             <item key=\"Connection\">Keep-Alive</item>          <item key=\"Accept-Language\">zh-cn</item>          <item key=\"User-Agent\">Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; Tablet PC 2.0; BOIE9;ZHCN)</item>            <!-- item key=\"Accept-Encoding\">gzip,deflate</item 该方式还没有支持-->            <item key=\"Accept-Charset\">GBK,utf-8;q=0.7,*;q=0.3</item>         </header>       <header id=\"queryAnnual_down\">          <item key=\"Accept\">image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*</item>             <item key=\"Referer\">http://${config.zg.ip}/ams/query005.do?method=forShowQueryConditions</item>       </header> <header id=\"query_down\">          <item key=\"Accept\">image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*</item>             <item key=\"Referer\">http://${config.zg.ip}/ams/query002.do?method=forShowQueryConditions</item>       </header>      <header id=\"do_down\">             <item key=\"Accept\">image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*</item>         </header>       <header id=\"zg_annual_post\">          <item key=\"Referer\">http://${config.zg.ip}/ams/accYearCheck.do?method=forshowQueryCondition</item>        </header>       <header id=\"zg_annual_submit\">            <item key=\"Referer\">http://${config.zg.ip}/ams/accYearCheck.do?method=forSearchAcc</item>         </header>       <header id=\"account_open\">            <item key=\"Referer\">http://${config.zg.ip}/ams/unApproveAccOpen.do?method=forInputAccSubmit</item>        </header>   </headers>      <!-- 信用代码证 -->  <headers type=\"xydmz\">        <header id=\"common\">          <item key=\"Pragma\">no-cache</item>            <item key=\"Host\">9.96.47.2</item>             <item key=\"Proxy-Connection\">Keep-Alive</item>            <item key=\"Accept\">text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8</item>             <item key=\"Connection\">Keep-Alive</item>          <item key=\"Accept-Language\">zh-cn</item>          <item key=\"User-Agent\">Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; Tablet PC 2.0; BOIE9;ZHCN)</item>            <!-- item key=\"Accept-Encoding\">gzip,deflate</item 该方式还没有支持-->            <item key=\"Accept-Charset\">GBK,utf-8;q=0.7,*;q=0.3</item>         </header>   </headers>      <!-- 杭州支票支票违规系统 -->     <headers type=\"zpwg\">         <header id=\"common\">          <item key=\"Pragma\">no-cache</item>            <item key=\"Host\">9.96.47.13</item>            <item key=\"Accept\">text/html, application/xhtml+xml, */*</item>           <item key=\"Connection\">Keep-Alive</item>          <item key=\"Accept-Language\">zh-cn</item>          <item key=\"User-Agent\">Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; Tablet PC 2.0; BOIE9;ZHCN)</item>            <item key=\"Accept-Encoding\">gzip, deflate</item>          <item key=\"Content-Type\">application/x-www-form-urlencoded</item>         </header>       <header id=\"check2\">          <item key=\"Referer\">http://9.96.47.13/zpcfnew/MultAgreementInputCheck.asp</item>      </header>       <header id=\"newcheck2\">           <item key=\"Referer\">http://9.96.47.13/zpcfnew/MultAgreementInputNewCheck2.asp</item>      </header>   </headers>      <!-- 信用代码证 -->  <headers type=\"nxjg\">         <header id=\"common\">          <!--            <item key=\"Cache-Control\">no-store,no-cache=set-cookie</item>             <item key=\"Date\">Wed, 23 Jan 2013 05:35:53 GMT</item>             <item key=\"Expires\">Thu, 01 Jan 1970 00:00:00 GMT</item>          <item key=\"Pragma\">No-cache</item>            <item key=\"Set-Cookie\">JSESSIONID=0000m2NWXd5s-xdKd7Ten-e_59Q:14naihlni; Path=/</item>            <item key=\"Content-Type\">text/plain</item>            <item key=\"Content-Language\">zh-CN</item>             <item key=\"Server\">IBM_HTTP_Server</item>             <item key=\"Connection\">close</item>           <item key=\"Location\">http://154.233.15.23/rcubbi_web/menu.do</item>           -->             <item key=\"Cache-Control\">no-store,no-cache=set-cookie</item>             <item key=\"Date\">Wed, 23 Jan 2013 05:35:53 GMT</item>             <item key=\"Expires\">Thu, 01 Jan 1970 00:00:00 GMT</item>          <item key=\"Pragma\">No-cache</item>            <item key=\"Accept\">text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8</item>             <item key=\"Accept-Encoding\">gzip, deflate</item>          <item key=\"Accept-Language\">zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3</item>            <item key=\"User-Agent\">Mozilla/5.0 (Windows NT 5.2; rv:14.0) Gecko/20100101 Firefox/14.0.1</item>             <item key=\"Connection\">keep-alive</item>          <item key=\"Host\">154.233.15.23</item>             <item key=\"Set-Cookie\">JSESSIONID=0000m2NWXd5s-xdKd7Ten-e_59Q:14naihlni; Path=/</item>            <item key=\"Content-Type\">text/plain</item>            <item key=\"Content-Language\">zh-CN</item>             <item key=\"Server\">IBM_HTTP_Server</item>         </header>   </headers> </root>";
            document = Jsoup.parse(xml, "", new Parser(new XmlTreeBuilder()));
            Elements allHeads = document.select("headers");
            for (Element headType : allHeads) {
                String headName = headType.attr("type");
                Elements heads = headType.select("header");
                for (Element head : heads) {
                    String headKey = head.attr("id");
                    List<Header> list = new ArrayList<Header>();
                    Elements items = head.getElementsByTag("item");
                    for (Element item : items) {
                        String key = (item.attr("key") + "").trim();
                        String text = (item.text() + "").trim();
                        text = replace(text);
                        list.add(new BasicHeader(key, text));
                    }
                    HEAD_CACHE.put(headName + "_" + headKey, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("XML读取异常！");
        }
    }
    
    private static String replace(String val) {
        return val;
    }
    
    public static String getType(HEADER_TYPES type) {
        return HEADER_TYPES_KEY[type.ordinal()];
    }
    
    public static List<Header> getCommonHeader(HEADER_TYPES type) {
        String typeKey = getType(type);
        List<Header> common = HEAD_CACHE.get(typeKey + "_common");
        List<Header> list = new ArrayList<Header>();
        list.addAll(common);
        return list;
    }
    
    public static List<Header> getHeader(HEADER_TYPES type, String key) {
        key = getType(type) + "_" + key;
        List<Header> common = getCommonHeader(type);
        if (!HEAD_CACHE.containsKey(key)) {
            return common;
        }
        List<Header> data = HEAD_CACHE.get(key);
        Map<String, Header> map = new HashMap<String, Header>();
        for (Header h : common) {
            map.put(h.getName(), h);
        }
        if (data != null) {
            for (Header h : data) {
                map.put(h.getName(), h);
            }
        }
        List<Header> list = new ArrayList<Header>();
        for (String mapkey : map.keySet()) {
            Header newHead = map.get(mapkey);
            list.add(newHead);
        }
        return list;
    }
    
    public static List<Header> getXydmzCommon() {
        return getCommonHeader(HEADER_TYPES.XYDMZ);
    }
    
    public static List<Header> getRhzgCommon() {
        return getCommonHeader(HEADER_TYPES.RHZG);
    }
    
    public static List<Header> getRhzgCommon(String ip) {
        List<Header> list = getCommonHeader(HEADER_TYPES.RHZG);
        return addHostToHeader(list, ip);
    }
    
    public static List<Header> getNxjgCommon() {
        return getCommonHeader(HEADER_TYPES.NXJG);
    }
    
    public static List<Header> getZpwgCommon() {
        return getCommonHeader(HEADER_TYPES.ZPWG);
    }
    
    public static List<Header> getZpwgHeader(String key) {
        return getHeader(HEADER_TYPES.ZPWG, key);
    }
    
    public static List<Header> getRhzgHeader(String key) {
        return getHeader(HEADER_TYPES.RHZG, key);
    }
    
    public static List<Header> addHostToHeader(List<Header> headerList, String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            ip = ip.replace("http://", "");
            Iterator<Header> it = headerList.iterator();
            while (it.hasNext()) {
                Header header = it.next();
                if (header.getName().equals("Host")) {
                    it.remove();
                }
            }
            headerList.add(new BasicHeader("Host", ip));
        }
        return headerList;
    }
}
