package com.ideatech.ams.system.permission.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统权限
 * @author liangding
 * @create 2018-05-06 下午7:46
 **/
@Entity
@Table(name = "sys_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_sys_permission set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class PermissionPo extends BaseMaintainablePo {
    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String title;

    /**
     * 上级权限ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * URL地址
     */
    private String url;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 权限类型
     */
    @Column(name = "permission_type")
    private String permissionType;

    /**
     * 顺序属性
     */
    @Column(name = "order_num")
    private Long orderNum;

    /**
     * HTTP方法
     */
    private String method;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用标记
     */
    private Boolean enabled;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
