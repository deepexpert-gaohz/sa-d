package com.ideatech.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Utility to access Spring context from a non-managed bean.
 *
 * @author SC
 */
@Component
@Slf4j
public final class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    /**
     * Description: get a bean from Spring context by type
     *
     * @param clz
     * @return
     */
    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }

    public static <T> Map<String, T> getBeans(Class<T> clz) {
        return applicationContext.getBeansOfType(clz);
    }

    /**
     * Description: get a bean from Spring context by type and qualifier
     *
     * @param name
     * @param clz
     * @return
     */
    public static <T> T getBean(String name, Class<T> clz) {
        return applicationContext.getBean(name, clz);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        log.info("ApplicationContextUtil.setApplicationContext加载");
        ApplicationContextUtil.applicationContext = applicationContext; // NOSONAR
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Map getAllProp(ConfigurableEnvironment environment) {
        ObjectMapper mapper = new ObjectMapper();
        String[] ignoreProp = {"ams.db.pass", "spring.datasource.druid.password", "spring.datasource.password", "ams.pdf.*"};
        Map propMap = new LinkedHashMap<String, String>(16);
        try {
            for (Iterator<PropertySource<?>> it = environment.getPropertySources().iterator(); it.hasNext(); ) {
                PropertySource propertySource = it.next();
                if (propertySource.getName().indexOf("applicationConfig") > -1) {
                    log.info("当前配置文件为{}", propertySource.getName());
                    loop:
                    for (Map.Entry<String, String> entry : ((Map<String, String>) propertySource.getSource()).entrySet()) {
                        for (String s : ignoreProp) {
                            if (s.indexOf("*") > -1) {
                                s = s.replace("*", "");
                                if (entry.getKey().indexOf(s) > -1) {
                                    continue loop;
                                }
                            } else {
                                if (s.equals(entry.getKey())) {
                                    continue loop;
                                }
                            }
                        }
                        if (!propMap.containsKey(entry.getKey())) {
                            propMap.put(entry.getKey(), entry.getValue());
                        }
                        log.info("{}={}", entry.getKey(), entry.getValue());
                    }
                }
            }
            log.info("最终的配置文件信息{}", mapper.writeValueAsString(propMap));
        } catch (Exception e) {
            log.error("打印所有配置文件异常", e);
        }
        return propMap;
    }

}
