package com.ideatech.ams.pbc.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author yang
 * @Date 2019/11/18 10:15
 * @Version 1.0
 */
@Slf4j
public class HtmlUtils {

    /**
     *  内容追加
     * @param uuid 唯一识别码
     * @param text 文件内容
     */
    public  static void info(String uuid,String text){
        log.info("\n<{}>\n{}\n<{}>",uuid,text,uuid);
    }
}
