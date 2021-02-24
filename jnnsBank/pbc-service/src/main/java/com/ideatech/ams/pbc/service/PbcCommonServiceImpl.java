package com.ideatech.ams.pbc.service;

import com.ideatech.ams.pbc.utils.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author yang
 * @Date 2019/11/20 17:11
 * @Version 1.0
 */
@Component
@Slf4j
public class PbcCommonServiceImpl implements PbcCommonService {
    @Autowired
    PbcSyncConfigService pbcSyncConfigService;
    @Override
    public void writeLog(String logflag, String html) {
        try{
            boolean htmlLog = pbcSyncConfigService.isHtmlLog();
            log.info("人行上报返回html是否独立记录一个文件：{}",htmlLog);
            if(htmlLog){
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                log.info("{}页面的唯一标识码:{}",logflag,uuid);
                HtmlUtils.info(uuid,html);
            }else{
                log.info("{}页面:\n{}",logflag,html);
            }
        }catch(Exception e){
            log.error("html日志记录异常：{}",e);
        }
    }
}
