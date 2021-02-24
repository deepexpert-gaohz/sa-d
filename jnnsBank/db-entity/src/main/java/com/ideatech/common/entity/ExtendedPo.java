package com.ideatech.common.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-05-02 上午11:27
 **/
@Data
@MappedSuperclass
public abstract class ExtendedPo extends BaseMaintainablePo {

    @Transient
    private Map<String, Object> ext;

    public abstract String getDocType();
}
