package com.ideatech.ams.image.utils;

import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * 双录短信模版格式化工具，功能待加强。
 */
public class MessageFormat {

    public static String formatMessage(String message, Object temp){
        Map<String, String> fieldMap = beanToMap(temp);
        Iterator<Map.Entry<String, String>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue()!=null){
                message = StringUtils.replace(message,"{"+entry.getKey()+"}",entry.getValue());
            }
        }
        return message;
    }

    /**
     * 将对象装换为map<String,String>
     * @param bean
     * @return
     */
    public static <T> Map<String, String> beanToMap(T bean) {
        Map<String, String> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(String.valueOf(key),beanMap.get(key)==null?null:String.valueOf(beanMap.get(key)));
            }
        }
        return map;
    }

}
