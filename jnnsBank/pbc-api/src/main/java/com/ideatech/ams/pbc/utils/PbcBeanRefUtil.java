package com.ideatech.ams.pbc.utils;

import com.ideatech.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PbcBeanRefUtil {
    
    /**
     * * 取Bean的属性和值对应关系的MAP *
     * 
     * @param bean *
     * @return Map
     */
    public static Map<String, Object> getFieldValueMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, Object> valueMap = new HashMap<String, Object>();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Method[] methodsP = cls.getSuperclass().getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        Field[] fieldsP = cls.getSuperclass().getDeclaredFields();
        // 反射父类
        for (Field field : fieldsP) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methodsP, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                valueMap.put(field.getName(), fieldVal);
            } catch (Exception e) {
                continue;
            }
        }
        for (Field field : fields) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                if (valueMap.containsKey(field.getName())) {
                    valueMap.remove(field.getName());
                }
                valueMap.put(field.getName(), fieldVal);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;
    }
    
    public static Map<String, Object> getFieldValueNotEmptyMap(Object bean) {
        Class<?> cls = bean.getClass();
        Map<String, Object> valueMap = new HashMap<String, Object>();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Method[] methodsP = cls.getSuperclass().getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        Field[] fieldsP = cls.getSuperclass().getDeclaredFields();
        // 反射父类
        for (Field field : fieldsP) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methodsP, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                if (fieldVal != null && fieldVal != "") {
                    valueMap.put(field.getName(), fieldVal);
                }
            } catch (Exception e) {
                continue;
            }
        }
        for (Field field : fields) {
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                if (valueMap.containsKey(field.getName())) {
                    valueMap.remove(field.getName());
                }
                if (fieldVal != null && StringUtils.isNotEmpty(fieldVal.toString())) {
                    valueMap.put(field.getName(), fieldVal);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;
    }
    
    /**
     * 当对象字段符合某个条件 赋值一个默认值
     * 
     * @param bean
     *            对象
     * @param collon
     *            符合条件值
     * @param defaultValue
     *            默认值
     */
    public static void setColumnDefaultValue(Object bean, Object collon, Object defaultValue) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getSuperclass().getDeclaredMethods();
        Field[] fields = cls.getSuperclass().getDeclaredFields();
        // 反射父类
        for (Field field : fields) {
            // String fieldType = field.getType().getSimpleName(); //字段类型
            try {
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                String fieldSetName = parSetName(field.getName());
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                if (collon == null) {
                    if (fieldVal == null) {
                        fieldSetMet.invoke(bean, defaultValue);
                        continue;
                    }
                }
                if (collon.equals(fieldVal)) {
                    fieldSetMet.invoke(bean, defaultValue);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
    
    /**
     * 设置对象中String 类型的字段若文件为null则设置为""
     * 
     * @param bean
     * @exception
     */
    public static void setFieldEmpty(Object bean) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Method[] methodsP = cls.getSuperclass().getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        Field[] fieldsP = cls.getSuperclass().getDeclaredFields();
        for (Field field : fieldsP) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methodsP, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methodsP, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                
                if (null == fieldVal) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, "");
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        for (Field field : fields) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                
                String fieldGetName = parGetName(field.getName());
                if (!checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
                if (null == fieldVal) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, "");
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
    
    /**
     * * set属性的值到Bean *
     * 
     * @param bean *
     * @param valMap
     */
    public static void setFieldValue(Object bean, Map<String, String> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Method[] methodsP = cls.getSuperclass().getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        Field[] fieldsP = cls.getSuperclass().getDeclaredFields();
        for (Field field : fieldsP) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methodsP, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        for (Field field : fields) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        System.out.println("not supper type" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
    
    /**
     * * 格式化string为Date *
     * 
     * @param datestr *
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * * 日期转化为String *
     * 
     * @param date *
     * @return date string
     */
    public static String fmtDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * * 判断是否存在某属性的 set方法 *
     * 
     * @param methods *
     * @param fieldSetMet *
     * @return boolean
     */
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * * 判断是否存在某属性的 get方法 *
     * 
     * @param methods *
     * @param fieldGetMet *
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * * 拼接某属性的 get方法 *
     * 
     * @param fieldName *
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + new String(fieldName.substring(0, 1)).toUpperCase() + new String(fieldName.substring(1));
    }
    
    /**
     * * 拼接在某属性的 set方法 *
     * 
     * @param fieldName *
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + new String(fieldName.substring(0, 1)).toUpperCase() + new String(fieldName.substring(1));
    }
}
