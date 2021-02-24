package com.ideatech.common.entity.util;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-02 上午11:04
 **/
@Slf4j
public class BaseMaintainablePoListener {
    @PreUpdate
    @PrePersist
    public void preUpdate(Object entity) {
        if (BaseMaintainablePo.class.isAssignableFrom(entity.getClass())) {
            BaseMaintainablePo e = (BaseMaintainablePo) entity;
            e.setLastUpdateDate(new Date());

            if (e.getLastUpdateBy() == null || "".equals(e.getLastUpdateBy())) {
                Long userId = SecurityUtils.getCurrentUserId();
                String modifyBy = "2";
                if (userId != null) {
                    modifyBy = String.valueOf(userId);
                }
                e.setLastUpdateBy(modifyBy);
            }
        } else {
            log.warn("not a basePo prePersist with BasePoListener");
        }
    }
}
