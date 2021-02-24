package com.ideatech.ams.kyc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangqingan
 * @version 09/02/2018 3:13 PM
 */
public class DateUtils {

    public static String getCurrentDate(){
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(curDate);
    }
}
