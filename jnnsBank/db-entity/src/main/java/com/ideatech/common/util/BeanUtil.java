package com.ideatech.common.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description 反射PO对象属性
 * @Author wanghongjie
 * @Date 2018/10/16
 **/
@Slf4j
public class BeanUtil {

    /**
     * 反射获取属性长度--只限String类型
     * @return
     */
    public static Map<String, Integer> fieldMapForErrorAccount(Class<?> clazz){
        List<Field> fields = ReflectionUtil.getFieldsIncludingSuperClasses(clazz);
        Map<String, Integer> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            if(field.getType() == String.class){
                if(field.isAnnotationPresent(Column.class)){
                    Column myAnnotation = field.getAnnotation(Column.class);
                    int length = myAnnotation.length();
                    if(length ==0){
                        length = 255;
                    }
                    if(field.isAnnotationPresent(Lob.class)) {
                        if("businessScope".equalsIgnoreCase(field.getName())) {  //年检核心数据导入上报人行长度限制489个汉字
                            fieldMap.put(field.getName().toUpperCase(),489);
                        } else {
                            fieldMap.put(field.getName().toUpperCase(),-1);
                        }
                    }else {
                        fieldMap.put(field.getName().toUpperCase(),length);
                    }
                }
            }
        }
        return fieldMap;
    }



    /**
     * 判断字符长度
     * @param obj
     * @param stringIntegerMap
     * @return
     */
    public static <T> String checkStringLength(T obj,Map<String, Integer> stringIntegerMap){
        Map<String, Field> fieldMap = ReflectionUtil.getFieldObjMap(obj);
        Iterator<Map.Entry<String, Field>> iterator = fieldMap.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()){
            Map.Entry<String, Field> next = iterator.next();
            String key = next.getKey();//字段大写
            Field field = next.getValue();
            if(field.getType() == String.class && stringIntegerMap.containsKey(key)){
                String fieldName = field.getName();//字段原来的名字
                Method getMethod = ReflectionUtil.getGetter(obj.getClass(), field);
                String value = null;
                try {
                    value = (String) getMethod.invoke(obj);
                } catch (Exception e) {
                    log.error("错误使用反射get方法: {}", e, key);
                }
                if(stringIntegerMap.containsKey(key) && StringUtils.isNotBlank(value) && StringUtils.length(value) > stringIntegerMap.get(key)){
                    if(StringUtils.isNotBlank(sb.toString())){
                        sb.append(",");
                    }
                    sb.append("字段["+key+"]长度超过["+stringIntegerMap.get(key)+"]");
                    ReflectionUtil.invokeSetter(obj,fieldName,StringUtils.substring(value,0,stringIntegerMap.get(key)));
                }
            }
        }
        if(StringUtils.isBlank(sb.toString())){
            return null;
        }else{
            return sb.toString();
        }
    }
}
