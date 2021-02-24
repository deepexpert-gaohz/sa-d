package com.ideatech.ams.mivs.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Entity
@Data
@Table(name = "mivs_business_accept_log")
public class BusinessAcceptTimeLog extends ExtendEntity {

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    private String sysInd;

    /**
     * QueryDate
     * 查询日期
     * <QueDt>  [1..1]  ISODate  禁止中文
     */
    private String queDt;

    /**
     * OrginalQueryDate
     * 原查询日期
     * <OrgnlQueDt>  [1..1]  ISODate  禁止中文
     */
    private String orgnlQueDt;
}
