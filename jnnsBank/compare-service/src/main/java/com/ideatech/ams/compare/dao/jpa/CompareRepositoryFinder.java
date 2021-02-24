/**
 *
 */
package com.ideatech.ams.compare.dao.jpa;

import com.ideatech.ams.compare.dao.data.CompareDataRepository;
import com.ideatech.ams.compare.entity.data.CompareData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 *
 */
@Component
public class CompareRepositoryFinder {

    @Autowired
    private ApplicationContext applicationContext;

    private static final String REPOSITORY = "Repository";

    /**
     * 根据class查找比对数据的Repository
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends CompareData> CompareDataRepository<T> getRepository(Class<T> clazz) {
        String name = StringUtils.uncapitalize(clazz.getSimpleName()) + REPOSITORY;
        return (CompareDataRepository<T>) applicationContext.getBean(name);
    }

    /**
     * 根据name查找比对数据的Repository
     * @param domainName
     * @return
     */
    @SuppressWarnings("unchecked")
    public CompareDataRepository<CompareData> getRepository(String domainName) {
        String name = StringUtils.uncapitalize(domainName) + REPOSITORY;
        return (CompareDataRepository<CompareData>) applicationContext.getBean(name);
    }

}
