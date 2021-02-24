package com.ideatech.ams.system.blacklist.entity;

import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 黑名单
 * @author liangding
 * @create 2018-06-26 上午10:51
 **/
@Entity
@Table(name = "sys_blacklist")
@Data
@SQLDelete(sql = "update yd_sys_blacklist set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class BlackListEntryPo extends BaseMaintainablePo {
    /**
     * 企业名称
     */
    private String entName;
    /**
     * 来源
     */
    @Enumerated(EnumType.STRING)
    private EBlackListEntrySource source;
    /**
     * 级别
     */
    private String level;
    /**
     * 类型
     */
    private String type;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
    /**
     * 是否白名单
     */
    private Boolean isWhite;
}
