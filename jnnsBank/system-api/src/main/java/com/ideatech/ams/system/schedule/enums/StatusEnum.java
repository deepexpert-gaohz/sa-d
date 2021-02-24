package com.ideatech.ams.system.schedule.enums;

import lombok.Getter;

/**
 * @author jzh
 * @date 2019-08-21.
 */

@Getter
public enum StatusEnum {

    UNEXECUTED("未执行"),
    EXECUTING("执行中");

    private String name;

    StatusEnum(String name) {
        this.name=name;
    }
}
