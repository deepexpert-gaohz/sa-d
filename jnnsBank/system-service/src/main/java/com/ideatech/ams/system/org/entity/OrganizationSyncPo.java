package com.ideatech.ams.system.org.entity;

import com.ideatech.ams.system.org.enums.SyncType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 *  同步机构数据表
 * @Author wanghongjie
 * @Date 2019/2/22
 **/
@Entity
@Table(name = "sys_organization_sync")
@Data
public class OrganizationSyncPo extends BaseMaintainablePo {

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构完整ID
     */
    @Column(name = "full_id")
    private String fullId;

    /**
     * 核心机构号
     */
    private String code;

    /**
     * 人行机构号
     */
    @Column(name = "pbc_code")
    private String pbcCode;

    /**
     * 金融机构编号
     */
    @Column(name = "institution_code")
    private String institutionCode;

    /**
     * 联系人手机号，逗号分隔
     */
    @Column(length = 500)
    private String mobile;
    /**
     * 网点电话
     */
    private String telephone;

    /**
     * 上级机构ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 之前机构完整ID
     */
    @Column(name = "before_full_id")
    private String beforeFullId;

    /**
     * 之后机构完整ID
     */
    @Column(name = "after_full_id")
    private String afterFullId;
    /**
     *  同步完成
     */
    private Boolean syncFinishStatus = Boolean.FALSE;
    /**
     * 同步成功
     */
    private Boolean syncSuccessStatus = Boolean.FALSE;
    /**
     * 错误信息
     */
    @Column(length = 2000)
    private String errorMsg;

    /**
     * 机构简称
     */
    private String shortName;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 网点地址
     */
    private String address;

    /**
     * 组织机构类型 BANK(银行类) 或者AGENCY(中介机构类)
     */
    private String orgType;

    /**
     * 网点是否开放 1为开放 0为不开放
     */
    private String isOpen;

    /**
     * 网点是否为业务网点 1是 0不是
     */
    private String isOperatingDepartment;

    /**
     * 同步类型
     */
    @Enumerated(EnumType.STRING)
    private SyncType syncType;

    /**
     * 父类同步id
     */
    private String parentSyncId;
    /**
     * 原始的pbcCode
     */
    private String originalPbcCode;

}
