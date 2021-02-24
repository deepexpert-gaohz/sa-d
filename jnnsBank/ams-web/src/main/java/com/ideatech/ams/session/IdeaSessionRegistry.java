package com.ideatech.ams.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author fantao
 * @date 2019-04-10 11:33
 */
@Slf4j
public class IdeaSessionRegistry extends SpringSessionBackedSessionRegistry {

    private static final String GET_PRINCIPAL_NAME_QUERY = "SELECT PRINCIPAL_NAME FROM " + JdbcOperationsSessionRepository.DEFAULT_TABLE_NAME;

    @PersistenceContext
    private EntityManager entityManager;

    public IdeaSessionRegistry(FindByIndexNameSessionRepository<ExpiringSession> sessionRepository) {
        super(sessionRepository);
    }

    @Override
    public List<Object> getAllPrincipals() {
        List mapList = null;
        try {
            Query nativeQuery = entityManager.createNativeQuery(GET_PRINCIPAL_NAME_QUERY);
            mapList = nativeQuery.getResultList();
        } catch (Exception e) {
            log.error("获取JDBC-SESSION异常", e);
        }
        return mapList;
    }


}
