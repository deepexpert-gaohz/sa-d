package com.ideatech.ams.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author liangding
 * @create 2018-06-14 下午4:46
 **/
public class WeblogicPresistenceListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernatePersistenceProviderResolver.register();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
