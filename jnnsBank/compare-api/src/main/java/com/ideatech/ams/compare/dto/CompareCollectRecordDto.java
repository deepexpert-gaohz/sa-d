package com.ideatech.ams.compare.dto;

import com.ideatech.ams.compare.enums.CollectState;
import lombok.Data;

/**
 * @Description 比对管理--采集记录DTO
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Data
public class CompareCollectRecordDto {

    private Long id;
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
    private String failReason;

    /**
     * 每条记录添加采集状态字段
     */
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
