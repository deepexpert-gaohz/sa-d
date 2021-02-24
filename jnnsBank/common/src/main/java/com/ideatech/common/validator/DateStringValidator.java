package com.ideatech.common.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author wangqingan
 * @version 12/02/2018 11:21 AM
 */
public class DateStringValidator implements ConstraintValidator<ValidateDateString,String> {
    String pattern = "yyyy-MM-dd";
    boolean isRequired = true;

    @Override
    public void initialize(ValidateDateString constraintAnnotation) {

        pattern = constraintAnnotation.pattern();
        isRequired = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            // 非必填字段验证
            if(StringUtils.isEmpty(value) && !isRequired){
                return true;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.parse(value);
            return true;
        } catch (ParseException e){
            return false;
        }
    }
}
