package com.ideatech.ams.system.meta.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 扩展属性文档定义
 * @author liangding
 * @create 2018-06-08 下午3:01
 **/
@Data
@Entity
@Table(name = "sys_meta_doc")
@SQLDelete(sql = "update yd_sys_meta_doc set yd_deleted = 1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class MetaDocPo extends BaseMaintainablePo {
    /**
     * 文档代码
     */
    private String code;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
