package com.ideatech.ams.risk.modelKind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
@Data
public class RiskRuleDto {

    private Long id;


    private String ruleName;//风险规则

    private String remakes;

    private String parentId;
}
