package com.ideatech.common.entity;

import javax.persistence.MappedSuperclass;

/**
 * @Description 采集对象的字段长度控制
 * @Author wanghongjie
 * @Date 2018/8/3
 **/
@MappedSuperclass
public class AmsEntityLength extends BaseMaintainablePo {

    /**
     * 报备时间长度
     */
    protected static final int DEFAULT_SYNC_DATE_COLUMN_LENGTH = 20;

    protected static final int DEFAULT_DATE_COLUMN_LENGTH = 30;

    protected static final int DEFAULT_SELECT_COLUMN_LENGTH = 30;

    protected static final int DEFAULT_ENUM_COLUMN_LENGTH = 40;

    protected static final int DEFAULT_COLUMN_50_LENGTH = 50;

    protected static final int DEFAULT_COLUMN_100_LENGTH = 100;

    protected static final int DEFAULT_COLUMN_500_LENGTH = 500;

    protected static final int DEFAULT_COLUMN_1000_LENGTH = 1000;

    protected static final int DEFAULT_BUSINESSSCOPE_VARCHAR_COLUMN_LENGTH = 2000;

    protected static final int DEFAULT_MAX_VARCHAR_COLUMN_LENGTH = 4000;

    protected static final int DEFAULT_CLOB_COLUMN_LENGTH = 64000;
}
