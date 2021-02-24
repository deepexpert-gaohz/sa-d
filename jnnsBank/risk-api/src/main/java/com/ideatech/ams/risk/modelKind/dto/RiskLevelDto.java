package com.ideatech.ams.risk.modelKind.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
public class RiskLevelDto {
    private Long id;

    private String levelName;//风险等级

    private String remakes;

    private String parentId;
}
