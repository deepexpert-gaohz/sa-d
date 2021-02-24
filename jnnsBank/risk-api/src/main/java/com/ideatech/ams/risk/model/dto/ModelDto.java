package com.ideatech.ams.risk.model.dto;

import lombok.Data;

@Data
public class ModelDto {

    private Long id;
    /**
     * 模型id
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 数据库名称
     */

    private String tableName;

    /**
     * 过程名称
     */
    private String procName;

    /**
     * 模型状态
     */
    private String status;

    /**
     * 模型描述
     */
    private String mdesc;


    /**
     * 风险类型
     */
    private String typeId;

    /**
     * 风险规则
     */
    private String ruleId;

    /**
     * 风险等级
     */
    private String levelId;


}
