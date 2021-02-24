/**
 *
 */
package com.ideatech.ams.annual.util;

import com.google.common.collect.Maps;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.common.enums.SaicStatusEnum;
import com.ideatech.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 * @author congzhou
 */
public final class AnnualReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnualReflectionUtil.class);

    public static List<Field> getFieldsIncludingSuperClasses(Class<?> clazz) {
        return getAllFieldsRec(clazz, new ArrayList<Field>());
    }

    public static Method getGetter(Class<?> clazz, Field field) {
        String filedName = field.getName();
        String firstLetter = filedName.substring(0, 1).toUpperCase();
        String getMethodName = "get" + firstLetter + filedName.substring(1);
        Method getMethod = null;
        try {
            getMethod = clazz.getDeclaredMethod(getMethodName);
        } catch (Exception e) {
            LOGGER.error("error while get getter", e);
        }
        return getMethod;
    }

    public static Method getSetter(Class<?> clazz, Field field) {
        Class<?> fieldType = field.getType();
        String filedName = field.getName();
        String firstLetter = filedName.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + filedName.substring(1);
        Method setMethod = null;
        try {
            setMethod = clazz.getDeclaredMethod(setMethodName, fieldType);
        } catch (Exception e) {
            LOGGER.error("error while get setter", e);
        }
        return setMethod;
    }

    public static Map<String, String> getFieldMap(Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            String fieldName = field.getName().toUpperCase();
            Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);
            String fieldValue = null;
            try {
                fieldValue = (String) getMethod.invoke(o);
            } catch (Exception e) {
                LOGGER.error("error while get: {}", e, fieldName);
            }
            if (null != fieldValue)
                fieldMap.put(fieldName, fieldValue);
        }
        return fieldMap;
    }

    /**
     *
     * 获取String和Date类型的字段，并进行对字段（status和type）值进行转化
     * @param o
     * @return
     */
    public static Map<String, String> getFieldMapJustStringAndDate(Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            String fieldName = field.getName();
            if(field.getType() == String.class){
                Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);
                String fieldValue = null;
                try {
                    fieldValue = (String) getMethod.invoke(o);
                } catch (Exception e) {
                    LOGGER.error("error while get: {}", e, fieldName);
                }
                if (null != fieldValue) {
                    fieldMap.put(fieldName, fieldValue);
                }

                if("status".equalsIgnoreCase(fieldName) && null != fieldValue) {
                    if("UnComplete".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "待受理");
                    } else if("SUCCESS".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "受理成功");
                    } else if("FAIL".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "受理退回");
                    }
                }

                if("type".equalsIgnoreCase(fieldName) && null != fieldValue) {
                    if("jiben".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "基本户");
                    } else if("yiban".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "一般户");
                    } else if("yusuan".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "预算户");
                    } else if("feiyusuan".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "非预算户");
                    } else if("linshi".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "临时户");
                    } else if("feilinshi".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "非临时户");
                    } else if("teshu".equalsIgnoreCase(fieldValue)) {
                        fieldMap.put(fieldName, "特殊户");
                    }
                }

            }else if(field.getType() == Date.class){
                Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);
                Date fieldValue = null;
                try {
                    fieldValue = (Date) getMethod.invoke(o);
                } catch (Exception e) {
                    LOGGER.error("error while get: {}", e, fieldName);
                }
                if (null != fieldValue)
                    fieldMap.put(fieldName, DateUtils.DateToStr(fieldValue, DateUtils.PARSE_PATTERNS[2]));
            }

        }
        return fieldMap;
    }

    /**
     *
     * 获取String和Date类型的字段，并进行对年检字段的值值进行转化
     * @param o
     * @return
     */
    public static Map<String, String> getAnnualFieldMap(Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            String fieldName = field.getName();

            if(field.getType() == String.class) {
                Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);
                String fieldValue = null;
                try {
                    fieldValue = (String) getMethod.invoke(o);
                } catch (Exception e) {
                    LOGGER.error("error while get: {}", e, fieldName);
                }
                if (null != fieldValue) {
                    fieldMap.put(fieldName, fieldValue);
                } else {
                    fieldMap.put(fieldName, "");
                }
            } else if(field.getType() == Date.class){
//                Method getMethod = ReflectionUtil.getGetter(clazz, field);
                Date fieldDateValue = null;
                try {
                    Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);
                    fieldDateValue = (Date) getMethod.invoke(o);
                } catch (Exception e) {
                    LOGGER.error("error while get: {}", e, fieldName);
                }
                if (null != fieldDateValue)
                    fieldMap.put(fieldName, DateUtils.DateToStr(fieldDateValue, DateUtils.PARSE_PATTERNS[2]));
            } else {
                Method getMethod = AnnualReflectionUtil.getGetter(clazz, field);

                if ("unilateral".equalsIgnoreCase(fieldName)) {
                    UnilateralTypeEnum fieldValue = null;
                    try {
                        fieldValue = (UnilateralTypeEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }
                }

                if ("black".equalsIgnoreCase(fieldName)) {
                    Boolean fieldValue = null;
                    try {
                        fieldValue = (Boolean) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if (true == fieldValue) {
                        fieldMap.put(fieldName, "为黑名单");
                    } else if (false == fieldValue) {
                        fieldMap.put(fieldName, "不为黑名单");
                    }
                }

                if ("match".equalsIgnoreCase(fieldName)) {
                    Boolean fieldValue = null;
                    try {
                        fieldValue = (Boolean) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if (true == fieldValue) {
                        fieldMap.put(fieldName, "匹配");
                    } else if (false == fieldValue) {
                        fieldMap.put(fieldName, "不匹配");
                    }
                }

                if ("saicStatus".equalsIgnoreCase(fieldName)) {
                    SaicStatusEnum fieldValue = null;
                    try {
                        fieldValue = (SaicStatusEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }
                }

                if ("result".equalsIgnoreCase(fieldName)) {
                    ResultStatusEnum fieldValue = null;

                    try {
                        fieldValue = (ResultStatusEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }
                }

                if ("pbcSubmitStatus".equalsIgnoreCase(fieldName)) {
                    PbcSubmitStatusEnum fieldValue = null;

                    try {
                        fieldValue = (PbcSubmitStatusEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }
                }

                if ("forceStatus".equalsIgnoreCase(fieldName)) {
                    ForceStatusEnum fieldValue = null;

                    try {
                        fieldValue = (ForceStatusEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }
                }

                if ("dataProcessStatus".equalsIgnoreCase(fieldName)) {
                    DataProcessStatusEnum fieldValue = null;

                    try {
                        fieldValue = (DataProcessStatusEnum) getMethod.invoke(o);
                    } catch (Exception e) {
                        LOGGER.error("error while get: {}", e, fieldName);
                    }

                    if(null != fieldValue) {
                        fieldMap.put(fieldName, fieldValue.getName());
                    }

                }
            }

        }
        return fieldMap;
    }

    /**
     *
     * 获取反射的Field的Map，以field.getName.toUpperCase()为key值，包含继承类
     *
     * @param o
     * @return
     */
    public static Map<String, Field> getFieldObjMap(Object o) {
        Class<?> clazz = o.getClass();
        List<Field> fields = getFieldsIncludingSuperClasses(clazz);
        Map<String, Field> fieldMap = Maps.newHashMap();
        for (Field field : fields) {
            String fieldName = field.getName().toUpperCase();
            fieldMap.put(fieldName,field);
        }
        return fieldMap;
    }

    private static List<Field> getAllFieldsRec(Class<?> clazz, List<Field> fields) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            getAllFieldsRec(superClazz, fields);
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    public static Class<?> getGenericParamClass(Class<?> clazz) {
        Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        return (Class<?>) type;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i=0; i<names.length; i++){
            if(i<names.length-1){
                String getterMethodName = "get" + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
            }else{
                String setterMethodName = "set" + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("error while method invoke: {}", e);
            return null;
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
     * 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("error while method invoke: {}", e);
            return null;
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     *
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null");
        Validate.notEmpty(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     *
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notEmpty(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }



}
