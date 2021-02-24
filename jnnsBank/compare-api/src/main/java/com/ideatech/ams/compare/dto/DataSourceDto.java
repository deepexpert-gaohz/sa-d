package com.ideatech.ams.compare.dto;

import com.ideatech.ams.compare.enums.CollectType;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据源dto
 */
@Data
public class DataSourceDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 数据采集方式
     */
    private CollectType collectType;
    /**
     * 数据类型
     */
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
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 开始时间str
     */
    private String beginDateStr;

    /**
     * 结束时间str
     */
    private String endDateStr;

    /**
     * 创建者集合
     */
    private List<String> createdByList;
    /**
     * 创建时间可读
     */
    private String createdDateStr;

    /**
     * 机构集合
     */
    private List<String> fullIdList;

    /**
     * 人行采集开始时间
     */
    private String pbcStartTime;

    /**
     * 人行采集结束时间
     */
    private String pbcEndTime;
}
