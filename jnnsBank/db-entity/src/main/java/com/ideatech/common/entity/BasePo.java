package com.ideatech.common.entity;

import com.ideatech.common.entity.util.BasePoListener;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-02 上午10:25
 **/
@Data
@EntityListeners(BasePoListener.class)
@MappedSuperclass
public class BasePo {
    /**
     * ID
     */
    @Id
    @GenericGenerator(name = "ideaIdGenerator", strategy = "com.ideatech.common.entity.id.IdeaIdGenerator")
    @GeneratedValue(generator = "ideaIdGenerator")
    protected Long id;

    /**
     * 审计日志，记录条目创建时间，自动赋值，不需要程序员手工赋值
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    /**
     * 创建人员
     */
    private String createdBy;


}
