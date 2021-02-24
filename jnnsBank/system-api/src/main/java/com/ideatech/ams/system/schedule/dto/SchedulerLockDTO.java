package com.ideatech.ams.system.schedule.dto;

import com.ideatech.ams.system.schedule.enums.StatusEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

/**
 * @author jzh
 * @date 2019-08-21.
 */

@Data
public class SchedulerLockDTO extends BaseMaintainableDto {

    private Long id;

    /**
     * 乐观锁
     */
    private Long versionCt;

    /**
     * 定时任务名称
     */
    private String name;

    /**
     * 定时任务执行状态
     */
    private StatusEnum status;

    /**
     * 任务执行开关
     */
    private Boolean onOff;

    /**
     * 上次执行时间
     */
    private Date lastRunTime;

    /**
     * 上次执行者
     */
    private String lastRunner;

    /**
     * cron 表达式
     */
    private String cronStr;

    /**
     * 定时周期（秒）
     */
    private Long cycle;

    /**
     * 最长执行时间
     */
    private Long maxRunTime;

    /**
     * 定时任务描述
     */
    private String description;

    /**
     * 备用字段
     */
    private String string001;
    private String string002;
    private String string003;
    private String string004;
    private String string005;
    private String string006;
    private String string007;
    private String string008;

}
