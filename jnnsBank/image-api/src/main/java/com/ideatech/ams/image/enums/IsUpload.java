package com.ideatech.ams.image.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 是否上传影像平台
 */
public enum IsUpload {
    FALSE("否"),
    TRUE("是");
    private String value;
    IsUpload(String value) {
        this.value = value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public static IsUpload str2enum(String isUpload) {
        if (StringUtils.isBlank(isUpload)) {
            return null;
        }
        if("FALSE".equals(isUpload) || "否".equals(isUpload)){
            return IsUpload.FALSE;
        }else if("TRUE".equals(isUpload) || "是".equals(isUpload)){
            return IsUpload.TRUE;
        }else{
            return  null;
        }
    }
}
