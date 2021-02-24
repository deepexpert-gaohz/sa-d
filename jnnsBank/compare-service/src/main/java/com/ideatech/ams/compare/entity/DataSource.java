package com.ideatech.ams.compare.entity;

import com.ideatech.ams.compare.enums.CollectType;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 数据源
 */
@Entity
@Data
public class DataSource extends BaseMaintainablePo {

    /**
     * 名称
     */
    @Column(length = 100)
    private String name;
    /**
     * 代码
     */
    @Column(length = 50)
    private String code;
    /**
     * 数据采集方式
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CollectType collectType;
    /**
     * 数据类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DataSourceEnum dataType;

    /**
     * 完整机构ID
     */
    private String organFullId;

    /**
     * 域对象
     */
    private String domain;
    /**
     * 域对象所在的包
     */
    private String domainPackage;

    /**
     * 人行采集开始时间
     */
    @Column(length = 10)
    private String pbcStartTime;

    /**
     * 人行采集结束时间
     */
    @Column(length = 10)
    private String pbcEndTime;
}
