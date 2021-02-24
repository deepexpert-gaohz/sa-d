/**
 * 
 */
package com.ideatech.common.util;

import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 反射获取/设置类属性的工具类，包装{@link PropertyUtils}和{@link BeanUtils}的相应方法。
 * 捕获其方法抛出的异常并转为抛出{@link IdeaException}
 * 
 * @author jojo
 *
 */
public class BeanValueUtils {

	/**
	 * 通过反射获取某个类的某个字段的值
	 * 
	 * @param entity
	 *            类的实例
	 * @param field
	 *            字段名
	 * @return
	 */
	public static final Object getValue(Object entity, String field) {
		Object value = null;
		try {
			value = PropertyUtils.getProperty(entity, field);
		} catch (Exception e) {
			return null;
		}
		return value;
	}

	/**
	 * 通过反射获取某个类的某个字段的值
	 * 
	 * @param entity
	 *            类的实例
	 * @param field
	 *            字段名
	 * @return
	 */
	public static final String getValueString(Object entity, String field) {
		try {
			String value = PropertyUtils.getProperty(entity, field).toString();
			return value;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 通过反射设置某个类的字段的值
	 * 
	 * @param entity
	 *            类的实例
	 * @param field
	 *            字段名
	 * @param value
	 *            值
	 */
	public static final void setValue(Object entity, String field, Object value) {
		try {
			BeanUtils.setProperty(entity, field, value);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "设置" + entity.getClass().getName() + "的属性" + field + "为" + value + "时失败");
		}
	}

	/**
	 * 对象拷贝属性忽略空值
	 * 
	 * @param source	如：a=1,b=2 其他为null值
	 * @param target	如：a=3,c=4 其他为null值
	 * return ： target为a=1,b=2,c=4 其他为null值
	 */
	public static void copyProperties(Object source, Object target) {
		org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}

	/**
	 * 对象拷贝属性
     * 将target对象中的null值，填充为source中的值
	 *
	 * @param source	如：a=1,b=2 其他为null值
	 * @param target	如：a=3,c=4 其他为null值
	 * return ： target为a=3,b=2,c=4 其他为null值
	 */
	public static void copyPropertiesForNull(Object source, Object target) {
		org.springframework.beans.BeanUtils.copyProperties(source, target, getNotNullPropertyNames(target));
	}

    /**
     * 对象拷贝属性
     * 将target对象中的空值或null值，填充为source中的值
     *
     * @param source	如：a=1,b=2,d=5 其他为null值
     * @param target	如：a=3,c=4,d="" 其他为null值
     * return ： target为a=3,b=2,c=4,d=5 其他为null值
     */
    public static void copyPropertiesForEmpty(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNotEmptyPropertyNames(target));
    }

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null || StringUtils.isBlank(srcValue.toString()))
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static Set getNullProNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null || StringUtils.isBlank(srcValue.toString()))
				emptyNames.add(pd.getName());
		}
		return emptyNames;
	}

	public static String[] getNotNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			if (pd.getName().equals("class")) {
				continue;
			}
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue != null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

    public static String[] getNotEmptyPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("class")) {
                continue;
            }
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                continue;
            }
            if ((src.getPropertyType(pd.getName()) == String.class && StringUtils.isEmpty((String) srcValue))) {
                continue;
            }
            emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

	/**
	 * 两个对象进行比对，以Map形式返回不一致属性，用分隔符拼接字段变更前与变更后的值
	 * 
	 * @param db
	 *            原先的对象
	 * @param newObject
	 *            最新的对象
	 * @param spilt
	 * @return 不一致的属性 key:属性名；value:变更前值+spilt+变更后值
	 */
	public static Map<String, String> compare(Object db, Object newObject, String spilt) {
		Map<String, String> map = new HashMap<String, String>();// 存放修改前与修改后的属性值
		try {
			Class<?> cDb = db.getClass();
			Class<?> cNew_ = newObject.getClass();
			Field[] filesDb = cDb.getDeclaredFields();
			for (Field field : filesDb) {
				String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				Method mdb = (Method) cDb.getMethod(getMethodName);
				Method mNew_ = (Method) cNew_.getMethod(getMethodName);
				// 自定义实现的注解类
				Object valDb = mdb.invoke(db);
				Object valNew = mNew_.invoke(newObject);
				if (!ObjectUtils.equals(valDb, valNew)) {
					map.put(field.getName(), String.valueOf(valDb) + spilt + String.valueOf(valNew));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * * 取Bean的属性和值对应关系的MAP *
	 * 
	 * @param bean
	 *            *
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

	/**
	 * * 拼接某属性的 get方法 *
	 * 
	 * @param fieldName
	 *            *
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
	 * @param fieldName
	 *            *
	 * @return String
	 */
	public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "set" + new String(fieldName.substring(0, 1)).toUpperCase() + new String(fieldName.substring(1));
	}

	/**
	 * * 判断是否存在某属性的 get方法 *
	 * 
	 * @param methods
	 *            *
	 * @param fieldGetMet
	 *            *
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
	 * * 判断是否存在某属性的 set方法 *
	 * 
	 * @param methods
	 *            *
	 * @param fieldSetMet
	 *            *
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
	 * 判断对象中属性值是否全为空
	 *
	 * 如果属性类型为基本数据类型，则会有默认值
	 * 影响正确判断，请特别注意
	 * @param object
	 * @return
	 */
	public static boolean checkObjAllFieldsIsNull(Object object) {
		if (null == object) {
			return true;
		}
		try {
			for (Field f : object.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
