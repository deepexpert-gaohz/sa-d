package com.ideatech.ams.account.service.pbc;

import com.ideatech.ams.pbc.service.PbcSyncConfigService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author yang
 * @Date 2019/11/20 15:32
 * @Version 1.0
 */
@Slf4j
public class PbcSyncConfigServiceImpl implements PbcSyncConfigService {
    @Autowired
    private ConfigService configService;
    @Override
    public boolean isJibenSync() {
        try {
            List<ConfigDto> pbcLogin = configService.findByKey("jibenSyncEnabled");
            if (CollectionUtils.isNotEmpty(pbcLogin)) {
                return Boolean.valueOf(pbcLogin.get(0).getConfigValue());
            }
        }catch (Exception e){
            log.error("查询先销后开配置异常：{}",e);
            return false;
        }
        return false;
    }

    @Override
    public boolean isHtmlLog() {
        try {
            List<ConfigDto> pbcLogin = configService.findByKey("htmlLogEnabled");
            if (CollectionUtils.isNotEmpty(pbcLogin)) {
                return Boolean.valueOf(pbcLogin.get(0).getConfigValue());
            }
        }catch (Exception e){
            log.error("查询html日志是否与系统日志分离异常：{}",e);
            return false;
        }
        return false;
    }

    @Override
    public Long getStopDate() {
        try {
            List<ConfigDto> pbcLogin = configService.findByKey("stopDate");
            if (CollectionUtils.isNotEmpty(pbcLogin)) {
                String strStopDate = pbcLogin.get(0).getConfigValue();
                return Long.valueOf(strStopDate);
            }
        }catch (Exception e){
            log.error("系统配置时间转换异常,返回默认暂停时间2000ms：{}",e);
            return 2000L;
        }
        return 2000L;
    }
}
