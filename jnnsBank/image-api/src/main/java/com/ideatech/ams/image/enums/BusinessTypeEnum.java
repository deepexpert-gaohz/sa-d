package com.ideatech.ams.image.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author jzh
 * @date 2020/4/29.
 */

@Getter
public enum BusinessTypeEnum {
    CORPORATE("corporate","对公业务"),
    PERSONAL("personal","个人业务");

    private String value;

    private String name;

    BusinessTypeEnum(String value,String name){
        this.value = value;
        this.name = name;
    }

    public static BusinessTypeEnum str2enum(String businessType) {
        if (StringUtils.isBlank(businessType)) {
            return null;
        }
        if (businessType.equals("corporate") || businessType.equals("对公业务")) {
            return BusinessTypeEnum.CORPORATE;
        } else if (businessType.equals("personal") || businessType.equals("个人业务")) {
            return BusinessTypeEnum.PERSONAL;
        }
        return null;
    }

}
