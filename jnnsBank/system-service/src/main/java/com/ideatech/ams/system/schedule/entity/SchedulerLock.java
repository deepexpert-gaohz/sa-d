package com.ideatech.ams.system.schedule.entity;

import com.ideatech.ams.system.schedule.enums.StatusEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jzh
 * @date 2019-08-21.
 */

@Data
@Entity
@Table(name = "yd_sys_scheduler_lock")
public class SchedulerLock extends BaseMaintainablePo {

    /**
     * 定时任务名称
     */
    @Column(length = 255)
    private String name;

    /**
     * 定时任务执行状态
     */
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    /**
     * 任务执行开关
     */
    private Boolean onOff;

    /**
     * 上次执行时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRunTime;

    /**
     * 上次执行者
     */
    @Column(length = 255)
    private String lastRunner;

    /**
     * cron 表达式
     */
    @Column(length = 255)
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
    @Column(length = 255)
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

    public SchedulerLock(){
    }

    public SchedulerLock(Long id,String name){
        this.id = id;
        this.name = name;
        this.maxRunTime = 3600L;
    }

    public SchedulerLock(Long id,String name,Long cycle){
        this.id = id;
        this.name = name;
        this.cycle = cycle;
        this.maxRunTime = cycle;
    }

    public SchedulerLock(Long id,String name,Long cycle,Long maxRunTime){
        this.id = id;
        this.name = name;
        this.cycle = cycle;
        this.maxRunTime = maxRunTime;
    }
    public SchedulerLock(Long id,String name,String cronStr){
        this.id = id;
        this.name = name;
        this.cronStr = cronStr;
        this.maxRunTime = 3600L;
    }

    public SchedulerLock(Long id,String name,String cronStr,Long maxRunTime){
        this.id = id;
        this.name = name;
        this.cronStr = cronStr;
        this.maxRunTime = maxRunTime;
    }

}
