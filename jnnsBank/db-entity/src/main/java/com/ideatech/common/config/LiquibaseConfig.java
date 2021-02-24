package com.ideatech.common.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @DependsOn(value = "entityManagerFactory")
    public SpringLiquibase liquibase() {
        LiquibaseProperties liquibaseProperties = liquibaseProperties();
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDataSource(getDataSource(liquibaseProperties));
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(true);
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        return liquibase;
    }


    private DataSource getDataSource(LiquibaseProperties liquibaseProperties) {
        if (liquibaseProperties.getUrl() == null) {
            return this.dataSource;
        }
        return DataSourceBuilder.create().url(liquibaseProperties.getUrl())
                .username(liquibaseProperties.getUser())
                .password(liquibaseProperties.getPassword()).build();
    }
}
