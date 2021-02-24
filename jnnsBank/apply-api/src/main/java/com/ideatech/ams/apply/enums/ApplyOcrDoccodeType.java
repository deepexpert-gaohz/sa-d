package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 预约影像类型
 */
public enum ApplyOcrDoccodeType {

    OCR_DECCODE_1 ("1", "开户许可证"),
    OCR_DECCODE_2 ("2", "营业执照"),
    OCR_DECCODE_4 ("4", "组织机构代码证"),
    OCR_DECCODE_26 ("26", "法人身份证正面"),
    OCR_DECCODE_27 ("27", "法人身份证反面"),
    OCR_DECCODE_261 ("261", "授权经办人1身份证正面"),
    OCR_DECCODE_271 ("271", "授权经办人1身份证反面"),
    OCR_DECCODE_262 ("262", "授权经办人2身份证正面"),
    OCR_DECCODE_272 ("272", "授权经办人2身份证反面"),
    OCR_DECCODE_263 ("263", "授权经办人3身份证正面"),
    OCR_DECCODE_273 ("273", "授权经办人3身份证反面"),
    OCR_DECCODE_264 ("264", "授权经办人4身份证正面"),
    OCR_DECCODE_274 ("274", "授权经办人4身份证反面"),
    OCR_DECCODE_29 ("29", "临时户批文批复"),
    OCR_DECCODE_299 ("299", "临时户相关材料"),
    OCR_DECCODE_42 ("42", "专户附件材料"),
    OCR_DECCODE_99 ("99", "其他");


    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(ApplyOcrDoccodeType type: ApplyOcrDoccodeType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    ApplyOcrDoccodeType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
