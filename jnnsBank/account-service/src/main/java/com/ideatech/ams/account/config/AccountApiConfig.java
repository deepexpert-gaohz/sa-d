package com.ideatech.ams.account.config;

import com.ideatech.ams.account.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountApiConfig {
    @Bean
    @ConditionalOnMissingBean(name = "acctNoGeneService")
    public AcctNoGeneService acctNoGeneService() {
        return new DefaultAcctNoGeneServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "coreOpenSyncService")
    public CoreOpenSyncService coreOpenSyncService() {
        return new DefaultCoreOpenSyncServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "coreChangeSyncService")
    public CoreChangeSyncService coreChangeSyncService() {
        return new DefaultCoreChangeSyncServiceImpl();
    }

}
