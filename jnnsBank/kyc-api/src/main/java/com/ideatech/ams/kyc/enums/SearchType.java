package com.ideatech.ams.kyc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangqingan
 * @version 09/02/2018 3:26 PM
 */
public enum SearchType {
    KHJD("客户尽调"),
    REAL_TIME("实时数据"),
    EXACT("存量数据");

    SearchType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static SearchType str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equalsIgnoreCase("khjd")) {
            return SearchType.KHJD;
        } else if (enmuStr.equalsIgnoreCase("real_time")) {
            return SearchType.REAL_TIME;
        } else if (enmuStr.equalsIgnoreCase("exact")) {
            return SearchType.EXACT;
        }
        return null;
    }
}
