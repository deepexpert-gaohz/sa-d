package com.ideatech.ams.account.enums.bill;

import lombok.Getter;

/**
 * 批量久悬类型
 * @author vantoo
 * @date 2018/10/16 2:16 PM
 */
@Getter
public enum BatchSuspendSourceEnum {

    HTML_UPLOAD("页面上传"),

    BATCH_FILE("批次文件");

    private String name;

    BatchSuspendSourceEnum(String name) {
        this.name = name;
    }
}
