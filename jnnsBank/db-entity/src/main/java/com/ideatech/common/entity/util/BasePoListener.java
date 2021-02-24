package com.ideatech.common.entity.util;

import com.ideatech.common.entity.BasePo;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-02 上午10:58
 **/
@Slf4j
public class BasePoListener {
    @PrePersist
    public void prePersist(Object entity) {
        if (BasePo.class.isAssignableFrom(entity.getClass())) {
            BasePo e = (BasePo) entity;
            if (e.getCreatedDate() == null) {
                e.setCreatedDate(new Date());
            }

            if (e.getCreatedBy() == null || "".equals(e.getCreatedBy())) {
                Long userId = SecurityUtils.getCurrentUserId();
                String createdBy = "2";
                if (userId != null) {
                    createdBy = String.valueOf(userId);
                }
                e.setCreatedBy(createdBy);
            }
        } else {
            log.warn("not a basePo prePersist with BasePoListener");
        }
    }
}
