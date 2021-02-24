package com.ideatech.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author wangqingan
 * @version 2018/4/24 4:53 PM
 */
public class BrowserUtil {

    /**
     * 判断是否是IE浏览器
     */
    public static boolean isMSBrowser(HttpServletRequest request) {
        String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal)){
                return true;
            }
        }
        return false;
    }

    public static String generateFileName(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
        //如果是IE浏览器，则用URLEncode解析
        if(isMSBrowser(request)){
            return URLEncoder.encode(fileName, "UTF-8");
        }else{//如果是谷歌、火狐则解析为ISO-8859-1
            return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
    }
}
