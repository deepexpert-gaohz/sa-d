package com.ideatech.ams.kyc.enums;

import lombok.Getter;

/**
 * 流水审核状态
 *
 * @author jogy.he
 */
@Getter
public enum HolidayTypeEnum {

    HOLIDAY_WORK("上班"),

    WORKDAY_REST("放假调休");

    HolidayTypeEnum(String value) {
        this.value = value;
    }

    private String value;

}
