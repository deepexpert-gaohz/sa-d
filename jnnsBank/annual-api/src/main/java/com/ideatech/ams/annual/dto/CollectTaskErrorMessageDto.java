package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.DataSourceEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @Description 采集的错误信息
 * @Author wanghongjie
 * @Date 2018/10/8
 **/
@Data
public class CollectTaskErrorMessageDto {
    private Long id;
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
     * 任务 Id
     */
    private Long taskId;

    private Long annualTaskId;

    private String error;

    @Enumerated(EnumType.STRING)
    private DataSourceEnum collectTaskType;
}
