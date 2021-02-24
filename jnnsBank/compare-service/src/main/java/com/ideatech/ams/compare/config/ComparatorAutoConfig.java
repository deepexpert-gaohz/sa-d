package com.ideatech.ams.compare.config;

import com.ideatech.ams.compare.spi.comparator.Comparator;
import com.ideatech.ams.compare.spi.comparator.DefaultComparator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 比对器配置
 *
 * @author fantao
 */
@Configuration
public class ComparatorAutoConfig {

    @Bean
    @ConditionalOnMissingBean(name = "defaultComparator")
    public Comparator defaultComparator() {
        return new DefaultComparator();
    }

}
