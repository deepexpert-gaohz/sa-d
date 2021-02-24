package com.ideatech.ams.kyc.util;

import java.text.DecimalFormat;

/**
 * @author wangqingan
 * @version 09/02/2018 5:33 PM
 */
public class RateUtils {

    /**
     * 占比格式化
     * @param date
     * @return
     */
    public static String getRateString(double date){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(date*100)+"%";
    }
}
