package com.ideatech.ams.risk.modelKind.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
@MappedSuperclass
public class BaseRisk {
    /**
     * ID
     */
    @Id
    @GenericGenerator(name = "ideaIdGenerator", strategy = "com.ideatech.common.entity.id.IdeaIdGenerator")
    @GeneratedValue(generator = "ideaIdGenerator")
    @Column(name = "id")
    protected Long id;


    @Column(name = "parent_id")
    private String parentId; //上级编号

    @Column(name = "create_by")
    private String createBy; //创建者

    @Column(name = "create_date")
    private Date createDate; //创建时间

    @Column(name = "update_by")
    private String updateBy; //更新者

    @Column(name = "update_date")
    private Date updateDate; //更新时间

    @Column(name = "remakes")
    private String remakes; //备注信息

    @Column(name = "del_flag")
    private String delFlag; //删除标记

    @Column(name = "parent_ids")
    private String parentIds; //所有上级编号

}
