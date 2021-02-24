package com.ideatech.ams.ws.enums;

import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.DisplayableError;

/**
 * @author vantoo
 * @date 14:03 2018/5/20
 */
public enum OpenAccountCode {

    ALLOWED("1", "允许开户"),
    NOT_ALLOWED("2", "不允许开户"),
    ALLOWED_BUT_PROMPT("3", "允许开户但有提示信息");

    private String code;
    private String value;

    OpenAccountCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public String code() {
        return this.code;
    }


    @Override
    public String toString() {
        return this.name();
    }
}
