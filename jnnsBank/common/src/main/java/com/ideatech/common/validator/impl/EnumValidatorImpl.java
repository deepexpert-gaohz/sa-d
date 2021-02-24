package com.ideatech.common.validator.impl;

/**
 * Created by hammer on 2018/3/19.
 */

import com.ideatech.common.validator.EnumValidator;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    String attribute = "";

//    String message = "validation failed";

    List<Enum> valueList = null;

    final String methodPrefix = "get";

    boolean acceptEmptyString = false;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //如果允许空字符串，直接通过验证
        if ("".equals(value) && acceptEmptyString) {
            return true;
        // 如果不允许空字符串，验证失败，否则字符不为空字串，进入后续验证
        } else if ("".equals(value) && !acceptEmptyString) {
            return false;
        }
        //没有指定枚举中的参数名称，直接用默认值进行匹配
        if (StringUtils.isEmpty(attribute)) {
            for(Enum e : valueList) {
                if (e.toString().toUpperCase().equals(value.toUpperCase())) {

                    return true;
                }
            }
//            message = "input value is illegal";
            return false;
        }

        String formatAttr = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
        //反射拿到对应的字段的get方法，将字段和输入值进行匹配
        for (Enum e : valueList) {
            try {
                Method m = e.getDeclaringClass().getDeclaredMethod(methodPrefix + formatAttr);
                Object specificKeyword = m.invoke(e);
                if (specificKeyword.toString().equals(value)) {
                    return true;
                }
            } catch (Exception exception) {
//                message = "attribute is not correct";
                return false;
            }
        }
//        message = "no attribute fit the given value";
        return false;

    }

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        attribute = constraintAnnotation.attribute();
        acceptEmptyString = constraintAnnotation.acceptEmptyString();
//        message = constraintAnnotation.message();
        valueList = new ArrayList<Enum>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for(@SuppressWarnings("rawtypes")
                Enum enumVal : enumValArr) {
            valueList.add(enumVal);//.toString().toUpperCase()
        }

    }

}
