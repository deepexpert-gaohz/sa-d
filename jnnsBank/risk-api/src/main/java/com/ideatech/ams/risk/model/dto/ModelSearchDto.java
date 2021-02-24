package com.ideatech.ams.risk.model.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ModelSearchDto  extends PagingDto<ModeAndKindlDto> {
    /**
     * 模型id
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型状态
     */
    private String status;

    /**
     * 风险类型
     */
    private String typeId;

    /**
     * 风险规则
     */
    private String ruleId;

    /**
     *风险等级
     */
    private String levelId;

    /**
     *提出部门
     */
    private String deptId;


    private  String tableName;
}
