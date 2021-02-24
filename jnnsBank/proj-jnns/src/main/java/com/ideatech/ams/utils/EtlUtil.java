package com.ideatech.ams.utils;

/**
 * Created by chenhao on 2019/4/7.
 */


import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class EtlUtil {
    private static final Logger log = LoggerFactory.getLogger(EtlUtil.class);


    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    /**
     * 调用trans文件 带参数的
     * @param params
     * @param transFileName
     * @throws Exception
     */
    public static void callNativeTransWithParams(String transFileName,String[] params) throws Exception{
        // 初始化
        KettleEnvironment.init();
        EnvUtil.environmentInit();
        String file = EtlUtil.class.getClassLoader().getResource(transFileName).getPath();
        TransMeta transMeta = new TransMeta(file);
        //转换
        Trans trans = new Trans(transMeta);
        //执行
        trans.execute(params);
        //等待结束
        trans.waitUntilFinished();
        //抛出异常
        if(trans.getErrors() > 0){
            throw new Exception("There are errors during transformation exception!(传输过程中发生异常)");
        }
    }
    /**
     * 调用job文件
     * @param jobName
     * @throws Exception
     */
    public static void callNativeJob(String jobName,Map<String,String> map) throws Exception {
        log.info("调用文件——--------------"+jobName);
        // 初始化
        KettleEnvironment.init();
        JobMeta jobMeta = new JobMeta(jobName, null);
        Job job = new Job(null, jobMeta);
        //向Job 脚本传递参数，脚本中获取参数值：${参数名}
        for (String key : map.keySet()) {
            String value = map.get(key);
            job.setVariable(key, value);
        }
        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
            throw new Exception("There are errors during job exception!(执行job发生异常)");
        }

    }
}
