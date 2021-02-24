package com.ideatech.ams.compare.dto;

import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

@Data
public class CompareTaskDto extends BaseMaintainableDto {
    private Long id;
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
    private String name;
    /**
     * 使用规则的id
     */
    private Long compareRuleId;
    /**
     * 规则名称
     */
    private String compareRuleName;
    /**
     * 当前状态
     */
    private CompareState state;
    /**
     * 暂停前的状态
     */
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
    private TaskRate rate;

    /**
     * 任务当前状态中文
     */
    private String compareStateCN;

    /**
     * 任务类型中文
     */
    private String taskTypeCN;


}
