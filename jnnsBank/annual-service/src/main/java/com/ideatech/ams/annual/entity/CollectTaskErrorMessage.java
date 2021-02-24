package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.entity.BasePo;
import lombok.Data;

import javax.persistence.*;

/**
 * @Description 采集任务的错误信息
 * @Author wanghongjie
 * @Date 2018/10/8
 **/
@Entity
@Data
public class CollectTaskErrorMessage extends BasePo {
    /**
     * 机构名称
     */
    private String bankName;
    /**
     * 组织结构ID
     */
    private Long organizationId;
    /**
     * 人行机构号
     */
    private String pbcCode;
    /**
     * 采集任务 Id
     */
    private Long taskId;

    /**
     * 年检任务ID
     */
    private Long annualTaskId;

    @Column(length = 2000)
    private String error;

    @Enumerated(EnumType.STRING)
    private DataSourceEnum collectTaskType;

}
