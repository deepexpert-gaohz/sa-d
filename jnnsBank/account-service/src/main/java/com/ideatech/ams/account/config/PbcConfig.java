package com.ideatech.ams.account.config;

import com.ideatech.ams.account.service.pbc.PbcMockServiceImpl;
import com.ideatech.ams.account.service.pbc.PbcSyncConfigServiceImpl;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.PbcSyncConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fantao
 * @date 2019-05-22 15:56
 */
@Configuration
public class PbcConfig {

    /**
     * 用本工程的实现覆盖次实现类
     * @return
     */
    @Bean
    public PbcMockService pbcMockService() {
        return new PbcMockServiceImpl();
    }
    @Bean
    public PbcSyncConfigService pbcSyncConfigService(){
        return new PbcSyncConfigServiceImpl();
    }


}
