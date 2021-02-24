package com.ideatech.ams.system.whitelist.entity;

import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 白名单
 */
@Entity
@Table(name = "sys_whitelist")
@Data
public class WhiteListPo extends BaseMaintainablePo {
    /**
     * 企业名称
     */
    private String entName;
    /**
     * 来源
     */
    @Enumerated(EnumType.STRING)
    private WhiteListEntrySource source;

    /**
     * 所属机构
     */
    private Long orgId;

    /**
     * 内部机构号
     */
    private String organCode;

    /**
     * 所属机构名称
     */
    private String orgName;

    /**
     * fullId
     */
    private String organFullId;

    /**
     * 状态
     */
    private String status;

    /**
     * 扩展字段
     */
    private String string001;

    private String string002;

    private String string003;

    private String string004;

    private String string005;

    private String string006;

    private String string007;

    private String string008;

    private String string009;

    private String string010;
}
