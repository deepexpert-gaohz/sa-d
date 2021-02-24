package com.ideatech.common.entity;

import com.ideatech.common.entity.util.BaseMaintainablePoListener;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-02 上午10:53
 **/
@Data
@EntityListeners(BaseMaintainablePoListener.class)
@MappedSuperclass
public class BaseMaintainablePo extends BasePo {

    /**
     * 最后修改人员
     */
    private String lastUpdateBy;

    /**
     * 最后修改日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    /**
     * 乐观锁属性
     */
    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

    /**
     * 法人机构层级
     */
    private String corporateFullId;

    /**
     * 法人机构行标识
     */
    private String corporateBank;
}
