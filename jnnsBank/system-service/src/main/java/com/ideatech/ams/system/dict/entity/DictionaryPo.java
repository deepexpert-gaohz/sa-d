package com.ideatech.ams.system.dict.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典
 * @author liangding
 * @create 2018-05-24 下午3:20
 **/
@Entity
@Table(name = "sys_dictionary")
@Data
@SQLDelete(sql = "update yd_sys_dictionary set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class DictionaryPo extends BaseMaintainablePo {
    /**
     * 字典名称
     */
    private String name;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;

}
