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
@Constraint(validatedBy = DateStringValidator.class)
@Documented
public @interface ValidateDateString {

    String pattern() default "yyyy-MM-dd";

    String message() default "数据有错误";

    boolean required() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
