package com.ideatech.ams.pbc.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AtomicLongUtils {

    public static AtomicLong al = new AtomicLong(1);

    public static String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


    public static boolean isPause(Long pbcCollectionLimitNum) throws Exception{
        if(resetNum(pbcCollectionLimitNum)){
            return false;
        }else {
            return false;
        }
    }

    public static boolean resetNum(Long pbcCollectionLimitNum) throws Exception {
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        log.info("当前数量统计为：" + al.get());
        if(al.get() >= pbcCollectionLimitNum){
            //每天采集上线不能大于系统配置的数量
            log.info("当前数量统计为：" + al.get() + ",人行账管系统配置采集数为：" + pbcCollectionLimitNum + ",系统睡眠15分钟。隔天继续采集");
            Thread.sleep(1000 * 60 * 15);
            if(!date.equals(nowDate)){
                al.set(1);
                date = nowDate;
                log.info("重置统计数量为：" + al.get());
            }else {
                resetNum(pbcCollectionLimitNum);
            }
            return true;
        }else {
            return true;
        }
    }
}
