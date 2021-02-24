package com.ideatech.ams.compare.entity;

import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * 比对任务
 */
@Entity
@Data
public class CompareTask extends BaseMaintainablePo {
    /**
     * 创建人
     */
    private String createName;
    /**
     * 创建时间
     */
    private String time;
    /**
     * 完整机构organFullId
     */
    private String organFullId;
    /**
     * 任务名称
     */
    @Column(length = 100)
    private String name;
    /**
     * 使用规则的id
     */
    private Long compareRuleId;
    /**
     * 当前状态
     */
    @Enumerated(EnumType.STRING)
    private CompareState state;
    /**
     * 暂停前的状态
     */
    @Enumerated(EnumType.STRING)
    private CompareState stateBeforePause;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 上一次启动的时间
     */
    private Date lastStartedTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 要处理的数据总数
     */
    private Integer count;
    /**
     * 已处理的数据总数
     */
    private Integer processed;
    /**
     * 失败原因
     */
    private String errorMsg;
    /**
     * 是否校验黑名单
     */
    private Boolean isCheckBlacklist;
    /**
     * 任务类型
     */
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    /**
     * 是否暂停
     */
    private Boolean isPause;
    /**
     * 根据task和uuid锁定compareTask外检的数据。
     */
    private String uuid;
    /**
     * 是否运行中
     */
    private Boolean isRunning;
    /**
     * 定时任务频率
     */
    @Enumerated(EnumType.STRING)
    private TaskRate rate;
}
