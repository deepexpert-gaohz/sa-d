package com.ideatech.common.util;


import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * 短信格式化
 */
public class MsgFormat {

    /**
     * 将短信模板中的数据替换成实体中的数据
     *
     * @param message 短信模板
     * @param temp    数据源实体
     */
    public static String formatMessage(String message, Object temp) {
        Map<String, String> fieldMap = ReflectionUtil.getFieldMapJustStringAndDate(temp);
        String newMessage = message;
        Iterator<Map.Entry<String, String>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            newMessage = StringUtils.replace(newMessage, "{" + entry.getKey() + "}", entry.getValue());
        }
        return newMessage;
    }

}
