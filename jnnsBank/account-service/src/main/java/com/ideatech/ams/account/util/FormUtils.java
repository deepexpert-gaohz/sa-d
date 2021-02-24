package com.ideatech.ams.account.util;

import org.apache.commons.lang.StringUtils;

public class FormUtils {

    public static String getComponentName(String acctType, String billType) {
        StringBuffer name = new StringBuffer();
        name.append(StringUtils.uncapitalize(acctType));
        name.append(StringUtils.capitalize(StringUtils.substringAfter(billType.toLowerCase(), "_")));
        return name.append("FormProcessor").toString();
    }

}
