package com.ideatech.ams.kyc.util;

import java.util.UUID;

/**
 * @author wangqingan
 * @version 09/02/2018 10:23 AM
 */
public class KeyGenerator {

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
