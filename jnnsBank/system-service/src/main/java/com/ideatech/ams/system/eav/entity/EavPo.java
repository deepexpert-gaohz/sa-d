package com.ideatech.ams.system.eav.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 扩展属性值
 * @author liangding
 * @create 2018-07-23 下午5:09
 **/
@Entity
@Table(name = "sys_eav")
@SQLDelete(sql = "update yd_sys_eav set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
@Data
public class EavPo extends BaseMaintainablePo {
    /**
     * 扩展属性文档ID
     */
    private Long docId;
    /**
     * 属性ID
     */
    private Long attrId;
    /**
     * 数据ID
     */
    private Long entityId;
    /**
     * 值
     */
    @Column(length = 250)
    private String value;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
