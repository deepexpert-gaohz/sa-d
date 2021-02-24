package com.ideatech.ams.annual.util;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class AnnualResultMsgFormat {

    public static String formatMessage(String message, AnnualResultDto temp){
        Map<String, String> fieldMap = AnnualReflectionUtil.getAnnualFieldMap(temp);

        Iterator<Map.Entry<String, String>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            message = StringUtils.replace(message,"{"+entry.getKey()+"}",entry.getValue());
        }
        return message;
    }

}
