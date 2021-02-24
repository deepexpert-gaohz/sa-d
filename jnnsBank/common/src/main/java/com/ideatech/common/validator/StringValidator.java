package com.ideatech.common.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangqingan
 * @version 12/02/2018 11:21 AM
 */
public class StringValidator implements ConstraintValidator<ValidateString,String> {

    private List<String> valueList;

    private boolean isRequired = true;

    @Override
    public void initialize(ValidateString constraintAnnotation) {
        valueList = new ArrayList<String>();
        for(String val : constraintAnnotation.acceptedValues()) {
            valueList.add(val.toUpperCase());
        }

        isRequired = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 非必填字段验证
        if(StringUtils.isEmpty(value) && !isRequired){
            return true;
        } else if(StringUtils.isEmpty(value)){
            return false;
        }

        if(!valueList.contains(value.toUpperCase())) {
            return false;
        }
        return true;
    }
}
