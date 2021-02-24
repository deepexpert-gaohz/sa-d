package com.ideatech.ams.system.user.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户
 * @author liangding
 * @create 2018-05-04 上午10:03
 **/
@Entity
@Table(name = "sys_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_sys_user set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class UserPo extends BaseMaintainablePo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户显示名称
     */
    private String cname;
    /**
     * 加密后密码
     */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 启用标记
     */
    private Boolean enabled;
    /**
     * 密码是否过期(true: 过期)
     */
    private Boolean isExpire = Boolean.FALSE;
    /**
     * 密码更新时间
     */
    private Date pwdUpdateDate;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;

    /**
     * 机构ID
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 锁定状态
     */
    private String lockedStatus;

    /**
     * 锁定时间
     */
    private String lockedTime;

    /**
     * 错误次数
     */
    private Integer pwFailCount;

}
