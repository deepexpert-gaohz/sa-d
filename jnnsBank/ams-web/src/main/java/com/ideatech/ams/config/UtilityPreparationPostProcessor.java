package com.ideatech.ams.config;

import com.ideatech.common.util.MessageUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author liangding
 * @create 2018-06-12 下午8:07
 **/
@Component
public class UtilityPreparationPostProcessor {
    @Autowired(required = false)
    private MessageSource messageSource;

    @PostConstruct
    public void postProcessAfterInitialization() throws BeansException {
        MessageUtil.setMessageSource(messageSource);
    }
}
