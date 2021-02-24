package com.ideatech.common.entity.id;

import com.ideatech.common.entity.BasePo;
import com.ideatech.common.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

/**
 * @author liangding
 * @create 2018-05-02 上午10:33
 **/
@Slf4j
public class IdeaIdGenerator implements IdentifierGenerator {

    private static final long WORK_ID = new Long(new Random().nextInt(30));

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        if (o instanceof BasePo) {
            if (((BasePo) o).getId() != null) {
                return ((BasePo) o).getId();
            }
        }
        try {
            IdWorker idWorker = ApplicationContextUtil.getBean(IdWorker.class);
            if (idWorker != null) {
                return idWorker.nextId();
            }
        } catch (Exception e) {
            log.error("未找到IdWorker，使用随机参数{}", WORK_ID, e);
        }
        return new IdWorker(WORK_ID).nextId();
    }
}
