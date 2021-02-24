package com.ideatech.ams.pbc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtils {
    
    /**
     * 用途：检查输入字符串是否只由汉字、字母、数字组成 入：value, 字符串 返回：如果通过验证返回true,否则返回false
     */
    public static boolean isNumberOrLetter(String str){
        boolean result = false;
        String regEx = "^[0-9a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        result = matcher.matches();
        return result;
    }
    
    /**
     * 则判断小数 比如输入的金额
     * @param str
     * @return
     */
    public static boolean isDecimal(String str){
        String regEx = "([1-9]+[0-9]*|0)(\\.[\\d]+)?";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.matches();
        return result;
    }
    
    /**
     * 验证正确的正则表达式
     * @param str
     * @return
     */
    public static boolean isZipCOde(String str){
        String regEx = "[0-9]\\d{5}(?!\\d)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    /**
     * 判断电话号码
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
//        String regEx = "^\\d{3}-?\\d{7,8}|\\d{4}-?\\d{7,8}|\\d{7,8}$";
        Pattern p1 = Pattern.compile("^\\d{3}-?\\d{7,8}|\\d{4}-?\\d{7,8}|\\d{7,8}$");
//        Pattern p2 = Pattern.compile("^(0[0-9]{2,3}\\-)?([1-9][0-9]{6,7})$");

//        if (str.startsWith("1")) {
            if (!(p1.matcher(str).matches())) {
                return false;
            }
//        }
        return true;
//        else if (str.startsWith("0") && !(str.length() < 16 && p2.matcher(str).matches())) {
//            return false;
//        }
//        else {
//            return true;
//        }
    }
    
    /**
     * 去除字符串中的中文
     * @param str
     * @return
     */
    public static String delChinese(String str){
    	String regEx = "[\u2E80-\u9FFF]"; 
        Pattern pattern = Pattern.compile(regEx);
        Matcher mat = pattern.matcher(str);
        String repickStr = mat.replaceAll(""); 
        return repickStr;
    }
    
    /**
     * 是否包含中文
     * @param str
     * @return
     */
    public static Boolean isExistChinese(String str){
    	String regEx = "[\u2E80-\u9FFF]"; 
        Pattern pattern = Pattern.compile(regEx);
        Matcher mat = pattern.matcher(str);
        if (mat.find()) {
            return true;
        }
        return false;
    }
   
    /**
     * 截取两头的空字符串,包含中文全角下的空字符串
     * 
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        input = trim(input);
        return input == null || input.length() == 0;
    }
    
    /**
     * 判断是否是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    
    /**
     * 截取两头的空字符串,包含中文全角下的空字符串
     * 
     * @param input
     * @return
     */
    public static String trim(String input) {
        if (input == null || input.length() == 0) return "";
        input = input.trim();
        int len = input.length();
        int count = len;
        int st = 0;
        int off = 0;
        char[] val = input.toCharArray();
        while (st < len && (val[off + st] <= ' ' || val[off + st] == 12288)) {
            st++;
        }
        while (st < len && (val[off + len - 1] <= ' ' || val[off + len - 1] == 12288)) {
            len--;
        }
        return ((st > 0) || (len < count)) ? new String(input.substring(st, len)) : input;
    }


    /**
     * 截取字符串
     * @param html
     * @param str1
     * @param str2
     * @return
     */
    public static String cutStr(String html, String str1, String str2) {
        String[] cutTags = new String[] {
            str1, str2
        };
        if (isEmpty(html)) return "";
        String[] tempArrays = null;
        int cutPoint = -1;
        for (int i = 0; i < cutTags.length; i++) {
            String ct = cutTags[i];
            if (isEmpty(ct)) continue;
            tempArrays = ct.split("\\|");
            for (String str : tempArrays) {
                cutPoint = html.indexOf(str);
                if (cutPoint == -1) continue;
                // 第一个标记为是截取开始
                if (i == 0)
                    html = new String(html.substring(cutPoint + str.length()));
                else
                    html = new String(html.substring(0, cutPoint));
                break;
            }
        }
        return html;
    }
    
    /**
     * 匹配正则
     * 
     * @param regex
     * @param source
     * @return
     */
    public static String matcher(String regex, String source) {
        Pattern pp = Pattern.compile(regex);
        Matcher pm = pp.matcher(source);
        if (pm.find()) {
            source = (pm.group(1) + "").trim();
            return source;
        }
        return null;
    }
    
}
