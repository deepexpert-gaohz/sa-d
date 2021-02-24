package com.ideatech.ams.pbc.utils;

public class StringUtils {
    public static String trimSpace(String text){
        text = text.replace("\u00A0", "");
        text = org.apache.commons.lang.StringUtils.trim(text);
        return text;
    }
}
