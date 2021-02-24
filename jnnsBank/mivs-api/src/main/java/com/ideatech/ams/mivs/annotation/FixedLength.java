package com.ideatech.ams.mivs.annotation;

import com.ideatech.ams.mivs.enums.MsgHeaderFieldTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定长报文自定义注解
 * 用来记录报文的属性
 *
 * @author fantao
 * @date 2019-07-04 16:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FixedLength {

    /**
     * 起始位置
     * @return
     */
    int index();

    /**
     * 长度
     *
     * @return
     */
    int length();

    /**
     * 类型
     *
     * @return
     */
    MsgHeaderFieldTypeEnum type();

    /**
     * 是否必须
     *
     * @return
     */
    boolean must() default true;

}
