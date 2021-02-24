package com.ideatech.ams.system.config.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 系统配置
 * @author liangding
 * @create 2018-05-15 下午4:20
 **/
@Data
@Entity
@Table(name = "sys_config")
@SQLDelete(sql = "update yd_sys_config set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class ConfigPo extends BaseMaintainablePo {
    /**
     * 属性名称
     */
    private String configKey;
    /**
     * 属性值
     */
    @Lob
    private String configValue;
    /**
     * 启用标记
     */
    private Boolean enabled = Boolean.TRUE;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
