package com.ideatech.common.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 针对不同浏览器，处理下载中文扩展名
 * Created with IntelliJ IDEA.
 * User: guozp
 * Date: 13-8-18
 * Time: 下午8:52
 * To change this template use File | Settings | File Templates.
 */
public class FileExtraUtils {

    public final static int BROWSER_TYPE_OTHERS = 0x000;
    public final static int BROWSER_TYPE_IE = 0x001;
    public final static int BROWSER_TYPE_FIREFOX = 0x002;
    public final static int BROWSER_TYPE_SAFARI = 0x003;
    public final static int BROWSER_TYPE_OPERA = 0x004;
    public final static int BROWSER_TYPE_CHROME = 0x005;

    /**
     * 针对不同浏览器，中文文件名的乱码问题，对不同的attachment的filename进行处理，返回处理后的结果
     * @param request
     * @param filename 原文件名
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String handleFileName(HttpServletRequest request,  String filename) throws UnsupportedEncodingException {
        String res = filename;
        switch (getBrowserType(request)){
            case BROWSER_TYPE_FIREFOX:
           //     res = "=?UTF-8?B?" + (new String(Base64.encodeBase64(filename.getBytes("UTF-8")),"iso-8859-1")) + "?=";
                break;
            case BROWSER_TYPE_CHROME:
            case BROWSER_TYPE_SAFARI:
            case BROWSER_TYPE_OPERA:
                res = new String(filename.getBytes("UTF-8"),"iso-8859-1");
                break;
            case BROWSER_TYPE_IE:
                res = java.net.URLEncoder.encode(filename,"UTF-8");
                if(res.length() > 150){//IE6 bug：文件名过长
                    res = new String(res.getBytes("GBK"),"iso8859-1");
                }
                break;
            case BROWSER_TYPE_OTHERS:
            default:
                res = java.net.URLEncoder.encode(filename,"UTF-8");
                break;
        }
        return res;
    }

    /**
     * 获取浏览器的类型
     * @param request
     * @return
     */
    public static int getBrowserType(HttpServletRequest request){
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        if(StringUtils.isNotEmpty(userAgent)){
            if (userAgent.indexOf("msie") >=0 ) return BROWSER_TYPE_IE;
            if (userAgent.indexOf("firefox") >= 0) return BROWSER_TYPE_FIREFOX;
            if (userAgent.indexOf("safari") >= 0) return BROWSER_TYPE_SAFARI;
            if (userAgent.indexOf("opera") >= 0) return BROWSER_TYPE_OPERA;
            if (userAgent.indexOf("applewebkit") >= 0) return BROWSER_TYPE_CHROME;
        }
        return BROWSER_TYPE_OTHERS;
    }
}
