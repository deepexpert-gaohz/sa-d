package com.ideatech.ams.service.impl;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.utils.DateUtils;
import com.ideatech.ams.utils.EtlUtil;
import com.ideatech.ams.service.EtlService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Etl文件处理服务类
 *
 * @auther zoulang
 * @create 2019-06-15 2:42 PM
 **/
@Service
@Log4j
public class EtlServiceImpl implements EtlService {
    @Value("${ams.etl.db.url}")
    private String url;
    @Value("${ams.db.user}")
    private String username;
    @Value("${ams.db.pass}")
    private String password;

    //核心数据存放地址
    @Value("${ams.etl.coreDataPath}")
    private String readPath;

    //产品数据初始化地址
    @Value("${import.file.location}")
    private String createPath;

    String dateFormat = "yyyyMMdd";

    @Autowired
    ConfigService configService;

    @Override
    public void doInitCore() {
        try {
            ConfigDto configDto = getProcessDate();
            String processDate = configDto.getConfigValue();
            String s = DateUtils.dayBefore(dateFormat);
                int date=daysBetween(processDate,  DateUtils.dayBefore(dateFormat), dateFormat);
                log.info("核心数据初始化任务开始...");
                Map<String, String> map = getParam(processDate);
                map.put("createPath", createPath);
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL url = classLoader.getResource("etl/JNNS_CLSJCSH.kjb");
                String file = url.toString();
                EtlUtil.callNativeJob(file, map);
                //更新日期
                processDate = DateUtils.dayafter(processDate, dateFormat);
                configDto.setConfigValue(processDate);
                configService.save(configDto);
                log.info("核心数据初始化任务结束...");
        } catch (Exception e) {
            log.error("核心数据初始化任务异常", e);
        }
    }

    /**
     * 字符串的日期格式的计算
     */
    public  int daysBetween(String smdate, String bdate,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    @Override
    public void douUpdateCoreData() {
        try {
            ConfigDto configDto = getProcessDate();
            String processDate = configDto.getConfigValue();
            if (DateUtils.daysBetween(processDate, DateUtils.dayBefore(dateFormat), dateFormat) >= 0) {
                log.info("核心数据T+1更新状态任务开始跑批。。。");
                Map<String, String> map = getParam(processDate);
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL url = classLoader.getResource("etl/JNNS_T+1.kjb");
                String file = url.toString();
                EtlUtil.callNativeJob(file, map);
                log.info("核心数据T+1更新状态任务开始跑批。。。");
                //更新日期
                processDate = DateUtils.dayafter(processDate, dateFormat);
                configDto.setConfigValue(processDate);
                configService.save(configDto);
                log.info("核心数据初始化任务结束...");
            }
        } catch (Exception e) {
            log.info("EtlAutoSchedule定时任务结束...");
        }
    }

    @Override
    public void doCoreData() {
        try {
            ConfigDto configDto = getProcessDate();
            String processDate = configDto.getConfigValue();
            int date=daysBetween(processDate,  DateUtils.dayBefore(dateFormat), dateFormat);
            if (date >= 0) {
                log.info("核心数据同步任务开始。。。");
                Map<String, String> map = getParam(processDate);
                map.put("createPath", createPath);
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL url = classLoader.getResource("etl/JNNS_CLSJCSH_2.kjb");
                String file = url.toString();
                EtlUtil.callNativeJob(file, map);
                log.info("核心数据同步任务开始。。。");
                //更新日期
//                processDate = DateUtils.dayafter(processDate, dateFormat);
//                configDto.setConfigValue(processDate);
//                configService.save(configDto);
                log.info("核心数据同步任务结束...");
            }
        } catch (Exception e) {
            log.info("EtlAutoSchedule定时任务结束...");
        }
    }

    /**
     * 获取核心处理日期
     *
     * @return
     */
    private ConfigDto getProcessDate() {
        ConfigDto configDto;
        String processDate;
        List<ConfigDto> configDtoList = configService.findByKey("coreBatchFileDate");
        if (CollectionUtils.isEmpty(configDtoList)) {
            processDate = DateUtils.dayBefore(dateFormat);
            configDto = new ConfigDto();
            configDto.setConfigKey("coreBatchFileDate");
            configDto.setConfigValue(processDate);
            configService.save(configDto);
        } else {
            configDto = configDtoList.get(0);
            processDate = configDto.getConfigValue();
            if (StringUtils.isBlank(processDate)) {
                processDate = DateUtils.dayBefore(dateFormat);
            }
        }
        return configDto;

    }

    /**
     * 设置etl参数
     *
     * @return
     */
    private Map getParam(String date) {
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("username", username);
        map.put("password", password);
        map.put("readPath", readPath);
        map.put("date", "TFS_CUST_CPRO_ACCT_INFO_"+date);
        map.put("orgFullId","''");
        return map;
    }



}
