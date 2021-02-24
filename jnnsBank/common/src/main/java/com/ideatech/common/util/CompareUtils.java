package com.ideatech.common.util;

import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 比较工具类
 * @author wanghongjie
 *
 * @version 2018-07-25 10:03
 */
public class CompareUtils {
	/**
	 * Map类比较
	 * @param oldMap
	 * @param newMap
	 * @param changeFields
	 * @param beforeChangeFieldValueMap
	 * @param afterChangeFieldValueMap
	 * @param sameChangeFieldValueSet
	 * @param changFieldNameList
	 * @param convertMap
	 * @param flag 是否新值覆盖旧值
	 */
	public static void compare(Map<String, String> oldMap,Map<String, String> newMap,List<String> changeFields,
			Map<String, Object> beforeChangeFieldValueMap,Map<String, Object> afterChangeFieldValueMap,List<String> sameChangeFieldValueSet,
			List<String> changFieldNameList,Map<String, String> convertMap,boolean flag) {
		String oldValue;
		String newValue;
		String oldKey;
		String newKey;
		if(changeFields != null && changeFields.size() != 0) {
            for(String field : changeFields) {
            	oldKey = field;
            	newKey = field;
            	if(convertMap.containsKey(field)) {
            		newKey = convertMap.get(field);
            	}
                oldValue = oldMap.get(oldKey) == null ? "" : oldMap.get(field).toString();
                newValue = newMap.get(newKey) == null ? "" : newMap.get(field).toString();

                //原值空并且现有值过滤
                if("bankCode".equals(field) || "bankName".equals(field)){
                	if(StringUtils.isBlank(oldValue) && StringUtils.isNotBlank(newValue)){
                		continue;
					}
				}

				boolean match = false;
				//注册资金特殊处理
				if ("registeredCapital".equals(field) && StringUtils.isNotBlank(oldValue) && StringUtils.isNotBlank(newValue)) {
					BigDecimal oldVal = new BigDecimal(oldValue);
					BigDecimal newVal = new BigDecimal(newValue);
					match = oldVal.compareTo(newVal) == 0;
				} else {
					match = !((StringUtils.isNotBlank(newValue) || flag) && !oldValue.equals(newValue));
				}

				if (!match) {
					beforeChangeFieldValueMap.put(field, oldValue);
					oldMap.put(field, newValue);
					afterChangeFieldValueMap.put(field, newValue);
					changFieldNameList.add(field);
                    /*if(StringUtils.isNotBlank(newValue)) {
                    	changFieldNameWithoutBlankList.add(field);
                    }*/
				} else {
					sameChangeFieldValueSet.add(field);
				}

            }
        }
	}
	

	/**
	 * 对应map进行NULL转化
	 * @param obj
	 * @param sameChangeFieldValueSet
	 */
	public static void convertToNUllFromMap(Object obj,List<String> sameChangeFieldValueSet){
    	try {
	    	for (String str : sameChangeFieldValueSet) {
				org.apache.commons.beanutils.BeanUtils.setProperty(obj, str, null);
			}
    	} catch (IllegalAccessException | InvocationTargetException e) {
    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "比对信息错误");
		}
    }
	
	/**
	 * 字段是否包含上报字段
	 * 
	 * @param changedFieldList
	 * @param changeSyncFieldList
	 * @return
	 */
	public static Boolean isSyncField(List<String> changedFieldList, List<String> changeSyncFieldList) {
		for (String changedField : changedFieldList) {
			for (String changeSyncField : changeSyncFieldList) {
				if (changedField.equalsIgnoreCase(changeSyncField)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 字段是否包含上报字段
	 *
	 * @param changedFieldList
	 * @param changeSyncFieldList
	 * @return
	 */
	public static Boolean isSyncField(Set<String> changedFieldList, List<String> changeSyncFieldList) {
		for (String changedField : changedFieldList) {
			for (String changeSyncField : changeSyncFieldList) {
				if (changedField.equalsIgnoreCase(changeSyncField)) {
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * 根据字段复制
	 * @param
	 * @param
	 */
	public static void convertToOldValueFromMap(Object newObj,Object oldObj,List<String> mValueSet){
    	try {
	    	for (String str : mValueSet) {
	    		String oldValue = org.apache.commons.beanutils.BeanUtils.getProperty(oldObj, str);
	    		String newValue = org.apache.commons.beanutils.BeanUtils.getProperty(newObj, str);
	    		if(StringUtils.isBlank(newValue)) {
					org.apache.commons.beanutils.BeanUtils.setProperty(newObj, str, oldValue);
	    		}
			}
    	} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
    		throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "字段复制错误");
		}
    }
	
	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

	public static String[] getNullAndEmptyPropertyNames(Object source) {
		BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();
		Set<String> emptyNames = new HashSet<>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (ObjectUtils.isEmpty(srcValue)) {
				emptyNames.add(pd.getName());
			}
		}
		return emptyNames.toArray(new String[emptyNames.size()]);
	}

	public static String[] getIsNotNullAndEmptyPropertyNames(Object source) {
		BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();
		Set<String> emptyNames = new HashSet<>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (!ObjectUtils.isEmpty(srcValue)) {
				emptyNames.add(pd.getName());
			}
		}
		return emptyNames.toArray(new String[emptyNames.size()]);
	}
	public static String[] getIsNotNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null && srcValue != "") {
            	emptyNames.add(pd.getName());
			}
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
