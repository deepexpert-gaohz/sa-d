package com.ideatech.ams.listener;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ejb.HibernatePersistence;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.Collections;
import java.util.List;

/**
 * @author liangding
 * @create 2018-06-14 下午4:47
 **/
@Slf4j
public class HibernatePersistenceProviderResolver implements
        PersistenceProviderResolver {

    private volatile PersistenceProvider persistenceProvider = new HibernatePersistence();

    @Override
    public List<PersistenceProvider> getPersistenceProviders() {
        return Collections.singletonList(persistenceProvider);
    }

    @Override
    public void clearCachedProviders() {
        persistenceProvider = new HibernatePersistence();
    }

    public static void register() {
        log.info("Registering HibernatePersistenceProviderResolver");
        PersistenceProviderResolverHolder
                .setPersistenceProviderResolver(new HibernatePersistenceProviderResolver());
    }
}

