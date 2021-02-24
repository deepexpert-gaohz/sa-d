package com.ideatech.ams.system.org.entity;

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
 * 机构
 * @author liangding
 * @create 2018-04-26 上午11:24
 **/
@Entity
@Table(name = "sys_organization")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_sys_organization set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class OrganizationPo extends BaseMaintainablePo {

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
     * 上级机构ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;

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
     * 网点电话
     */
    private String telephone;



}
