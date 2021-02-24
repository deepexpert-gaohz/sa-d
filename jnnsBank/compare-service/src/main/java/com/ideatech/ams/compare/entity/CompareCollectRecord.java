package com.ideatech.ams.compare.entity;

import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 比对管理--采集数据记录表
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Entity
@Data
public class CompareCollectRecord extends BaseMaintainablePo {
    /**
     * 采集任务ID
     */
    private Long collectTaskId;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户名称
     */
    private String depositorName;

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 失败状态
     */
    @Column(length = 2000)
    private String failReason;

    /*
     * 每条记录添加采集状态字段
     */
    @Enumerated(EnumType.STRING)
    private CollectState collectState;


    /**
     * 年检任务ID
     */
    private Long compareTaskId;


    /**
     * 数据源
     */
    private String dataSourceType;

    /**
     * 行内机构号
     */
    private String organCode;
    /**
     * 机构fullid
     */
    private String organFullId;
}
