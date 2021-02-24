package com.ideatech.ams.compare.dto;

import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CompareTaskSearchDto extends PagingDto<CompareTaskDto> {
    private Date createdDate;

    private String createdBy;
    private String lastUpdateBy;
    private Date lastUpdateDate;
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
     * fullId集合
     */
    private List<String> fullIdList;
}
