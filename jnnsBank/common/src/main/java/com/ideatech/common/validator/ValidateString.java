package com.ideatech.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author wangqingan
 * @version 12/02/2018 11:19 AM
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValidator.class)
@Documented
public @interface ValidateString {

    String[] acceptedValues();

    String message() default "数据有错误";

    boolean required() default false;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
