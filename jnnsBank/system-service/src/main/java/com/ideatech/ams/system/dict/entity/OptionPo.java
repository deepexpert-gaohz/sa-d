package com.ideatech.ams.system.dict.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典项
 * @author liangding
 * @create 2018-05-24 下午3:21
 **/
@Data
@Entity
@Table(name = "sys_option")
@SQLDelete(sql = "update yd_sys_option set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class OptionPo extends BaseMaintainablePo {
    /**
     * 字典ID
     */
    private Long dictionaryId;
    /**
     * 字典项名称
     */
    private String name;
    /**
     * 字典项值
     */
    private String value;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
