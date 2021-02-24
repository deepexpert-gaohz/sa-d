package com.ideatech.ams.account.service.pbc;

import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 挡板实现类，使用注入方式，这样pbc工程无需依赖无用的工程
 * @author fantao
 */
public class PbcMockServiceImpl implements PbcMockService {

    @Autowired
    private ConfigService configService;

    @Override
    public Boolean isLoginMockOpen() {
        List<ConfigDto> pbcLogin = configService.findByKey("pbcLoginMockEnabled");
        if (CollectionUtils.isNotEmpty(pbcLogin)) {
            return Boolean.valueOf(pbcLogin.get(0).getConfigValue());
        }
        return false;
    }

    @Override
    public Boolean isSyncMockOpen() {
        List<ConfigDto> pbcSync = configService.findByKey("pbcSyncMockEnabled");
        if (CollectionUtils.isNotEmpty(pbcSync)) {
            return Boolean.valueOf(pbcSync.get(0).getConfigValue());
        }
        return true;
    }
}
