package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工商数据查询历史,根据IDP的接口细分
 */
@Entity
@Table(name = "yd_saicsearchhistory")
@Data
public class SaicSearchHistory extends BaseMaintainablePo {
    /**
     * 查询企业
     */
    @Column(name = "yd_entname")
    private String entname;
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;
    /**
     * 查询日期
     */
    @Column(name = "yd_querydate")
    private String querydate;

    /**
     * 查询结果Y/N
     */
    @Column(name = "yd_queryresult")
    private String queryresult;

    /**
     * 查询类型
     */
    @Column(name = "yd_searchtype")
    private String searchtype;

    /**
     * 查询URL
     */
    @Column(name = "yd_searchurl")
    private String searchurl;

    /**
     * 用户
     */
    @Column(name = "yd_username")
    private String username;

    /**
     * 机构的fullid
     */
    @Column(name = "yd_orgfullid")
    private String orgfullid;

}